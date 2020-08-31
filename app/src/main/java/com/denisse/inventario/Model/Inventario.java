package com.denisse.inventario.Model;

import com.denisse.inventario.Model.Empleado.Area;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Inventario implements Serializable {

    private String id;
    private String codigo;
    private String descripcion;
    private String tipo;
    private Area area;
    private String color;
    private String vida_util;
    private int cantidad;
    private String talla;
    private String fecha_registro;

    public Inventario(String id, String codigo, String descripcion, String tipo, Area area) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.area = area;
    }

    public Inventario(String id, String codigo, String descripcion, String tipo, Area area, String color, String vida_util, int cantidad, String talla, String fecha_registro) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.area = area;
        this.color = color;
        this.vida_util = vida_util;
        this.cantidad = cantidad;
        this.talla = talla;
        this.fecha_registro = fecha_registro;
    }

    public Inventario() {
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("codigo")
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @PropertyName("descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @PropertyName("tipo")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @PropertyName("area")
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @PropertyName("color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @PropertyName("vida_util")
    public String getVida_util() {
        return vida_util;
    }

    public void setVida_util(String vida_util) {
        this.vida_util = vida_util;
    }

    @PropertyName("cantidad")
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @PropertyName("talla")
    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    @PropertyName("fecha_registro")
    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "id='" + id + '\'' +
                ", codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", area=" + area +
                ", color='" + color + '\'' +
                ", vida_util='" + vida_util + '\'' +
                ", cantidad=" + cantidad +
                ", talla='" + talla + '\'' +
                ", fecha_registro='" + fecha_registro + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("codigo", codigo);
        result.put("descripcion", descripcion);
        result.put("tipo", tipo);
        result.put("area", area);
        result.put("color", color);
        result.put("vida_util", vida_util);
        result.put("cantidad", cantidad);
        result.put("talla", talla);
        result.put("fecha_registro", fecha_registro);
        return result;
    }
}
