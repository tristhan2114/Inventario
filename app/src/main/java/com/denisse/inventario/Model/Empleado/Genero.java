package com.denisse.inventario.Model.Empleado;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Genero implements Serializable {

    private String id;
    private String descripcion;

    public Genero(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Genero() {
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String strToString() {
        return "Area{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static List<Genero> generos(){
        List<Genero> list = new ArrayList<>();
        list.add(new Genero("1", "- SELECCIONE UNA OPCIÓN -"));
        list.add(new Genero("1", "Masculino"));
        list.add(new Genero("2", "Femenino"));
        return list;
    }
}
