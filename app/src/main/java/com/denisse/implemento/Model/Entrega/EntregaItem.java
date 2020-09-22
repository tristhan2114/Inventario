package com.denisse.implemento.Model.Entrega;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class EntregaItem implements Serializable {

    private String fecha;
    private String talla;
    private String descripcion;
    private Boolean nuevo;
    private Boolean reposicion;
    private String motivo_cambio;
    private int cantidad;

    public EntregaItem(String fecha, String talla, String descripcion, Boolean nuevo, Boolean reposicion, String motivo_cambio, int cantidad) {
        this.fecha = fecha;
        this.talla = talla;
        this.descripcion = descripcion;
        this.nuevo = nuevo;
        this.reposicion = reposicion;
        this.motivo_cambio = motivo_cambio;
        this.cantidad = cantidad;
    }

    public EntregaItem() {
    }

    @PropertyName("fecha")
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @PropertyName("talla")
    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    @PropertyName("descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @PropertyName("motivo_cambio")
    public String getMotivo_cambio() {
        return motivo_cambio;
    }

    public void setMotivo_cambio(String motivo_cambio) {
        this.motivo_cambio = motivo_cambio;
    }

    @PropertyName("nuevo")
    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    @PropertyName("reposicion")
    public Boolean getReposicion() {
        return reposicion;
    }

    public void setReposicion(Boolean reposicion) {
        this.reposicion = reposicion;
    }

    @PropertyName("cantidad")
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "EntregaItem{" +
                "fecha='" + fecha + '\'' +
                ", talla='" + talla + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nuevo=" + nuevo +
                ", reposicion=" + reposicion +
                ", cantidad=" + cantidad +
                ", motivo_cambio='" + motivo_cambio + '\'' +
                '}';
    }
}
