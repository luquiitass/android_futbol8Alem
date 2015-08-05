package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Logica.Partido;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Observable;
import java.util.Observer;


public class Act_finalizarPartido extends ActionBarActivity implements Observer{

    private Principal principal;
    private Partido unPartido;

    final private DiversosDialog pDialog=new DiversosDialog();

    private Spinner SPN_GL;
    private Spinner SPN_GV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_finalizar_partido);
        SPN_GL=(Spinner)findViewById(R.id.SPN_FinalizarPartido_GL);
        SPN_GV=(Spinner)findViewById(R.id.SPN_FinalizarPartido_GV);
        principal=(Principal)getIntent().getSerializableExtra("principal");
        principal.addObserver(this);
        unPartido=principal.obtenerPartido(getIntent().getIntExtra("unPartido",-1));
        unPartido.addObserver(this);
        setTitle(getString(R.string.LG_finalizarPartido)+" "+principal.queAdministro());
        cargarView();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_finalizar_partido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private void cargarView(){
        EditText ET_eL=(EditText)findViewById(R.id.ET_FinalPartido_EquipoLocales);
        EditText ET_eV=(EditText)findViewById(R.id.ET_FinalPartido_EquipoVisitante);
        ET_eL.setText(unPartido.getEquipoLocal());
        ET_eV.setText(unPartido.getEquipoVisitante());
        String[]items={"Seleccionar","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
        SPN_GL.setAdapter(new ArrayAdapter<String>(this,R.layout.libre_veterano, items));
        SPN_GV.setAdapter(new ArrayAdapter<String>(this, R.layout.libre_veterano, items));
    }

    public void actualizarResultadoPartido(View view){
        if (SPN_GL.getSelectedItemPosition()!=0 && SPN_GV.getSelectedItemPosition()!=0) {
            pDialog.onProgresSDialog(this, getString(R.string.LG_cargandoResultados));
            unPartido.modificarResultados(Integer.parseInt(SPN_GL.getSelectedItem().toString()),Integer.parseInt(SPN_GV.getSelectedItem().toString()));
        }else {
            Toast.makeText(this,getString(R.string.LG_debeColocarLosGolesEnAmbosPartidos),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!=null){
            switch (data.toString()){
                case "Actualizado":
                    Toast.makeText(this,getString(R.string.LG_resultadoModificado),Toast.LENGTH_LONG).show();
                    devolverPrincipal(true);
                    break;
                case  "noActualizado":
                    Toast.makeText(this,getResources().getString(R.string.LG_noSePudoFinalizarElPartido),Toast.LENGTH_LONG).show();
                    break;
                case "Error de Conexión":
                    Toast.makeText(this,getResources().getText(R.string.LG_errorDeConexion),Toast.LENGTH_LONG).show();
                    break;
            }
            pDialog.ofProgressDialog();
        }
    }




    public void devolverPrincipal(Boolean finalizar){
        Intent intent=new Intent();
        intent.putExtra("principal", principal);
        intent.putExtra("finalizar",finalizar);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                devolverPrincipal(false);
                return true;
        }
        return false;
    }
}
