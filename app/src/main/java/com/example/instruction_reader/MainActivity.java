package com.example.instruction_reader;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private long findStringInFile(String fileName,String string,AssetManager am){
        try {
            InputStream input = am.open("texts/" + fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            long row=0;
            while ((line = reader.readLine()) != null)
            {
                row++;
                if (line.toLowerCase().contains(string.toLowerCase())){
                    return row;
                }
            }
            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private ArrayList<String> findFilesWithText(String s){
        AssetManager am = this.getAssets();
        ArrayList<String> fileNames;
        ArrayList<String> result = new ArrayList<>();
        try {
            fileNames = new ArrayList<>(Arrays.asList(am.list("texts")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String name:fileNames){
            if (findStringInFile(name,s,am)!=-1){
                result.add(name);
            }
        }
        return result;

    }
    ListView lw;
    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lw = findViewById(R.id.fileNames);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.Edit);
        AssetManager am = this.getAssets();


        ArrayList<String> fileNames = null;
        try {
            fileNames = new ArrayList<>(Arrays.asList(am.list("texts")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        String[] s = new String[fileNames.size()];
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        lw.setAdapter(adapter);

        ArrayList<String> finalFileNames = fileNames;
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Reader.class);
                intent.putExtra("fileName", finalFileNames.get(i));
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text =editText.getText().toString();
                ArrayList<String> names = findFilesWithText(text);
                adapter.clear();
                adapter.addAll(names);
                adapter.notifyDataSetChanged();

            }
        });
    }
}