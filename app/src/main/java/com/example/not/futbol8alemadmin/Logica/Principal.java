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
    private int libre=-1;
    /**
     * 
     */
    public Principal(int libre) {
        this.equipos=new ArrayList<Equipo>();
        this.partidos=new ArrayList<Partido>();
        this.libre=libre;
    }

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
    }

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

    /**
     * 
     */

    public void obtenerPartidosEquipos() {
        String url="http://lucasdb1.esy.es/conectFutbol8/GetDatos.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        operacionesSelectDB(url,par,"todo");
    }

    /**
     * 
     */
    public void obtenerEquipos() {
        String url="http://lucasdb1.esy.es/conectFutbol8/GetEquipos2.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        operacionesSelectDB(url,par,"equipos");
    }

    public void obtenerPartidos() {
        String url="http://lucasdb1.esy.es/conectFutbol8/GetPartidos2.php";
        RequestParams par=new RequestParams();
        par.put("libre",this.libre);
        operacionesSelectDB(url,par,"partidos");
    }


    public Partido obtenerPartido(int id_parido) {
        for (int i=0;i<this.partidos.size();i++){
            if (partidos.get(i).getId_partido()==id_parido){
                return partidos.get(i);
            }
        }
        return null;
    }


    public Boolean existePartido(Partido unPartido) {
        for (Partido partido:this.partidos){
            if (partido.equals(unPartido)){
                return  true;
            }
        }
        return false;
    }

    /**
     * 
     */
    public void crearPartido(String equipoLocal, String equipoVisitante, String canchaeDe, String direccion, String feha, String hora)throws Exepcion {
        Partido unPartido=new Partido(equipoLocal,equipoVisitante,canchaeDe,direccion,feha,hora,this.libre);
        if (!existePartido(unPartido)) {
            String url = "http://lucasdb1.esy.es/conectFutbol8/PutPartido.php?";
            RequestParams par = new RequestParams();
            par.put("eL", unPartido.getEquipoLocal());
            par.put("eV", unPartido.getEquipoVisitante());
            par.put("canchaDe", unPartido.getCanchaeDe());
            par.put("direc", unPartido.getDireccion());
            par.put("horaEncuentro", unPartido.getHora());
            par.put("fecha", unPartido.getFeha());
            par.put("libre",this.libre);
            operacionesInsertDB(url, par, "partido");
        }else{throw new Exepcion("Ya está creado éste partido");}
    }

    /**
     * 
     */
    public void modificarPartido(int idPartido,String equipoLocal, String equipoVisitante, String canchaeDe, String direccion, String feha, String hora) {
        Partido unPartido=new Partido(equipoLocal,equipoVisitante,canchaeDe,direccion,feha,hora,this.libre);
            String url = "http://lucasdb1.esy.es/conectFutbol8/UpdateModificarPartido.php?";
            RequestParams par = new RequestParams();
            par.put("id_partido",idPartido);
            par.put("eL", unPartido.getEquipoLocal());
            par.put("eV", unPartido.getEquipoVisitante());
            par.put("canchaDe", unPartido.getCanchaeDe());
            par.put("direc", unPartido.getDireccion());
            par.put("horaEncuentro", unPartido.getHora());
            par.put("fecha", unPartido.getFeha());
            par.put("libre",this.libre);
            operacionesUpdateDB(url, par);
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
    public void crearEquipo(String nombreEquipo,String fechaInicio,String direccionCancha)throws Exepcion {

        if (!existeEquipo(nombreEquipo)) {
            Equipo unEquipo=new Equipo(nombreEquipo,fechaInicio,direccionCancha,this.libre);
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
    public void finalizarPartido(Partido unPartido) {
        String url="http://lucasdb1.esy.es/conectFutbol8/UpdateFinalizarPartido.php?";
        RequestParams par=new RequestParams();
        par.put("id_partido",unPartido.getId_partido());
        par.put("GL",unPartido.getGolesLocal());
        par.put("GV",unPartido.getGolesVisitante());
        par.put("ganador",unPartido.getGanador());
        par.put("jugado",1);
        operacionesUpdateDB(url,par);
    }




    // Todo ------------------------Select a la base de datos---------------------------

    public void operacionesSelectDB(String url,RequestParams par,final String accion){
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url,par, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response=new String(responseBody);
                    if (statusCode == 200) {
                        obtenerDatosJSON(response,accion);
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
    }

    public void obtenerDatosJSON( String response,String accion){
        try {
            JSONArray jsonArray = new JSONArray(response);
            int ultimapos=0;
            String estado="Error de conexión";
            if (accion.equals("partidos") || accion.equals("todo")){
                try {
                    removerColeccionCompleta(this.partidos);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ultimapos = i;
                        Boolean jugado = false;
                        if (jsonArray.getJSONObject(i).getString("jugado").equals("1")) {
                            jugado = true;
                        }
                        Partido unPartido = new Partido(jsonArray.getJSONObject(i).getInt("part"), jsonArray.getJSONObject(i).getString("eqL"), jsonArray.getJSONObject(i).getString("eqV"), jsonArray.getJSONObject(i).getInt("glL"), jsonArray.getJSONObject(i).getInt("glV"), jsonArray.getJSONObject(i).getString("canchaDe"), jsonArray.getJSONObject(i).getString("direccion"), jsonArray.getJSONObject(i).getString("fecha"), jsonArray.getJSONObject(i).getString("hora"), jsonArray.getJSONObject(i).getString("jugado"), jugado);
                        this.partidos.add(unPartido);
                    }
                    estado = "cargarPartidos";
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (accion.equals("equipos") || accion.equals("todo")){
                removerColeccionCompleta(this.equipos);
                for (int i = ultimapos; i < jsonArray.length(); i++) {

                    Equipo unEquipo = new Equipo(jsonArray.getJSONObject(i).getString("eq"), jsonArray.getJSONObject(i).getString("fI"), jsonArray.getJSONObject(i).getString("fR"),this.libre, jsonArray.getJSONObject(i).getString("dire"));
                    equipos.add(unEquipo);
                }
                if (accion.equals("todo")){
                    estado="cargarTodo";
                }else{
                    estado="cargarEquipos";
                }
            }
            setChanged();
            notifyObservers(estado);
        }catch (Exception e){
            e.printStackTrace();
            setChanged();
            notifyObservers("Error de conexión");
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
                                if (response.equals("Insertado")){
                                    setChanged();
                                    notifyObservers("partidoInsertado");
                                }else{
                                    setChanged();
                                    notifyObservers("partidoNoInsertado");
                                }
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
    }


    // Todo ----------------------Update a la base de adtos-------------------------------------------
    public void operacionesUpdateDB(String url,RequestParams par){
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

    private void removerColeccionCompleta(ArrayList list){
        for (int i=0;i<list.size();i++){
            list.remove(i);
        }
    }



}