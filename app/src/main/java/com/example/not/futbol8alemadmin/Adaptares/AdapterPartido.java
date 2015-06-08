package com.example.not.futbol8alemadmin.Adaptares;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.not.futbol8alemadmin.Logica.Partido;
import com.example.not.futbol8alemadmin.R;

import java.util.List;

public class AdapterPartido extends ArrayAdapter<Partido> {

    private final List<Partido> list;
    private final Activity context;

    public AdapterPartido(Activity context, List<Partido> list) {
        super(context, R.layout.un_partido_lista, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView equiL;
        protected TextView equiV;
        protected TextView hora;
        protected TextView lugar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.un_partido_lista, null);

            viewHolder = new ViewHolder();
            viewHolder.equiL = (TextView) convertView.findViewById(R.id.UnPart_eqL);
            viewHolder.equiV = (TextView) convertView.findViewById(R.id.UnPart_eqV);
            viewHolder.hora = (TextView) convertView.findViewById(R.id.UnPart_hora);
            viewHolder.lugar=(TextView)convertView.findViewById(R.id.UnPart_lucar);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.UnPart_eqL, viewHolder.equiL);
            convertView.setTag(R.id.UnPart_eqV, viewHolder.equiV);
            convertView.setTag(R.id.UnPart_hora, viewHolder.hora);
            convertView.setTag(R.id.UnPart_lucar,viewHolder.lugar);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.equiL.setText(String.valueOf(list.get(position).getEquipoLocal()));
        viewHolder.equiV.setText(list.get(position).getEquipoVisitante());
        viewHolder.hora.setText(list.get(position).getHora());
        viewHolder.lugar.setText(String.valueOf(list.get(position).getCanchaeDe()));
        return convertView;
    }
}

