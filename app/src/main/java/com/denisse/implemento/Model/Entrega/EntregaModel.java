package com.denisse.implemento.Model.Entrega;

import com.denisse.implemento.Model.Empleado.Departamento;
import com.denisse.implemento.Model.Empleado.Empleado;
import com.denisse.implemento.Model.Empleado.Puesto;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntregaModel implements Serializable {

    private String id;
    private String tipo_entrega;
    private String fecha_creacion;
    private long fecha_time_creacion;
    private String fecha_entrega;
    private long fecha_time_entrega;
    private Empleado empleado;
    private Departamento departamento;
    private Puesto puesto;
    private List<EntregaItem> entregaItems;
    private Boolean is_create;
    private Boolean is_entregado;

    public EntregaModel() {
    }

    public EntregaModel(String id, String tipo_entrega, String fecha_creacion, long fecha_time_creacion, String fecha_entrega, long fecha_time_entrega, Empleado empleado, Departamento departamento, Puesto puesto, List<EntregaItem> entregaItems, Boolean is_create, Boolean is_entregado) {
        this.id = id;
        this.tipo_entrega = tipo_entrega;
        this.fecha_creacion = fecha_creacion;
        this.fecha_time_creacion = fecha_time_creacion;
        this.fecha_entrega = fecha_entrega;
        this.fecha_time_entrega = fecha_time_entrega;
        this.empleado = empleado;
        this.departamento = departamento;
        this.puesto = puesto;
        this.entregaItems = entregaItems;
        this.is_create = is_create;
        this.is_entregado = is_entregado;
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("tipo_entrega")
    public String getTipo_entrega() {
        return tipo_entrega;
    }

    public void setTipo_entrega(String tipo_entrega) {
        this.tipo_entrega = tipo_entrega;
    }

    @PropertyName("empleado")
    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @PropertyName("entregaItems")
    public List<EntregaItem> getEntregaItems() {
        return entregaItems;
    }

    public void setEntregaItems(List<EntregaItem> entregaItems) {
        this.entregaItems = entregaItems;
    }

    @PropertyName("departamento")
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @PropertyName("puesto")
    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    @PropertyName("fecha_creacion")
    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @PropertyName("fecha_time_creacion")
    public long getFecha_time_creacion() {
        return fecha_time_creacion;
    }

    public void setFecha_time_creacion(long fecha_time_creacion) {
        this.fecha_time_creacion = fecha_time_creacion;
    }

    @PropertyName("fecha_entrega")
    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    @PropertyName("fecha_time_entrega")
    public long getFecha_time_entrega() {
        return fecha_time_entrega;
    }

    public void setFecha_time_entrega(long fecha_time_entrega) {
        this.fecha_time_entrega = fecha_time_entrega;
    }

    @PropertyName("is_create")
    public Boolean getIs_create() {
        return is_create;
    }

    public void setIs_create(Boolean is_create) {
        this.is_create = is_create;
    }

    @PropertyName("is_entregado")
    public Boolean getIs_entregado() {
        return is_entregado;
    }

    public void setIs_entregado(Boolean is_entregado) {
        this.is_entregado = is_entregado;
    }

    @Override
    public String toString() {
        return "EntregaModel{" +
                "id='" + id + '\'' +
                ", tipo_entrega='" + tipo_entrega + '\'' +
                ", fecha_creacion='" + fecha_creacion + '\'' +
                ", fecha_time_creacion=" + fecha_time_creacion +
                ", fecha_entrega='" + fecha_entrega + '\'' +
                ", fecha_time_entrega=" + fecha_time_entrega +
                ", empleado=" + empleado +
                ", departamento=" + departamento +
                ", puesto=" + puesto +
                ", is_create=" + is_create +
                ", is_entregado=" + is_entregado +
                ", entregaItems=" + entregaItems +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("tipo_entrega", tipo_entrega);
        result.put("fecha_creacion", fecha_creacion);
        result.put("fecha_time_creacion", fecha_time_creacion);
        result.put("fecha_entrega", fecha_entrega);
        result.put("fecha_time_entrega", fecha_time_entrega);
        result.put("departamento", departamento);
        result.put("is_create", is_create);
        result.put("is_entregado", is_entregado);
        result.put("puesto", puesto);
        result.put("empleado", empleado);
        result.put("entregaItems", entregaItems);
        return result;
    }
}
