package com.denisse.implemento.Model.Empleado;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jornada implements Serializable {

    private String id;
    private String descripcion;

    public Jornada(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Jornada() {
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

    public static List<Jornada>jornadas(){
        List<Jornada>list = new ArrayList<>();
        list.add(new Jornada("0", "- SELECCIONE UNA OPCIÓN -"));
        list.add(new Jornada("1", "7am - 4pm"));
        list.add(new Jornada("2", "4pm - 7am"));
        //list.add(new Jornada("3", "10pm - 7am"));
        return list;
    }
}
