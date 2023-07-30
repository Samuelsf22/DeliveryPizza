package org.example.Modelo;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.example.Main;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PedidoDAO {
    public boolean anadirPedido (Pedido pedido, JPanel panel){
        Connection conn = Conexion.getConexion();
        String sql = "insert into pedido (id_cliente, fecha, total) VALUES (?,CONVERT_TZ(NOW(), '+00:00', '-05:00'),?);";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setInt   (1,pedido.getId_cliente());
            pstms.setDouble(2,pedido.getTotal());
            pstms.executeUpdate();
            conn.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "El pedido no se agregó\n"+e,"Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public ArrayList<Pedido> verHistorialPedido(int id_cliente) {
        Connection conn = Conexion.getConexion();
        var pedidos = new ArrayList<Pedido>();
        String sql = "select fecha, total from pedido where id_cliente = ?;";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setInt(1,id_cliente);
            ResultSet rs = pstms.executeQuery();
            while (rs.next()) {
                var pedido = new Pedido(rs.getString("fecha"),rs.getDouble("total"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                conn.close();
            }catch (SQLException e){
                System.out.println(e);
            }
        }
        return pedidos;
    }
    public Pedido verPedido(int id_cliente, int id) {
        Connection conn = Conexion.getConexion();
        Pedido pedido = null;
        String sql = "select fecha, total from pedido where id_cliente = ? and id = ?;";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setInt(1,id_cliente);
            pstms.setInt(2,id);
            ResultSet rs = pstms.executeQuery();
            while (rs.next()) {
                pedido = new Pedido(id, id_cliente,rs.getString("fecha"),rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return pedido;
    }
    public ArrayList<DetallesPedido> verDetallePedido(int id_pedido){
        Connection conn = Conexion.getConexion();
        var detallePedidos = new ArrayList<DetallesPedido>();
        String sql = "select d.* from detalle_pedidos d inner join pedido p on p.id = d.id_pedido where p.id = ?;";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setInt(1,id_pedido);
            ResultSet rs = pstms.executeQuery();
            while (rs.next()) {
                var detallePedido = new DetallesPedido(rs.getInt("id"),rs.getString("nombre"),
                                    rs.getDouble("precio"), rs.getString("tamanio"),
                                    rs.getInt("cantidad"));
                detallePedidos.add(detallePedido);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                conn.close();
            }catch (SQLException e){
                System.out.println(e);
            }
        }
        return detallePedidos;
    }
    public void anadirDetallePedido (DetallesPedido detallePedido){
        Connection conn = Conexion.getConexion();
        String sql = "insert into detalle_pedidos (nombre, precio, tamanio ,cantidad, id_pedido) values (?,?,?,?,?);";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setString(1,detallePedido.getNombre());
            pstms.setDouble(2,detallePedido.getPrecio());
            pstms.setString(3,detallePedido.getTamanio());
            pstms.setInt   (4,detallePedido.getCantidad());
            pstms.setInt   (5,detallePedido.getIdPedido());
            pstms.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int idPedido (int id_cliente){
        Connection conn = Conexion.getConexion();
        String sql = "SELECT MAX(id) FROM pedido where id_cliente = ?;";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setInt(1, id_cliente);
            ResultSet rs = pstms.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void cancelar(int id_cliente, int id_pedido){
        Connection conn = Conexion.getConexion();
        String sqlDetalle = "delete from detalle_pedidos where id_pedido = ?;";
        String sqlPedido = "delete from pedido where id_cliente = ? and id = ?;";
        try {
            var pstmsDetalle = conn.prepareStatement(sqlDetalle, Statement.RETURN_GENERATED_KEYS);
            pstmsDetalle.setInt(1, id_pedido);
            pstmsDetalle.executeUpdate();
            var pstmsPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            pstmsPedido.setInt(1, id_cliente);
            pstmsPedido.setInt(2, id_pedido);
            pstmsPedido.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pdfPedido(int id_cliente, int id_pedido, String nombreCLiente) {
        String fechaPedido = null, total = null;
        try {
            FileOutputStream archivo;
            String url = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            File salida = new File(url + File.separator + "pedido"+id_pedido+".pdf");
            archivo = new FileOutputStream(salida);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            //Fecha
            Pedido pedido = verPedido(id_cliente, id_pedido);
            if (pedido != null) {
                fechaPedido = pedido.getFecha();
                total = String.valueOf(pedido.getTotal());
            }

            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] columnWidthsEncabezado = new float[]{20f, 20f, 60f, 60f};
            Encabezado.setWidths(columnWidthsEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            //info empresa
            Image img = Image.getInstance(Main.class.getResource("/img/logo-trnasparente.png"));
            Encabezado.addCell(img);
            Encabezado.addCell("");
            Encabezado.addCell("Ruc: 7485125259"
                    + "\nNombre: " + "Pizzería Don Marco"
                    + "\nTeléfono: " + "927 937 573"
                    + "\nDirección: " + "Jr. Lima 156");
            //
            Encabezado.addCell( "Usuario: " + nombreCLiente + "\nN° Pedido: " + id_pedido + "\nFecha y hora: " + fechaPedido);


            doc.add(Encabezado);
            doc.add(Chunk.NEWLINE);

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.getDefaultCell().setBorder(0);
            float[] columnWidths = new float[]{10f, 25f, 25f, 15f, 15f};
            tabla.setWidths(columnWidths);
            tabla.setHorizontalAlignment(Element.ALIGN_LEFT);

            Font negrita = new Font(Font.FontFamily.COURIER, 12, Font.BOLD, BaseColor.BLACK);
            PdfPCell c1 = new PdfPCell(new Phrase("Cant.", negrita));
            PdfPCell c2 = new PdfPCell(new Phrase("Producto.", negrita));
            PdfPCell c3 = new PdfPCell(new Phrase("Tamaño.", negrita));
            PdfPCell c4 = new PdfPCell(new Phrase("Unidad", negrita));
            PdfPCell c5 = new PdfPCell(new Phrase("Total", negrita));

            c1.setBorder(Rectangle.NO_BORDER);
            c2.setBorder(Rectangle.NO_BORDER);
            c3.setBorder(Rectangle.NO_BORDER);
            c4.setBorder(Rectangle.NO_BORDER);
            c5.setBorder(Rectangle.NO_BORDER);

            c1.setBackgroundColor(new BaseColor(255, 183, 58));
            c2.setBackgroundColor(new BaseColor(255, 183, 58));
            c3.setBackgroundColor(new BaseColor(255, 183, 58));
            c4.setBackgroundColor(new BaseColor(255, 183, 58));
            c5.setBackgroundColor(new BaseColor(255, 183, 58));

            tabla.addCell(c1);
            tabla.addCell(c2);
            tabla.addCell(c3);
            tabla.addCell(c4);
            tabla.addCell(c5);

            //detalles del pedido
            ArrayList<DetallesPedido> detalle = verDetallePedido(id_pedido);
            for (DetallesPedido detallePedido : detalle) {
                String subTotal = String.format("%.2f",detallePedido.getCantidad() * detallePedido.getPrecio());
                tabla.addCell(String.valueOf(detallePedido.getCantidad()));
                tabla.addCell(detallePedido.getNombre());
                tabla.addCell(detallePedido.getTamanio());
                tabla.addCell(String.valueOf(detallePedido.getPrecio()));
                tabla.addCell(subTotal);
            }

            doc.add(tabla);
            Paragraph agra = new Paragraph();
            agra.add(Chunk.NEWLINE);
            agra.add("Total S/ " + total);
            agra.setAlignment(Element.ALIGN_RIGHT);
            doc.add(agra);
            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelacion \n\n");
            firma.add("——————————————————————————————\n");
            firma.add("Firma \n");
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);
            Paragraph gr = new Paragraph();
            gr.add(Chunk.NEWLINE);
            gr.add("Gracias por su compra, vuelva pronto");
            gr.setAlignment(Element.ALIGN_CENTER);
            doc.add(gr);
            doc.close();
            archivo.close();
            Desktop.getDesktop().open(salida);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
