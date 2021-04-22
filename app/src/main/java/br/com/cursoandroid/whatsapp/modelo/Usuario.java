package br.com.cursoandroid.whatsapp.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;

public class Usuario {

    private String cd_usuario;
    private String nome;
    private String email;
    private String senha;

    public Usuario(){

    }
    public void salvar(){

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child( getCd_usuario() ).setValue( this );

    }

    @Exclude
    public String getCd_usuario() {
        return cd_usuario;
    }

    public void setCd_usuario(String cd_usuario) {
        this.cd_usuario = cd_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
