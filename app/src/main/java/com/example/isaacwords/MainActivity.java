package com.example.isaacwords;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView outputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputTextView = findViewById(R.id.outputTextView);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Abecedario
        String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String vowels = "1234567890"; // Criterio: vocales

        // Crear botones dinámicamente para el abecedario
        for (char letter : alphabet.toCharArray()) {
            Button button = new Button(this);
            button.setText(String.valueOf(letter));
            // Asignar color según el criterio (vocales y consonantes)
            if (vowels.contains(String.valueOf(letter))) {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Rojo para vocales
            } else {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Azul para consonantes
            }

            // Establecer parámetros de diseño para hacerlo responsivo
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Ancho responsivo
            params.height = 0; // Altura responsiva
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Peso de la fila
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Peso de la columna
            button.setLayoutParams(params);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outputTextView.append(((Button) v).getText().toString());
                }
            });

            // Añadir el botón al GridLayout
            gridLayout.addView(button);
        }

        // Botón para espacio
        Button spaceButton = new Button(this);
        spaceButton.setText("Espacio");
        GridLayout.LayoutParams spaceParams = new GridLayout.LayoutParams();
        spaceParams.width = 0;
        spaceParams.height = 0;
        spaceParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        spaceParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        spaceButton.setLayoutParams(spaceParams);
        spaceButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Verde para espacio
        spaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputTextView.append(" ");
            }
        });
        gridLayout.addView(spaceButton);

        // Botón para borrar
        Button deleteButton = new Button(this);
        deleteButton.setText("Borrar");
        GridLayout.LayoutParams deleteParams = new GridLayout.LayoutParams();
        deleteParams.width = 0;
        deleteParams.height = 0;
        deleteParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        deleteParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        deleteButton.setLayoutParams(deleteParams);
        deleteButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light)); // Naranja para borrar
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = outputTextView.getText().toString();
                if (!currentText.isEmpty()) {
                    outputTextView.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });
        gridLayout.addView(deleteButton);
    }
}
