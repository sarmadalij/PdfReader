package com.sarmadali.pdfreader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    RecyclerView recyclerView;
    TextView txtPermission;
    Button btnGrantPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userStoragePermission();

    }


    //check permission
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    //request permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    //on request permission handle
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showPdfFiles();
            invisibleTxtViewAndButton();
        } else {
            txtVisibleOnPermissionNotGranted();
        }
    }

    //method to get all pdf files
    private ArrayList<String> fetchPdfFiles(){
        ContentResolver contentResolver = getContentResolver();

        String mime = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = "application/pdf";  // Correct MIME type for PDF files
        String [] args = new String[]{mimeType};
        String [] proj = {MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME};

        String sortingOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";
        Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"),
                proj, mime, args, sortingOrder);

        ArrayList<String> pdfFiles = new ArrayList<>();
        if (cursor != null){
            while(cursor.moveToNext()){
                int index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);

                String path = cursor.getString(index);
                pdfFiles.add(path);
            }
            cursor.close();
        }
         return pdfFiles;
    }

    private void showPdfFiles(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setting adapter and calling fetch pdf function
        recyclerView.setAdapter(new AdapterFetchPdf(this, fetchPdfFiles()));
    }

    //visible the text if permission is not granted
    private void txtVisibleOnPermissionNotGranted(){
        txtPermission = findViewById(R.id.txtPermissionRequired);
        txtPermission.setVisibility(View.VISIBLE);

        btnGrantPermission = findViewById(R.id.grantPermissionBtn);
        btnGrantPermission.setVisibility(View.VISIBLE);

        btnGrantPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userStoragePermission();
            }
        });
    }

    private void invisibleTxtViewAndButton(){
        txtPermission = findViewById(R.id.txtPermissionRequired);
        txtPermission.setVisibility(View.INVISIBLE);

        btnGrantPermission = findViewById(R.id.grantPermissionBtn);
        btnGrantPermission.setVisibility(View.INVISIBLE);
    }

    //handle permission
    private void userStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                showPdfFiles();
                invisibleTxtViewAndButton();
            } else {
                requestPermission();
            }
        } else {
            showPdfFiles();
            invisibleTxtViewAndButton();
        }
    }

}