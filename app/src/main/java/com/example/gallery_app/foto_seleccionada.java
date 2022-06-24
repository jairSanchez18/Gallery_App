package com.example.gallery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.util.Locale;

public class foto_seleccionada extends AppCompatActivity {

    ImageView fotoseleccionada;
    Uri ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_seleccionada);

        InicializarControles();
        VerFoto();
    }

    private void InicializarControles() {
        fotoseleccionada = (ImageView) findViewById(R.id.imgFotoSeleccionada);
    }

    private void VerFoto() {
        Intent intent = getIntent();
        ruta = Uri.parse(intent.getStringExtra("foto"));
        fotoseleccionada.setImageURI(ruta);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                boolean eliminado = new File(String.valueOf(ruta)).delete();
                if (eliminado) {
                    Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(this, "Ocurrio un error al eliminar la foto", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }
}