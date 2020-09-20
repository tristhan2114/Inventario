package com.denisse.implemento.Model.Empleado;

import android.os.Parcel;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Empleado implements Serializable {

    private String id;
    private String ci;
    private String nombres;
    private String apellidos;
    private int edad;
    private String foto;
    private Genero genero;
    private Departamento departamento;
    private Jornada jornada;
    private Puesto puesto;
    private boolean estado;

    /*public Empleado(String id, String ci, String nombres, String apellidos, int edad, String foto, Genero genero, Departamento departamento, Jornada jornada, boolean estado) {
        this.id = id;
        this.ci = ci;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.foto = foto;
        this.genero = genero;
        this.departamento = departamento;
        this.jornada = jornada;
        this.estado = estado;
    }*/

    public Empleado(String id, String ci, String nombres, String apellidos, int edad, String foto, Genero genero, Departamento departamento, Jornada jornada, Puesto puesto, boolean estado) {
        this.id = id;
        this.ci = ci;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.foto = foto;
        this.genero = genero;
        this.departamento = departamento;
        this.jornada = jornada;
        this.puesto = puesto;
        this.estado = estado;
    }

    public Empleado() {
    }

    protected Empleado(Parcel in) {
        id = in.readString();
        ci = in.readString();
        nombres = in.readString();
        apellidos = in.readString();
        edad = in.readInt();
        foto = in.readString();
        estado = in.readByte() != 0;
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("ci")
    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    @PropertyName("nombres")
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @PropertyName("apellidos")
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @PropertyName("edad")
    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @PropertyName("foto")
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @PropertyName("genero")
    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    @PropertyName("departamento")
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @PropertyName("jornada")
    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    @PropertyName("estado")
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @PropertyName("puesto")
    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id='" + id + '\'' +
                ", ci='" + ci + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", edad=" + edad +
                ", foto='" + foto + '\'' +
                ", genero=" + genero +
                ", departamento=" + departamento +
                ", jornada=" + jornada +
                ", puesto=" + puesto +
                ", estado=" + estado +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("ci", ci);
        result.put("nombres", nombres);
        result.put("apellidos", apellidos);
        result.put("edad", edad);
        result.put("foto", foto);
        result.put("genero", genero);
        result.put("departamento", departamento);
        result.put("jornada", jornada);
        result.put("puesto", puesto);
        result.put("estado", estado);
        return result;
    }

}
