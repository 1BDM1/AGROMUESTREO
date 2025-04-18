package com.techco.Agromuestreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;


public class pdfviewer extends AppCompatActivity {

    String ruta;
    PDFView PDF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        PDF = findViewById(R.id.pdfView);
        try {
            Bundle miBundle = this.getIntent().getExtras();
            ruta = miBundle.getString("RUTA");
            String s= String.valueOf(ruta);
            File arch = new File(s);
            if (arch.exists()) {
                Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", arch);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    // Utils.showSnackBar(root.getResources().getString(R.string.error_pdf), root);
                }
            }
            //Toast.makeText(this, ruta, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Toast.makeText(this,"ERROR PDF",Toast.LENGTH_SHORT).show();
        }

        //Intent i = new Intent(pdfviewer.this, MainActivity.class);
        //startActivity(i);
        //pdfviewer.this.finish();
    }
}