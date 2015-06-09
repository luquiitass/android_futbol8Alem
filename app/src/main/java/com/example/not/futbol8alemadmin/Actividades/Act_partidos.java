package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Adaptares.AdapterPartido;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Observable;
import java.util.Observer;


public class Act_partidos extends ActionBarActivity implements Observer{

    private Principal principal;

    private ListView LV_partidos;

    private final DiversosDialog pDialog=new DiversosDialog();


    // todo ---------metodos heredados-----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_partidos);
        principal=(Principal)getIntent().getSerializableExtra("principal");
        setTitle("Partidos "+principal.queAdministro());
        principal.addObserver(this);
        pDialog.onProgresSDialog(this,"Cargando...");
        principal.obtenerPartidos();
        LV_partidos=(ListView)findViewById(R.id.LV_partidos);
        cargarEventosListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pDialog.onProgresSDialog(this,"Cargando...");
        principal.obtenerPartidos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_partidos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!=null){
            switch (data.toString()){
                case "cargarPartidos":
                    cargarListViewPartidos();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,"Error de conexión",Toast.LENGTH_LONG).show();
                    break;
            }
            pDialog.ofProgressDialog();
        }
    }


    // todo ------------------mis metodos-----------------------------------------

    public void cargarEventosListView(){
        LV_partidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Act_partidos.this, Act_Partido_A_finalizar.class);
                intent.putExtra("partido",principal.getPartidosNoJugados().get(position));
                intent.putExtra("principal",principal);
                startActivity(intent);
            }
        });
    }

    public void cargarListViewPartidos(){
        LV_partidos.setAdapter(new AdapterPartido(this,principal.getPartidosNoJugados()));
    }

}
