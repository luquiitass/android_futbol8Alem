package com.example.not.futbol8alemadmin.Actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.widget.TextView;


public class DiversosDialog {
    private ProgressDialog pDialog;

    public DiversosDialog() {

    }

    public void onProgresSDialog(Context context,String mensaje){
        this.pDialog=new ProgressDialog(context);
        pDialog.setMessage(mensaje);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void ofProgressDialog(){
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void mensajeDialoge(Context context,String mensaje,String titulo,int ico){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(titulo);
        alertDialog.setIcon(ico);
        TextView editText=new TextView(context);
        editText.setTextSize(25);
        editText.setText(mensaje);
        alertDialog.setView(editText);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void elimenarSecionGuardada(Context context){
        SharedPreferences preferences=context.getSharedPreferences("sesionUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean("existeSesion",false);
        editor.commit();
    }

}
