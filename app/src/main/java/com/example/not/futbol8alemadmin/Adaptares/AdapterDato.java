package com.example.not.futbol8alemadmin.Adaptares;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.not.futbol8alemadmin.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterDato extends ArrayAdapter<Datos> {

    private final List<Datos> list;
    private final Activity context;
    private List<LinearLayout> linearLayouts=new ArrayList<LinearLayout>();

    public AdapterDato(Activity context, List list) {
        super(context, R.layout.layou_datos, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView nombre;
        protected TextView dato;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {

            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.layou_datos, null);
            viewHolder = new ViewHolder();
            viewHolder.nombre = (TextView) convertView.findViewById(R.id.nombreDato);
            viewHolder.dato = (TextView) convertView.findViewById(R.id.volorDato);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.nombreDato, viewHolder.nombre);
            convertView.setTag(R.id.volorDato, viewHolder.dato);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nombre.setText(list.get(position).getNombre());
        viewHolder.dato.setText(list.get(position).getDato());;
        notifyDataSetChanged();

        return convertView;
    }

}