package com.example.not.futbol8alemadmin.Logica;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Partido extends Observable implements Serializable{

    protected int id_partido;
    private String equipoLocal;
    private String equipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private String canchaeDe;
    private String direccion;
    private String fecha;
    private String hora;
    private String ganador;
    private Boolean jugado;
    private int libre;
    private ArrayList<Comentario> comentarios;


    public Partido() {
    }

    public Partido(String equipoLocal, String equipoVisitante, String canchaeDe, String direccion, String feha, String hora,int libre) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.canchaeDe = canchaeDe;
        this.direccion = direccion;
        this.fecha = formatBDFecha(feha);
        this.hora = hora;
        this.libre=libre;
        this.jugado=false;
    }

    public Partido(int id_partido, String equipoLocal, String equipoVisitante, int golesLocal, int golesVisitante, String canchaeDe, String direccion, String feha, String hora, String ganador, Boolean jugado,int libre) {
        this.id_partido = id_partido;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.canchaeDe = canchaeDe;
        this.direccion = direccion;
        this.fecha = formatAPPFecha(feha);
        this.hora = hora;
        this.libre=libre;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public int getLibre() {
        return libre;
    }

    public void setLibre(int libre) {
        this.libre = libre;
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
    private String obtenerGanador(int gL,int gV){
        String ganador="";
        if (gL<gV){
            ganador=equipoVisitante;
        }else if (gL>gV){
            ganador=equipoLocal;
        }else if (gL==gV){
            ganador="Empatado";
        }
        return ganador;
    }

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
    public void modificarResultados(final int gL, final int gV) {
        String url="http://lucasdb1.esy.es/conectFutbol8/UpdateFinalizarPartido.php?";
        RequestParams par=new RequestParams();
        par.put("id_partido",id_partido);
        par.put("GL",gL);
        par.put("GV",gV);
        par.put("ganador",obtenerGanador(gL,gV));
        par.put("jugado",1);
        par.put("libre",this.libre);

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url,par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String retorno=new String(responseBody);
                    String estado="";
                    if (statusCode == 200) {
                        if (retorno.equals("Actualizado ")) {
                            estado = "Actualizado";
                            golesLocal=gL;
                            golesVisitante=gV;
                            ganador=obtenerGanador(gL,gV);
                            jugado=true;
                        } else {
                            estado = "noActualizado";
                        }
                    }else{
                        estado="Error de conexión";
                    }
                    setChanged();
                    notifyObservers(estado);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    setChanged();
                    notifyObservers("Error de conexión");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            setChanged();
            notifyObservers("Error de conexión");
        }
    }


    public boolean equals(Partido o) {
        return (this.equipoLocal.equals(o.getEquipoLocal()) || (this.equipoLocal.equals(o.getEquipoVisitante()))) && (this.equipoVisitante.equals(o.getEquipoVisitante()) || (this.equipoVisitante.equals(o.getEquipoLocal()))) && this.fecha.equals(formatAPPFecha(o.getFecha())) && this.libre == o.libre;
    }

    public void modificarPartido_BD(final String m_equipoLocal,final String m_equipoVisitante,final String m_canchaeDe,final String m_direccion,final String m_fecha,final String m_hora) {
        String url = "http://lucasdb1.esy.es/conectFutbol8/UpdateModificarPartido.php?";
        RequestParams par = new RequestParams();
        par.put("id_partido",this.id_partido);
        par.put("eL", m_equipoLocal);
        par.put("eV", m_equipoVisitante);
        par.put("canchaDe", m_canchaeDe);
        par.put("dir", m_direccion);
        par.put("hora",hora);
        par.put("fecha", formatBDFecha(m_fecha));
        par.put("libre",this.libre);

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url,par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String retorno=new String(responseBody);
                    String estado="";
                    if (statusCode == 200) {
                        if (retorno.equals("Actualizado ")) {
                            estado = "Actualizado";
                            setEquipoLocal(m_equipoLocal);
                            setEquipoVisitante(m_equipoVisitante);
                            setCanchaeDe(m_canchaeDe);setDireccion(m_direccion);
                            setHora(m_hora);
                            setFecha(m_fecha);
                        } else {
                            estado = "noActualizado";
                        }
                    }else{
                        estado="Error de conexión";
                    }
                    setChanged();
                    notifyObservers(estado);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    setChanged();
                    notifyObservers("Error de conexión");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            setChanged();
            notifyObservers("Error de conexión");
        }
    }

}