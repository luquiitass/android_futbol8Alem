package com.example.not.futbol8alemadmin.Logica;


import com.example.not.futbol8alemadmin.Exepcion.Exepcion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Principal extends Observable implements Serializable{

    private ArrayList<Equipo> equipos;
    private ArrayList<Partido> partidos;
    /**
     * 
     */
    public Principal() {
        this.equipos=new ArrayList<Equipo>();
        this.partidos=new ArrayList<Partido>();
    }


    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    /**
     * 
     */

    public void obtenerPartidosEquipos() {
        String url="http://lucasdb1.esy.es/conectFutbol8/GetDatos.php";
        operacionesSelectDB(url,"todo");
    }

    /**
     * 
     */
    public void obtenerEquipos() {
        String url="http://lucasdb1.esy.es/conectFutbol8/GetEquipos2.php";
        operacionesSelectDB(url,"equipos");
    }


    public Partido obtenerPartido(int id_parido) {
        for (int i=0;i<this.partidos.size();i++){
            if (partidos.get(i).getId_partido()==id_parido){
                return partidos.get(i);
            }
        }
        return null;
    }


    public Boolean existePartido(int id_partido) {
        for (int i=0;i<this.partidos.size();i++){
            if (partidos.get(i).getId_partido()==id_partido){
                return  true;
            }
        }
        return false;
    }

    /**
     * 
     */
    public void crearPartido(String equipoLocal, String equipoVisitante, String canchaeDe, String direccion, String feha, String hora){
        Partido unPartido=new Partido(equipoLocal,equipoVisitante,canchaeDe,direccion,feha,hora);
        String url = "http://lucasdb1.esy.es/conectFutbol8/PutEquipo.php?";
        RequestParams par = new RequestParams();
        par.put("eL",unPartido.getEquipoLocal());
        par.put("eV",unPartido.getEquipoVisitante());
        par.put("canchaDe",unPartido.getCanchaeDe());
        par.put("direc",unPartido.getDireccion());
        par.put("horaEncuentro",unPartido.getHora());
        par.put("fecha",unPartido.getFeha());
        operacionesInsertDB(url, par, "partido");

    }

    /**
     * 
     */
    public void modificarPartido() {
        // TODO implement here
    }

    /**
     * 
     */
    public void eliminarPartido() {

    }


    public Equipo obtenerEquipo(String nombreEquipo) {
        for (int i=0;i<this.equipos.size();i++){
            if (equipos.get(i).getNombreEquipo()==nombreEquipo){
                return  equipos.get(i);
            }
        }
        return null;
    }


    public Boolean existeEquipo(String nombreEquipo) {
        for (int i=0;i<this.equipos.size();i++){
            if (equipos.get(i).getNombreEquipo()==nombreEquipo){
                return  true;
            }
        }
        return false;
    }

    /**
     * 
     */
    public void crearEquipo(String nombreEquipo,String fechaInicio,String direccionCancha,int libre)throws Exepcion {

        if (!existeEquipo(nombreEquipo)) {
            Equipo unEquipo=new Equipo(nombreEquipo,fechaInicio,direccionCancha,libre);
            String url = "http://lucasdb1.esy.es/conectFutbol8/PutEquipo.php?";
            RequestParams par = new RequestParams();
            par.put("nombreEquipo", unEquipo.getNombreEquipo());
            par.put("direccionCancha", unEquipo.getDireccionCancha());
            par.put("fechaInicio", unEquipo.getFchaInicio());
            par.put("fechaRegistro",unEquipo.getFechaRegitro());
            par.put("libre", unEquipo.getLibre());
            operacionesInsertDB(url,par,"equipo");
        }else{
            throw new Exepcion("Ya existe un equipo con este nombre");
        }
    }

    /**
     * 
     */
    public void modificarEquipo() {
        // TODO implement here
    }

    /**
     * 
     */
    public void eliminarEquipo() {
        // TODO implement here
    }

    /**
     * 
     */
    public void modificarResultaosPartido() {
        // TODO implement here
    }

    /**
     * 
     */
    public void finalizarPartido() {
        // TODO implement here
    }




    // Todo ------------------------Select a la base de datos---------------------------

    public void operacionesSelectDB(String url,final String accion){
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        obtenerDatosJSON(new String(responseBody),accion);
                    }else{
                        setChanged();
                        notifyObservers("Error de conexión");
                    }
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
        setChanged();
        notifyObservers();
    }

    public void obtenerDatosJSON( String response,String accion){
        try {
            JSONArray jsonArray = new JSONArray(response);
            int ultimapos=0;
            String estado="";
            if (accion.equals("partidos") || accion.equals("todo")){

                for (int i = 0; i < jsonArray.length(); i++) {
                    ultimapos=i;
                    Boolean jugado = false;
                    if (jsonArray.getJSONObject(i).getString("jugado").equals("1")) {
                        jugado=true;
                    }
                    Partido unPartido=new Partido(jsonArray.getJSONObject(i).getInt("part"),jsonArray.getJSONObject(i).getString("eqL"), jsonArray.getJSONObject(i).getString("eqV"), jsonArray.getJSONObject(i).getInt("glL"), jsonArray.getJSONObject(i).getInt("glV"), jsonArray.getJSONObject(i).getString("canchaDe"), jsonArray.getJSONObject(i).getString("direccion"),jsonArray.getJSONObject(i).getString("fecha"), jsonArray.getJSONObject(i).getString("hora"),jsonArray.getJSONObject(i).getString("jugado"),jugado );
                    this.partidos.add(unPartido);
                    estado="cargarPartidos";
                }
            }
            if (accion.equals("equipos") || accion.equals("todo")){
                for (int i = ultimapos; i < jsonArray.length(); i++) {

                    Equipo unEquipo = new Equipo(jsonArray.getJSONObject(i).getString("eq"), jsonArray.getJSONObject(i).getString("fI"), jsonArray.getJSONObject(i).getString("fR"), jsonArray.getJSONObject(i).getInt("libre"), jsonArray.getJSONObject(i).getString("dire"));
                    equipos.add(unEquipo);
                }
                if (estado.equals("cargarPartidos")){
                    estado="cagarTodo";
                }else{
                    estado="cargarEquipos";
                }
            }
            setChanged();
            notifyObservers(estado);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    // Todo ----------------------Insert a la base de adtos-------------------------------------------
    public void operacionesInsertDB(String url,RequestParams par, final String accion){
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url,par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response=new String(responseBody);
                    if (statusCode == 200) {
                        switch (accion){
                            case "equipo":
                                if (response.equals("Insertado")){
                                    setChanged();
                                    notifyObservers("equipoInsertado");
                                }else{
                                    setChanged();
                                    notifyObservers("equipoNoInsertado");
                                }
                                break;
                            case "partido":

                                break;
                        }
                    }else{
                        setChanged();
                        notifyObservers("Error de conexión");
                    }
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
        setChanged();
        notifyObservers();
    }


    // Todo ----------------------Update a la base de adtos-------------------------------------------
    public void operacionesUpdateDB(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        //obtenerDatosJSON(new String(responseBody));
                        setChanged();
                        notifyObservers("cargar");
                    }else{
                        setChanged();
                        notifyObservers("Error de conexión");
                    }
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
        setChanged();
        notifyObservers();
    }



}