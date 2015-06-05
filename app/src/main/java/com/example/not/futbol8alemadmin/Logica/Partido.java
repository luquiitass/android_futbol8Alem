package com.example.not.futbol8alemadmin.Logica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Partido {

    protected int id_partido;
    private String equipoLocal;
    private String equipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private String canchaeDe;
    private String direccion;
    private String feha;
    private String hora;
    private String ganador;
    private Boolean jugado;
    private ArrayList<Comentario> comentarios;


    public Partido() {
    }

    public Partido(String equipoLocal, String equipoVisitante, String canchaeDe, String direccion, String feha, String hora) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.canchaeDe = canchaeDe;
        this.direccion = direccion;
        this.feha = formatBDFecha(feha);
        this.hora = hora;
    }

    public Partido(int id_partido, String equipoLocal, String equipoVisitante, int golesLocal, int golesVisitante, String canchaeDe, String direccion, String feha, String hora, String ganador, Boolean jugado) {
        this.id_partido = id_partido;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.canchaeDe = canchaeDe;
        this.direccion = direccion;
        this.feha = formatAPPFecha(feha);
        this.hora = hora;
        this.ganador = ganador;
        this.jugado = jugado;
        this.comentarios=new ArrayList<Comentario>();
    }

    public int getId_partido() {
        return id_partido;
    }

    public void setId_partido(int id_partido) {
        this.id_partido = id_partido;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public String getCanchaeDe() {
        return canchaeDe;
    }

    public void setCanchaeDe(String canchaeDe) {
        this.canchaeDe = canchaeDe;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFeha() {
        return feha;
    }

    public void setFeha(String feha) {
        this.feha = feha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public Boolean getJugado() {
        return jugado;
    }

    public void setJugado(Boolean jugado) {
        this.jugado = jugado;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    private String formatAPPFecha(String fecha){
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatoSalida= new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar= Calendar.getInstance();

        try {
            calendar.setTime(format.parse(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  formatoSalida.format(calendar.getTime());
    }

    private String formatBDFecha(String fecha){
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatoSalida= new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar= Calendar.getInstance();

        try {
            calendar.setTime(formatoSalida.parse(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  format.format(calendar.getTime());
    }


    /**
     * 
     */
    public void obtenerComentarios() {
        // TODO implement here
    }

    /**
     * 
     */
    public void modificarResultado() {
        // TODO implement here
    }

    /**
     * 
     */
    public void finalizarPartido() {
        // TODO implement here
    }

}