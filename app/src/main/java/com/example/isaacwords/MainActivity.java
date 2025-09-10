package com.example.isaacwords;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView outputTextView;
    private File file;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        outputTextView = findViewById(R.id.outputTextView);
        outputTextView.setMovementMethod(new ScrollingMovementMethod());
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Inicializar archivo .log diario
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nombreArchivo = "IsaacWords_" + fecha + ".log";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), nombreArchivo);

        // Inicializar TextToSpeech
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("es", "ES"));
                tts.setSpeechRate(0.8f); // Más lento
                tts.setPitch(1.2f);      // Tono más cálido
            }
        });

        // Letras del teclado
        String alphabet = "1234567890QWERTYUIOPASDFGHJKLÑZXCVBNM";
        String vowels = "AEIOU";

        for (char letter : alphabet.toCharArray()) {
            Button button = new Button(this);
            button.setText(String.valueOf(letter));
            button.setTextSize(12);
            button.setTextColor(Color.BLACK);

            if (Character.isDigit(letter)) {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light)); // Números: naranja
            } else {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Letras: azul
            }


            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            button.setLayoutParams(params);

            button.setOnClickListener(v -> {
                String selectedText = ((Button) v).getText().toString();
                outputTextView.append(selectedText);
                saveToFile(selectedText);
            });

            gridLayout.addView(button);
        }

        // Botón Espacio
        Button spaceButton = new Button(this);
        spaceButton.setText("Espacio");
        spaceButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        spaceButton.setLayoutParams(genericParams());
        spaceButton.setOnClickListener(v -> {
            outputTextView.append(" ");
            saveToFile(" ");
        });
        gridLayout.addView(spaceButton);

        // Botón Borrar
        Button deleteButton = new Button(this);
        deleteButton.setText("Borrar");
        deleteButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        deleteButton.setLayoutParams(genericParams());
        deleteButton.setOnClickListener(v -> {
            String currentText = outputTextView.getText().toString();
            if (!currentText.isEmpty()) {
                outputTextView.setText(currentText.substring(0, currentText.length() - 1));
                saveCurrentTextToFile();
            }
        });
        deleteButton.setOnLongClickListener(v -> {
            outputTextView.setText("");
            saveCurrentTextToFile();
            return true;
        });
        gridLayout.addView(deleteButton);

        // Botón Leer
        Button speakButton = new Button(this);
        speakButton.setText("Leer");
        speakButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        speakButton.setLayoutParams(genericParams());

        speakButton.setOnClickListener(v -> {
            String texto = outputTextView.getText().toString();
            if (!texto.isEmpty()) {
                speakButton.setBackgroundColor(Color.YELLOW);
                new Handler().postDelayed(() -> {
                    speakButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                }, 500);

                // Lectura palabra por palabra
                String[] palabras = texto.split(" ");
                for (String palabra : palabras) {
                    tts.speak(palabra, TextToSpeech.QUEUE_ADD, null, null);
                }
            }
        });

        speakButton.setOnLongClickListener(v -> {
            String texto = outputTextView.getText().toString();
            if (!texto.isEmpty()) {
                tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
            }
            return true;
        });

        gridLayout.addView(speakButton);
    }

    private GridLayout.LayoutParams genericParams() {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 0;
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        return params;
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
