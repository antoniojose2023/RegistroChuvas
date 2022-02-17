package br.com.maddevmobile.registrodechuvas.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.maddevmobile.registrodechuvas.R;
import br.com.maddevmobile.registrodechuvas.adapter.AdapterRecyclerViewChuva;
import br.com.maddevmobile.registrodechuvas.controller.Controller;
import br.com.maddevmobile.registrodechuvas.model.Chuva;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricoFragment extends Fragment implements AdapterRecyclerViewChuva.ClickChuva {


    private RecyclerView recyclerView;

    private List<Chuva> listaChuva = new ArrayList<>();
    private List<Chuva> listaChuva1 = new ArrayList<>();

    private Controller controller;


    private AdapterRecyclerViewChuva adapterRecyclerViewChuva;

    public HistoricoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        controller = new Controller(getActivity());



        listaChuva1 = controller.getChuvaController();
        for(Chuva chuva: listaChuva1){


            listaChuva.add( chuva );
        }


        configurarRecyclerView(view);





        return view;

    }

    //----------------------- configurando recyclerView chuva -----------------------------
    private void configurarRecyclerView(View view){



        recyclerView = view.findViewById(R.id.recyclerView_chuva);

        adapterRecyclerViewChuva = new AdapterRecyclerViewChuva(getContext(), listaChuva, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter( adapterRecyclerViewChuva );




    }

    @Override
    public void onClickChuva(Chuva chuva) {

        dialgConfirmarExclusao(chuva);

    }


    private void dialgConfirmarExclusao(final Chuva chuva){

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Confirmação de exclusão")
                .setMessage("Você tem certeza que deseja exlcuir este registro?")
                .setCancelable(true)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(controller.deleteDados(chuva)){

                            Toast.makeText(getContext(), "Excluido com  sucesso", Toast.LENGTH_LONG).show();

                            adapterRecyclerViewChuva.exlcuir(chuva);

                        }else{

                            Toast.makeText(getContext(), "Erro ao excluir dados", Toast.LENGTH_LONG).show();
                        }

                    }
                }).create();


        dialog.show();


    }



}
