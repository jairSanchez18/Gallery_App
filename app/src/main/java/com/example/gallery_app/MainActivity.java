package com.example.gallery_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    FloatingActionButton btnabrircamara;
    ImageView imgmostrarimagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InicializarControles();
        TomarFoto();
    }

    private void InicializarControles() {
        btnabrircamara = (FloatingActionButton) findViewById(R.id.btnAbrircamara);
        imgmostrarimagen = (ImageView) findViewById(R.id.imgMostrarimagen);
        MostrarImagen();
    }

    private void TomarFoto() {
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        Snackbar.make(findViewById(R.id.btnAbrircamara), "Ir a admin. de permisos/Permisos de camara", BaseTransientBottomBar.LENGTH_SHORT)
                                .setAnchorView(R.id.btnAbrircamara).setAction("Ir", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivityForResult(new Intent(Settings.ACTION_PRIVACY_SETTINGS), 0);
                                    }
                                }).show();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    abrirCamara();
                }
            } else {
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
            case REQUEST_IMAGE_CAPTURE: {
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

    private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            SharedPreferences mSharedPreferences = getSharedPreferences("Imagen_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("keyStoredImage", "");
            editor.commit();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            SavePicture(imageBitmap);
            MostrarImagen();
        }
    }

    private void SavePicture(Bitmap imageBitmap) {
        SharedPreferences mSharedPreferences = getSharedPreferences("Imagen_preferences", Context.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] compressImage = baos.toByteArray();
        String sEncodedImage = Base64.encodeToString(compressImage, Base64.DEFAULT);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("keyStoredImage", sEncodedImage);
        editor.commit();
    }

    private void MostrarImagen() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Imagen_preferences", Context.MODE_PRIVATE);

        if (mSharedPreferences.contains("keyStoredImage")) {

            String encodedImage = mSharedPreferences.getString("keyStoredImage", null);

            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);

            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);

            imgmostrarimagen.setImageBitmap(bitmapImage);
        }
    }
}