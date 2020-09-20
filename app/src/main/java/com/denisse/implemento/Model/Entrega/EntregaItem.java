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

    public EntregaItem(String fecha, String talla, String descripcion, Boolean nuevo, Boolean reposicion, String motivo_cambio) {
        this.fecha = fecha;
        this.talla = talla;
        this.descripcion = descripcion;
        this.nuevo = nuevo;
        this.reposicion = reposicion;
        this.motivo_cambio = motivo_cambio;
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

    @PropertyName("nuevo")
    public Boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    @PropertyName("recepcion")
    public Boolean isReposicion() {
        return reposicion;
    }

    public void setReposicion(Boolean reposicion) {
        this.reposicion = reposicion;
    }

    @PropertyName("motivo_cambio")
    public String getMotivo_cambio() {
        return motivo_cambio;
    }

    public void setMotivo_cambio(String motivo_cambio) {
        this.motivo_cambio = motivo_cambio;
    }

    @Override
    public String toString() {
        return "EntregaItem{" +
                "fecha='" + fecha + '\'' +
                ", talla='" + talla + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nuevo=" + nuevo +
                ", reposicion=" + reposicion +
                ", motivo_cambio='" + motivo_cambio + '\'' +
                '}';
    }
}
