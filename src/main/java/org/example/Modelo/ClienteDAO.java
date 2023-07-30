package org.example.Modelo;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDAO {
    private final Mensajes mensajes = new Mensajes();
    public boolean registrarCliente(Cliente cliente, JPanel panel){
        try {
            if (esCorreo(cliente.getCorreo())){
                Connection conn = Conexion.getConexion();
                String checkSQL = "select email FROM cliente WHERE email = ?;";
                var check = conn.prepareStatement(checkSQL);
                check.setString(1,cliente.getCorreo());
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    mensajes.mostrarMensaje(panel,"El correo ya está usado en otra cuenta","Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    String sql = "insert into cliente (nombre, apellidos, email, contraseña) VALUES (?,?,?,aes_encrypt(?,?));";
                    var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pstms.setString(1,cliente.getNombre());
                    pstms.setString(2,cliente.getApellidos());
                    pstms.setString(3,cliente.getCorreo());
                    pstms.setString(4,cliente.getContrasena());
                    pstms.setString(5,"cliente_pz2026");
                    pstms.executeUpdate();
                    conn.close();
                    mensajes.mostrarMensaje(panel,"Cliente registrado","Registro", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }else {
                mensajes.mostrarMensaje(panel,"Correo inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }catch(SQLException e) {
            mensajes.mostrarMensaje(panel,"Cliente no registrado\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean consultarUsuario(Cliente cliente, JPanel panel) {
        try {
            if (esCorreo(cliente.getCorreo())){
                Connection conn = Conexion.getConexion();
                String emailCorrecto = null;
                String contraseñaCorrecta = null;
                var pstms = conn.prepareStatement("SELECT email, cast(aes_decrypt(contraseña,?) as char) from cliente where email = ?;");
                pstms.setString(1,"cliente_pz2026");
                pstms.setString(2,cliente.getCorreo());
                ResultSet rs = pstms.executeQuery();
                if (rs.next()) {
                    emailCorrecto = rs.getString(1);
                    contraseñaCorrecta = rs.getString(2);
                }
                if (cliente.getCorreo().equals(emailCorrecto) && cliente.getContrasena().equals(contraseñaCorrecta)) {
                    return true;
                } else if (!cliente.getCorreo().equals(emailCorrecto) || !cliente.getContrasena().equals(contraseñaCorrecta)) {
                    mensajes.mostrarMensaje(panel,"Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }else {
                mensajes.mostrarMensaje(panel,"Correo inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public boolean esCorreo(String correo) {
        try {
            InternetAddress internetAddress = new InternetAddress(correo);
            internetAddress.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    public Cliente verCliente(String email) {
        try {
            Connection conn = Conexion.getConexion();
            Cliente cliente = null;
            var pstms = conn.prepareStatement("select id, nombre, apellidos from cliente where email = ?;");
            pstms.setString(1,email);
            ResultSet rs = pstms.executeQuery();
            while (rs.next()) {
                cliente = new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"), email);
            }
            return cliente;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizarCliente(Cliente cliente){
        try {
            if (esCorreo(cliente.getCorreo())){
                Connection conn = Conexion.getConexion();
                String sql = "update cliente set nombre = ?, apellidos = ?, email = ?, contraseña = aes_encrypt(?,?) where id = ?;";
                var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstms.setString(1,cliente.getNombre());
                pstms.setString(2,cliente.getApellidos());
                pstms.setString(3,cliente.getCorreo());
                pstms.setString(4,cliente.getContrasena());
                pstms.setString(5,"cliente_pz2026");
                pstms.setInt(6,cliente.getId());
                pstms.executeUpdate();
                conn.close();
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean borrarCliente(int id_cliente){
        try {
            Connection conn = Conexion.getConexion();
            String sql = "delete from cliente where id = ? ;";
            var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1,id_cliente);
            pstmt.executeUpdate();
            conn.close();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
