package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UlistViewAdapter extends ArrayAdapter<ULinfo> {

    public UlistViewAdapter(Context context, ULinfo[] uLinfoArrayList) {
        super(context, R.layout.listitem,R.id.blabla, uLinfoArrayList);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ULinfo uLinfo = getItem(position);

        if (convertView==null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem,parent, false);

        }

        TextView txtname = convertView.findViewById(R.id.txtname);
        TextView txtphone = convertView.findViewById(R.id.txtphone);
        TextView txtdistance = convertView.findViewById(R.id.txtdistance);
        TextView txtduration = convertView.findViewById(R.id.txtduration);
        ImageView profileImg = convertView.findViewById(R.id.profileImg);

        //profileImg.setImageBitmap(uLinfo.bitmapimg);

        Glide.with(getContext()).load(uLinfo.imageurl).into(profileImg);
        txtname.setText(uLinfo.name);
        txtphone.setText(uLinfo.phone);
        txtdistance.setText(uLinfo.distance);
        txtduration.setText(uLinfo.duration);

        profileImg.setScaleX(1);
        profileImg.setScaleY(1);

        Log.d(TAG, "info101 2 "+ uLinfo.imageurl);

        return super.getView(position, convertView, parent);
    }
}
