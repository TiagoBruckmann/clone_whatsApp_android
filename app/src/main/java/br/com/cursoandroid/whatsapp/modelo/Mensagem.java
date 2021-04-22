package br.com.cursoandroid.whatsapp.modelo;

public class Mensagem {

    private String cd_usuario;
    private String ds_mensagem;

    public Mensagem() {
    }

    public String getCd_usuario() {
        return cd_usuario;
    }

    public void setCd_usuario(String cd_usuario) {
        this.cd_usuario = cd_usuario;
    }

    public String getDs_mensagem() {
        return ds_mensagem;
    }

    public void setDs_mensagem(String ds_mensagem) {
        this.ds_mensagem = ds_mensagem;
    }
}
