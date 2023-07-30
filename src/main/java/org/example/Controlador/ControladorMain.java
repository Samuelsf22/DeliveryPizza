package org.example.Controlador;

import org.example.Main;
import org.example.Modelo.*;
import org.example.Vista.VentanaLogin;
import org.example.Vista.VentanaMain;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ControladorMain extends JFrame implements ActionListener, KeyListener, MouseListener, ChangeListener {
    private static VentanaMain frmMain = new VentanaMain();
    private final Eventos eventos;
    private final Mensajes mensajes;
    private final PedidoDAO pedidoDAO;
    private final PizzaDAO pizzaDAO;
    private final ClienteDAO clienteDAO;
    private int id_cliente;
    private final ArrayList<PedidoLista> detallePedido = new ArrayList<>();
    private final ArrayList<Pizza> listaPizza = new ArrayList<>();

    public ControladorMain(VentanaMain frmMain, Eventos eventos, Mensajes mensajes, PedidoDAO pedidoDAO, PizzaDAO pizzaDAO, ClienteDAO clienteDAO) {

        this.eventos = eventos;
        this.mensajes = mensajes;
        this.pedidoDAO = pedidoDAO;
        this.pizzaDAO = pizzaDAO;
        this.clienteDAO = clienteDAO;

        ControladorMain.frmMain = frmMain;
        //Pedido
        ControladorMain.frmMain.btnAgregarPedido.addActionListener(this);
        ControladorMain.frmMain.btnContinuarPedido.addActionListener(this);
        ControladorMain.frmMain.btnVerDescripcion.addActionListener(this);
        ControladorMain.frmMain.cbTamanio.addActionListener(this);
        ControladorMain.frmMain.spCantidadPedido.addChangeListener(this);
        ControladorMain.frmMain.tableProductoPedido.addMouseListener(this);
        ControladorMain.frmMain.tablaListaPedido.addMouseListener(this);
        ControladorMain.frmMain.btnQuitarPedido.addActionListener(this);

        //Pago
        ControladorMain.frmMain.btnFinalizarPago.addActionListener(this);
        ControladorMain.frmMain.btnCancelarPago.addActionListener(this);

        //InfoPersonal
        ControladorMain.frmMain.btnActualizarInfoP.addActionListener(this);
        ControladorMain.frmMain.btnEliminarInfoP.addActionListener(this);

        ControladorMain.frmMain.txtNombreInfoP.addKeyListener(this);
        ControladorMain.frmMain.txtApellidosInfoP.addKeyListener(this);

        //Cambio de paneles
        ControladorMain.frmMain.btnPedidoMain.addActionListener(this);
        ControladorMain.frmMain.btnHistorial.addActionListener(this);
        ControladorMain.frmMain.btnConfiguracion.addActionListener(this);
        ControladorMain.frmMain.btnCerrarSesion.addActionListener(this);

        actualizarPizza();
        cambioVentanas(true,false, false, false);
        verVentana();
    }
    public void verVentana() {
        setContentPane(frmMain.LayoutPrincipal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        Image icono = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/img/logo-trnasparente.png"));
        setIconImage(icono);

        Color amarillo = new Color(255, 183, 58);
        Color negro1 = new Color(35,35,35);

        frmMain.scrollProducto.getViewport().setBackground(negro1);//para cambiar el color del ScrollPanel
        frmMain.scrollProducto.setBorder(new LineBorder(Color.BLACK,0));

        frmMain.scrollPago.getViewport().setBackground(negro1);
        frmMain.scrollPago.setBorder(new LineBorder(Color.BLACK,0));

        frmMain.ScrollLista.getViewport().setBackground(negro1);
        frmMain.ScrollLista.setBorder(new LineBorder(Color.BLACK,0));

        frmMain.scrollHistorial.getViewport().setBackground(negro1);
        frmMain.scrollHistorial.setBorder(new LineBorder(Color.BLACK,0));

        frmMain.scrollDescripcion.getViewport().setBackground(negro1);
        frmMain.scrollDescripcion.setBorder(new LineBorder(Color.BLACK,0));

        //encabezado de las tablas

        frmMain.tableProductoPedido.getTableHeader().setBackground(negro1);
        frmMain.tableProductoPedido.getTableHeader().setForeground(Color.WHITE);

        frmMain.tablaListaPedido.getTableHeader().setBackground(negro1);
        frmMain.tablaListaPedido.getTableHeader().setForeground(Color.WHITE);

        frmMain.tablePago.getTableHeader().setBackground(negro1);
        frmMain.tablePago.getTableHeader().setForeground(Color.WHITE);

        frmMain.tableHistorial.getTableHeader().setBackground(negro1);
        frmMain.tableHistorial.getTableHeader().setForeground(Color.WHITE);

        frmMain.txtNombreInfoP.setBorder(new LineBorder(amarillo, 1));
        frmMain.txtApellidosInfoP.setBorder(new LineBorder(amarillo, 1));
        frmMain.txtCorreoInfoP.setBorder(new LineBorder(amarillo, 1));
        frmMain.txtPassInfoP.setBorder(new LineBorder(amarillo, 1));
        frmMain.txtDescripcion.setBorder(new LineBorder(amarillo, 1));
        frmMain.pmDescripcion.setBorder(new LineBorder(amarillo, 0));

        frmMain.cbTamanio.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {//pintar lista
                    setBackground(amarillo);
                    setForeground(negro1);
                } else {
                    setBackground(negro1);
                    setForeground(Color.white);
                }
                return this;
            }
        });

        SpinnerNumberModel nm = new SpinnerNumberModel();
        nm.setMaximum(50);
        nm.setMinimum(0);
        nm.setValue(0);
        nm.setStepSize(1);//para el salto de numeros
        frmMain.spCantidadPedido.setModel(nm);

        JComponent editor = frmMain.spCantidadPedido.getEditor();

        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;

            // Obtener el campo de texto interno del editor
            JFormattedTextField textField = spinnerEditor.getTextField();

            // Cambiar el color de fondo y el color de fuente del campo de texto
            textField.setBackground(negro1);
            textField.setForeground(Color.WHITE);

            // Aplicar una fábrica de formateadores personalizada para mantener los colores
            DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory();
            formatterFactory.setDefaultFormatter(new DefaultFormatter());
            textField.setFormatterFactory(formatterFactory);
        }

        String [] tamanio = {"Seleccionar","Personal","Mediana","Grande","Familiar","Super Familiar"};
        for (String tamano: tamanio) {
            frmMain.cbTamanio.addItem(tamano);
        }
        frmMain.txtDescripcion.setLineWrap(true); // Habilitar el ajuste de línea automático
        frmMain.txtDescripcion.setWrapStyleWord(true); // Hacer que las palabras se ajusten a las líneas
        frmMain.txtDescripcion.setForeground(Color.WHITE);

    }

    //Inicio
    public void setUsuario(String correo){
        Cliente cliente = clienteDAO.verCliente(correo);
        id_cliente = cliente.getId();
        frmMain.lblNombreUser.setText(cliente.getNombre()+" "+ cliente.getApellidos());
        //informacion Personal
        frmMain.txtNombreInfoP.setText(cliente.getNombre());
        frmMain.txtApellidosInfoP.setText(cliente.getApellidos());
        frmMain.txtCorreoInfoP.setText(cliente.getCorreo());

        actualizarHistorial(id_cliente);

    }
    private void actualizarPizza() {
        var productos = pizzaDAO.verPizza();
        var modT = new DefaultTableModel(new String[]{"ID","Nombre"}, productos.size()){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < productos.size(); i++) {
            var producto = productos.get(i);
            modT.setValueAt(producto.getId(),i,0);
            modT.setValueAt(producto.getNombre(),i,1);
            var pizza = new Pizza(producto.getId(),producto.getNombre(),producto.getPrecio(), producto.getTamanio(), producto.getDescripcion());
            listaPizza.add(pizza);
        }
        frmMain.tableProductoPedido.setModel(modT);
        //para pintar el resaltado, y las filas
        Tablas c = new Tablas();
        c.colorResaltado( new Color(255, 183, 58));
        for (int i = 0; i <  frmMain.tableProductoPedido.getColumnCount(); i++) {
            frmMain.tableProductoPedido.getColumnModel().getColumn(i).setCellRenderer(c);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //Pedido
        if (e.getSource() == frmMain.btnAgregarPedido){
            int registro = frmMain.tableProductoPedido.getSelectedRow();
            if (registro >= 0) {
                if (!frmMain.lblNombreProducto.getText().isEmpty() && (int) frmMain.spCantidadPedido.getValue() != 0 &&
                        frmMain.cbTamanio.getSelectedIndex() != 0 && !frmMain.lblPrecioUnidad.getText().equals("S/ 0.00")){
                    // Obtener los valores del pedido desde la vista
                    var id     = frmMain.tableProductoPedido.getValueAt(registro, 0).toString();
                    var nombre = frmMain.tableProductoPedido.getValueAt(registro, 1).toString();
                    int cantidad = Integer.parseInt(frmMain.spCantidadPedido.getValue().toString());
                    String tamanio = (String) frmMain.cbTamanio.getSelectedItem();

                    // Buscar si el producto ya está en la lista
                    int indiceExistente = -1;
                    for (int i = 0; i < detallePedido.size(); i++) {
                        PedidoLista productoExistente = detallePedido.get(i);
                        if (productoExistente.getNombre().equals(nombre) && productoExistente.getTamanio().equals(tamanio)) {
                            indiceExistente = i;
                            break;
                        }
                    }
                    if (indiceExistente != -1) {
                        // Si el producto ya está en la lista, sumar las cantidades y actualizar el subtotal
                        PedidoLista productoExistente = detallePedido.get(indiceExistente);
                        int nuevaCantidad = productoExistente.getCantidad() + cantidad;
                        double nuevoSubTotal = Double.parseDouble(String.format("%.2f",productoExistente.getSubTotal() + obtenerPrecioTotal()));
                        productoExistente.setCantidad(nuevaCantidad);
                        productoExistente.setSubTotal(nuevoSubTotal);
                    } else {
                        double precio = Double.parseDouble(String.format("%.2f", obtenerPrecioUnidad()));
                        double subTotal = Double.parseDouble(String.format("%.2f",  obtenerPrecioTotal()));
                        PedidoLista nuevoProducto = new PedidoLista(id, nombre, precio ,tamanio, cantidad, subTotal);
                        detallePedido.add(nuevoProducto);
                    }

                    // Actualizar el modelo de la tabla en la vista
                    var modT = new DefaultTableModel(new String[]{"ID","Nombre","Precio","Tamaño","Cantidad","Subtotal"}, detallePedido.size()){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    double total = 0;
                    for (int i = 0; i < detallePedido.size(); i++) {
                        PedidoLista producto = detallePedido.get(i);
                        var subtotal = Double.parseDouble(String.format("%.2f", producto.getPrecio() * producto.getCantidad()));
                        total += subtotal;
                        modT.setValueAt(producto.getId(), i, 0);
                        modT.setValueAt(producto.getNombre(), i, 1);
                        modT.setValueAt(producto.getPrecio(), i, 2);
                        modT.setValueAt(producto.getTamanio(), i, 3);
                        modT.setValueAt(producto.getCantidad(), i, 4);
                        modT.setValueAt(producto.getSubTotal(), i, 5);
                    }
                    frmMain.lblTotalPedido.setText("S/ " + String.format("%.2f", total));
                    frmMain.tablaListaPedido.setModel(modT);
                    Tablas c = new Tablas();
                    c.colorResaltado( new Color(255, 183, 58));
                    for (int i = 0; i <  frmMain.tablaListaPedido.getColumnCount(); i++) {
                        frmMain.tablaListaPedido.getColumnModel().getColumn(i).setCellRenderer(c);
                    }
                    //limpiado los datos
                    frmMain.spCantidadPedido.setValue(0);
                    frmMain.lblNombreProducto.setText("");
                    frmMain.lblPrecioUnidad.setText("S/ 0.00");
                    frmMain.lblPrecioTotal.setText("S/ 0.00");
                    frmMain.cbTamanio.setSelectedIndex(0);
                    frmMain.tableProductoPedido.clearSelection();
                    frmMain.txtDescripcion.setText("Sin descripción");
                }else {
                    mensajes.mostrarMensaje(frmMain.LayoutP, "Seleccione correctamente una fila\ny tambien verifique los campos", "Sin datos", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                mensajes.mostrarMensaje(frmMain.LayoutP, "Seleccione una fila", "Sin datos", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == frmMain.btnContinuarPedido){
            if (frmMain.tablaListaPedido.getRowCount() != 0) {
                String texto = frmMain.lblTotalPedido.getText();
                String textoSinPrefijo = texto.replace("S/ ", "");
                var total = Double.parseDouble(textoSinPrefijo);
                var pedido = new Pedido(id_cliente, total);
                if (pedidoDAO.anadirPedido(pedido, frmMain.LayoutP)){
                    //obtenemos el id segun el id del cliente
                    int id_pedido = pedidoDAO.idPedido(id_cliente);
                    if (id_pedido !=0){
                        for (int i = 0; i< frmMain.tablaListaPedido.getRowCount(); i++){
                            var nombre = frmMain.tablaListaPedido.getValueAt(i, 1).toString();
                            var precio = Double.parseDouble(frmMain.tablaListaPedido.getValueAt(i, 2).toString());
                            var tamanio = frmMain.tablaListaPedido.getValueAt(i, 3).toString();
                            var cantidad = Integer.parseInt(frmMain.tablaListaPedido.getValueAt(i, 4).toString());
                            var detalles = new DetallesPedido(nombre, precio, tamanio, cantidad, id_pedido);
                            pedidoDAO.anadirDetallePedido(detalles);
                        }
                        //actualizamos los detalles del pedido realizado
                        actualizarDetallePedido(id_pedido);
                        //enviamos los datos de la fecha y total al panel de pago
                        Pedido pedido1 = pedidoDAO.verPedido(id_cliente, id_pedido);
                        frmMain.lblFechaPago.setText(pedido1.getFecha());
                        frmMain.lblTotalPago.setText(String.valueOf(pedido1.getTotal()));

                        limpiarPedido();

                        //bloqueamos el panel del pedido para evitar que se pueda hacer otro pedido sin completar el actual,
                        // aunque luego sufrira actualizaciones de interfaz para mejorar estos detalles
                        frmMain.LayoutPedido.setVisible(false);
                        frmMain.LayoutPago.setVisible(true);
                        frmMain.btnConfiguracion.setEnabled(false);
                        frmMain.btnHistorial.setEnabled((false));
                        frmMain.btnPedidoMain.setEnabled(false);
                    }
                }
            }else {
                mensajes.mostrarMensaje(frmMain.LayoutPedido, "La tabla esta vacía", "Sin datos", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == frmMain.cbTamanio){
            frmMain.lblPrecioUnidad.setText("S/ " + String.format("%.2f",obtenerPrecioUnidad()));
            frmMain.lblPrecioTotal.setText("S/ " + String.format("%.2f", obtenerPrecioTotal()));
        }
        if (e.getSource() == frmMain.btnVerDescripcion){
            frmMain.txtDescripcion.setText(obtenerDescrpcion());
            frmMain.pmDescripcion.show(frmMain.btnVerDescripcion, 0, frmMain.btnVerDescripcion.getHeight());//para ver popup menu debajo del boton
        }
        if (e.getSource() == frmMain.btnQuitarPedido){
            int fila = frmMain.tablaListaPedido.getSelectedRow();
            if (fila >= 0) {
                var model = (DefaultTableModel) frmMain.tablaListaPedido.getModel();
                double subTotal = Double.parseDouble(frmMain.tablaListaPedido.getValueAt(fila, 5).toString());
                String texto = frmMain.lblTotalPedido.getText();
                String textoSinPrefijo = texto.replace("S/ ", "");
                var total = Double.parseDouble(textoSinPrefijo);
                frmMain.lblTotalPedido.setText("S/ "+String.format("%.2f", total- subTotal));
                //nombre
                frmMain.lblNombreProductoLista.setText("");
                model.removeRow(fila);
                detallePedido.remove(fila);
                frmMain.tablaListaPedido.repaint();
            }else {
                mensajes.mostrarMensaje(frmMain.LayoutP, "Seleccione una fila", "Sin datos", JOptionPane.ERROR_MESSAGE);
            }
        }

        //Pago
        if (e.getSource() == frmMain.btnFinalizarPago){
            int pregunta = mensajes.mensajeCOnfirmar(frmMain.LayoutPago, "¿Estás seguro de finalizar?", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            if (pregunta == 0) {
                int id_pedido = pedidoDAO.idPedido(id_cliente);
                if (id_pedido !=0) {
                    frmMain.LayoutPedido.setVisible(true);
                    frmMain.LayoutPago.setVisible(false);
                    frmMain.btnConfiguracion.setEnabled(true);
                    frmMain.btnHistorial.setEnabled(true);
                    frmMain.btnPedidoMain.setEnabled(true);

                    actualizarHistorial(id_cliente);
                    var nombre = frmMain.txtNombreInfoP.getText();
                    var apellidos = frmMain.txtApellidosInfoP.getText();
                    pedidoDAO.pdfPedido(id_cliente,id_pedido, nombre + " " + apellidos);
                    var model = (DefaultTableModel) frmMain.tablePago.getModel();
                    model.setRowCount(0);
                    frmMain.lblTotalPago.setText("");
                }
            }

        }
        if (e.getSource() == frmMain.btnCancelarPago){
            cancelarPedido();
        }

        //Cambio de paneles
        if (e.getSource() == frmMain.btnPedidoMain){
            if (!frmMain.LayoutPago.isVisible()){
                cambioVentanas(true, false, false, false);
            }
        }
        if (e.getSource() == frmMain.btnHistorial){
            cambioVentanas(false, false, true, false);
        }
        if (e.getSource() == frmMain.btnConfiguracion){
            cambioVentanas(false, false, false, true);
        }
        if (e.getSource() == frmMain.btnCerrarSesion){
            if (frmMain.LayoutPago.isVisible()){
                if (cancelarPedido()){
                    var consultaCliente = new ClienteDAO() ;
                    var frmRegister = new VentanaLogin();
                    new ControladorLogin(consultaCliente,frmRegister).setVisible(true);
                    dispose();
                }
            }else {
                var consultaCliente = new ClienteDAO() ;
                var frmRegister = new VentanaLogin();
                new ControladorLogin(consultaCliente,frmRegister).setVisible(true);
                dispose();
            }
        }

        //Informacion Personal
        if (e.getSource() == frmMain.btnActualizarInfoP){
            var nombre = frmMain.txtNombreInfoP.getText();
            var apellido = frmMain.txtApellidosInfoP.getText();
            var correo = frmMain.txtCorreoInfoP.getText();
            var pass = frmMain.txtPassInfoP.getText();
            if (nombre.isEmpty() && apellido.isEmpty() && correo.isEmpty() && pass.isEmpty()){
                mensajes.mostrarMensaje(frmMain.LayoutConfiguracion, "Rellene todos los campos", "Error", JOptionPane.INFORMATION_MESSAGE);
            }else {
                var cliente = new Cliente(id_cliente,nombre, apellido, correo, pass);
                if(!clienteDAO.actualizarCliente(cliente)){
                    mensajes.mostrarMensaje(frmMain.LayoutConfiguracion,"Error al actualizar los datos", "Error", JOptionPane.ERROR_MESSAGE);
                }else {
                    //actualizamos la interfaz
                    setUsuario(correo);
                    frmMain.txtPassInfoP.setText("");
                    mensajes.mostrarMensaje(frmMain.LayoutConfiguracion,"Datos actualizados", "Información Personal", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        if (e.getSource() == frmMain.btnEliminarInfoP){
            int pregunta = mensajes.mensajeCOnfirmar(frmMain.LayoutPago, "¿Quieres eliminar esta cuenta?", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
            if (pregunta == 0) {
                if (clienteDAO.borrarCliente(id_cliente)){
                    frmMain.btnCerrarSesion.doClick();
                }
            }
        }
    }

    //cambio Ventanas
    public void cambioVentanas(boolean pedido, boolean pago, boolean historial, boolean configuracion){
        frmMain.LayoutPedido.setVisible(pedido);
        frmMain.LayoutPago.setVisible(pago);
        frmMain.LayoutHistorial.setVisible(historial);
        frmMain.LayoutConfiguracion.setVisible(configuracion);
    }

    //Pedido
    public double obtenerPrecioTotal(){
        int registro = frmMain.tableProductoPedido.getSelectedRow();
        if (registro >= 0) {
            // Obtener los valores del pedido desde la vista
            var nombre = frmMain.tableProductoPedido.getValueAt(registro, 1).toString();
            var cantidad = Integer.parseInt(frmMain.spCantidadPedido.getValue().toString());
            String tamanio = (String) frmMain.cbTamanio.getSelectedItem();
            double precio = 0;
            Pizza pizzaEncontrada = null;
            for (Pizza pizza : listaPizza) {
                if (pizza.getNombre().equals(nombre)) {
                    pizzaEncontrada = pizza;
                    break; // Salir del bucle cuando se encuentra la pizza
                }
            }
            if (pizzaEncontrada != null) {
                precio = pizzaEncontrada.getPrecio();
                if (tamanio.equals("Personal"))         precio *= 0.20;
                if (tamanio.equals("Mediana"))          precio *= 0.50;
                if (tamanio.equals("Grande"))           precio *= 1.00;
                if (tamanio.equals("Familiar"))         precio *= 1.50;
                if (tamanio.equals("Super Familiar"))   precio *= 2.30;
                if (tamanio.equals("Seleccionar"))      precio *= 0.00;
            }
            return precio * cantidad;
        }else {
            return 0.00;
        }
    }
    public double obtenerPrecioUnidad(){
        int registro = frmMain.tableProductoPedido.getSelectedRow();
        if (registro >= 0) {
            // Obtener los valores del pedido desde la vista
            var nombre = frmMain.tableProductoPedido.getValueAt(registro, 1).toString();
            String tamanio = (String) frmMain.cbTamanio.getSelectedItem();
            double precio = 0;
            Pizza pizzaEncontrada = null;
            for (Pizza pizza : listaPizza) {
                if (pizza.getNombre().equals(nombre)) {
                    pizzaEncontrada = pizza;
                    break; // Salir del bucle cuando se encuentra la pizza
                }
            }
            if (pizzaEncontrada != null) {
                precio = pizzaEncontrada.getPrecio();
                if (tamanio.equals("Personal"))         precio *= 0.20;
                if (tamanio.equals("Mediana"))          precio *= 0.50;
                if (tamanio.equals("Grande"))           precio *= 1.00;
                if (tamanio.equals("Familiar"))         precio *= 1.50;
                if (tamanio.equals("Super Familiar"))   precio *= 2.30;
                if (tamanio.equals("Seleccionar"))      precio *= 0.00;
            }
            return precio;
        }else {
            return 0.00;
        }
    }
    public String obtenerDescrpcion(){
        int registro = frmMain.tableProductoPedido.getSelectedRow();
        if (registro >= 0) {
            var nombre = frmMain.tableProductoPedido.getValueAt(registro, 1).toString();
            String descripcion = "";
            Pizza pizzaEncontrada = null;
            for (Pizza pizza : listaPizza) {
                if (pizza.getNombre().equals(nombre)) {
                    pizzaEncontrada = pizza;
                    break; // Salir del bucle cuando se encuentra la pizza
                }
            }
            if (pizzaEncontrada != null) {
                descripcion = pizzaEncontrada.getDescripcion();
            }
            return descripcion;
        }else {
            return "Sin descripción";
        }
    }
    public boolean cancelarPedido(){
        int pregunta = mensajes.mensajeCOnfirmar(frmMain.LayoutPago, "¿Estás seguro de cancelar?", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        if (pregunta == 0) {
            frmMain.LayoutPedido.setVisible(true);
            frmMain.LayoutPago.setVisible(false);
            frmMain.btnConfiguracion.setEnabled(true);
            frmMain.btnHistorial.setEnabled(true);
            frmMain.btnPedidoMain.setEnabled(true);
            int id_pedido = pedidoDAO.idPedido(id_cliente);
            pedidoDAO.cancelar(id_cliente, id_pedido);
            return true;
        }
        return false;
    }
    private void limpiarPedido() {
        DefaultTableModel model = (DefaultTableModel) frmMain.tablaListaPedido.getModel();
        model.setRowCount(0);
        detallePedido.clear();
        frmMain.lblTotalPedido.setText("S/ 0.00");
        frmMain.lblNombreProductoLista.setText("");
        frmMain.lblNombreProducto.setText("");
        frmMain.lblPrecioUnidad.setText("S/ 0.00");
        frmMain.lblPrecioTotal.setText("S/ 0.00");
        frmMain.cbTamanio.setSelectedIndex(0);
        frmMain.tableProductoPedido.clearSelection();
        frmMain.txtDescripcion.setText("Sin descripción");
        frmMain.spCantidadPedido.setValue(0);
    }

    //Pago
    private void actualizarDetallePedido(int id_pedido){
        var pedidos = pedidoDAO.verDetallePedido(id_pedido);
        var modT = new DefaultTableModel(new String[]{ "Cantidad", "Nombre", "Precio","Tamaño", "Subtotal"}, pedidos.size()){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < pedidos.size(); i++) {
            var pedido = pedidos.get(i);
            var subtotal = Math.round(pedido.getPrecio() * pedido.getCantidad() * 100.0) / 100.0;
            modT.setValueAt(pedido.getCantidad(),i,0);
            modT.setValueAt(pedido.getNombre(),i,1);
            modT.setValueAt(pedido.getPrecio(),i,2);
            modT.setValueAt(pedido.getTamanio(),i,3);
            modT.setValueAt(subtotal ,i,4);
        }
        frmMain.tablePago.setModel(modT);
        //para pintar el resaltado, y las filas
        Tablas c = new Tablas();
        c.colorResaltado( new Color(255, 183, 58));
        for (int i = 0; i <  frmMain.tablePago.getColumnCount(); i++) {
            frmMain.tablePago.getColumnModel().getColumn(i).setCellRenderer(c);
        }
    }

    //Historial
    private void actualizarHistorial(int id_cliente){
        var pedidos = pedidoDAO.verHistorialPedido(id_cliente);
        var modT = new DefaultTableModel(new String[]{"Fecha", "Total"}, pedidos.size()){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < pedidos.size(); i++) {
            var pedido = pedidos.get(i);
            modT.setValueAt(pedido.getFecha(),i,0);
            modT.setValueAt(pedido.getTotal(),i,1);
        }
        frmMain.tableHistorial.setModel(modT);
        //para pintar el resaltado, y las filas
        Tablas c = new Tablas();
        c.colorResaltado( new Color(255, 183, 58));
        for (int i = 0; i <  frmMain.tableHistorial.getColumnCount(); i++) {
            frmMain.tableHistorial.getColumnModel().getColumn(i).setCellRenderer(c);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == frmMain.txtNombreInfoP || e.getSource() == frmMain.txtApellidosInfoP){
            eventos.isLetter(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == frmMain.tableProductoPedido){
            int registro = frmMain.tableProductoPedido.getSelectedRow();
            if (registro >= 0) {
                frmMain.lblNombreProducto.setText(frmMain.tableProductoPedido.getValueAt(registro, 1).toString());
                frmMain.spCantidadPedido.setValue(0);
                frmMain.lblPrecioUnidad.setText("S/ 0.00");
                frmMain.cbTamanio.setSelectedIndex(0);
            }
        }
        if (e.getSource() == frmMain.tablaListaPedido){
            int registro = frmMain.tablaListaPedido.getSelectedRow();
            if (registro >= 0) {
                frmMain.lblNombreProductoLista.setText(frmMain.tablaListaPedido.getValueAt(registro, 1).toString());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == frmMain.spCantidadPedido){
            frmMain.lblPrecioUnidad.setText("S/ " + String.format("%.2f",obtenerPrecioUnidad()));
            frmMain.lblPrecioTotal.setText("S/ " + String.format("%.2f", obtenerPrecioTotal()));
        }
    }
}