package com.denisse.implemento.Model.Empleado;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Puesto implements Serializable {

    private String id;
    private String descripcion;

    public Puesto(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Puesto() {
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
        return "Puesto{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static List<Puesto> puestos (){ //
        List<Puesto>list = new ArrayList<>();
        list.add(new Puesto("0", "- SELECCIONE UNA OPCIÓN -"));
        list.add(new Puesto("1", "ANALISTA ADMINISTRATIVA FINANCIERA"));
        list.add(new Puesto("2", "OBRERO DE RETAILS"));
        list.add(new Puesto("3", "OPERADOR DE ALLOYD - LTG"));
        list.add(new Puesto("4", "SUPERVISOR DE LUNCH TO GO"));
        list.add(new Puesto("5", "OBRERO DE AUTOCLAVE"));
        list.add(new Puesto("6", "OPERADOR DE AUTOCLAVE"));
        list.add(new Puesto("7", "SUPERVISOR DE TURNO - AUTOCLAVE"));
        list.add(new Puesto("8", "ASISTENTE DE SEGURIDAD INTEGRAL"));
        list.add(new Puesto("9", "GERENTE DE SEGURIDAD INTEGRAL"));
        list.add(new Puesto("10", "INSPECTOR DE SEGURIDAD INDUSTRIAL"));
        list.add(new Puesto("11", "AUXILIAR DE BODEGA"));
        list.add(new Puesto("12", "OBRERO DE EMBARQUE"));
        list.add(new Puesto("13", "ANALISTA DE COMERCIO EXTERIOR"));
        list.add(new Puesto("14", "ANALISTA DE LABORATORIO - P.T."));
        list.add(new Puesto("15", "AUXILIAR DE LIMPIEZA - BAÑOS"));
        list.add(new Puesto("16", "OBRERO DE SANITIZACIÓN"));
        list.add(new Puesto("17", "ASISTENTE DE NÓMINA SENIOR"));
        list.add(new Puesto("18", "PASANTE DE RECURSOS HUMANOS"));
        list.add(new Puesto("19", "SUPERVISOR DE NÓMINA"));
        list.add(new Puesto("20", "ASISTENTE DE CAPACITACIÓN Y DESARROLLO"));
        list.add(new Puesto("21", "ANALISTA DE PROCESOS DE CALIDAD"));

        return list;
    }

    public static List<Puesto> roles (){
        List<Puesto>list = new ArrayList<>();
        list.add(new Puesto("0", "- SELECCIONE UNA OPCIÓN -"));
        list.add(new Puesto("1", "Asistente"));
        list.add(new Puesto("2", "Inspector"));
        list.add(new Puesto("3", "Jefe"));
        list.add(new Puesto("4", "Administrador"));

        return list;
    }
}
