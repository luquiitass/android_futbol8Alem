package com.example.not.futbol8alemadmin.Logica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Equipo {

    protected String nombreEquipo;
    private String fechaInicio;
    private String fechaRegitro;
    private int libre;
    private String direccionCancha;
    private Date mesPago;
    private int cantPartidosGanados;
    private int cantPartidosEmpatados;
    private int cantPartidosPerdidos;
    private ArrayList<Partido> partidos;

    /**
     *
     */
    // Todo-------------Constructores-----------------------


    public Equipo() {
    }

    public Equipo(String nombreEquipo, String fechaInicio,String direccionCancha, int libre) {
        this.nombreEquipo = nombreEquipo;
        this.direccionCancha=direccionCancha;
        this.libre = libre;
        this.fechaInicio=formatBDFecha(fechaInicio);
        this.fechaRegitro=getStringFechaRegistro();
        this.mesPago=new Date();
        this.cantPartidosEmpatados=0;
        this.cantPartidosGanados=0;
        this.cantPartidosPerdidos=0;
        this.partidos=new ArrayList<Partido>();
    }

    public Equipo(String nombreEquipo, String fechaInicio, String fechaRegitro, int libre, String direccionCancha) {
        this.fechaInicio = formatAPPFecha(fechaInicio);
        this.nombreEquipo = nombreEquipo;
        this.fechaRegitro = formatAPPFecha(fechaRegitro);
        this.libre = libre;
        this.direccionCancha = direccionCancha;
        this.cantPartidosGanados =0;
        this.cantPartidosEmpatados =0;
        this.cantPartidosPerdidos =0;
        this.partidos=new ArrayList<Partido>();
    }

// Todo --------------------Get Set -------------------------------------

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getFchaInicio() {
        return fechaInicio;
    }

    public void setFchaInicio(String fchaInicio) {
        this.fechaInicio = fchaInicio;
    }

    public String getDireccionCancha() {
        return direccionCancha;
    }

    public void setDireccionCancha(String direccionCancha) {
        this.direccionCancha = direccionCancha;
    }

    public String getFechaRegitro() {
        return fechaRegitro;
    }

    public void setFechaRegitro(String fechaRegitro) {
        this.fechaRegitro = fechaRegitro;
    }

    public Date getMesPago() {
        return mesPago;
    }

    public void setMesPago(Date mesPago) {
        this.mesPago = mesPago;
    }

    public int getLibre() {
        return libre;
    }

    public void setLibre(int libre) {
        this.libre = libre;
    }

    public int getCantPartidosGanados() {
        return cantPartidosGanados;
    }

    public void setCantPartidosGanados(int cantPartidosGanados) {
        this.cantPartidosGanados = cantPartidosGanados;
    }

    public int getCantPartidosEmpatados() {
        return cantPartidosEmpatados;
    }

    public void setCantPartidosEmpatados(int cantPartidosEmpatados) {
        this.cantPartidosEmpatados = cantPartidosEmpatados;
    }

    public int getCantPartidosPerdidos() {
        return cantPartidosPerdidos;
    }

    public void setCantPartidosPerdidos(int cantPartidosPerdidos) {
        this.cantPartidosPerdidos = cantPartidosPerdidos;
    }

    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(ArrayList<Partido> partidos) {
        this.partidos = partidos;
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

    private String getStringFechaRegistro(){
        DateFormat formatoSalida= new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        return  formatoSalida.format(calendar.getTime());
    }

    public Calendar getFechadeInicioCalendar(String fecha){
        DateFormat format= new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar= Calendar.getInstance();

        try {
            calendar.setTime(format.parse(this.fechaInicio));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  Calendar.getInstance();
    }



    // Todo ------------------------Operaciones-----------------------

    public void obtenerPartidos() {
        // TODO implement here
    }

}