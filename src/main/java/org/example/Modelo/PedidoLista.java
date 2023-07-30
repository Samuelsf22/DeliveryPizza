package org.example.Modelo;

public class PedidoLista {
    String id;
    String nombre;
    double precio;
    String tamanio;
    int cantidad;
    double subTotal;

    public PedidoLista(String id, String nombre, double precio, String tamanio, int cantidad, double subTotal) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.tamanio = tamanio;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
