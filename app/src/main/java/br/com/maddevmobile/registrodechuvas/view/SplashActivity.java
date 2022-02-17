package br.com.maddevmobile.registrodechuvas.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.maddevmobile.registrodechuvas.R;

public class SplashActivity extends AppCompatActivity {

    private TextView titulo_tela_splash;
    private ImageView imageView_tela_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ViewGroup viewGroup = findViewById(R.id.root);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TransitionManager.beginDelayedTransition(viewGroup, new Explode());


                titulo_tela_splash = findViewById(R.id.text_titulo_telaSplash);
                titulo_tela_splash.setVisibility(View.VISIBLE);

                imageView_tela_splash = findViewById(R.id.image_telaSplash);
                imageView_tela_splash.setVisibility(View.VISIBLE);


            }
        },2000);



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

              startActivity(new Intent(getApplicationContext(), MainActivity.class));
              finish();

            }
        },5000);



    }
}
