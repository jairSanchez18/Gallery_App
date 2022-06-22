package com.example.gallery_app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterImages extends ArrayAdapter<String> {
    private List<String> imagesPath;

    public AdapterImages(Context context, List<String> ruta) {
        super(context, R.layout.galeria, ruta);

        imagesPath = ruta;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.galeria, null);

        ImageView image = (ImageView) item.findViewById(R.id.img);

        image.setImageURI(Uri.parse(imagesPath.get(position)));

        return item;
    }
}
