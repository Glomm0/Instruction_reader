package com.example.instruction_reader;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reader extends AppCompatActivity {
    TextView tw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        tw = findViewById(R.id.textView2);
        String fileName = (String) getIntent().getSerializableExtra("fileName");

        AssetManager am = this.getAssets();


        ArrayList<String> fileNames = null;
        try {
            fileNames = new ArrayList<>(Arrays.asList(am.list("texts")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String i : fileNames) {
            System.out.println(i);

        }
        List<String> mLines;
        try {
            mLines = new ArrayList<>();
            InputStream input = am.open("texts/" + fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;

            while ((line = reader.readLine()) != null)
                mLines.add(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tw.setText(String.join("",mLines));
        tw.setMovementMethod(new ScrollingMovementMethod());
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}