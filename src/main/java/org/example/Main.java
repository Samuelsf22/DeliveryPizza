package org.example;

import org.example.Controlador.ControladorLogin;
import org.example.Modelo.ClienteDAO;
import org.example.Vista.VentanaLogin;

public class Main {
    public static void main(String[] args) {
        var consultaCliente = new ClienteDAO() ;
        var frmRegister = new VentanaLogin();
        new ControladorLogin(consultaCliente,frmRegister).setVisible(true);
    }
}