package br.com.cursoandroid.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;


public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "Whats.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "usuarioAutenticacao";
    private final String CHAVE_NOME = "nomeUsuarioLogado";
    //private final String CHAVE_NOME = "nome";
    //private String CHAVE_FONE = "fone";
    //private String CHAVE_TOKEN = "token";

    public Preferencias(Context contextoParamentro){

        contexto = contextoParamentro;
        preferences = contexto.getSharedPreferences( NOME_ARQUIVO, MODE );
        editor = preferences.edit();

    }

    public void salvarDados( String usuarioAutenticacao, String nomeUsuario ){
        editor.putString( CHAVE_IDENTIFICADOR, usuarioAutenticacao );
        editor.putString( CHAVE_NOME, nomeUsuario );
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString( CHAVE_IDENTIFICADOR, null );
    }

    public String getNome(){
        return preferences.getString( CHAVE_NOME, null );
    }

    /*

    public void salvarUserPreferencias( String nome, String fone, String token ){

        editor.putString(CHAVE_NOME, nome);
        //editor.putString(CHAVE_FONE, fone);
        //editor.putString(CHAVE_TOKEN, token);
        editor.commit();

    }

    public HashMap<String, String> getDadosUser(){

        HashMap<String, String> dadosUser = new HashMap<>();

        dadosUser.put( CHAVE_NOME, preferences.getString(CHAVE_NOME, null) );
        dadosUser.put( CHAVE_FONE, preferences.getString( CHAVE_FONE, null ) );
        dadosUser.put( CHAVE_TOKEN, preferences.getString( CHAVE_TOKEN, null ) );

        return  dadosUser;

    }


     */
}
