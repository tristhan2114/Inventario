package com.denisse.implemento.Model.Empleado;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Departamento implements Serializable {

    private String id;
    private String descripcion;

    public Departamento(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Departamento() {
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
        return "Departamento{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static List<Departamento> areas (){ //
        List<Departamento>list = new ArrayList<>();
        list.add(new Departamento("0", "- SELECCIONE UNA OPCIÓN -"));
        list.add(new Departamento("1", "ADMINISTRACIÓN"));
        list.add(new Departamento("2", "AUTOCLAVES"));
        list.add(new Departamento("3", "DEPARTAMENTO MÉDICO"));
        list.add(new Departamento("4", "EMBARQUE"));
        list.add(new Departamento("5", "ENCARTONADO"));
        list.add(new Departamento("6", "ETIQUETADO"));
        list.add(new Departamento("7", "EXPORTACIÓN"));
        list.add(new Departamento("8", "INSPECCIÓN POUCH"));
        list.add(new Departamento("9", "LINEA DE PRODUCCIÓN"));
        list.add(new Departamento("10", "LONG TOGO (ARMADO PACK)"));
        list.add(new Departamento("11", "MANTENIMIENTO"));
        list.add(new Departamento("12", "MAQUINAS DE ENLATADO"));
        list.add(new Departamento("13", "OPERACIONES MARÍTIMAS"));
        list.add(new Departamento("14", "POUCK PACK"));
        list.add(new Departamento("15", "PREPARACIÓN"));
        list.add(new Departamento("16", "RECURSOS HUMANOS"));
        list.add(new Departamento("17", "SANITIZACIÓN"));
        list.add(new Departamento("18", "SEGURIDAD INUSTRIAL"));
        list.add(new Departamento("19", "CONTABILIDAD"));

        return list;
    }

    public static List<Departamento> roles (){
        List<Departamento>list = new ArrayList<>();
        list.add(new Departamento("0", "- SELECCIONE UNA OPCIÓN -"));
        list.add(new Departamento("1", "Asistente"));
        list.add(new Departamento("2", "Inspector"));
        list.add(new Departamento("3", "Jefe"));
        list.add(new Departamento("4", "Administrador"));

        return list;
    }
}
