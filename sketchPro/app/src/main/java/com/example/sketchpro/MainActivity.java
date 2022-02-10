package com.example.sketchpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.google.android.material.slider.RangeSlider;

import java.io.OutputStream;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {

    private DrawView paint;


    // creating objects of type button
    private ImageButton  color, line, circle, rect, tran;

    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    private RangeSlider rangeSlider;
    private int id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paint = (DrawView) findViewById(R.id.draw_view);
        rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
        rangeSlider.setVisibility(View.VISIBLE);
//        undo = (ImageButton) findViewById(R.id.btn_undo);
        rect = (ImageButton) findViewById(R.id.btn_rectangle);
        tran = (ImageButton) findViewById(R.id.btn_triangle);
        color = (ImageButton) findViewById(R.id.btn_color);

        line = (ImageButton) findViewById(R.id.btn_line);
        circle = (ImageButton) findViewById(R.id.btn_circle);






//        undo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                paint.undo();
//            }
//        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        // get the integer value of color
                        // selected from the dialog box and
                        // set it as the stroke color
                        paint.setColor(color);
                    }
                    @Override
                    public void onCancel() {
                        colorPicker.dismissDialog();
                    }
                })
                        // set the number of color columns
                        // you want  to show in dialog.
                        .setColumns(5)
                        // set a default color selected
                        // in the dialog
                        .setDefaultColorButton(Color.parseColor("#000000"))
                        .show();
            }
        });



        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = 1;
                paint.id = id;
            }
        });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = 2;
                paint.id = id;

            }
        });

        rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = 3;
                paint.id = id;
            }
        });

        tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = 4;
                paint.id = id;
            }
        });

        // set the range of the RangeSlider
        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                paint.setStrokeWidth((int) value);
                paint.setStrokeSize((int) value);
            }
        });
        
        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                Log.d(TAG, "onGlobalLayout: " + id);
                paint.init(height, width, id);
            }
        });



    }

    private static final String TAG = "MainActivity";



}