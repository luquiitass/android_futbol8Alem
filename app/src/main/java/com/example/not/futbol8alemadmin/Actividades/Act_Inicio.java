package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;


public class Act_Inicio extends ActionBarActivity {

    private int request_code=1;

    private Principal principal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        int libre;
        if (getIntent().getBooleanExtra("Libre",false)){
            libre=1;
        }else{
            libre=0;
        }
        this.principal=new Principal(libre);
        setTitle("Adm Equipos C."+principal.queAdministro());
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

        return super.onOptionsItemSelected(item);
    }


// TODO ---------------mis metodos--------------
    public void irEquiposOnClick(View view){
        Intent intent=new Intent(this,Act_Equipos.class);
        intent.putExtra("principal", this.principal);
        startActivity(intent);
    }

    public void irPartidosOnClick(View view){
        Intent intent=new Intent(this,Act_Partidoss.class);
        intent.putExtra("principal",this.principal);
        startActivityForResult(intent,request_code);
    }

    public void finalizarPartidoOnClick(View view){
        Intent intent=new Intent(this,Act_Resultados.class);
        intent.putExtra("principal", this.principal);
        startActivityForResult(intent,request_code);
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
        }
    }
}
