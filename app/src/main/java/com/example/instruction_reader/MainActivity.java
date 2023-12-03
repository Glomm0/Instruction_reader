package com.example.instruction_reader;

import static java.lang.Thread.sleep;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.splashscreen.SplashScreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private void setDataDir() throws IOException {
        File file = this.getBaseContext().getExternalFilesDir("data");
        if (!file.exists())
            file.mkdir();
        AssetManager am = this.getAssets();
        ArrayList<String> fileNamesDocx = new ArrayList<>(Arrays.asList(am.list("docs")));
        for(String i:fileNamesDocx) {
            File temp = new File(i);

        }
    }
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
            setDataDir();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lw = findViewById(R.id.fileNames);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.Edit);
        AssetManager am = this.getAssets();


        ArrayList<String> fileNames = null;
        ArrayList<String> fileNamesDocx = null;
        try {
            fileNames = new ArrayList<>(Arrays.asList(am.list("texts")));
            fileNamesDocx = new ArrayList<>(Arrays.asList(am.list("docs")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        String[] s = new String[fileNames.size()];
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        lw.setAdapter(adapter);

        ArrayList<String> finalFileNames = fileNames;
        ArrayList<String> finalFileNamesDocx = fileNamesDocx;
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                File file = new File("/android_asset/docs/"+finalFileNamesDocx.get(i));

                Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                ContentResolver cR = getApplicationContext().getContentResolver();
                String type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, type);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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