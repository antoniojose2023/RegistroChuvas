package br.com.maddevmobile.registrodechuvas.util;


import android.content.Context;

import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class Util {


    //---------------------------------------atributos -------------------------------------------

    public final static String TABELA_CHUVA = "CREATE TABLE chuva (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeCidade TEXT, nomeEstado TEXT , nomeBairro TEXT, chuvaMM TEXT, data TEXT, hora TEXT )";

    public static final String SELECT_TABELA_CHUVA = "SELECT * FROM chuva Order by data DESC";






    //------------------------------------------metodos ---------------------------------------
    public static boolean validate(AppCompatActivity activity, int requestCode, String[] permissions) {

        List<String> list = new ArrayList<String>();

        for (String permission : permissions) {

            boolean ok = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if (!ok) { // se for false
                list.add(permission);
            }
        }
        if (list.isEmpty()) {
            return true;
        }

        String[] newPermissions = new String[list.size()];
        list.toArray(newPermissions);

        //solicita a permissao

        ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
        return false;
    }







    public static boolean statusInternet_MoWi(Context context) {

        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conexao != null) {

            // PARA DISPOSTIVOS NOVOS
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                NetworkCapabilities recursosRede = conexao.getNetworkCapabilities(conexao.getActiveNetwork());

                if (recursosRede != null) {//VERIFICAMOS SE RECUPERAMOS ALGO

                    if (recursosRede.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                        //VERIFICAMOS SE DISPOSITIVO TEM 3G
                        return true;

                    } else if (recursosRede.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                        //VERIFICAMOS SE DISPOSITIVO TEM WIFFI
                        return true;

                    }

                    //N??O POSSUI UMA CONEXAO DE REDE V??LIDA

                    return false;

                }

            } else {//COMECO DO ELSE

                // PARA DISPOSTIVOS ANTIGOS  (PRECAU????O)
                NetworkInfo informacao = conexao.getActiveNetworkInfo();


                if (informacao != null && informacao.isConnected()) {
                    return true;
                } else
                    return false;


            }//FIM DO ELSE
        }


        return false;
    }



    }
