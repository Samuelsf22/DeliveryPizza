package org.example.Modelo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PizzaDAO {
    public boolean anadirPizza (Pizza registerPizza, JPanel panel){
        Connection conn = Conexion.getConexion();
        String sql = "insert into Pizza values (?,?,?,?,?);";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setString(1,registerPizza.getId());
            pstms.setString(2,registerPizza.getNombre());
            pstms.setDouble(3,registerPizza.getPrecio());
            pstms.setString(4,registerPizza.getTamanio());
            pstms.setString(5,registerPizza.getDescripcion());
            pstms.executeUpdate();
            conn.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public ArrayList<Pizza> verPizza() {
        Connection conn = Conexion.getConexion();
        var registerPizzas = new ArrayList<Pizza>();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from Pizza;");
            while (rs.next()) {
                var registerPizza = new Pizza(rs.getString("id"),rs.getString("nombre"),
                        rs.getDouble("precio"),rs.getString("tamaño"),rs.getString("descripcion"));
                registerPizzas.add(registerPizza);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registerPizzas;
    }
    public boolean actualizarPizza (Pizza registerPizza){
        Connection conn = Conexion.getConexion();
        String sql = "update Pizza set nombre = ?, precio = ?, tamaño = ?, descripcion = ? where id = ?;";
        try {
            var pstms = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstms.setString(1,registerPizza.getNombre());
            pstms.setDouble(2,registerPizza.getPrecio());
            pstms.setString(3,registerPizza.getTamanio());
            pstms.setString(4,registerPizza.getDescripcion());
            pstms.setString(5,registerPizza.getId());
            pstms.executeUpdate();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean borrarPizza(String id){
        Connection conn = Conexion.getConexion();
        String sql = "delete from Pizza where id = ? ;";
        try {
            var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,id);
            pstmt.executeUpdate();
            conn.close();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
