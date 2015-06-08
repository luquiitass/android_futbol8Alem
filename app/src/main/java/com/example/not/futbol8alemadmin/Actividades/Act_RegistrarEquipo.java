package com.example.not.futbol8alemadmin.Actividades;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Exepcion.Exepcion;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class Act_RegistrarEquipo extends ActionBarActivity implements Observer{

    private Principal principal;



    private int año;
    private int mes;
    private int dia;
    static final int DATE_DIALOG_ID = 0;

    private EditText ET_fechaInicio;
    private Spinner SPI_equipoLibres;
    private EditText ET_nombreEquipo;
    private EditText ET_direccionCancha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__registrar_equipo);
        ET_fechaInicio=(EditText)findViewById(R.id.ET_regEquipo_fechaInicio);
        SPI_equipoLibres=(Spinner)findViewById(R.id.regEquipo_Spinnerlibre);
        ET_nombreEquipo=(EditText)findViewById(R.id.ET_regEquipo_nombreEquipo);
        ET_direccionCancha=(EditText)findViewById(R.id.ET_regEquipo_direccion);
        principal=(Principal)getIntent().getExtras().getSerializable("principal");
        principal.getEquipos();
        principal.addObserver(this);
        cargarViewInicio();
    }

    public void registrarEquipo(View view) throws Exepcion {
        try {
            if (SPI_equipoLibres.getSelectedItemPosition() != 0) {
                int libre =-1;
                if (SPI_equipoLibres.getSelectedItem().toString().equals("Libre")) {
                    libre = 1;
                } else if (SPI_equipoLibres.getSelectedItem().toString().equals("Veteranos")) {
                    libre = 0;
                }
                if (!ET_nombreEquipo.getText().toString().equals("") && !ET_direccionCancha.getText().toString().equals("")) {
                    this.principal.crearEquipo(ET_nombreEquipo.getText().toString(), ET_fechaInicio.getText().toString(), ET_direccionCancha.getText().toString(), libre);
                }else {
                    Toast.makeText(this,"Debe completar todos los campos",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"Seleccionar el tipo de equipo",Toast.LENGTH_LONG).show();
            }
        }catch (Exepcion e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void cargarViewInicio(){
        cargargarFechaActual();
        cargarItemSpinner();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Todo --------------------Metodos para modificar y cargar datos en las View al iniciar---------------

    public void cargarItemSpinner(){
        String[] item={"Seleccionar","Libre","Veteranos"};
        SPI_equipoLibres.setAdapter(new ArrayAdapter<String>(this,R.layout.libre_veterano,item));

    }

    public void cargargarFechaActual(){
        final Calendar c= Calendar.getInstance();
        año=c.get(Calendar.YEAR);
        mes=c.get(Calendar.MONTH);
        dia=c.get(Calendar.DAY_OF_MONTH);
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
                    this.finish();
                    Toast.makeText(this, "Insertado exitosamente", Toast.LENGTH_LONG).show();
                    break;
                case "equipoNoInsertado":
                    Toast.makeText(this, "No se pudo registrar el equipo", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
