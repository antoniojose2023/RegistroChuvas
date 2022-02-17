package br.com.maddevmobile.registrodechuvas.controller;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import br.com.maddevmobile.registrodechuvas.database.Database;
import br.com.maddevmobile.registrodechuvas.model.Chuva;

public class Controller extends Database {

    private ContentValues contentValues;

    private List<Chuva> lista = new ArrayList<>();


    public Controller(@Nullable Context context) {
        super(context);
    }


    public boolean salvarDados(Chuva chuva){

        contentValues = new ContentValues();

        contentValues.put("nomeCidade", chuva.getNomeCidade());
        contentValues.put("nomeEstado", chuva.getNomeEstado());
        contentValues.put("nomeBairro", chuva.getNomeBairro());
        contentValues.put("chuvaMM", chuva.getChuvaMM());
        contentValues.put("data", chuva.getData());
        contentValues.put("hora", chuva.getHora());

        return insert("chuva", contentValues);

    }


    public boolean deleteDados(Chuva chuva){

        contentValues = new ContentValues();

        contentValues.put("id", chuva.getId());


        return delete("chuva", contentValues);

    }


    public List<Chuva> getChuvaController(){

        lista = getChuva();

        return lista;

    }




}
