package com.sarmadali.pdfreader;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class ReadPdfFiles extends AppCompatActivity {

    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pdf_files);

        displayPdf();

    }

    private void displayPdf(){
        pdfView = findViewById(R.id.pdfViewer);

        // Get the file path from the intent
        String path = getIntent().getStringExtra("PDF_FILE_PATH");

        if (path != null) {
            File pdfFile = new File(path);
            if (pdfFile.exists()) {
                pdfView.fromFile(pdfFile)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .enableDoubletap(true)
                        .swipeHorizontal(false)
                        .spacing(2) //spacing between pages
                        .onLoad(nbPages -> {
                            // Code to execute when PDF is loaded
                        })
                        .onPageChange((page, pageCount) -> {
                            // Code to execute when the page is changed
                        })
                        .scrollHandle(new DefaultScrollHandle(this))
                        .enableAnnotationRendering(true)
                        .password(null)
                        .load();
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid file path", Toast.LENGTH_SHORT).show();
        }
    }
}