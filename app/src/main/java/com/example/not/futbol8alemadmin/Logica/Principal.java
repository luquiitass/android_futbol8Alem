package com.example.not.futbol8alemadmin.Logica;


import com.example.not.futbol8alemadmin.Exepcion.Exepcion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Principal extends Observable implements Serializable,Observer{

    private static final String ERRORCONEXION="Error de conexión";
    private ArrayList<Equipo> equipos;
    private ArrayList<Partido> partidos;
    private ArrayList<Caja> cajas;
    private int libre=-1;
    /**
     * 
     */
    public Principal(int libre) {
        this.equipos=new ArrayList<Equipo>();
        this.partidos=new ArrayList<Partido>();
        this.libre=libre;
    }//Constructor --Inicia las colecciones de equipos y partidos... indicando si se trata de equipos libres o veteranos

    public int getLibre() {
        return libre;
    }

    public void setLibre(int libre) {
        this.libre = libre;
    }

    public String queAdministro(){
        if (libre==0){
            return "Veteranos";
        }
        return "Libre";
    } //retorna un cadena de texto indicando si se esta administrando la categoria veteranos o libres

    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    public ArrayList<Partido>getPartidosJugados(){
        ArrayList<Partido>retorno=new ArrayList<Partido>();
        for (Partido partido:this.partidos){
            if (partido.getJugado()){
                retorno.add(partido);
            }
        }
        return retorno;
    }

    public ArrayList<Partido>getPartidosNoJugados(){
        ArrayList<Partido>retorno=new ArrayList<Partido>();
        for (Partido partido:this.partidos){
            if (!partido.getJugado()){
                retorno.add(partido);
            }
        }
        return retorno;
    }

    public ArrayList<String>getNombresEquipos(){
        ArrayList<String> retorno=new ArrayList<String>();
        for (Equipo unEquipo:this.equipos){
            retorno.add(unEquipo.getNombreEquipo());
        }
        return retorno;
    } //retorna un coleccion solamente con los nombres de los equipos

    // Todo -------------------- metodos que trabajan con las colecciones locales --------------------

    private void eliminarEquipo(String nombreEquipo){
        for (int i=0;i<this.equipos.size();i++){
            if (this.equipos.get(i).getNombreEquipo().equals(nombreEquipo)){
                this.equipos.remove(i);
                i=equipos.size();
            }
        }
    } //elemina un equipo de la coleccion local

    private void eliminarPartido(int id_partido){
        for(int i=0;i<this.partidos.size();i++){
            if (partidos.get(i).getId_partido()==id_partido){
                partidos.remove(i);
                i=partidos.size();
            }
        }
    }


    public Partido obtenerPartido(int id_parido) {
        for (int i=0;i<this.partidos.size();i++){
            if (partidos.get(i).getId_partido()==id_parido){
                return partidos.get(i);
            }
        }
        return null;
    } // retorna el partido que contenga el id pasado por parametro

    public Boolean existePartido(Partido unPartido) {
        for (Partido partido:this.partidos){
            if (partido.equals(unPartido)){
                return  true;
            }
        }
        return false;
    } // verifica si ya existe un partido con los mismo datos

    public Equipo obtenerEquipo(String nombreEquipo) {
        for (int i=0;i<this.equipos.size();i++){
            if (equipos.get(i).getNombreEquipo().equals(nombreEquipo)){
                return  equipos.get(i);
            }
        }
        return null;
    }// retorna el equipo que contenga el nombre pasado por parametro

    public Boolean existeEquipo(String nombreEquipo) {
        for (int i=0;i<this.equipos.size();i++){
            if (equipos.get(i).getNombreEquipo().equals(nombreEquipo)){
                return  true;
            }
        }
        return false;
    } //verifica si ya existe un equipo con el mismo nombre
    /**
     * 
     */

    public void obtenerPartidosEquipos_BD() {
        String url="http://futbol8alem.com/conectFutbol8/GetDatos.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        par.put("FI",sumar_restar_dias_a_fechaActual(-7));
        par.put("FF",sumar_restar_dias_a_fechaActual(7));
        operacionesSelectDB(url, par, "todo");
    }

    /**
     * 
     */
    public void obtenerEquipos_BD() {
        String url="http://futbol8alem.com/conectFutbol8/GetEquipos2.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        operacionesSelectDB(url,par,"equipos");
    }

    public void obtenerPartidos_BD() {
        String url="http://lfutbol8alem.com/conectFutbol8/GetPartidos2.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        par.put("FI",sumar_restar_dias_a_fechaActual(-7));
        par.put("FF",sumar_restar_dias_a_fechaActual(7));
        operacionesSelectDB(url,par,"partidos");
    }

    public void obtenerPartidos_BD(String fechaInicio,String fechaFinal) {
        String url="http://futbol8alem.com/conectFutbol8/GetPartidos2.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        par.put("FI",fechaInicio);
        par.put("FF",fechaFinal);
        operacionesSelectDB(url,par,"partidos");
    }

    public void obtenerPartidosDeEquipo(String nombreEquipo) {
        if (existeEquipo(nombreEquipo)) {
            String url = "http://futbol8alem.com/conectFutbol8/GetPartidosDeEquipo.php?";
            RequestParams par = new RequestParams();
            par.put("libre", this.libre);
            par.put("id_partido", nombreEquipo);
            operacionesSelectDB(url, par, "partidos");
        }else{
            setChanged();
            notifyObservers("cargarPartidos");
        }
    }

    /**
     * 
     */
    public void crearPartido_BD(String equipoLocal, String equipoVisitante, String canchaeDe, String direccion, String feha, String hora)throws Exepcion {
        Partido unPartido=new Partido(equipoLocal,equipoVisitante,canchaeDe,direccion,feha,hora,this.libre);
        if (!existePartido(unPartido)) {
            String url = "http://futbol8alem.com/conectFutbol8/PutPartido.php?";
            RequestParams par = new RequestParams();
            par.put("eL", unPartido.getEquipoLocal());
            par.put("eV", unPartido.getEquipoVisitante());
            par.put("canchaDe", unPartido.getCanchaeDe());
            par.put("direc", unPartido.getDireccion());
            par.put("horaEncuentro", unPartido.getHora());
            par.put("fecha", unPartido.getFecha());
            par.put("libre",this.libre);
            unPartido.setFecha(feha);
            operacionesInsertDB(url, par, "partido",unPartido);
        }else{throw new Exepcion("Ya está creado éste partido");}
    }

    /**
     * 
     */

    public void eliminarPartido_BD(int id_partido) {
        String url="http://futbol8alem.com/conectFutbol8/DeletePartido.php?";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        par.put("id_partido", id_partido);
        operacionesDeletDB(url, par,"partido",String.valueOf(id_partido));
    }


    /**
     * 
     */
    public void crearEquipo_BD(String nombreEquipo, String fechaInicio, String direccionCancha,String telefono)throws Exepcion {

        if (!existeEquipo(nombreEquipo)) {
            Equipo unEquipo=new Equipo(nombreEquipo,fechaInicio,direccionCancha,telefono,this.libre);
            String url = "http://futbol8alem.com/conectFutbol8/PutEquipo.php?";
            RequestParams par = new RequestParams();
            par.put("nombreEquipo", unEquipo.getNombreEquipo());
            par.put("direccionCancha", unEquipo.getDireccionCancha());
            par.put("fechaInicio", unEquipo.getFchaInicio());
            par.put("fechaRegistro",unEquipo.getFechaRegitro());
            par.put("tel",unEquipo.getTelefono());
            par.put("libre", unEquipo.getLibre());
            unEquipo.setFchaInicio(fechaInicio);
            unEquipo.setFechaRegitro(unEquipo.getFechaRegFormAPP());

            operacionesInsertDB(url,par,"equipo",unEquipo);
        }else{
            throw new Exepcion("Ya existe un equipo con este nombre");
        }
    }

    /**
     * 
     */

    public void eliminarEquipo_BD(String nombreEquipo) {
        String url="http://futbol8alem.com/conectFutbol8/DeleteEquipo.php?";
        RequestParams par=new RequestParams();
        par.put("nombreEquipo",nombreEquipo);
        par.put("libre",this.libre);
        operacionesDeletDB(url, par, "equipo", nombreEquipo);
    }

    /**
     * 
     */



    // Todo ------------------------Select a la base de datos---------------------------

    public void operacionesSelectDB(String url,RequestParams par,final String accion){
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url, par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    if (statusCode == 200) {
                        obtenerDatosJSON(response, accion);
                    } else {
                        setChanged();
                        notifyObservers(ERRORCONEXION);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    setChanged();
                    notifyObservers(ERRORCONEXION);
                }

            });
        }catch (Exception e){
            e.printStackTrace();
            setChanged();
            notifyObservers(ERRORCONEXION);
        }
    }

    public void obtenerDatosJSON( String response,String accion){
        try {
            if (response.equals("null")){
                setChanged();
                notifyObservers("cargarTodo");
            }else{
                JSONArray jsonArray = new JSONArray(response);
                String estado = ERRORCONEXION;

                int ultimapos = 0;
                if (accion.equals("partidos") || accion.equals("todo")) {
                    ultimapos = obtenerJSONPartidos(jsonArray);
                    estado = "cargarPartidos";
                }
                if (accion.equals("equipos") || accion.equals("todo")) {
                    obtenerJSONEquipos(jsonArray, ultimapos);
                    if (accion.equals("todo")) {
                        estado = "cargarTodo";
                    } else {
                        estado = "cargarEquipos";
                    }
                }
                setChanged();
                notifyObservers(estado);
            }
        }catch (Exception e){
            setChanged();
            notifyObservers(ERRORCONEXION);
        }

    }

    private int obtenerJSONPartidos(JSONArray jsonArray) {
        int retorno=0;
        try {
            this.partidos.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                retorno = i;
                Boolean jugado = false;
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if (jsonArray.getJSONObject(i).getString("jugado").equals("1")) {
                    jugado = true;
                }
                Partido unPartido = new Partido(jsonArray.getJSONObject(i).getInt("part"), jsonArray.getJSONObject(i).getString("eqL"), jsonArray.getJSONObject(i).getString("eqV"), jsonArray.getJSONObject(i).getInt("glL"), jsonArray.getJSONObject(i).getInt("glV"), jsonArray.getJSONObject(i).getString("canchaDe"), jsonArray.getJSONObject(i).getString("direccion"), jsonArray.getJSONObject(i).getString("fecha"), jsonArray.getJSONObject(i).getString("hora"), jsonArray.getJSONObject(i).getString("jugado"), jugado,this.libre);
                this.partidos.add(unPartido);
            }
        }catch (Exception e){
            return retorno;
        }
        return retorno;
    }

    private void obtenerJSONEquipos(JSONArray jsonArray,int ultimapos){
        try {
            this.equipos.clear();
            for (int i = ultimapos; i < jsonArray.length(); i++) {
                boolean visible=false;
                if (jsonArray.getJSONObject(i).getInt("vs")==1){
                    visible=true;
                }

                Equipo unEquipo = new Equipo(jsonArray.getJSONObject(i).getString("eq"), jsonArray.getJSONObject(i).getString("fI"), jsonArray.getJSONObject(i).getString("fR"), this.libre, jsonArray.getJSONObject(i).getString("dire"),jsonArray.getJSONObject(i).getString("tel"),visible);
                equipos.add(unEquipo);
            }
        }catch (Exception e){

        }
    }


    // Todo ----------------------Insert a la base de adtos-------------------------------------------
    public void operacionesInsertDB(String url,RequestParams par, final String accion,final Object object){
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url, par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String estado = ERRORCONEXION;
                    String response = new String(responseBody);
                    if (statusCode == 200) {
                        switch (accion) {
                            case "equipo":
                                if (response.equals("Insertado")) {
                                    estado = "equipoInsertado";
                                    equipos.add((Equipo)object);
                                } else {
                                    estado = "equipoNoInsertado";
                                }
                                break;
                            case "partido":
                                if (response.equals("Insertado")) {
                                    estado = "partidoInsertado";
                                    partidos.add((Partido)object);
                                } else {
                                    estado = "partidoNoInsertado";
                                }
                                break;
                        }
                        setChanged();
                        notifyObservers(estado);

                    } else {
                        setChanged();
                        notifyObservers(ERRORCONEXION);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    setChanged();
                    notifyObservers(ERRORCONEXION);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            setChanged();
            notifyObservers(ERRORCONEXION);
        }
    }


    // Todo ----------------------Update a la base de adtos-------------------------------------------

    public void operacionesDeletDB(String url,RequestParams par,final String accion,final String id){
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url,par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String retorno=new String(responseBody);
                    String estado=ERRORCONEXION;
                    if (statusCode == 200) {
                        if (retorno.equals("borrado")) {
                            estado = "borrado";
                            switch (accion){
                                case "equipo":
                                    eliminarEquipo(id);
                                    break;
                                case "partido":
                                    eliminarPartido(Integer.parseInt(id));
                                    break;
                            }
                        } else {
                            estado = "noBorrado";
                        }
                    }else{
                        estado=ERRORCONEXION;
                    }
                    setChanged();
                    notifyObservers(estado);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    setChanged();
                    notifyObservers(ERRORCONEXION);
                }
            });
        }catch (Exception e){
            setChanged();
            notifyObservers(ERRORCONEXION);
        }
    }

    private String sumar_restar_dias_a_fechaActual(int cantDias){
        Calendar calendar;
        try {
            String place = "America/Argentina/Buenos_Aires";
            java.util.TimeZone zone = java.util.TimeZone.getTimeZone(place);
            calendar = java.util.Calendar.getInstance(zone);
            calendar.add(Calendar.DAY_OF_YEAR, cantDias);
        }catch (Exception e){
            calendar= new GregorianCalendar();
        }
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }






    //---todo ---------------------------esto seria la parte donde estarian los metodos para realizar el cobro----------------------
    public void abrirCaja_BD()throws Exepcion{
        if (!existeCajaAbierta()) {
            Caja unCaja = new Caja();
            unCaja.guardar_BD();
        }else{ throw new Exepcion("Ya existe una caja abierta");
        }
    }

    public void cerrarCaja()throws Exepcion{
        if (existeCajaAbierta()){
            Caja unaCaja=obtenerCajaAbierta();
            unaCaja.addObserver(this);
            unaCaja.cerrarCaja();

        }else {
            throw new Exepcion("Todas las cajas estan cerradas");
        }
    }


    public Boolean existeCajaAbierta(){
        for (Caja unaCaja:this.cajas){
            if (!unaCaja.getCerrada()){
                return true;
            }
        }
        return false;
    }

    public Caja  obtenerCajaAbierta(){
        for (Caja unaCaja:this.cajas){
            if (!unaCaja.getCerrada()){
                return unaCaja;
            }
        }
        return null;
    }


    public void nuevoMoviento(){
        if (existeCajaAbierta()){
            Caja unaCaja=obtenerCajaAbierta();

        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!= null) {
            String estado="";
            switch (data.toString()) {
                case "cajaCreada":
                    cajas.add((Caja)observable);
                    estado=data.toString();
                    break;
                case "cajaCerrada":
                    estado=data.toString();
                    break;
            }
            setChanged();
            notifyObservers(data.toString());
        }
    }
}