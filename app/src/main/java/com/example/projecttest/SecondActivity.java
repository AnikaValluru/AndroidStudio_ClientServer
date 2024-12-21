package com.example.projecttest;

////////////////////////////////////////////////////////////////////
//
// Client side code
//
// Operations:
// 1. esperanto: Replace "the" with "la" and "The" with "La"
// 2. reverse: Reverse the lines of the paragraph using a stack
// 3. word-count-nost: Count words not containing s or t
//
//
// Author: Anika Valluru
//
/////////////////////////////////////////////////////////////////////

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private EditText operationEditText, fileEditText;
    private Button processButton;
    private TextView resultTextView;

    private String hostName;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //connect xml file with .java
        operationEditText = findViewById(R.id.edit_operation);
        fileEditText = findViewById(R.id.edit_filename);
        processButton = findViewById(R.id.processButton);
        resultTextView = findViewById(R.id.resultTextView);

        //get sever information to connect client with server
        hostName = getIntent().getStringExtra("hostName");
        port = getIntent().getIntExtra("port", 0);

        processButton.setOnClickListener(v -> {
            //read user input
            String operation = operationEditText.getText().toString().trim();
            String fileName = fileEditText.getText().toString().trim();

            if (operation.isEmpty() || fileName.isEmpty()) {
                Toast.makeText(this, "Please enter operation and file name", Toast.LENGTH_SHORT).show();
                return;
            }

            processParagraph(operation, fileName);
        });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }
    //process assets file using server side code and return result
    private void processParagraph(String operation, String fileName) {
        new Thread(() -> {
            try (Socket socket = new Socket(hostName, port)) {
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                List<String> fileLines = readFileFromAssets(fileName);

                //error handling if file not found
                if (fileLines == null || fileLines.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(SecondActivity.this,
                            "ERROR: File not found", Toast.LENGTH_SHORT).show());
                    return;
                }

                //send operation and file lines to server
                out.println(operation);
                out.println(fileLines.size());
                for (String line : fileLines) {
                    out.println(line);
                }
                //process and display output
                int numLines = Integer.parseInt(in.readLine());
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < numLines; i++) {
                    result.append(in.readLine()).append("\n");
                }

                runOnUiThread(() -> resultTextView.setText(result.toString()));
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(SecondActivity.this,
                        "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    //retrieve .txt assets file
    private List<String> readFileFromAssets(String fileName) {
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = getAssets().open(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            //error handling
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "ERROR " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        return lines;
    }
}
