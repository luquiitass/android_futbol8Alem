package com.example.not.futbol8alemadmin.Logica;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Usuario implements Serializable{

    protected String id_usuario;
    private String contraseña;
    private String nombre;
    private String apellido;
    private ArrayList<Comentario> comentarios;

    /**

     */
    public Usuario() {
    }



    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public void obtenerComentarios() {
        // TODO implement here
    }

}