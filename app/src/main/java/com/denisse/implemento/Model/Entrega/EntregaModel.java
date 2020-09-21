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
    private Empleado empleado;
    private Departamento departamento;
    private Puesto puesto;
    private List<EntregaItem> entregaItems;

    public EntregaModel() {
    }

    public EntregaModel(String id, String tipo_entrega, String fecha_creacion, Empleado empleado, Departamento departamento, Puesto puesto, List<EntregaItem> entregaItems) {
        this.id = id;
        this.tipo_entrega = tipo_entrega;
        this.fecha_creacion = fecha_creacion;
        this.empleado = empleado;
        this.departamento = departamento;
        this.puesto = puesto;
        this.entregaItems = entregaItems;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("tipo_entrega", tipo_entrega);
        result.put("fecha_creacion", fecha_creacion);
        result.put("departamento", departamento);
        result.put("puesto", puesto);
        result.put("empleado", empleado);
        result.put("entregaItems", entregaItems);
        return result;
    }
}
