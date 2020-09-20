package com.denisse.implemento.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Administracion implements Serializable {

    private String id;
    private String name_user;
    private String correo;
    private String contrasenia;
    private String rol;
    private boolean activo;

    public Administracion(String id, String name_user, String correo, String contrasenia, String rol, boolean activo) {
        this.id = id;
        this.name_user = name_user;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.activo = activo;
    }

    public Administracion() {
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("name_user")
    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    @PropertyName("correo")
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @PropertyName("contrasenia")
    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @PropertyName("rol")
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @PropertyName("activo")
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Administracion{" +
                "id='" + id + '\'' +
                ", name_user='" + name_user + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name_user", name_user);
        result.put("correo", correo);
        result.put("contrasenia", contrasenia);
        result.put("rol", rol);
        result.put("activo", activo);
        return result;
    }
}
