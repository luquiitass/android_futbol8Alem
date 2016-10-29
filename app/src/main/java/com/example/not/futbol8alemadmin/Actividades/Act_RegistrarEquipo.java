package com.example.not.futbol8alemadmin.Actividades;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Exepcion.Exepcion;
import com.example.not.futbol8alemadmin.Logica.Equipo;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class Act_RegistrarEquipo extends ActionBarActivity implements Observer{

    private Principal principal;
    private Equipo unEquipo;

    private Boolean modificarEquipo;

    private int año;
    private int mes;
    private int dia;
    static final int DATE_DIALOG_ID = 0;

    private EditText ET_fechaInicio;
    private EditText ET_nombreEquipo;
    private EditText ET_direccionCancha;
    private EditText ET_telefono;
    private CheckBox CB_visible;

    private final DiversosDialog pDialog=new DiversosDialog();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__registrar_equipo);
        ET_fechaInicio=(EditText)findViewById(R.id.ET_regEquipo_fechaInicio);
        ET_nombreEquipo=(EditText)findViewById(R.id.ET_regEquipo_nombreEquipo);
        ET_direccionCancha=(EditText)findViewById(R.id.ET_regEquipo_direccion);
        ET_telefono=(EditText)findViewById(R.id.ET_regEquipo_telefono);
        principal=(Principal)getIntent().getExtras().getSerializable("principal");
        modificarEquipo=getIntent().getBooleanExtra("modificarUnEquipo",false);
        if (!modificarEquipo) {
            setTitle("Registrar Equipo C." + principal.queAdministro());
            principal.getEquipos();
        }else {
            setTitle("Modificar Equipo C."+principal.queAdministro());
            CB_visible=(CheckBox)findViewById(R.id.CB_visible);
            unEquipo=principal.obtenerEquipo(getIntent().getStringExtra("unEquipo"));
            unEquipo.addObserver(this);
            cargarViewParaModificar();
            Button BTN_modificarDatosEquipos=(Button)findViewById(R.id.BTN_RE_registrarEquipo);
            BTN_modificarDatosEquipos.setText("Confirmar modificaciones");
        }
        principal.addObserver(this);
        cargarViewInicio();
    }

    private void cargarViewParaModificar(){
        ET_nombreEquipo.setText(unEquipo.getNombreEquipo());
        ET_direccionCancha.setText(unEquipo.getDireccionCancha());
        ET_telefono.setText(unEquipo.getTelefono());
        if (unEquipo.isVisible()){
            CB_visible.setChecked(true);
            CB_visible.setText("visible");
        }else{
            CB_visible.setChecked(false);
            CB_visible.setText("No visible");
        }
        CB_visible.setEnabled(true);
    }

    public void registrarEquipo(View view) throws Exepcion {
        try {
                if (!ET_nombreEquipo.getText().toString().trim().equals("") && !ET_direccionCancha.getText().toString().trim().equals("")) {
                    pDialog.onProgresSDialog(this,"Cargando...");
                    String telefono="Sin número";
                    if (!ET_telefono.getText().toString().trim().equals("")){
                        telefono=ET_telefono.getText().toString();
                    }
                    if (!modificarEquipo) {
                        this.principal.crearEquipo_BD(ET_nombreEquipo.getText().toString(), ET_fechaInicio.getText().toString(), ET_direccionCancha.getText().toString(),telefono);
                    }else{
                        unEquipo.modificarEquipo_BD(unEquipo.getNombreEquipo(), ET_nombreEquipo.getText().toString(), ET_fechaInicio.getText().toString(), ET_direccionCancha.getText().toString(),ET_telefono.getText().toString(),CB_visible.isChecked());
                    }
                }else {
                    Toast.makeText(this,"Debe completar todos los campos",Toast.LENGTH_LONG).show();
                }
        }catch (Exepcion e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            pDialog.ofProgressDialog();
        }
    }

    public void cargarViewInicio(){
        cargargarFechaActual();
    }

    public void seleccionarFechaInicio(View view) {
        showDialog(DATE_DIALOG_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act__registrar_equipo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    //Todo --------------------Metodos para modificar y cargar datos en las View al iniciar---------------


    public void cargargarFechaActual(){
        if (!modificarEquipo) {
            final Calendar c = Calendar.getInstance();
            año = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
        }else{
            final Calendar c = unEquipo.getFechadeInicioCalendar();
            año = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
        }
        updateDisplay();

    }

    public void updateDisplay(){
        ET_fechaInicio.setText(new StringBuilder()
                .append(dia).append("/").append(mes+1).append("/").append(año).append(""));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener= new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int año2, int mes2, int dia2) {
            año=año2;
            mes=mes2;
            dia=dia2;
            updateDisplay();
        }
    };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,mDateSetListener,año,mes,dia);
        }
        return null;
    }


    @Override
    public void update(Observable observable, Object data) {
        if (data != null) {
            switch (data.toString()) {
                case "equipoInsertado":
                    Toast.makeText(this,getResources().getString(R.string.LG_equipoRegistrado), Toast.LENGTH_LONG).show();
                    devolverPrincipal(false);
                    break;
                case "equipoNoInsertado":
                    Toast.makeText(this,getResources().getString(R.string.LG_noSePudoRegistrarEquipo), Toast.LENGTH_LONG).show();
                    break;
                case "Actualizado":
                    Toast.makeText(this,getResources().getString(R.string.LG_equipoModificado),Toast.LENGTH_LONG).show();
                    devolverPrincipal(true);
                    break;
                case "noActualizado":
                    Toast.makeText(this,getResources().getString(R.string.LG_noSePudoModificar),Toast.LENGTH_LONG).show();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,getResources().getString(R.string.LG_errorDeConexion),Toast.LENGTH_LONG).show();
                    break;
            }
            pDialog.ofProgressDialog();
        }
    }

    public void devolverPrincipal(Boolean conEquipo){
        Intent intent=new Intent();
        intent.putExtra("principal", principal);
        if(conEquipo) {
            intent.putExtra("nombreE", unEquipo.getNombreEquipo());
        }
        intent.putExtra("recargar",true);
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
