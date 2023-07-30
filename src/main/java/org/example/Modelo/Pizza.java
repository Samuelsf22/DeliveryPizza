package org.example.Modelo;

public class Pizza {
    String id;
    String nombre;
    double precio;
    String tamanio;
    String descripcion;

    public Pizza(double precio, String tamanio) {
        this.precio = precio;
        this.tamanio = tamanio;
    }

    public Pizza(String id, String nombre, double precio, String tamanio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.tamanio = tamanio;
    }

    public Pizza(String id, String nombre, double precio, String tamanio, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.tamanio = tamanio;
        this.descripcion = descripcion;
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

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
