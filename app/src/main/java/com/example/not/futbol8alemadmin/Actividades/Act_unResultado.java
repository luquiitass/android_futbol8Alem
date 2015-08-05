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

import com.example.not.futbol8alemadmin.Logica.Partido;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Observable;
import java.util.Observer;


public class Act_unResultado extends ActionBarActivity implements Observer{

    private Principal principal;
    private Partido unPartido;

    private int request_code=1;

    private DiversosDialog pDialog=new DiversosDialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_un_resultado);
        principal=(Principal)getIntent().getSerializableExtra("principal");
        unPartido= principal.obtenerPartido(getIntent().getIntExtra("unResultado", -1));
        setTitle("Partido jugado C."+principal.queAdministro());
        principal.addObserver(this);
        cargaaarDatosEnViews();
    }

    private void cargaaarDatosEnViews(){
        TextView TV_eqL=(TextView)findViewById(R.id.TV_eqL);
        TextView TV_eqV=(TextView)findViewById(R.id.TV_eqV);
        TextView TV_gL=(TextView)findViewById(R.id.TV_golesL);
        TextView TV_gV=(TextView)findViewById(R.id.TV_golesV);

        TextView TV_canchade=(TextView)findViewById(R.id.TV_canchaDe);
        TextView TV_fecha=(TextView)findViewById(R.id.TV_fecha);
        TextView TV_hora=(TextView)findViewById(R.id.TV_hora);
        TextView TV_direccion=(TextView)findViewById(R.id.TV_direccion);

        TV_eqL.setText(unPartido.getEquipoLocal());
        TV_eqV.setText(unPartido.getEquipoVisitante());
        TV_gL.setText(String.valueOf(unPartido.getGolesLocal()));
        TV_gV.setText(String.valueOf(unPartido.getGolesVisitante()));
        TV_canchade.setText(unPartido.getCanchaeDe());
        TV_direccion.setText(unPartido.getDireccion());
        TV_hora.setText(unPartido.getHora());
        TV_fecha.setText(unPartido.getFecha());
    }

    public void eliminarPartidoOnClick(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(getString(R.string.LG_advertenciaEliminarEquipo));
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog.onProgresSDialog(Act_unResultado.this,getString(R.string.LG_eliminando));
                principal.eliminarPartido_BD(unPartido.getId_partido());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void modificarResultadosOnClick(View view){
        Intent intent=new Intent(this,Act_finalizarPartido.class);
        intent.putExtra("unPartido",unPartido.getId_partido());
        intent.putExtra("principal",principal);
        startActivityForResult(intent,request_code );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_un_resultado, menu);
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
                case "borrado":
                    Toast.makeText(this,getString(R.string.LG_partidoEliminado), Toast.LENGTH_LONG).show();
                    devolverPrincipal();
                    break;
                case "noBorrado":
                    Toast.makeText(this,getString(R.string.LG_noSePudiEliminarPartido),Toast.LENGTH_LONG).show();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,getString(R.string.LG_errorDeConexion),Toast.LENGTH_LONG).show();
                    break;
            }
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
            unPartido=principal.obtenerPartido(unPartido.getId_partido());
            cargaaarDatosEnViews();
        }
    }
}
