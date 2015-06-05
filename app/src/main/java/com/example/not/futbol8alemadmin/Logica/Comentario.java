package com.example.not.futbol8alemadmin.Logica;

import java.util.*;

/**
 * 
 */
public class Comentario {

    private int id_partido;

    private String id_usuario;

    private String apellidoNombre_usuario;

    private String cuerpo;

    private Date fecha;


    public Comentario() {
    }

    /**
     *
     */

    public int getId_partido() {
        return id_partido;
    }

    public void setId_partido(int id_partido) {
        this.id_partido = id_partido;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getApellidoNombre_usuario() {
        return apellidoNombre_usuario;
    }

    public void setApellidoNombre_usuario(String apellidoNombre_usuario) {
        this.apellidoNombre_usuario = apellidoNombre_usuario;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}