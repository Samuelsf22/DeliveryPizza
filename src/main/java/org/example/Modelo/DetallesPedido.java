package org.example.Modelo;

public class DetallesPedido {
    int id;
    String nombre;
    double precio;
    String tamanio;
    int cantidad;
    int idPedido;

    public DetallesPedido(int id, String nombre, double precio, String tamanio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.tamanio = tamanio;
        this.cantidad = cantidad;
    }

    public DetallesPedido(String nombre, double precio, int cantidad, int idPedido) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idPedido = idPedido;
    }

    public DetallesPedido(String nombre, double precio, String tamanio, int cantidad, int idPedido) {
        this.nombre = nombre;
        this.precio = precio;
        this.tamanio = tamanio;
        this.cantidad = cantidad;
        this.idPedido = idPedido;
    }

    public DetallesPedido(int id, String nombre, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public DetallesPedido(int id, String nombre, double precio, int cantidad, int idPedido) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idPedido = idPedido;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
}
