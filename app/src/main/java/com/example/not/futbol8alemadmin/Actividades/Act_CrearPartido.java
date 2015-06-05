package com.example.not.futbol8alemadmin.Actividades;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Logica.Equipo;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;


public class Act_CrearPartido extends ActionBarActivity implements Observer{
    private Principal principal;
    private ArrayList<Equipo> equipos=new ArrayList<Equipo>();

    private Spinner SPN_eL;
    private Spinner SPN_eV;
    private Spinner SPN_canchaDe;
    private CheckBox CheckBox_otradireccion;

    private EditText ET_fecha;
    private EditText ET_hora;
    private EditText ET_direccionCancha;

    private int año;
    private int mes;
    private int dia;

    private int hora;
    private int minutos;

    static final int Diaalog_fecha= 0;
    static final int Dialog_hora = 77;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__crear_partido);
        principal=new Principal();
        principal.addObserver(this);
        SPN_eL=(Spinner)findViewById(R.id.crearPart_Spiner_eL);
        SPN_eV=(Spinner)findViewById(R.id.crearPart_Spiner_eV);
        SPN_canchaDe=(Spinner)findViewById(R.id.crearPart_Spiner_canchaDe);
        ET_direccionCancha=(EditText)findViewById(R.id.crearPart_ET_direccion);
        ET_fecha=(EditText)findViewById(R.id.crearPart_ET_fecha);
        ET_hora=(EditText)findViewById(R.id.crearPart_ET_hora);
        CheckBox_otradireccion=(CheckBox)findViewById(R.id.crearPart_ChecB_otraDireccion);
        principal.obtenerEquipos();
    }

    public void cargarView(){
        cargarFechaHoraActual();
        eventosChackBox();
        eventosEnSpinner();
    }

    public void seleccionarFecha(View view){
        showDialog(Diaalog_fecha);
    }

    public void seleccionarHora(View view){
        showDialog(Dialog_hora);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act__crear_partido, menu);
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

    public void cargarSpinnerEquipos(){
        ArrayList <String>list=new ArrayList<String>();
        list.add("Seleccionar");
        for (Equipo equipo:principal.getEquipos()){
            list.add(equipo.getNombreEquipo());
        }
        SPN_eL.setAdapter(new ArrayAdapter<String>(this, R.layout.libre_veterano,list));
        SPN_eV.setAdapter(new ArrayAdapter<String>(this, R.layout.libre_veterano,list));
    }

    private void eventosChackBox(){
        CheckBox_otradireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckBox_otradireccion.isChecked()){
                    ET_direccionCancha.setEnabled(true);
                }else{
                    ET_direccionCancha.setEnabled(false);
                    if (SPN_canchaDe.getSelectedItemPosition()!= 0) {
                        ET_direccionCancha.setText(principal.obtenerEquipo(SPN_canchaDe.getSelectedItem().toString()).getDireccionCancha());
                    }else{
                        ET_direccionCancha.setText("");
                    }
                }
            }
        });
    }

    private void eventosEnSpinner(){
        this.SPN_eL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!SPN_eL.getSelectedItem().equals(SPN_eV.getSelectedItem())) {
                    if (SPN_eV.getSelectedItemPosition() != 0 && SPN_eL.getSelectedItemPosition() != 0) {
                        String[] item = {"Seleccionar EquipoLocal", SPN_eL.getSelectedItem().toString(), SPN_eV.getSelectedItem().toString()};
                        SPN_canchaDe.setAdapter(new ArrayAdapter<String>(Act_CrearPartido.this, R.layout.libre_veterano, item));
                    }
                }else{
                    Toast.makeText(Act_CrearPartido.this,"Los equipos deben ser distintos",Toast.LENGTH_LONG).show();
                    SPN_eL.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.SPN_eV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!SPN_eL.getSelectedItem().equals(SPN_eV.getSelectedItem())) {
                    if (SPN_eV.getSelectedItemPosition()!=0 && SPN_eL.getSelectedItemPosition()!=0){
                        String[] item={"Seleccionar EquipoLocal",SPN_eL.getSelectedItem().toString(),SPN_eV.getSelectedItem().toString()};
                        SPN_canchaDe.setAdapter(new ArrayAdapter<String>(Act_CrearPartido.this,R.layout.libre_veterano,item));
                    }
                }else{
                    Toast.makeText(Act_CrearPartido.this,"Los equipos deben ser distintos",Toast.LENGTH_LONG).show();
                    SPN_eV.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.SPN_canchaDe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ET_direccionCancha.setText(principal.obtenerEquipo(parent.getSelectedItem().toString()).getDireccionCancha());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void cargarFechaHoraActual(){
        Calendar c=Calendar.getInstance();
        año=c.get(Calendar.YEAR);
        mes=c.get(Calendar.MONTH);
        dia=c.get(Calendar.DAY_OF_MONTH);
        updateFecha();
        hora=c.get(Calendar.HOUR_OF_DAY);
        minutos=c.get(Calendar.MINUTE);
        updateHora();
    }

    public void updateFecha(){
        ET_fecha.setText(new StringBuilder()
                .append(dia).append("/").append(mes+1).append("/").append(año).append(""));
    }

    public void updateHora(){
        ET_hora.setText(new StringBuilder()
                .append(hora).append(":").append(minutos).append(":").append("00").append(""));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener= new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int año2, int mes2, int dia2) {
            año=año2;
            mes=mes2;
            dia=dia2;
            updateFecha();
        }
    };

    public TimePickerDialog.OnTimeSetListener  mTimeSetListener=new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hora=hourOfDay;
            minutos=minute;
            updateHora();

        }
    };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case Diaalog_fecha:
                return new DatePickerDialog(this,mDateSetListener,año,mes,dia);
            case Dialog_hora:
                return new TimePickerDialog(this,mTimeSetListener,hora,minutos, android.text.format.DateFormat.is24HourFormat(this));
        }
        return null;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data!=null){
            switch (data.toString()){
                case "cargarEquipos":
                    this.equipos=((Principal)observable).getEquipos();
                    cargarSpinnerEquipos();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,"Error de Conexión",Toast.LENGTH_LONG).show();
                    break;
            }

        }
        cargarView();
    }
}
