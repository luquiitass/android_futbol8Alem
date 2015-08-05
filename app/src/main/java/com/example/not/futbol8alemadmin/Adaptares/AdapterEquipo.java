package com.example.not.futbol8alemadmin.Adaptares;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.not.futbol8alemadmin.Logica.Equipo;
import com.example.not.futbol8alemadmin.Logica.Principal;
import com.example.not.futbol8alemadmin.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterEquipo extends ArrayAdapter<Equipo> {

    private final List<Equipo> list;
    private final Activity context;

    public AdapterEquipo(Activity context, List list) {
        super(context, R.layout.layout_equipo_lista, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView nombre;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {

            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.layout_equipo_lista, null);
            viewHolder = new ViewHolder();
            viewHolder.nombre = (TextView) convertView.findViewById(R.id.TV_nombreEquipo);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.nombreDato, viewHolder.nombre);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nombre.setText(list.get(position).getNombreEquipo());
        return convertView;
    }

    public void actualizar(Principal principal){
        list.clear();
        list.addAll(principal.getEquipos());
        notifyDataSetChanged();
    }

}