package com.example.gallery_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMARA = 100;
    FloatingActionButton btnabrircamara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InicializarControles();
        TomarFoto();
    }

    private void InicializarControles() {
        btnabrircamara = (FloatingActionButton) findViewById(R.id.btnAbrircamara);
    }

    private void TomarFoto(){
        btnabrircamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermisosCamara();
            }
        });
    }

    private void PermisosCamara() {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        Snackbar.make(findViewById(R.id.btnAbrircamara), "Ir a admin. de permisos/Permisos de camara", BaseTransientBottomBar.LENGTH_SHORT)
                                .setAnchorView(R.id.btnAbrircamara).setAction("Ir", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivityForResult(new Intent(Settings.ACTION_PRIVACY_SETTINGS), 0);
                                    }
                                }).show();
                    } else {
                        // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA);
                        // MY_PERMISSIONS_REQUEST_CAMARA es una constante definida en la app. El método callback obtiene el resultado de la petición.
                    }
                } else { //have permissions
                    abrirCamara();
                }
            } else { // Pre-Marshmallow
                abrirCamara();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMARA: {
                // Si la petición es cancelada, el array resultante estará vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El permiso ha sido concedido.
                    abrirCamara();
                } else {
                    // Permiso denegado, deshabilita la funcionalidad que depende de este permiso.
                }
                return;
            }
            // otros bloques de 'case' para controlar otros permisos de la aplicación
        }
    }

    public void abrirCamara (){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,0);
    }
}