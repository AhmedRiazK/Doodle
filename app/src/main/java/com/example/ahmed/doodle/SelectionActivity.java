package com.example.ahmed.doodle;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.example.doodle.R;

public class SelectionActivity extends AppCompatActivity {
    Display display;
    DisplayMetrics metrics;
    BluetoothAdapter btadapter;
    boolean change = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        display = getWindowManager().getDefaultDisplay();
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        btadapter = BluetoothAdapter.getDefaultAdapter();
        Button textDocument, doodle;
        textDocument = (Button) findViewById(R.id.txtdc);
        //textDocument.setWidth(display.getWidth() / 2);
        textDocument.setWidth(metrics.widthPixels / 2);
        doodle = (Button) findViewById(R.id.scriible);
        //doodle.setWidth(display.getWidth() / 2);
        doodle.setWidth(metrics.widthPixels / 2);
        if (Build.VERSION.SDK_INT > 24) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("permission", "granted");
            } else {
                ActivityCompat.requestPermissions(SelectionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Log.d("permission", "requested");
            }
        }
        textDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change = true;
                Intent i = new Intent(SelectionActivity.this, TextDocument.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        doodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change = false;
                Intent i = new Intent(SelectionActivity.this, Doodle.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public void finish() {
        if (change) {
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        } else {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        btadapter.disable();
    }
}
