package br.com.maddevmobile.registrodechuvas.model;

import java.io.Serializable;

public class Chuva implements Serializable {

    private int id;
    private  String nomeCidade;
    private String nomeEstado;
    private String nomeBairro;
    private String chuvaMM;
    private String data;
    private String hora;

    public Chuva(){

    }



    public Chuva(String nomeCidade, String nomeEstado, String nomeBairro, String chuvaMM, String data, String hora) {
        this.nomeCidade = nomeCidade;
        this.nomeEstado = nomeEstado;
        this.nomeBairro = nomeBairro;
        this.chuvaMM = chuvaMM;
        this.data = data;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCidade() {
        return nomeCidade;
    }

    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }

    public String getNomeEstado() {
        return nomeEstado;
    }

    public void setNomeEstado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }

    public String getNomeBairro() {
        return nomeBairro;
    }

    public void setNomeBairro(String nomeBairro) {
        this.nomeBairro = nomeBairro;
    }

    public String getChuvaMM() {
        return chuvaMM;
    }

    public void setChuvaMM(String chuvaMM) {
        this.chuvaMM = chuvaMM;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }



}
