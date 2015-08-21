package com.example.not.futbol8alemadmin.Actividades;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.not.futbol8alemadmin.Exepcion.Exepcion;
import com.example.not.futbol8alemadmin.Logica.Equipo;
import com.example.not.futbol8alemadmin.Logica.Partido;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;


public class Act_CrearPartido extends ActionBarActivity implements Observer{

    private Principal principal;
    private Partido unPartido;

    private int request_code=1;

    private Spinner SPN_eL;
    private Spinner SPN_eV;
    private Spinner SPN_canchaDe;
    private CheckBox CheckBox_otradireccion;
    private Button BTN_Crear_Modificar;
    private EditText ET_fecha;
    private EditText ET_hora;
    private EditText ET_direccionCancha;

    private Boolean esModificar;

    private int año;
    private int mes;
    private int dia;

    private int hora;
    private int minutos;

    static final int Diaalog_fecha= 0;
    static final int Dialog_hora = 77;

    private final DiversosDialog pDialog=new DiversosDialog();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__crear_partido);

        SPN_eL=(Spinner)findViewById(R.id.crearPart_Spiner_eL);
        SPN_eV=(Spinner)findViewById(R.id.crearPart_Spiner_eV);
        SPN_canchaDe=(Spinner)findViewById(R.id.crearPart_Spiner_canchaDe);
        ET_direccionCancha=(EditText)findViewById(R.id.crearPart_ET_direccion);
        ET_fecha=(EditText)findViewById(R.id.crearPart_ET_fecha);
        ET_hora=(EditText)findViewById(R.id.crearPart_ET_hora);
        CheckBox_otradireccion=(CheckBox)findViewById(R.id.crearPart_ChecB_otraDireccion);
        BTN_Crear_Modificar=(Button)findViewById(R.id.BTN_Crear_modificar_partido);

        principal=(Principal)getIntent().getSerializableExtra("principal");
        principal.addObserver(this);

        if (getIntent().getBooleanExtra("modificar",false)){
            esModificar=true;
            unPartido=principal.obtenerPartido(getIntent().getIntExtra("unPartido",-1));
            unPartido.addObserver(this);
            setTitle("Modificar partido " + principal.queAdministro());
            unPartido.addObserver(this);
            BTN_Crear_Modificar.setText("Modificar");
            cargarViewParaModificar();
        }else{
            esModificar=false;
            setTitle("Crear partido " + principal.queAdministro());
            BTN_Crear_Modificar.setText("Crear Partido");
        }
        cargarView();



    }

    public void registrarEquipoOnClick(View view){
        Intent intent=new Intent(this,Act_RegistrarEquipo.class);
        intent.putExtra("principal",principal);
        startActivityForResult(intent,request_code);
    }

    public void eventoBootonOnClicK(View view){
       try {
           if (((Button) view).getText().toString().equals("Modificar")) {
               crearModificarPartido("modificar");
           } else if (((Button) view).getText().toString().equals("Crear Partido")) {
               crearModificarPartido("crear");
           }
       }catch (Exception e){
           Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
           pDialog.ofProgressDialog();
       }
    }


    private void crearModificarPartido(String accion)throws Exception{
        if (SPN_eL.getSelectedItemPosition()!=0 && SPN_eV.getSelectedItemPosition()!=0){
            if (SPN_canchaDe.getSelectedItemPosition()!=0){
                if (!ET_direccionCancha.getText().toString().equals("")){
                    pDialog.onProgresSDialog(this, "Cargando...");
                    switch (accion){
                        case "modificar":
                            unPartido.modificarPartido_BD( SPN_eL.getSelectedItem().toString(), SPN_eV.getSelectedItem().toString(), SPN_canchaDe.getSelectedItem().toString(), ET_direccionCancha.getText().toString(), ET_fecha.getText().toString(), ET_hora.getText().toString());
                            break;
                        case "crear":
                            principal.crearPartido_BD(SPN_eL.getSelectedItem().toString(), SPN_eV.getSelectedItem().toString(), SPN_canchaDe.getSelectedItem().toString(), ET_direccionCancha.getText().toString(), ET_fecha.getText().toString(), ET_hora.getText().toString());
                            break;
                    }
                }else{Toast.makeText(this,getString(R.string.LG_debeColocarDireccionCancha),Toast.LENGTH_SHORT).show();}
            }else{Toast.makeText(this,R.string.LG_debeSeleccinarEquipoLocal,Toast.LENGTH_SHORT).show();
                SPN_canchaDe.setFocusable(true);}
        }else{Toast.makeText(this,getString(R.string.LG_debeSaleccionarLosEquiposDelPartido),Toast.LENGTH_SHORT).show();}
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
        return super.onOptionsItemSelected(item);
    }

    public void cargarView(){
        cargarFechaHoraActual();
        eventosEnSpinner();
        cargarSpinnerEquipos();
        eventosChackBox();
    }

    private void cargarViewParaModificar() {
        ET_hora.setText(unPartido.getHora());
        ET_fecha.setText(unPartido.getFecha());

    }

    private int obtenerPosicionParaSpiines(ArrayList<String> list,String objeto){
        int retorno=0;
        for (int i=0;i<list.size();i++){
            if (list.get(i).equals(objeto)){
                retorno= i;
            }
        }
        return retorno;
    }

    public void cargarSpinnerEquipos(){
        ArrayList <String>list=new ArrayList<String>();
        list.add("Seleccionar");
        for (Equipo equipo:principal.getEquipos()){
            list.add(equipo.getNombreEquipo());
        }
        SPN_eL.setAdapter(new ArrayAdapter<String>(this, R.layout.libre_veterano,list));
        SPN_eV.setAdapter(new ArrayAdapter<String>(this, R.layout.libre_veterano, list));
        if (esModificar){
            SPN_eL.setSelection(obtenerPosicionParaSpiines(list,unPartido.getEquipoLocal()));
            SPN_eV.setSelection(obtenerPosicionParaSpiines(list,unPartido.getEquipoVisitante()));
        }
    }


    private void eventosChackBox(){
        CheckBox_otradireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckBox_otradireccion.isChecked()) {
                    ET_direccionCancha.setEnabled(true);
                } else {
                    ET_direccionCancha.setEnabled(false);
                    if (SPN_canchaDe.getSelectedItemPosition() != 0) {
                        ET_direccionCancha.setText(principal.obtenerEquipo(SPN_canchaDe.getSelectedItem().toString()).getDireccionCancha());
                    } else {
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
                if (SPN_eV.getSelectedItemPosition() != 0 && SPN_eL.getSelectedItemPosition() != 0) {
                    if (!SPN_eL.getSelectedItem().equals(SPN_eV.getSelectedItem())) {
                            String[] item = {"Seleccionar EquipoLocal", SPN_eL.getSelectedItem().toString(), SPN_eV.getSelectedItem().toString()};
                            SPN_canchaDe.setAdapter(new ArrayAdapter<String>(Act_CrearPartido.this, R.layout.libre_veterano, item));
                    }else{
                        Toast.makeText(Act_CrearPartido.this,"Los equipos deben ser distintos",Toast.LENGTH_LONG).show();
                        SPN_eL.setSelection(0);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.SPN_eV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (SPN_eV.getSelectedItemPosition()!=0 && SPN_eL.getSelectedItemPosition()!=0) {
                    if (!SPN_eL.getSelectedItem().equals(SPN_eV.getSelectedItem())) {
                        ArrayList<String> item=new ArrayList<String>();
                        item.add("Seleccionar Equipo Local");
                        item.add( SPN_eL.getSelectedItem().toString());
                        item.add(SPN_eV.getSelectedItem().toString());
                        item.add("Neutral");
                        SPN_canchaDe.setAdapter(new ArrayAdapter<String>(Act_CrearPartido.this, R.layout.libre_veterano, item));
                        if (esModificar) {
                            SPN_canchaDe.setSelection(obtenerPosicionParaSpiines(item, unPartido.getCanchaeDe()));
                        }
                    } else {
                        Toast.makeText(Act_CrearPartido.this, "Los equipos deben ser distintos", Toast.LENGTH_LONG).show();
                        SPN_eV.setSelection(0);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.SPN_canchaDe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position!=3) {
                    ET_direccionCancha.setText(principal.obtenerEquipo(parent.getSelectedItem().toString()).getDireccionCancha());
                    CheckBox_otradireccion.setEnabled(true);
                }else if (position==3){
                    ET_direccionCancha.setEnabled(true);
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
        hora=c.get(Calendar.HOUR_OF_DAY);
        minutos=c.get(Calendar.MINUTE);
        if (!esModificar) {
            updateHora();
            updateFecha();
        }
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
                case "cargarTodo":
                    cargarView();
                    break;
                case "Error de conexión":
                    Toast.makeText(this,getResources().getText(R.string.LG_errorDeConexion),Toast.LENGTH_LONG).show();
                    devolverPrincipal();
                    break;
                case "partidoInsertado":
                    Toast.makeText(this,getResources().getText(R.string.LG_partidoCreado),Toast.LENGTH_LONG).show();
                    devolverPrincipal();
                    break;
                case "partidoNoInsertado":
                    Toast.makeText(this,getResources().getText(R.string.LG_noSeCreoElPartio),Toast.LENGTH_LONG).show();
                    break;
                case "Actualizado":
                    Toast.makeText(this,getResources().getText(R.string.LG_partidoModificado),Toast.LENGTH_LONG).show();
                    devolverPrincipal();
                    break;
                case "noActualizado":
                    Toast.makeText(this,getString(R.string.LG_noSePudoModificar),Toast.LENGTH_LONG).show();
                    break;
            }
            pDialog.ofProgressDialog();

        }
    }

    public void devolverPrincipal(){
        Intent intent=new Intent();
        intent.putExtra("principal", principal);
        intent.putExtra("recargar",true);
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
            cargarSpinnerEquipos();
                        //otros metodos
        }
    }

}
