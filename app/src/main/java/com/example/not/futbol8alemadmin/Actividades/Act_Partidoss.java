package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Adaptares.AdapterPartido;
import com.example.not.futbol8alemadmin.Logica.Partido;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import eu.erikw.PullToRefreshListView;


public class Act_Partidoss extends ActionBarActivity implements Observer{

    private Principal principal;

    private int request_code=1;
    private int request_code_busqueda=2;

    private PullToRefreshListView LV_partidos;
    private AdapterPartido adapterPartido;

    private final DiversosDialog pDialog=new DiversosDialog();

    private TextView TV_sinNadaQueMostrar;


    // todo ---------metodos heredados-----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_partidos);
        principal=(Principal)getIntent().getSerializableExtra("principal");
        setTitle("Partidos C." + principal.queAdministro());
        principal.addObserver(this);

        TV_sinNadaQueMostrar=(TextView)getLayoutInflater().inflate(R.layout.sin_nada_que_mostrar,null);
        TV_sinNadaQueMostrar.setText("No se han encontrado partidos");

        LV_partidos=(PullToRefreshListView)findViewById(R.id.LV_partidos);
        adapterPartido=new AdapterPartido(this,new ArrayList());
        LV_partidos.setAdapter(adapterPartido);

        pDialog.onProgresSDialog(this,getString(R.string.LG_cargando));
        principal.obtenerPartidosEquipos_BD();
        cargarEventosListView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_partidos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_bar_add:
                Intent intent=new Intent(this,Act_CrearPartido.class);
                intent.putExtra("principal", principal);
                startActivityForResult(intent,request_code);
                break;
            case R.id.action_bar_buscar:
                Intent intent1=new Intent(this,Act_BusquedaPartidos.class);
                intent1.putExtra("principal",principal);
                startActivityForResult(intent1,request_code_busqueda);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!=null){
            switch (data.toString()){
                case "cargarTodo":
                    cargarListViewPartidos();
                    break;
                case "cargarPartidos":
                    cargarListViewPartidos();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,getResources().getString(R.string.LG_errorDeConexionDesliceHaciaAbajo),Toast.LENGTH_LONG).show();
                    break;
            }
            pDialog.ofProgressDialog();
            LV_partidos.onRefreshComplete();
        }
    }


    // todo ------------------mis metodos-----------------------------------------

    public void cargarEventosListView(){
        LV_partidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Act_Partidoss.this, Act_unPartido.class);
                intent.putExtra("unPartido",principal.getPartidosNoJugados().get(position).getId_partido());
                intent.putExtra("principal",principal);
                startActivityForResult(intent,request_code);
                //startActivity(intent);
            }
        });
        LV_partidos.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                principal.obtenerPartidosEquipos_BD();
            }
        });
    }

    public void cargarListViewPartidos(){
        adapterPartido.actualizar(principal);
        mensajeSinNadaQueMostrar(R.id.lay_partidos,TV_sinNadaQueMostrar,adapterPartido.contieneDatos());
    }

    private void mensajeSinNadaQueMostrar(int layout_id,TextView msj,boolean colocar){
        ViewGroup layout=(ViewGroup)findViewById(layout_id);
        if (!colocar) {
            if (layout.getChildCount() == 1) {
                layout.addView(msj, 0);
            }
        }else{
            if (layout.getChildCount() == 2) {
                layout.removeViewAt(0);
            }
        }
    }


    public void devolverPrincipal(){
        Intent intent=new Intent();
        intent.putExtra("principal", principal);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                devolverPrincipal();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode==request_code)&&(resultCode==RESULT_OK)){
            principal=(Principal)data.getSerializableExtra("principal");
            principal.deleteObservers();
            principal.addObserver(this);
            if (data.getBooleanExtra("recargar",false)){
                principal.obtenerPartidosEquipos_BD();
            }
            adapterPartido.actualizar(principal);
        }else if((requestCode==request_code_busqueda)&&(resultCode==RESULT_OK)){
            String tipoBusqueda=data.getStringExtra("tipoBusqueda");
            switch (tipoBusqueda){
                case "fecha":
                    pDialog.onProgresSDialog(this,"Cargando partidos...");
                    principal.obtenerPartidos_BD(data.getStringExtra("FI"),data.getStringExtra("FF"));
                    break;
                case "equipo":
                    principal.obtenerPartidosDeEquipo(data.getStringExtra("equipo"));
                    break;
            }
        }
    }
}
