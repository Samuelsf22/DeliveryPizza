package org.example.Modelo;

public class Pedido {
    int id;
    int id_cliente;
    String fecha;
    double total;

    public Pedido(int id, int id_cliente, String fecha, double total) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.fecha = fecha;
        this.total = total;
    }

    public Pedido(int id_cliente, double total) {
        this.id_cliente = id_cliente;
        this.total = total;
    }

    public Pedido(String fecha, double total) {
        this.fecha = fecha;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

