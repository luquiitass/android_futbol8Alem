package com.example.not.futbol8alemadmin.Actividades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Logica.Equipo;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;


public class Act_BusquedaPartidos extends Activity{

    private int año;
    private int mes;
    private int dia;

    static final int Diaalog_fecha= 0;


    private Principal unPrincipal;


    private RequestParams par=new RequestParams();

    private CheckBox Chek_porFecha;
    private CheckBox Chek_porEquipo;

    private EditText ET_fechaInicio;
    private EditText ET_fechaFinal;

    private int fechaSeleccionada=0;

    private LinearLayout lay_fechas;
    private Spinner SPN_equipos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__busqueda_partidos);
        unPrincipal=(Principal)getIntent().getSerializableExtra("principal");

        this.Chek_porEquipo=(CheckBox)findViewById(R.id.check_busqueda_x_equipo);
        this.Chek_porFecha=(CheckBox)findViewById(R.id.check_busqueda_x_fecha);

        this.ET_fechaInicio=(EditText)findViewById(R.id.ET_fechaInicio);
        this.ET_fechaFinal=(EditText)findViewById(R.id.ET_fechaFinal);

        this.SPN_equipos=(Spinner)findViewById(R.id.SPN_equipos);
        this.lay_fechas=(LinearLayout)findViewById(R.id.lay_busq_x_fecha);

        eventosEnCheckBox();
        cargarSpinnerEquipos();
        cargarFechaActual();
    }

    public void seleccionarFechaOnClick(View view){
        if (view.getId()==R.id.ET_fechaFinal){
            fechaSeleccionada=2;
        }else if(view .getId()==R.id.ET_fechaInicio){
            fechaSeleccionada=1;
        }
        showDialog(Diaalog_fecha);
    }

    private void eventosEnCheckBox(){
        this.Chek_porEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Chek_porEquipo.isChecked()) {
                    Chek_porFecha.setChecked(false);
                    lay_fechas.setVisibility(View.GONE);
                    SPN_equipos.setVisibility(View.VISIBLE);

                } else {
                    SPN_equipos.setVisibility(View.GONE);
                    lay_fechas.setVisibility(View.GONE);
                }
            }
        });
        this.Chek_porFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Chek_porFecha.isChecked()) {
                    Chek_porEquipo.setChecked(false);
                    lay_fechas.setVisibility(View.VISIBLE);
                    SPN_equipos.setVisibility(View.GONE);

                } else {
                    SPN_equipos.setVisibility(View.GONE);
                    lay_fechas.setVisibility(View.GONE);
                }
            }
        });
    }

    private void cargarSpinnerEquipos(){
        ArrayList<String>list=new ArrayList<>();
        list.add("Seleccionar Equipo");
        for (Equipo unEquipo:unPrincipal.getEquipos()){
            list.add(unEquipo.getNombreEquipo());
        }
        SPN_equipos.setAdapter(new ArrayAdapter<String>(this, R.layout.libre_veterano, list));
    }

    public void realizarBusquedaOnClick(View view){
        if (Chek_porEquipo.isChecked()) {
            if (SPN_equipos.getSelectedItemPosition() != 0) {
                devolverPrincipal();
            } else {
                Toast.makeText(this, "Debe seleccionar un equipo", Toast.LENGTH_LONG).show();
            }
        }else if (Chek_porFecha.isChecked()){
                devolverPrincipal();
        }else {
            Toast.makeText(this, "Debe seleccionar el metodo de busqueda", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cargarFechaActual(){
        Calendar c=Calendar.getInstance();
        año=c.get(Calendar.YEAR);
        mes=c.get(Calendar.MONTH);
        dia=c.get(Calendar.DAY_OF_MONTH);

        updateFecha();

    }

    public void updateFecha(){
        if (fechaSeleccionada==1 || fechaSeleccionada==0) {
            ET_fechaInicio.setText(new StringBuilder()
                    .append(año).append("-").append(String.format("%02d", mes + 1)).append("-").append(String.format("%02d",dia)).append(""));
        }
        if(fechaSeleccionada==2 || fechaSeleccionada==0) {
            ET_fechaFinal.setText(new StringBuilder()
                    .append(año).append("-").append(String.format("%02d",mes+1)).append("-").append(String.format("%02d",dia)).append(""));
        }
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case Diaalog_fecha:
                return new DatePickerDialog(this,mDateSetListener,año,mes,dia);
             }
        return null;
    }


    public void devolverPrincipal(){
        Intent intent=new Intent();
        if (Chek_porFecha.isChecked()){
            intent.putExtra("tipoBusqueda","fecha");
            intent.putExtra("FI",ET_fechaInicio.getText().toString());
            intent.putExtra("FF",ET_fechaFinal.getText().toString());
        }else if(Chek_porEquipo.isChecked()){
            intent.putExtra("tipoBusqueda","equipo");
            intent.putExtra("equipo",SPN_equipos.getSelectedItem().toString());
        }
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
}
