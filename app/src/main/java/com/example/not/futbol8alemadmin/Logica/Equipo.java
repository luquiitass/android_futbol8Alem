package com.example.not.futbol8alemadmin.Logica;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Equipo extends Observable implements Serializable{

    protected String nombreEquipo;
    private String fechaInicio;
    private String fechaRegitro;
    private int libre;
    private String direccionCancha;
    private String telefono;
    private Calendar mesPago;
    private int cantPartidosGanados;
    private int cantPartidosEmpatados;
    private int cantPartidosPerdidos;
    private boolean  visible;
    private ArrayList<Partido> partidos;

    /**
     *
     */
    // Todo-------------Constructores-----------------------


    public Equipo() {
    }

    public Equipo(String nombreEquipo, String fechaInicio,String direccionCancha,String telefono, int libre) {
        this.nombreEquipo = nombreEquipo;
        this.direccionCancha=direccionCancha;
        this.libre = libre;
        this.fechaInicio=formatBDFecha(fechaInicio);
        this.fechaRegitro=getStringFechaRegistro();
        this.telefono=telefono;
        this.mesPago=Calendar.getInstance();
        this.cantPartidosEmpatados=0;
        this.cantPartidosGanados=0;
        this.cantPartidosPerdidos=0;
        this.visible=false;
        this.partidos=new ArrayList<Partido>();
    }

    public Equipo(String nombreEquipo, String fechaInicio, String fechaRegitro, int libre, String direccionCancha,String telefono,boolean visible) {
        this.fechaInicio = formatAPPFecha(fechaInicio);
        this.nombreEquipo = nombreEquipo;
        this.fechaRegitro = formatAPPFecha(fechaRegitro);
        this.libre = libre;
        this.direccionCancha = direccionCancha;
        if (telefono.equals("null")){
            this.telefono="Sin número";
        }else{
            this.telefono=telefono;
        }
        this.visible=visible;
        this.cantPartidosGanados =0;
        this.cantPartidosEmpatados =0;
        this.cantPartidosPerdidos =0;
        this.partidos=new ArrayList<Partido>();
    }

// Todo --------------------Get Set -------------------------------------


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

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

    public Calendar getMesPago() {
        return mesPago;
    }

    public void setMesPago(Calendar mesPago) {
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

    public String getFechaRegFormAPP(){
        return formatAPPFecha(fechaRegitro);
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

    public Calendar getFechadeInicioCalendar(){
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


    public void modificarEquipo_BD(final String nombreEquipoV,final String m_nombreEquipoN,final String m_fechaInicio,final String m_direccionCancha,final String m_telefono,final boolean visible){

        String url="http://futbol8alem.com/conectFutbol8/UpdateEquipo.php?";
        RequestParams par=new RequestParams();
        par.put("nombreEV",nombreEquipoV);
        par.put("nombreEN",m_nombreEquipoN);
        par.put("fechaInicio",formatBDFecha(m_fechaInicio));
        par.put("dire",m_direccionCancha);
        par.put("tel",m_telefono);
        par.put("libre",this.libre);
        if (visible){
            par.put("visible",1);
        }else {
            par.put("visible",0);
        }



        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url,par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String retorno=new String(responseBody);
                    String estado="";
                    if (statusCode == 200) {
                        if (retorno.equals("Actualizado")) {
                            estado = "Actualizado";
                            setNombreEquipo(m_nombreEquipoN);
                            setDireccionCancha(m_direccionCancha);
                            setFchaInicio(m_fechaInicio);
                            setTelefono(m_telefono);
                            setVisible(visible);
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