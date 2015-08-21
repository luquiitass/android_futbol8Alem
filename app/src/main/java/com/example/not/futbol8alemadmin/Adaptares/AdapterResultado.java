package com.example.not.futbol8alemadmin.Adaptares;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.not.futbol8alemadmin.Logica.Partido;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.List;

public class AdapterResultado extends ArrayAdapter<Partido> {

    private final List<Partido> list;
    private final Activity context;

    public AdapterResultado(Activity context, List<Partido> list) {
        super(context, R.layout.un_resultado_lista, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView equiL;
        protected TextView equiV;
        protected TextView golL;
        protected TextView fecha;
        protected TextView golV;
        protected TextView vs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.un_resultado_lista, null);

            viewHolder = new ViewHolder();
            viewHolder.equiL = (TextView) convertView.findViewById(R.id.UnPart_eqL);
            viewHolder.equiV = (TextView) convertView.findViewById(R.id.UnPart_eqV);
            viewHolder.golL = (TextView) convertView.findViewById(R.id.TV_golesL);
            viewHolder.fecha = (TextView) convertView.findViewById(R.id.TV_fecha);
            viewHolder.golV=(TextView)convertView.findViewById(R.id.TV_golesV);
            viewHolder.vs=(TextView)convertView.findViewById(R.id.TV_vs);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.UnPart_eqL, viewHolder.equiL);
            convertView.setTag(R.id.UnPart_eqV, viewHolder.equiV);
            convertView.setTag(R.id.TV_golesL, viewHolder.golL);
            convertView.setTag(R.id.TV_golesV,viewHolder.golV);
            convertView.setTag(R.id.TV_fecha,viewHolder.fecha);
            convertView.setTag(R.id.TV_vs,viewHolder.vs);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.equiL.setText(String.valueOf(list.get(position).getEquipoLocal()));
        viewHolder.equiV.setText(list.get(position).getEquipoVisitante());
        viewHolder.fecha.setText(list.get(position).getFecha());
        if (list.get(position).getJugado()){
            viewHolder.golL.setText(String.valueOf(list.get(position).getGolesLocal()));
            viewHolder.golV.setText(String.valueOf(list.get(position).getGolesVisitante()));
            viewHolder.vs.setText("-");
        }else{
            viewHolder.vs.setText("Partido Pendiente");
        }

        return convertView;
    }

    public void actualizar(Principal principal){
        list.clear();
        list.addAll(principal.getPartidosJugados());
        this.notifyDataSetChanged();
    }

    public Partido getItem(int pos){
        return list.get(pos);
    }

    public boolean contieneDatos(){
        return !list.isEmpty();
    }
}

