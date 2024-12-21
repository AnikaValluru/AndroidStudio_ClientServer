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
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText hostInput, portInput;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect xml file with .java
        hostInput = findViewById(R.id.hostInput);
        portInput = findViewById(R.id.portInput);
        connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(v -> {
            String hostName = hostInput.getText().toString();
            int port = Integer.parseInt(portInput.getText().toString());

            //prompt for host name and port number
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("hostName", hostName);
            intent.putExtra("port", port);
            startActivity(intent);
        });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

}
