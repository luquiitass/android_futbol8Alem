package com.example.not.futbol8alemadmin.Actividades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.not.futbol8alemadmin.Adaptares.AdapterDato;
import com.example.not.futbol8alemadmin.Adaptares.Datos;
import com.example.not.futbol8alemadmin.Logica.Partido;
import com.example.not.futbol8alemadmin.R;

import java.util.ArrayList;


public class Act_Partido_A_finalizar extends ActionBarActivity {

    private Partido unPartido;

    private ListView LV_listaPrincipal;
    private Button BTN_finalizarPartdo;
    private Button BTN_modificarPartdo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__partido__a_finalizar);
        LV_listaPrincipal=(ListView)findViewById(R.id.LV_Partidos_a_finalizar);
        unPartido =(Partido)getIntent().getSerializableExtra("partido");
        cargarView();
        ArrayList<String> o=new ArrayList<>();
        LV_listaPrincipal.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, o));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__partido__a_finalizar, menu);
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

    public void cargarView(){
        mostrarEquipos();
        mostrarDatos();
        mostrarBotonTerminaFinalizarPartido();
    }

    public void mostrarEquipos(){
        View equipos=getLayoutInflater().inflate(R.layout.un_partido_equipos_sin_resultados,null);
        TextView TV_eL=(TextView)equipos.findViewById(R.id.part_eqL);
        TextView TV_eV=(TextView)equipos.findViewById(R.id.part_eqV);
        TV_eL.setText(this.unPartido.getEquipoLocal());
        TV_eV.setText(unPartido.getEquipoVisitante());
        LV_listaPrincipal.addHeaderView(equipos);
    }

    public void mostrarDatos(){
        nuevoSubtitulo("Datos del partido");
        ArrayList<Datos> datos=new ArrayList<Datos>();
        datos.add(new Datos("Cancha de:",unPartido.getCanchaeDe()));
        datos.add(new Datos("Fecha:",unPartido.getFeha()));
        datos.add(new Datos("Hora",unPartido.getHora()));
        datos.add(new Datos("Dirección:",unPartido.getDireccion()));
        AdapterDato adapterDato= new AdapterDato(this,datos);
        for (int i=0;i<datos.size();i++){
            LV_listaPrincipal.addHeaderView(adapterDato.getView(i,null,null));
        }
    }

    private void mostrarBotonTerminaFinalizarPartido(){
        nuevoSubtitulo("Operaciones");
        View view=getLayoutInflater().inflate(R.layout.layout_terminar_modificar_partido,null);
        this.BTN_finalizarPartdo=(Button)view.findViewById(R.id.BT_finalizarPartido);
        this.BTN_modificarPartdo=(Button)view.findViewById(R.id.BT_modificarPartido);
        LV_listaPrincipal.addHeaderView(view);

    }

    private void nuevoSubtitulo(String subtitulo){
        View unSubtitulo=getLayoutInflater().inflate(R.layout.layout_subtitulo,null);
        TextView TV_subtitulo=(TextView)unSubtitulo.findViewById(R.id.TV_subtitulo);
        TV_subtitulo.setText(subtitulo);
        LV_listaPrincipal.addHeaderView(unSubtitulo);
    }

    private void eventosBotones(){
        this.BTN_modificarPartdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Act_Partido_A_finalizar.this,Act_CrearPartido.class);
                intent.putExtra("patido",unPartido);
                startActivity(intent);
                finish();
            }
        });
    }
}
