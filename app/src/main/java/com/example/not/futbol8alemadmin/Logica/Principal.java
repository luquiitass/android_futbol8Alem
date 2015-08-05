package com.example.not.futbol8alemadmin.Logica;


import com.example.not.futbol8alemadmin.Exepcion.Exepcion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Principal extends Observable implements Serializable{

    private static final String ERRORCONEXION="Error de conexíon";
    private ArrayList<Equipo> equipos;
    private ArrayList<Partido> partidos;
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
        String url="http://lucasdb1.esy.es/conectFutbol8/GetDatos.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        operacionesSelectDB(url,par,"todo");
    }

    /**
     * 
     */
    public void obtenerEquipos_BD() {
        String url="http://lucasdb1.esy.es/conectFutbol8/GetEquipos2.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        operacionesSelectDB(url,par,"equipos");
    }

    public void obtenerPartidos_BD() {
        String url="http://lucasdb1.esy.es/conectFutbol8/GetPartidos2.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        operacionesSelectDB(url,par,"partidos");
    }

    /**
     * 
     */
    public void crearPartido_BD(String equipoLocal, String equipoVisitante, String canchaeDe, String direccion, String feha, String hora)throws Exepcion {
        Partido unPartido=new Partido(equipoLocal,equipoVisitante,canchaeDe,direccion,feha,hora,this.libre);
        if (!existePartido(unPartido)) {
            String url = "http://lucasdb1.esy.es/conectFutbol8/PutPartido.php?";
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
        String url="http://lucasdb1.esy.es/conectFutbol8/DeletePartido.php?";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        par.put("id_partido", id_partido);
        operacionesDeletDB(url, par,"partido",String.valueOf(id_partido));
    }


    /**
     * 
     */
    public void crearEquipo_BD(String nombreEquipo, String fechaInicio, String direccionCancha)throws Exepcion {

        if (!existeEquipo(nombreEquipo)) {
            Equipo unEquipo=new Equipo(nombreEquipo,fechaInicio,direccionCancha,this.libre);
            String url = "http://lucasdb1.esy.es/conectFutbol8/PutEquipo.php?";
            RequestParams par = new RequestParams();
            par.put("nombreEquipo", unEquipo.getNombreEquipo());
            par.put("direccionCancha", unEquipo.getDireccionCancha());
            par.put("fechaInicio", unEquipo.getFchaInicio());
            par.put("fechaRegistro",unEquipo.getFechaRegitro());
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
        String url="http://lucasdb1.esy.es/conectFutbol8/DeleteEquipo.php?";
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
            JSONArray jsonArray = new JSONArray(response);
            String estado = ERRORCONEXION;

                int ultimapos = 0;
                if (accion.equals("partidos") || accion.equals("todo")) {
                    ultimapos=obtenerJSONPartidos(jsonArray);
                    estado = "cargarPartidos";
                }
                if (accion.equals("equipos") || accion.equals("todo")) {
                    obtenerJSONEquipos(jsonArray,ultimapos);
                    if (accion.equals("todo")) {
                        estado = "cargarTodo";
                    } else {
                        estado = "cargarEquipos";
                    }
                }
            setChanged();
            notifyObservers(estado);
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
                if (jsonArray.getJSONObject(i).getString("jugado").equals("1")) {
                    jugado = true;
                }
                Partido unPartido = new Partido(jsonArray.getJSONObject(i).getInt("part"), jsonArray.getJSONObject(i).getString("eqL"), jsonArray.getJSONObject(i).getString("eqV"), jsonArray.getJSONObject(i).getInt("glL"), jsonArray.getJSONObject(i).getInt("glV"), jsonArray.getJSONObject(i).getString("canchaDe"), jsonArray.getJSONObject(i).getString("direccion"), jsonArray.getJSONObject(i).getString("fecha"), jsonArray.getJSONObject(i).getString("hora"), jsonArray.getJSONObject(i).getString("jugado"), jugado);
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

                Equipo unEquipo = new Equipo(jsonArray.getJSONObject(i).getString("eq"), jsonArray.getJSONObject(i).getString("fI"), jsonArray.getJSONObject(i).getString("fR"), this.libre, jsonArray.getJSONObject(i).getString("dire"));
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

}