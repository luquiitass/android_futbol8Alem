package com.example.not.futbol8alemadmin.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Logica.Equipo;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Observable;
import java.util.Observer;


public class Act_unEquipo extends ActionBarActivity implements Observer{
    private int request_code=1;

    private Principal principal;
    private Equipo unEquipo;


    private DiversosDialog pDialog=new DiversosDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_un_equipo);
        principal=(Principal)getIntent().getSerializableExtra("principal");
        principal.addObserver(this);
        unEquipo=principal.obtenerEquipo(getIntent().getStringExtra("unEquipo"));
        setTitle("Equipo C."+principal.queAdministro());
        cargarDatos();


    }

    private void cargarDatos(){
        TextView TV_equipo =(TextView)findViewById(R.id.TV_unEquipo_nombreEquipo);
        TextView TV_fechaInicio =(TextView)findViewById(R.id.TV_unEquipo_fechaIni);
        TextView TV_fechaReg =(TextView)findViewById(R.id.TV_unEquipo_fechaReg);
        TextView TV_direccion =(TextView)findViewById(R.id.TV_unEquipo_direccion);
        TextView TV_telefono= (TextView)findViewById(R.id.TV_unEquipo_telefono);

        TV_equipo.setText(unEquipo.getNombreEquipo());
        TV_fechaInicio.setText(unEquipo.getFchaInicio());
        TV_fechaReg.setText(unEquipo.getFechaRegitro());
        TV_direccion.setText(unEquipo.getDireccionCancha());
        TV_telefono.setText(unEquipo.getTelefono());
    }

    public void editarEquipoOnClick(View view){
        Intent intent=new Intent(this,Act_RegistrarEquipo.class);
        intent.putExtra("principal",principal);
        intent.putExtra("modificarUnEquipo",true);
        intent.putExtra("unEquipo",unEquipo.getNombreEquipo());
        startActivityForResult(intent,request_code);
    }

    public void eliminarEquipoOnClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(getString(R.string.LG_advertenciaEliminarEquipo));
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog.onProgresSDialog(Act_unEquipo.this,getString(R.string.LG_eliminando));
                principal.eliminarEquipo_BD(unEquipo.getNombreEquipo());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_un_equipo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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
            String nuevoID=data.getStringExtra("nombreE");
            if (nuevoID!=null) {
                unEquipo = principal.obtenerEquipo(nuevoID);
            }cargarDatos();
            //otros metodos
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!=null){
            switch (data.toString()){
                case "Error de conexión":
                    Toast.makeText(this,getResources().getText(R.string.LG_errorDeConexion),Toast.LENGTH_LONG).show();
                    break;
                case "borrado":
                    Toast.makeText(this,getString(R.string.LG_equipoEliminado),Toast.LENGTH_LONG).show();
                    devolverPrincipal();
                    break;
                case "noBorrado":
                    Toast.makeText(this,getResources().getString(R.string.LG_noSePudiEliminarPartido),Toast.LENGTH_LONG).show();
            }
            pDialog.ofProgressDialog();
        }
    }
}
