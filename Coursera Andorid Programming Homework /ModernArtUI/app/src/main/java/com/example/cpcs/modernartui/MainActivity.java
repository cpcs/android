package com.example.cpcs.modernartui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final int[] recIds = new int []{
            R.id.rec1, R.id.rec2,  R.id.rec3,  R.id.rec4,  R.id.rec5,
                    R.id.rec6,  R.id.rec7,  R.id.rec8,  R.id.rec9,
                    R.id.rec10,  R.id.rec11, R.id.rec12, R.id.rec13};



        final List<View> rectangles = new ArrayList();



        for (int i = 0; i < recIds.length; ++i) {
            View rec = findViewById(recIds[i]);
            String tag = (String) rec.getTag();
            if (tag == null) {
                continue;
            }
            int color = Color.parseColor(tag);
            if ((color != ContextCompat.getColor(this, R.color.white))
                    && (color != ContextCompat.getColor(this, R.color.black))
                    && (color != ContextCompat.getColor(this, R.color.gray))) {
                rectangles.add(rec);
            }
        }
        Log.i("cpcs", "" + rectangles.size());
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double ratio = progress * 1. / seekBar.getMax();
                for (View rec : rectangles) {
                    int orgColor = Color.parseColor((String) rec.getTag());
                    int desColor = (0x00ffffff - (orgColor | 0xff000000)) | (orgColor & 0xff000000);
                    int orgR = (orgColor >> 16) & 0xff;
                    int orgG = (orgColor >> 8) & 0xff;
                    int orgB = orgColor & 0xff;
                    int desR = (desColor >> 16) & 0xff;
                    int desG = (desColor >> 8) & 0xff;
                    int desB = desColor & 0xff;
                    rec.setBackgroundColor(Color.rgb(
                            orgR + (int) ((desR - orgR) * ratio),
                            orgG + (int) ((desG - orgG) * ratio),
                            orgB + (int) ((desB - orgB) * ratio)));
                    rec.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.more_information) {
            AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance();
            alertDialogFragment.setCancelable(false);
            alertDialogFragment.show(getFragmentManager(), "MomaAlert");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
