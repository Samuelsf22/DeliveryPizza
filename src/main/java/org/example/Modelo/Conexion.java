package org.example.Modelo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String driver ="com.mysql.cj.jdbc.Driver";
    private static final String user=   "";
    private static final String pass=   "";
    private static final String url =   "";

    public static Connection getConexion(){
        Connection conn = null;
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);
        }catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        return conn;
    }
}
