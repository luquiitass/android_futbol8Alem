package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Observable;
import java.util.Observer;


public class Act_Inicio extends ActionBarActivity implements Observer{

    private Principal principal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        this.principal=new Principal();
        principal.addObserver(this);
    }

// Todo------------------Metodos de super Calse--------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
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


// TODO ---------------mis metodos--------------
    public void registrarUsuarioOnClick(View view){
        Intent intent=new Intent(this,Act_RegistrarEquipo.class);
        intent.putExtra("principal",this.principal);
        startActivity(intent);
    }

    public void crearPartidoOnClick(View view){
        Intent intent=new Intent(this,Act_CrearPartido.class);
        intent.putExtra("principal",this.principal);
        startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data.equals("cargado")){
            Button bot=(Button)findViewById(R.id.BTN_ini_registrarEquipo);
            bot.setClickable(true);
        }
    }
}
