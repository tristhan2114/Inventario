package com.denisse.implemento.Utils.Alarma;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Alarma implements Serializable {

    private String id;
    private String fecha_creacion;
    private String titulo;
    private String mensaje;
    private String type_entrega;
    private String id_entrega;
    private long fecha_entrega_time;
    private boolean estado;

    public Alarma() {
    }

    public Alarma(String id, String fecha_creacion, String titulo, String mensaje, String type_entrega, String id_entrega, long fecha_entrega_time, boolean estado) {
        this.id = id;
        this.fecha_creacion = fecha_creacion;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.type_entrega = type_entrega;
        this.id_entrega = id_entrega;
        this.fecha_entrega_time = fecha_entrega_time;
        this.estado = estado;
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("fecha_creacion")
    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @PropertyName("titulo")
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @PropertyName("mensaje")
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @PropertyName("type_entrega")
    public String getType_entrega() {
        return type_entrega;
    }

    public void setType_entrega(String type_entrega) {
        this.type_entrega = type_entrega;
    }

    @PropertyName("id_entrega")
    public String getId_entrega() {
        return id_entrega;
    }

    public void setId_entrega(String id_entrega) {
        this.id_entrega = id_entrega;
    }

    @PropertyName("fecha_entrega_time")
    public long getFecha_entrega_time() {
        return fecha_entrega_time;
    }

    public void setFecha_entrega_time(long fecha_entrega_time) {
        this.fecha_entrega_time = fecha_entrega_time;
    }

    @PropertyName("estado")
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("fecha_creacion", fecha_creacion);
        result.put("titulo", titulo);
        result.put("mensaje", mensaje);
        result.put("type_entrega", type_entrega);
        result.put("id_entrega", id_entrega);
        result.put("fecha_entrega_time", fecha_entrega_time);
        result.put("estado", estado);
        return result;
    }

    @Override
    public String toString() {
        return "Alarma{" +
                "id='" + id + '\'' +
                ", fecha_creacion='" + fecha_creacion + '\'' +
                ", titulo='" + titulo + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", type_entrega='" + type_entrega + '\'' +
                ", id_entrega='" + id_entrega + '\'' +
                ", fecha_entrega_time=" + fecha_entrega_time +
                ", estado=" + estado +
                '}';
    }
}
