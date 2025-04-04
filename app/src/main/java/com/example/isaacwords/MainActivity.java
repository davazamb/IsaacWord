package com.example.isaacwords;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView selectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedTextView = findViewById(R.id.selectedTextView);

        // Botón A
        Button buttonA = findViewById(R.id.buttonA);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToTextView("A");
            }
        });

        // Botón B
        Button buttonB = findViewById(R.id.buttonB);
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToTextView("B");
            }
        });

        // Botón Espacio
        Button buttonSpace = findViewById(R.id.buttonSpace);
        buttonSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendToTextView(" ");
            }
        });

        // Botón Borrar
        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastCharacter();
            }
        });
    }

    private void appendToTextView(String text) {
        selectedTextView.setText(selectedTextView.getText().toString() + text);
    }

    private void deleteLastCharacter() {
        String currentText = selectedTextView.getText().toString();
        if (!currentText.isEmpty()) {
            selectedTextView.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
}