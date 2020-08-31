package com.denisse.inventario.Model.Empleado;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Area implements Serializable {

    private String id;
    private String descripcion;

    public Area(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Area() {
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

    public static List<Area> areas (){
        List<Area>list = new ArrayList<>();
        list.add(new Area("0", "- SELECCIONE UNA OPCIÓN -"));
        list.add(new Area("1", "ADMINISTRACIÓN"));
        list.add(new Area("2", "AUTOCLAVES"));
        list.add(new Area("3", "DEPARTAMENTO MÉDICO"));
        list.add(new Area("4", "EMBARQUE"));
        list.add(new Area("5", "ENCARTONADO"));
        list.add(new Area("6", "ETIQUETADO"));
        list.add(new Area("7", "EXPORTACIÓN"));
        list.add(new Area("8", "INSPECCIÓN POUCH"));
        list.add(new Area("9", "LINEA DE PRODUCCIÓN"));
        list.add(new Area("10", "LONG TOGO (ARMADO PACK)"));
        list.add(new Area("11", "MANTENIMIENTO"));
        list.add(new Area("12", "MAQUINAS DE ENLATADO"));
        list.add(new Area("13", "OPERACIONES MARÍTIMAS"));
        list.add(new Area("14", "POUCK PACK"));
        list.add(new Area("15", "PREPARACIÓN"));
        list.add(new Area("16", "RECURSOS HUMANOS"));
        list.add(new Area("17", "SANITIZACIÓN"));
        list.add(new Area("18", "SEGURIDAD INUSTRIAL"));

        return list;
    }
}
