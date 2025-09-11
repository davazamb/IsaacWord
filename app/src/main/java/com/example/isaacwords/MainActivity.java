package com.example.isaacwords;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText outputEditText;
    private File file;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        outputEditText = findViewById(R.id.outputEditText);
        int nightModeFlags = getResources().getConfiguration().uiMode &
                android.content.res.Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            outputEditText.setTextColor(Color.WHITE);
        } else {
            outputEditText.setTextColor(Color.BLACK);
        }


        outputEditText.setMovementMethod(new ScrollingMovementMethod());

        // Bloquear teclado del sistema
        try {
            Method method = EditText.class.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.invoke(outputEditText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Button lineBreakButton = findViewById(R.id.lineBreakButton);

        // Archivo .log diario
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String nombreArchivo = "IsaacWords_" + fecha + ".log";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), nombreArchivo);

        // TextToSpeech
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("es", "ES"));
                tts.setSpeechRate(0.8f);
                tts.setPitch(1.2f);
            }
        });

        // Letras y números
        String alphabet = "1234567890QWERTYUIOPASDFGHJKLÑZXCVBNM";

        for (char letter : alphabet.toCharArray()) {
            Button button = new Button(this);
            button.setText(String.valueOf(letter));
            button.setTextSize(14);
            button.setTextColor(Color.BLACK);

            if (Character.isDigit(letter)) {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            } else {
                button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            button.setLayoutParams(params);

            button.setOnClickListener(v -> {
                int cursorPos = outputEditText.getSelectionStart();
                outputEditText.getText().insert(cursorPos, button.getText().toString());
                saveToFile(button.getText().toString());
            });

            gridLayout.addView(button);
        }

        // Botón Espacio
        Button spaceButton = new Button(this);
        spaceButton.setText("Espacio");
        spaceButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        spaceButton.setLayoutParams(genericParams());

        spaceButton.setOnClickListener(v -> {
            int cursorPos = outputEditText.getSelectionStart();
            outputEditText.getText().insert(cursorPos, " ");
            saveToFile(" ");
        });

//        spaceButton.setOnLongClickListener(v -> {
//            int cursorPos = outputEditText.getSelectionStart();
//            outputEditText.getText().insert(cursorPos, "\n");
//            saveToFile("[SALTO DE LÍNEA]");
//            return true;
//        });

        gridLayout.addView(spaceButton);

        // Botón Borrar
        Button deleteButton = new Button(this);
        deleteButton.setText("Borrar");
        deleteButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        deleteButton.setLayoutParams(genericParams());

        deleteButton.setOnClickListener(v -> {
            int cursorPos = outputEditText.getSelectionStart();
            if (cursorPos > 0) {
                outputEditText.getText().delete(cursorPos - 1, cursorPos);
                saveToFile("[BORRAR]");
            }
        });

        deleteButton.setOnLongClickListener(v -> {
            outputEditText.post(() -> {
                try {
                    outputEditText.setText("");
                    outputEditText.setSelection(0); // Cursor al inicio
                    saveToFile("[BORRAR TODO]");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return true;
        });


        gridLayout.addView(deleteButton);

        // Botón Leer
        Button speakButton = new Button(this);
        speakButton.setText("Leer");
        speakButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        speakButton.setLayoutParams(genericParams());

        speakButton.setOnClickListener(v -> {
            String texto = outputEditText.getText().toString();
            if (!texto.isEmpty()) {
                speakButton.setBackgroundColor(Color.YELLOW);
                new Handler().postDelayed(() -> {
                    speakButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                }, 500);

                saveToFile("[LECTURA]");
                String[] palabras = texto.split(" ");
                for (String palabra : palabras) {
                    tts.speak(palabra, TextToSpeech.QUEUE_ADD, null, null);
                }
            }
        });

        speakButton.setOnLongClickListener(v -> {
            String texto = outputEditText.getText().toString();
            if (!texto.isEmpty()) {
                saveToFile("[LECTURA REPETIDA]");
                tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
            }
            return true;
        });

        gridLayout.addView(speakButton);

        // Botón Salto de línea al lado del EditText
        lineBreakButton.setOnClickListener(v -> {
            int cursorPos = outputEditText.getSelectionStart();
            outputEditText.getText().insert(cursorPos, "\n");
            saveToFile("[SALTO DE LÍNEA]");
        });
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
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        String logEntry = "[" + timestamp + "] " + content + "\n";

        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(logEntry.getBytes());
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