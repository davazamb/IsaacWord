package com.example.isaacwords;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.speech.tts.TextToSpeech;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    private TextView outputTextView;
    private File file;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Horizontal
        outputTextView = findViewById(R.id.outputTextView);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        outputTextView.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        // Crear el archivo donde se guardará el texto
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nombreArchivo = "LogIsaacWords_" + fecha + ".txt";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), nombreArchivo);
        // Abecedario
        String alphabet = "1234567890QWERTYUIOPASDFGHJKLÑZXCVBNM";
        String vowels = "1234567890"; // Criterio: vocales

        // Crear botones dinámicamente para el abecedarioc
        for (char letter : alphabet.toCharArray()) {
            Button button = new Button(this);
            button.setText(String.valueOf(letter));
            // Asignar color según el criterio (vocales y consonantes)
            if (vowels.contains(String.valueOf(letter))) {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Rojo para vocales
                button.setTextColor(Color.WHITE); // Texto en negro
                button.setTextSize(12);
            } else {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Azul para consonantes
                button.setTextColor(Color.BLACK); // Texto en negro
                button.setTextSize(12);
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
                    // Añadir la letra seleccionada al TextView
                    String selectedText = ((Button) v).getText().toString();
                    outputTextView.append(selectedText);

                    // Guardar automáticamente en el archivo
                    saveToFile(selectedText);
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
                // Guardar automáticamente el espacio en el archivo
                saveToFile(" ");
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
                    // Borrar la última letra o espacio del TextView
                    outputTextView.setText(currentText.substring(0, currentText.length() - 1));
                    // Guardar el texto actual en el archivo
                    saveCurrentTextToFile();
                }
            }
        });
        // Pulsación larga: Borrar todo
        deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Eliminar todo el contenido del TextView
                outputTextView.setText("");
                saveCurrentTextToFile();
                return true; // Indica que se manejó la acción de pulsación larga
            }
        });

        gridLayout.addView(deleteButton);
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("es", "ES")); // Español
                tts.setSpeechRate(0.8f); // Más lento
                tts.setPitch(1.0f);      // Tono neutro
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Puedes mostrar un mensaje si el idioma no está disponible
                }
            }
        });


        Button speakButton = new Button(this);
        speakButton.setText("Leer");
        GridLayout.LayoutParams speakParams = new GridLayout.LayoutParams();
        speakParams.width = 0;
        speakParams.height = 0;
        speakParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        speakParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        speakButton.setLayoutParams(speakParams);
        speakButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple)); // Color distintivo

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = outputTextView.getText().toString();
                if (!texto.isEmpty()) {
                    tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        gridLayout.addView(speakButton);

        // Botón para SALTO LINEA
//        Button lineButton = new Button(this);
//        lineButton.setText("SALTO");
//        GridLayout.LayoutParams lineParams = new GridLayout.LayoutParams();
//        lineParams.width = 0;
//        lineParams.height = 0;
//        lineParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
//        lineParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
//        lineButton.setLayoutParams(lineParams);
//        lineButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Verde para espacio
//        lineButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                outputTextView.append("salto");
//                // Guardar automáticamente el espacio en el archivo
//                saveToFile(" ");
//            }
//        });
//        gridLayout.addView(lineButton);


    }

    private void saveToFile(String content) {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(content.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCurrentTextToFile() {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(outputTextView.getText().toString().getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}
