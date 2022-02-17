package br.com.maddevmobile.registrodechuvas.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.maddevmobile.registrodechuvas.R;
import br.com.maddevmobile.registrodechuvas.adapter.AdapterRecyclerViewChuva;
import br.com.maddevmobile.registrodechuvas.controller.Controller;
import br.com.maddevmobile.registrodechuvas.model.Chuva;
import br.com.maddevmobile.registrodechuvas.util.DialogProgress;
import br.com.maddevmobile.registrodechuvas.util.Util;

import static androidx.core.app.ActivityCompat.recreate;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroFragment extends Fragment implements View.OnClickListener {


    private EditText editText_nome_cidade;
    private EditText editText_nome_estado;
    private EditText editText_nome_bairro;
    private EditText editText_chuva;
    private EditText editText_dataAtual;
    private EditText editText_horaAtual;

    private EditText editText_pesquisa;
    private ImageView imageView_pesquisa;

    private CardView button_salvar;

    private DialogProgress dialogProgress = new DialogProgress();

    private Context context;

    private InterstitialAd interstitialAd;

    private AdapterRecyclerViewChuva adapterRecyclerViewChuva;

    public RegistroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        MobileAds.initialize(getContext(), "ca-app-pub-4791263942734059~3660237702");

        interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId("ca-app-pub-4791263942734059/6549290776");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        adapterRecyclerViewChuva = new AdapterRecyclerViewChuva();

        iniciaComponentes(view);

        retornaDataHoraAtual();

        context = getActivity();

        return view;
    }

    //---------------------- inicia os componentes do layout ---------------------------
    private void iniciaComponentes(View view){

       editText_nome_cidade = view.findViewById(R.id.edit_text_nome_cidade);
       editText_nome_estado = view.findViewById(R.id.edit_text_nome_estado);
       editText_nome_bairro = view.findViewById(R.id.edit_text_nome_bairro);
       editText_chuva = view.findViewById(R.id.edit_text_chuva_mm);
       editText_dataAtual = view.findViewById(R.id.edit_text_data_atual);
       editText_horaAtual = view.findViewById(R.id.edit_text_hora_atual);

       editText_pesquisa = view.findViewById(R.id.edit_text_cep);
       imageView_pesquisa = view.findViewById(R.id.image_pesquisa);
       button_salvar = view.findViewById(R.id.card_enviar_dados);

       button_salvar.setOnClickListener(this);
       imageView_pesquisa.setOnClickListener(this);




    }

    //----------------------eventos de click ---------------------------
    @Override
    public void onClick(View v) {

            switch (v.getId()){

                case R.id.card_enviar_dados:

                    validaCamposFormularioDados();

                    break;

                case R.id.image_pesquisa:

                    validaCampoPesquisa();

                    break;

            }

    }

    //----------------------data e hora automatica do sistema ---------------------------
    private void retornaDataHoraAtual(){


        Locale locale = new Locale("pt", "br");

        Date date = Calendar.getInstance(locale).getTime();

        DateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");


        String data = dataFormat.format(date);
        String hora = horaFormat.format(date);

        editText_dataAtual.setText(data);
        editText_horaAtual.setText(hora);

    }

    //------------------------------ valida campos cep -------------------------------------
    private void validaCampoPesquisa(){

            String pesquisa = editText_pesquisa.getText().toString().trim();

            if( !pesquisa.isEmpty()){


                if(Util.statusInternet_MoWi(context)){


                    String urlBase = "https://viacep.com.br/ws/"+pesquisa+"/json/";

                    //-------------------- classe para consumir API em segundo plano ----------------------------------
                    CepAscyncTacks cepAscyncTacks = new CepAscyncTacks();
                    cepAscyncTacks.execute(urlBase);

                }else{

                    Toast.makeText(getContext(), "Sem com conexão com internet", Toast.LENGTH_SHORT).show();

                }



            }else{

                Toast.makeText(getContext(), "Campo CEP vázio", Toast.LENGTH_SHORT).show();
            }
    }


    //---------------------- metodo para validar campos para salva banco de dados  -------------------------
    @SuppressLint("RestrictedApi")
    private void validaCamposFormularioDados(){


        //------------------------- retornando o valores --------------------------------
        String nome_cidade = editText_nome_cidade.getText().toString();
        String nome_estado = editText_nome_estado.getText().toString();
        String nome_bairro = editText_nome_bairro.getText().toString();
        String chuva_em_mm = editText_chuva.getText().toString();
        String dataString = editText_dataAtual.getText().toString();
        String horaString = editText_horaAtual.getText().toString();



        if(nome_cidade.isEmpty() || nome_estado.isEmpty() || nome_bairro.isEmpty() || chuva_em_mm.isEmpty()){

            Toast.makeText(getContext(),  "Existem campos vázios", Toast.LENGTH_SHORT).show();

        }else{


            Chuva chuva = new Chuva(nome_cidade, nome_estado, nome_bairro, chuva_em_mm, dataString, horaString);

            Controller controller = new Controller(getActivity());

            if(controller.salvarDados( chuva )){

                limpaCampos();
                Toast.makeText(getActivity(), "Sucesso ao salvar os dados", Toast.LENGTH_SHORT).show();

               // adapterRecyclerViewChuva.adiciona(chuva);

                atualizar();

            }else{

                Toast.makeText(getActivity(), "Erro ao salvar os dados", Toast.LENGTH_SHORT).show();
            }



            if(interstitialAd.isLoaded())
                interstitialAd.show();
        }


    }

    //-----------------------------requisição em background(segundo plano) ----------------------------------
    public class CepAscyncTacks extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialogProgress.show(getFragmentManager(), "10");

        }

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0];

            InputStream inputStream ;
            InputStreamReader inputStreamReader ;
            BufferedReader bufferedReader ;
            StringBuffer stringBuffer= null;


            try {

                URL urlConnection = new URL(url);

                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();

                inputStream = connection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                bufferedReader = new BufferedReader(inputStreamReader);


                String linha;
                stringBuffer = new StringBuffer();

                while ((linha = bufferedReader.readLine()) != null){

                    stringBuffer.append( linha );

                }



            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }


            return stringBuffer.toString();

        }


        @Override
        protected void onPostExecute(String resultado){
            super.onPostExecute(resultado);

            try {

                JSONObject jsonObject = new JSONObject(resultado);

                String cidade = jsonObject.getString("localidade");
                String uf = jsonObject.getString("uf");

                editText_nome_cidade.setText( cidade );
                editText_nome_estado.setText( uf );


            } catch (JSONException e) {

                e.printStackTrace();
            }

            limpaCampoCep();
            dialogProgress.dismiss();

        }


    }


    //------------- limpa campos formulario registro de chuva -------------------------
    private void limpaCampos(){
        editText_nome_cidade.setText("");
        editText_nome_estado.setText("");
        editText_nome_bairro.setText("");
        editText_chuva.setText("");



    }

    //------------- limpa campos cep -------------------------
    private void limpaCampoCep(){

        editText_pesquisa.setText("");

    }


    private void atualizar() {

        final DialogProgress dialogProgress = new DialogProgress();
        dialogProgress.show(getFragmentManager(), "10");


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                recreate(getActivity());

                dialogProgress.dismiss();


            }
        }, 3000);



    }

}
