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

import com.example.gallery_app.Alerta.Alerta;

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
                Alerta alerta = new Alerta(ruta);
                alerta.show(getSupportFragmentManager(), "alertTag");
                break;
        }
        return true;
    }
}