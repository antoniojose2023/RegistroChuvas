package br.com.maddevmobile.registrodechuvas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import br.com.maddevmobile.registrodechuvas.model.Chuva;
import br.com.maddevmobile.registrodechuvas.util.Util;

public class Database extends SQLiteOpenHelper {

    private final static String NOME_BANCO = "bdChuva";
    private final static int VERSAO = 1;

    private Cursor cursor;
    private SQLiteDatabase db;


    public Database(@Nullable Context context) {
        super(context, NOME_BANCO, null, VERSAO);

        db = getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Util.TABELA_CHUVA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean insert(String tabela, ContentValues dados){

        boolean retorno =  true;

        try{

            retorno = db.insert(tabela, null, dados) > 0;

        }catch (Exception ex){

            retorno = false;
        }


        return retorno;

    }


    public List<Chuva> getChuva(){

            List<Chuva> chuvas = new ArrayList<>();
            Chuva chuva;


            try{

                cursor = db.rawQuery(Util.SELECT_TABELA_CHUVA, null);

                if(cursor.moveToFirst()){


                    do{

                        chuva = new Chuva();

                        chuva.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        chuva.setNomeCidade(cursor.getString(cursor.getColumnIndex("nomeCidade")));
                        chuva.setNomeEstado(cursor.getString(cursor.getColumnIndex("nomeEstado")));
                        chuva.setNomeBairro(cursor.getString(cursor.getColumnIndex("nomeBairro")));
                        chuva.setChuvaMM(cursor.getString(cursor.getColumnIndex("chuvaMM")));
                        chuva.setData(cursor.getString(cursor.getColumnIndex("data")));
                        chuva.setHora(cursor.getString(cursor.getColumnIndex("hora")));

                        chuvas.add( chuva );

                    }while (cursor.moveToNext());

                }

            }catch (SQLException ex){

                Log.i("CHUVA", "getChuva: "+ex.getMessage());
            }



            return chuvas;

    }


    public boolean delete(String tabela, ContentValues dados){

        boolean retorno = true;

        try{

            int id = dados.getAsInteger("id");

            retorno = db.delete(tabela, "id=?", new String[]{Integer.toString(id)}) > 0;

        }catch (SQLException ex){

            Log.i("delete: ", ex.getMessage());
            retorno = false;

        }


        return retorno;



    }


}
