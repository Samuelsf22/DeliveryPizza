package org.example.Controlador;

import org.example.Main;
import org.example.Modelo.*;
import org.example.Vista.VentanaLogin;
import org.example.Vista.VentanaMain;
import org.example.Vista.VentanaPizza;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class ControladorLogin extends JFrame implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private static  VentanaLogin frmLogin = new VentanaLogin();
    private final ClienteDAO consultaCliente;
    private final Eventos eventos = new Eventos();
    private final Color amarillo1 = new Color(209,154,64);
    private final Color amarillo2 = new Color(250, 195, 105);
    private int x;
    private int y;
    public ControladorLogin(ClienteDAO consultaCliente, VentanaLogin frmLogin) {
        this.consultaCliente = consultaCliente;
        ControladorLogin.frmLogin = frmLogin;
        ControladorLogin.frmLogin.lblBtnCerrar.addMouseListener(this);
        ControladorLogin.frmLogin.LayoutTitleBar.addMouseListener(this);
        ControladorLogin.frmLogin.LayoutTitleBar.addMouseMotionListener(this);

        ControladorLogin.frmLogin.lblRegister.addMouseListener(this);
        ControladorLogin.frmLogin.lblIniciarSesion.addMouseListener(this);
        ControladorLogin.frmLogin.btnLogin.addActionListener(this);
        ControladorLogin.frmLogin.btnRegistrar.addActionListener(this);

        ControladorLogin.frmLogin.txtNombreRegistro.addKeyListener(this);
        ControladorLogin.frmLogin.txtApellidosRegistro.addKeyListener(this);

        verLogin();
    }
    public void verLogin(){
        setContentPane(frmLogin.LayoutPrincipal) ;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
        setUndecorated(true);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        // Establece el icono de la ventana
        Image icono = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/img/logo-trnasparente.png"));
        setIconImage(icono);

        //
        frmLogin.LayoutRegister.setVisible(false);

        Color amarillo = new Color(255,183,58);
        frmLogin.txtNombreRegistro       .setBorder(new LineBorder(amarillo,1));
        frmLogin.txtApellidosRegistro    .setBorder(new LineBorder(amarillo,1));
        frmLogin.txtCorreoRegistro       .setBorder(new LineBorder(amarillo,1));
        frmLogin.txtContrasenaRegistro   .setBorder(new LineBorder(amarillo,1));
        frmLogin.txtContrasenaLogin      .setBorder(new LineBorder(amarillo,1));
        frmLogin.txtCorreoLogin          .setBorder(new LineBorder(amarillo,1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== frmLogin.btnRegistrar){
            var nombre = frmLogin.txtNombreRegistro.getText();
            var apellidos = frmLogin.txtApellidosRegistro.getText();
            var email = frmLogin.txtCorreoRegistro.getText();
            var pass = frmLogin.txtContrasenaRegistro.getText();
            var cliente = new Cliente(nombre, apellidos, email, pass);
            if (consultaCliente.registrarCliente(cliente, frmLogin.LayoutPrincipal)){
                limpiarRegister();
                limpiarLogin();
                verLayoutLogin();
            }

        }
        if (e.getSource()== frmLogin.btnLogin){
            var correo = frmLogin.txtCorreoLogin.getText();
            var contrasenia = frmLogin.txtContrasenaLogin.getText();
            var cliente = new Cliente(correo, contrasenia);
            if (correo.equals("admin") && contrasenia.equals("admin")){
                var pizzaDAO = new PizzaDAO() ;
                var frmPizza = new VentanaPizza();
                var mensajes = new Mensajes();
                new ControladorPizza(pizzaDAO,frmPizza, eventos, mensajes).setVisible(true);
                dispose();
            }else {
                if (consultaCliente.consultarUsuario(cliente, frmLogin.LayoutPrincipal)){
                    limpiarLogin();
                    var frmMain = new VentanaMain();
                    var pedidoDAO = new PedidoDAO();
                    var pzDAO = new PizzaDAO();
                    var clienteDAO = new ClienteDAO();
                    var mensajes = new Mensajes();
                    var crtl = new ControladorMain(frmMain, eventos, mensajes, pedidoDAO, pzDAO, clienteDAO);
                    crtl.setUsuario(correo);
                    crtl.setVisible(true);
                    dispose();
                }
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == frmLogin.lblBtnCerrar)      System.exit(0);
        if (e.getSource() == frmLogin.lblIniciarSesion)  verLayoutLogin(); limpiarLogin();
        if (e.getSource() == frmLogin.lblRegister)       verLayoutRegister(); limpiarRegister();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        despintarLbloBtn(e);
        if (e.getSource() == frmLogin.LayoutTitleBar){
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pintarLbloBtn(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        pintarLbloBtn(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        despintarLbloBtn(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Devuelve la coordenada X en pantalla de un componente o ventana en particular
        if (e.getSource() == frmLogin.LayoutTitleBar){
            int xx= e.getXOnScreen();
            int yy= e.getYOnScreen();
            setLocation(xx-x,yy-y);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void despintarLbloBtn(MouseEvent e){
        if (e.getSource() == frmLogin.lblBtnCerrar){
            frmLogin.lblBtnCerrar.setIcon(null);
        }
        if (e.getSource() == frmLogin.lblIniciarSesion){
            frmLogin.lblIniciarSesion.setForeground(amarillo1);
        }
        if (e.getSource() == frmLogin.lblRegister){
            frmLogin.lblRegister.setForeground(amarillo1);
        }
    }
    private void pintarLbloBtn(MouseEvent e){
        if (e.getSource() == frmLogin.lblBtnCerrar){
            Icon icono = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/bntCerrar.png"))).getImage());
            frmLogin.lblBtnCerrar.setIcon(icono);
        }
        if (e.getSource() == frmLogin.lblIniciarSesion){
            frmLogin.lblIniciarSesion.setForeground(amarillo2);
        }
        if (e.getSource() == frmLogin.lblRegister){
            frmLogin.lblRegister.setForeground(amarillo2);
        }
    }
    private void verLayoutLogin(){
        frmLogin.LayoutRegister.setVisible(false);
        frmLogin.LayoutLogin.setVisible(true);
    }
    private void verLayoutRegister(){
        frmLogin.LayoutLogin.setVisible(false);
        frmLogin.LayoutRegister.setVisible(true);
    }
    private void limpiarLogin(){
        frmLogin.txtContrasenaLogin.setText("");
        frmLogin.txtCorreoLogin.setText("");
    }
    private void limpiarRegister(){
        frmLogin.txtNombreRegistro.setText("");
        frmLogin.txtApellidosRegistro.setText("");
        frmLogin.txtContrasenaRegistro.setText("");
        frmLogin.txtCorreoRegistro.setText("");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == frmLogin.txtNombreRegistro || e.getSource() == frmLogin.txtApellidosRegistro){
            eventos.isLetter(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
