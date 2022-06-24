package com.example.gallery_app.Alerta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.gallery_app.MainActivity;
import com.example.gallery_app.foto_seleccionada;

import java.io.File;

public class Alerta extends DialogFragment {

    Uri ruta;

    public Alerta(Uri ruta) {
        this.ruta = ruta;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Eliminar")
                .setMessage("¿Seguro que desea eliminar la imagen?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        boolean eliminado = new File(String.valueOf(ruta)).delete();
                        if (eliminado) {
                            Toast.makeText(getContext().getApplicationContext(), "Imagen eliminada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getContext().getApplicationContext(), "Ocurrio un error al eliminar la foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
