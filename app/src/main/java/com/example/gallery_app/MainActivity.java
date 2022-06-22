package com.example.gallery_app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    FloatingActionButton btnabrircamara;
    GridView contenedor_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InicializarControles();
        TomarFoto();
    }

    private void InicializarControles() {
        btnabrircamara = (FloatingActionButton) findViewById(R.id.btnAbrircamara);
        contenedor_imagen = (GridView) findViewById(R.id.foto);
        LoadImages();
        VerImagen();
        //MostrarImagen();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            SavePicture(imageBitmap);
            LoadImages();
            //MostrarImagen();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SavePicture(Bitmap imageBitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] compressImage = baos.toByteArray();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy-HH:mm:ss");

            String nombre_imagen = "IMG-" + dtf.format(LocalDateTime.now()) + ".png";

            FileOutputStream out = getApplicationContext().openFileOutput(nombre_imagen, Context.MODE_PRIVATE);

            out.write(compressImage);
            out.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error SavePicture... " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
/*
    private void MostrarImagen() {
        try {
            FileInputStream input = new FileInputStream(getApplicationContext().getFilesDir().getPath()+"/imagen.png");

            Bitmap bitmap = BitmapFactory.decodeStream(input);

            imgmostrarimagen.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Error MostrarImagen... " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
*/

    private List<String> RecibirImagenes() {
        List<String> images = new ArrayList<>();

        File f = new File(getApplicationContext().getFilesDir().getPath());
        File[] files = f.listFiles();
        if (f.exists()) {
            if (files.length != 0) {
                for (File file : files) {
                    //Toast.makeText(_context, ""+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    images.add(file.getAbsolutePath());
                }
            }
        }
        return images;
    }

    public void LoadImages() {
        List<String> imagesPath;
        imagesPath = this.RecibirImagenes();

        if(imagesPath.size() != 0) {
            AdapterImages adapter = new AdapterImages(this, imagesPath);
            contenedor_imagen.setAdapter(adapter);
        }
    }

    private void VerImagen(){
        contenedor_imagen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), foto_seleccionada.class);
                intent.putExtra("foto", RecibirImagenes().get(position));
                startActivity(intent);
            }
        });
    }
}