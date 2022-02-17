package br.com.maddevmobile.registrodechuvas.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.maddevmobile.registrodechuvas.R;
import br.com.maddevmobile.registrodechuvas.controller.Controller;
import br.com.maddevmobile.registrodechuvas.database.Database;
import br.com.maddevmobile.registrodechuvas.model.Chuva;
import br.com.maddevmobile.registrodechuvas.util.PdfCreator;
import br.com.maddevmobile.registrodechuvas.util.Util;

public class MainActivity extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TextView textView_titulo_toolbar;
    Controller controller;
    List<Chuva> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configuraToolbar();
        configuraSmartTablLayout();


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){

            String permissoes[] = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            Util.validate(this, 100, permissoes);
        }


        controller = new Controller(this);
        list = new ArrayList<Chuva>();
        list = controller.getChuvaController();

    }


    private void configuraToolbar(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView_titulo_toolbar = findViewById(R.id.text_titulo_toolbar);
        textView_titulo_toolbar.setText("Registro de Chuvas");


    }

    private void configuraSmartTablLayout(){

        smartTabLayout = findViewById(R.id.viewpagertab);
        viewPager = findViewById(R.id.viewpager);


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Registro", RegistroFragment.class)
                .add("Recentes", HistoricoFragment.class)
                .create());



        viewPager.setAdapter( adapter );
        smartTabLayout.setViewPager( viewPager );
    }

    //------------------------------------- inflar o menu ------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,  menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_gerar_pdf){

            try {
                gerarPdf(list);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void gerarPdf(List<Chuva> list) throws FileNotFoundException, DocumentException {

        ParcelFileDescriptor descriptor = null;
        OutputStream outputStream;
        Uri uri = null;
        File pdf = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "report"+System.currentTimeMillis());
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+"/report");

            ContentResolver resolver = getContentResolver();
            uri = resolver.insert(MediaStore.Downloads.getContentUri("external"), contentValues);

            descriptor = resolver.openFileDescriptor(uri, "rw");
            outputStream = new FileOutputStream(descriptor.getFileDescriptor());

        }else{

            File diretoryMain = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File diretoryFull = new File(diretoryMain.getPath()+"/report/");

            if(!diretoryFull.exists()){
                    diretoryFull.mkdir();
            }

            String nameFile = "report"+System.currentTimeMillis()+".pdf";
            pdf = new File(diretoryFull.getPath() +"/"+nameFile);

            outputStream = new FileOutputStream(pdf);

        }

        Document document = new Document();
        PdfCreator pdfCreator = new PdfCreator();

        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        pdfWriter.setPageEvent( pdfCreator);

        pdfWriter.setBoxSize("box", new Rectangle(0,0,0,0));
        document.open();

        Font font = new Font(Font.FontFamily.HELVETICA);
        font.setColor(BaseColor.BLACK);

        Font font1 = new Font(Font.FontFamily.HELVETICA);
        font1.setStyle(Font.UNDERLINE);
        font1.setSize(24f);
        font1.setColor(BaseColor.BLACK);

        Drawable drawable = getResources().getDrawable( R.drawable.logorelatorio);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        try {
            Image image = Image.getInstance(baos.toByteArray());
            image.setAlignment(Element.ALIGN_CENTER);
            document.add( image );
        } catch (IOException e) {
            e.printStackTrace();
        }

        Paragraph paragraph = new Paragraph("Relatório de Chuvas \n\n", font1);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add( paragraph );

        for(Chuva chuva : list){

            String cidade = "Cidade :  "+chuva.getNomeCidade();
            paragraph = new Paragraph(cidade, font);
            document.add( paragraph );

            String bairro = "Bairro :  "+chuva.getNomeBairro();
            paragraph = new Paragraph(bairro, font);
            document.add( paragraph );

            String estado = "Estado :  "+chuva.getNomeEstado();
            paragraph = new Paragraph(estado, font);
            document.add( paragraph );

            String chuvaMM = "Chuva em MM :  "+chuva.getChuvaMM();
            paragraph = new Paragraph(chuvaMM, font);
            document.add( paragraph );

            String data = "Data :  "+chuva.getData();
            paragraph = new Paragraph(data, font);
            document.add( paragraph );

            String hora = "Hora :  "+chuva.getHora();
            paragraph = new Paragraph(hora, font);
            document.add( paragraph );

            paragraph = new Paragraph("---------------------------------------------------------\n");
            document.add( paragraph );

        }


        document.close();


        viewPdf( pdf, uri );

    }

    private void viewPdf(File pdf, Uri uri){

        PackageManager packageManager = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType( "application/pdf" );

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if(list.size() > 0){

            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                  intent1.setDataAndType( uri, "application/pdf" );

            }else{

                  Uri uri1 = FileProvider.getUriForFile( this, "br.com.maddevmobile.registrodechuvas", pdf );
                  intent1.setDataAndType( uri1, "application/pdf" );
            }

            startActivityForResult( intent1, 200 );

        }else{
            Toast.makeText(this, "Instale um leitor de pdf . . .", Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Boolean permissoes = true;

        if(requestCode == 100){

              for(int permissao : grantResults){

                    if(permissao == PackageManager.PERMISSION_DENIED){
                            permissoes = false;
                            break;
                    }

              }

              if(!permissoes){
                    Toast.makeText(this, "Por favor ative as permissões necessários para que o app funcione normalmente", Toast.LENGTH_LONG).show();
                    finish();
              }

        }
    }
}
