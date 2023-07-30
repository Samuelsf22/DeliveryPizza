package org.example.Controlador;

import org.example.Main;
import org.example.Modelo.*;
import org.example.Vista.VentanaPizza;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class ControladorPizza extends JFrame implements ActionListener, MouseListener, KeyListener {
    private static VentanaPizza frmRegister = new VentanaPizza();     //interfaz
    private final PizzaDAO pizzaDAO;  //modelo
    private final Eventos eventos;
    private final Mensajes mensajes;

    public ControladorPizza(PizzaDAO pizzaDAO, VentanaPizza frmRegister, Eventos eventos, Mensajes mensajes) {
        this.pizzaDAO = pizzaDAO;
        ControladorPizza.frmRegister = frmRegister;
        this.eventos = eventos;
        this.mensajes = mensajes;
        ControladorPizza.frmRegister.btnAnadir.addActionListener(this);
        ControladorPizza.frmRegister.btnModificar.addActionListener(this);
        ControladorPizza.frmRegister.btnEliminar.addActionListener(this);
        ControladorPizza.frmRegister.btnActualizar.addActionListener(this);
        ControladorPizza.frmRegister.tableRegister.addMouseListener(this);

        ControladorPizza.frmRegister.txtIdRegister.addKeyListener(this);
        ControladorPizza.frmRegister.txtPrecioRegister.addKeyListener(this);
        ControladorPizza.frmRegister.txtNombreRegister.addKeyListener(this);

        verVentana();
    }
    public void verVentana(){
        setContentPane(frmRegister.LayoutRegisterPrincipal) ;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);

        pack();
        setLocationRelativeTo(null);

        String [] tamanio = {"Seleccionar","Personal","Mediana","Grande","Familiar","Super Familiar"};

        for (String tamano: tamanio) {
            frmRegister.cbTamanio.addItem(tamano);
        }
        actualizarRegistro();
        Color amarillo = new Color(255, 183, 58);
        Color negro1 = new Color(35,35,35);

        frmRegister.txtIdRegister.setBorder(new LineBorder(amarillo, 1));
        frmRegister.txtNombreRegister.setBorder(new LineBorder(amarillo, 1));
        frmRegister.txtPrecioRegister.setBorder(new LineBorder(amarillo, 1));
        frmRegister.cbTamanio.setBorder(new LineBorder(amarillo, 1));
        frmRegister.txtDescripcionRegister.setBorder(new LineBorder(amarillo, 1));

        frmRegister.LayoutScrollDescripcion.getViewport().setBackground(negro1);
        frmRegister.LayoutScrollDescripcion.setBorder(new LineBorder(Color.BLACK,0));

        frmRegister.ScrollTable.getViewport().setBackground(negro1);
        frmRegister.ScrollTable.setBorder(new LineBorder(Color.BLACK,0));

        frmRegister.tableRegister.getTableHeader().setBackground(negro1);
        frmRegister.tableRegister.getTableHeader().setForeground(Color.WHITE);

        frmRegister.txtDescripcionRegister.setLineWrap(true); // Habilitar el ajuste de línea automático
        frmRegister.txtDescripcionRegister.setWrapStyleWord(true); // Hacer que las palabras se ajusten a las líneas

        Image icono = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/img/logo-trnasparente.png"));
        setIconImage(icono);

        frmRegister.cbTamanio.setRenderer(new DefaultListCellRenderer() {
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

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frmRegister.btnAnadir){
            if (verificar()){
                String id = frmRegister.txtIdRegister.getText();
                String nombre = frmRegister.txtNombreRegister.getText();
                double precio = Double.parseDouble(frmRegister.txtPrecioRegister.getText());
                String tamanio = (String) frmRegister.cbTamanio.getSelectedItem();
                String descripcion = frmRegister.txtDescripcionRegister.getText();
                var registerPizza = new Pizza(id, nombre, precio, tamanio, descripcion);
                if (pizzaDAO.anadirPizza(registerPizza, frmRegister.LayoutRegister)){
                    actualizarRegistro();
                    limpiar();
                }
            }
        }
        if (e.getSource() == frmRegister.btnModificar){
            if (verificar()){
                var id = frmRegister.txtIdRegister.getText();
                var nombre = frmRegister.txtNombreRegister.getText();
                var precio = Double.parseDouble(frmRegister.txtPrecioRegister.getText());
                var tamanio = (String) frmRegister.cbTamanio.getSelectedItem();
                var descripcion = frmRegister.txtDescripcionRegister.getText();
                var registerPizza = new Pizza(id, nombre, precio, tamanio, descripcion);
                if (pizzaDAO.actualizarPizza(registerPizza)){
                    actualizarRegistro();
                    limpiar();
                }
            }
        }
        if (e.getSource() == frmRegister.btnEliminar){
            if (!frmRegister.txtIdRegister.getText().isEmpty()){
                var id = frmRegister.txtIdRegister.getText();
                if (pizzaDAO.borrarPizza(id)){
                    actualizarRegistro();
                    limpiar();
                }
            }else {
                JOptionPane.showMessageDialog(frmRegister.LayoutRegister, "Escriba un iD(6 digitos)");
            }
        }
        if (e.getSource() == frmRegister.btnActualizar){
            actualizarRegistro();
        }
    }
    private void limpiar(){
        frmRegister.txtIdRegister.setText("");
        frmRegister.txtNombreRegister.setText("");
        frmRegister.txtPrecioRegister.setText("");
        frmRegister.cbTamanio.setSelectedIndex(0);
        frmRegister.txtDescripcionRegister.setText("");
    }
    private boolean verificar(){
        String mensajeError = "";
        if (frmRegister.txtIdRegister.getText().length()>6){
            mensajeError += "Rellene el ID (6 dígitos)\n";
        }
        if (frmRegister.txtNombreRegister.getText().isEmpty()){
            mensajeError += "Rellene el nombre\n";
        }
        if (frmRegister.txtPrecioRegister.getText().isEmpty()){
            mensajeError += "Rellene el precio\n";
        }
        if (Objects.equals(frmRegister.cbTamanio.getSelectedItem(), "Seleccionar")){
            mensajeError += "Seleccione un tamaño\n";
        }

        if (mensajeError.isEmpty()){
            return true;
        }else {
            JOptionPane.showMessageDialog(frmRegister.LayoutRegister, mensajeError);
            return false;
        }
    }
    public void actualizarRegistro(){
        var registerPizzas = pizzaDAO.verPizza();
        var modT = new DefaultTableModel(new String[]{"ID","Nombre","Precio","Tamaño","Descripción"}, registerPizzas.size()){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < registerPizzas.size(); i++) {
            var registerPizza = registerPizzas.get(i);
            modT.setValueAt(registerPizza.getId(),i,0);
            modT.setValueAt(registerPizza.getNombre(),i,1);
            modT.setValueAt(registerPizza.getPrecio(),i,2);
            modT.setValueAt(registerPizza.getTamanio(),i,3);
            modT.setValueAt(registerPizza.getDescripcion(),i,4);

        }
        frmRegister.tableRegister.setModel(modT);

        Tablas c = new Tablas();
        c.colorResaltado( new Color (255, 183, 58));
        for (int i = 0; i <  frmRegister.tableRegister.getColumnCount(); i++) {
            frmRegister.tableRegister.getColumnModel().getColumn(i).setCellRenderer(c);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int resgistro = frmRegister.tableRegister.getSelectedRow();//el resgistro seleccionado
        if (resgistro>=0){
            frmRegister.txtIdRegister.setText(frmRegister.tableRegister.getValueAt(resgistro, 0).toString());
            frmRegister.txtNombreRegister.setText(frmRegister.tableRegister.getValueAt(resgistro, 1).toString());
            frmRegister.txtPrecioRegister.setText(frmRegister.tableRegister.getValueAt(resgistro, 2).toString());
            frmRegister.cbTamanio.setSelectedItem(frmRegister.tableRegister.getValueAt(resgistro, 3).toString());
            frmRegister.txtDescripcionRegister.setText(frmRegister.tableRegister.getValueAt(resgistro, 4).toString());
        }else {
            JOptionPane.showMessageDialog(frmRegister.LayoutRegister, "Seleccione una fila");
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == frmRegister.txtIdRegister) eventos.isUpperCasOrDigit(e, 6);
        if (e.getSource() == frmRegister.txtPrecioRegister) eventos.isDigitDecimal(e,8,2);
        if (e.getSource() == frmRegister.txtNombreRegister) eventos.isLetter(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
