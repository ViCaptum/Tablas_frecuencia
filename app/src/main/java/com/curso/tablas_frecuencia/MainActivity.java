package com.curso.tablas_frecuencia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.tablas_frecuencia.adapter.FrecuenciaSimpleAdapter;
import com.curso.tablas_frecuencia.adapter.FrequencyAdapter;
import com.curso.tablas_frecuencia.controller.MainController;
import com.curso.tablas_frecuencia.model.EstadisticaUtils;
import com.curso.tablas_frecuencia.model.ScannerUtils;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etNumbersInput;
    private TextView tvN, tvMin, tvMax, tvRange, tvClasses, tvWidth, tvMedia, tvMediana, tvModa, tvSortedNumbers, tvVarianza, tvUSTD, tvCoefVar, tvAsimetria, tvCurtosis;
    private RecyclerView rvFrequencyTable, rvFrecuenciaSimple;

    private MainController controller;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainController();
        initViews();
        setupLaunchers();

        findViewById(R.id.btnCalculate).setOnClickListener(v -> procesarInformacion());
        findViewById(R.id.btnScanCamera).setOnClickListener(v -> abrirCamara());
    }

    private void initViews() {
        etNumbersInput = findViewById(R.id.etNumbersInput);
        tvN = findViewById(R.id.tvN); tvMin = findViewById(R.id.tvMin); tvMax = findViewById(R.id.tvMax);
        tvRange = findViewById(R.id.tvRange); tvClasses = findViewById(R.id.tvClasses); tvWidth = findViewById(R.id.tvWidth);
        tvMedia = findViewById(R.id.tvMedia); tvMediana = findViewById(R.id.tvMediana); tvModa = findViewById(R.id.tvModa);
        tvVarianza = findViewById(R.id.tvVarianza); tvUSTD = findViewById(R.id.tvUSTD); tvCoefVar = findViewById(R.id.tvCoefVar);
        tvAsimetria = findViewById(R.id.tvAsimetria); tvCurtosis = findViewById(R.id.tvCurtosis);
        tvSortedNumbers = findViewById(R.id.tvSortedNumbers);
        rvFrequencyTable = findViewById(R.id.rvFrequencyTable);
        rvFrecuenciaSimple = findViewById(R.id.rvFrecuenciaSimple);

        rvFrequencyTable.setLayoutManager(new LinearLayoutManager(this));
        rvFrecuenciaSimple.setLayoutManager(new LinearLayoutManager(this));
    }

    private void procesarInformacion() {
        String input = etNumbersInput.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Ingrese datos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            List<Double> datos = controller.obtenerDatosProcesados(input);

            // Cálculos base
            int n = datos.size();
            double min = EstadisticaUtils.getMin(datos);
            double max = EstadisticaUtils.getMax(datos);
            double rango = EstadisticaUtils.getRango(max, min);
            int k = EstadisticaUtils.getNumeroClases(n);
            double amplitud = EstadisticaUtils.getAmplitud(rango, k);
            double media = EstadisticaUtils.calcularMedia(datos);

            // Actualizar Vistas
            mostrarResultadosUI(datos, n, min, max, rango, k, amplitud, media);

            // Actualizar Tablas vía Controller
            rvFrequencyTable.setAdapter(new FrequencyAdapter(controller.obtenerListaAgrupada(datos, min, k, amplitud)));
            rvFrecuenciaSimple.setAdapter(new FrecuenciaSimpleAdapter(controller.obtenerListaSimple(datos)));

            finalizarProceso();
        } catch (Exception e) {
            Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarResultadosUI(List<Double> datos, int n, double min, double max, double r, int k, double a, double media) {
        tvN.setText("n: " + n);
        tvMin.setText("Min: " + formatDecimal(min));
        tvMax.setText("Max: " + formatDecimal(max));
        tvRange.setText("Rango: " + formatDecimal(r));
        tvClasses.setText("Clases: " + k);
        tvWidth.setText("Amplitud: " + formatDecimal(a));

        tvMedia.setText("Media: " + formatDecimal(media));
        tvMediana.setText("Mediana: " + formatDecimal(EstadisticaUtils.calcularMediana(datos)));
        tvModa.setText("Moda: " + formatearModa(EstadisticaUtils.calcularModa(datos)));

        double var = EstadisticaUtils.calcularVarianza(datos, media);
        double ustd = EstadisticaUtils.calcularDesviacionEstandar(var);
        tvVarianza.setText("Varianza: " + formatDecimal(var));
        tvUSTD.setText("USTD: " + formatDecimal(ustd));
        tvCoefVar.setText("CV: " + formatDecimal(EstadisticaUtils.calcularCoeficienteVariacion(ustd, media)) + "%");
        tvAsimetria.setText("Asimetría: " + formatDecimal(EstadisticaUtils.calcularAsimetria(datos, media, ustd)));
        tvCurtosis.setText("Curtosis: " + formatDecimal(EstadisticaUtils.calcularCurtosis(datos, media, ustd)));

        mostrarDatosOrdenados(datos);
    }

    private void finalizarProceso() {
        etNumbersInput.setText("");
        etNumbersInput.clearFocus();
        ocultarTeclado();
    }

    private String formatDecimal(double d) {
        if (d == (long) d) return String.valueOf((long) d);
        return String.format(Locale.getDefault(), "%.2f", d);
    }

    private String formatearModa(List<Double> modas) {
        if (modas.isEmpty()) return "No hay";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modas.size(); i++) {
            sb.append(formatDecimal(modas.get(i)));
            if (i < modas.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private void mostrarDatosOrdenados(List<Double> lista) {
        int n = lista.size();
        int columnas = (int) Math.ceil(Math.sqrt(n));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(String.format(Locale.getDefault(), "%-6s", formatDecimal(lista.get(i))));
            if ((i + 1) % columnas == 0) sb.append("\n");
            else sb.append("   ");
        }
        tvSortedNumbers.setText(sb.toString());
    }

    private void setupLaunchers() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) ejecutarIntentCamara();
            else Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                if (bitmap != null) procesarImagenConIA(bitmap);
            }
        });
    }

    private void abrirCamara() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) ejecutarIntentCamara();
        else requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
    }

    private void ejecutarIntentCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePictureIntent);
    }

    private void procesarImagenConIA(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image).addOnSuccessListener(visionText -> {
            String numeros = ScannerUtils.extraerNumeros(visionText);

            if (!numeros.isEmpty()) {
                String actual = etNumbersInput.getText().toString().trim();
                String combinado = actual.isEmpty() ? numeros : actual + " " + numeros;

                etNumbersInput.setText(combinado);
                etNumbersInput.setSelection(combinado.length());

                Toast.makeText(this, "Detectado: " + numeros, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "No se detectaron números claros", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error de ML Kit: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}