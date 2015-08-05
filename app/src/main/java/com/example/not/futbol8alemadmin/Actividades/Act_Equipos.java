package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Adaptares.AdapterEquipo;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Observable;
import java.util.Observer;

import eu.erikw.PullToRefreshListView;


public class Act_Equipos extends ActionBarActivity implements Observer{

    private Principal principal;

    private final int request_code=1;

    private PullToRefreshListView LV_principal;
    private AdapterEquipo adapterEquipo;

    private DiversosDialog pDialog=new DiversosDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__equipos);
        principal=(Principal)getIntent().getSerializableExtra("principal");
        setTitle("Equipos C."+principal.queAdministro());
        LV_principal=(PullToRefreshListView)findViewById(R.id.LV_principal);
        adapterEquipo=new AdapterEquipo(this,principal.getEquipos());
        LV_principal.setAdapter(adapterEquipo);
        principal.addObserver(this);
        pDialog.onProgresSDialog(this,getString(R.string.LG_espereUnosSegundos));
        principal.obtenerEquipos_BD();
        eventosEnLV();

    }

    private void eventosEnLV(){
        LV_principal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Act_Equipos.this, Act_unEquipo.class);
                intent.putExtra("principal",principal);
                intent.putExtra("unEquipo",principal.getEquipos().get(position).getNombreEquipo());
                startActivityForResult(intent,request_code);
            }
        });
        LV_principal.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                principal.obtenerEquipos_BD();
            }
        });
    }

    public void cargarEquiposEnLV(){
        adapterEquipo.actualizar(principal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act__equipos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bar_add) {
            Intent intent=new Intent(this,Act_RegistrarEquipo.class);
            intent.putExtra("principal",principal);
            startActivityForResult(intent,request_code);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!=null){
            switch (data.toString()){
                case "cargarEquipos":
                    adapterEquipo.notifyDataSetChanged();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,getString(R.string.LG_errorDeConexionDesliceHaciaAbajo),Toast.LENGTH_LONG).show();
                    break;
            }
            LV_principal.onRefreshComplete();
            pDialog.ofProgressDialog();
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
            if (data.getBooleanExtra("recargar",false)){
                principal.obtenerEquipos_BD();
            }
            adapterEquipo.actualizar(principal);
            //otros metodos
        }
    }
}
