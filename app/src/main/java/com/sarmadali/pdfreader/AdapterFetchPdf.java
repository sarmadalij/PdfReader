package com.sarmadali.pdfreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class AdapterFetchPdf extends RecyclerView.Adapter<AdapterFetchPdf.PdfAdapterViewHolder> {

    Context context;
    List<String> pdfFiles;

    public AdapterFetchPdf(Context context, List<String> pdfFiles){
        this.context=context;
        this.pdfFiles=pdfFiles;
    }

    @NonNull
    @Override
    public PdfAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inlfate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pdflayout_sample, parent, false);

        return new PdfAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfAdapterViewHolder holder, int position) {

        String path = pdfFiles.get(position);
        File pdfFile = new File(path);
        String filename = pdfFile.getName();

        holder.pdfName.setText(filename);

        //pass the file path to ReadPdfFiles class and display the pdf
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Opening "+ filename, Toast.LENGTH_SHORT).show();

                if (pdfFile.exists()) {
                    // Create an Intent to start PdfViewerActivity
                    Intent intent = new Intent(context, ReadPdfFiles.class);
                    intent.putExtra("PDF_FILE_PATH", path);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //share the pdf file
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                // send intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf"); //mime type

                // If the file exists, get its URI using FileProvider
                if (pdfFile.exists()) {
                    Uri pdfUri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            pdfFile);

                    shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Start the activity to share the PDF file
                    context.startActivity(Intent.createChooser(shareIntent, "Share PDF File"));

                } else {
                    // Show a message if the file does not exist
                    Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    static class PdfAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView pdfName;
        CardView cardView;
        ImageView share;
        public PdfAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.pdfFileName);
            cardView = itemView.findViewById(R.id.cardView);
            share = itemView.findViewById(R.id.shareFile);

        }
    }


 }
