package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Adaptares.AdapterResultado;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Observable;
import java.util.Observer;

import eu.erikw.PullToRefreshListView;


public class Act_Resultados extends ActionBarActivity implements Observer{

    private Principal principal;

    private int request_code=1;


    private PullToRefreshListView LV_resultados;
    private AdapterResultado adapterResultado;

    private DiversosDialog pDialog=new DiversosDialog();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__resultados);
        principal=(Principal)getIntent().getSerializableExtra("principal");
        setTitle("Resultados de partidos C."+principal.queAdministro());
        principal.addObserver(this);
        LV_resultados=(PullToRefreshListView)findViewById(R.id.LV_resultados);
        adapterResultado=new AdapterResultado(this,principal.getPartidosJugados());
        LV_resultados.setAdapter(adapterResultado);
        pDialog.onProgresSDialog(this,getString(R.string.LG_cargandoResultados));
        principal.obtenerPartidos_BD();
        cargarEventosLV();
    }


    private void cargarEventosLV(){
        LV_resultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Act_Resultados.this,Act_unResultado.class);
                intent.putExtra("principal",principal);
                intent.putExtra("unResultado",principal.getPartidosJugados().get(position).getId_partido());
                startActivityForResult(intent,request_code);
            }
        });
        LV_resultados.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                principal.obtenerPartidos_BD();
            }
        });
    }

    private void cargarResultadosLV(){
        adapterResultado.actualizar(principal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act__resultados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!=null){
            switch (data.toString()){
                case "cargarPartidos":
                    cargarResultadosLV();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,getString(R.string.LG_errorDeConexionDesliceHaciaAbajo),Toast.LENGTH_LONG).show();
                    break;
            }
            pDialog.ofProgressDialog();
            LV_resultados.onRefreshComplete();
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
            principal.addObserver(this);
            adapterResultado.actualizar(principal);

        }
    }
}
