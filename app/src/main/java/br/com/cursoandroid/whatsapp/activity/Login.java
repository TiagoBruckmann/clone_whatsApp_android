package br.com.cursoandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import br.com.cursoandroid.whatsapp.R;
import br.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.cursoandroid.whatsapp.helper.Permissao;
import br.com.cursoandroid.whatsapp.helper.Preferencias;
import br.com.cursoandroid.whatsapp.modelo.Usuario;

public class Login extends AppCompatActivity {

    /*
    private EditText nome;
    private EditText ddd_pais;
    private EditText ddd;
    private EditText fone;
    private Button botaoCadastrar;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };

     */

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference firebase;
    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUserLogado();

        email = findViewById(R.id.logar_email_id);
        senha = findViewById(R.id.logar_senha_id);
        botaoLogar = findViewById(R.id.botao_logar_id);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail( email.getText().toString() );
                usuario.setSenha( senha.getText().toString() );
                validarLogin();

            }
        });


        /*
        Permissao.validaPermissao( 1,this, permissoesNecessarias );

        nome = findViewById(R.id.nome_id);
        ddd_pais = findViewById(R.id.ddd_pais_id);
        ddd = findViewById(R.id.ddd_id);
        fone = findViewById(R.id.fone_id);
        botaoCadastrar = findViewById(R.id.botao_cadastrar_id);


        // definindo as mascaras
        SimpleMaskFormatter simpleMaskDdd_pais = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskDdd = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskFone = new SimpleMaskFormatter("NNNNN-NNNN");

        MaskTextWatcher maskDdd_pais = new MaskTextWatcher(ddd_pais, simpleMaskDdd_pais);
        MaskTextWatcher maskDdd = new MaskTextWatcher(ddd, simpleMaskDdd);
        MaskTextWatcher maskFone = new MaskTextWatcher(fone, simpleMaskFone);

        ddd_pais.addTextChangedListener( maskDdd_pais );
        ddd.addTextChangedListener( maskDdd );
        fone.addTextChangedListener( maskFone );

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeUser = nome.getText().toString();
                String foneCompleto =
                        ddd_pais.getText().toString() +
                        ddd.getText().toString() +
                        fone.getText().toString();

                //retirando a formatação do numero de telefone
                String foneSemFormatacao = foneCompleto.replace("+", "");
                foneSemFormatacao = foneSemFormatacao.replace("-", "");

                //gerar token
                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt( 9999 - 1000 ) + 1000;
                String token = String.valueOf( numeroRandomico );
                String mensagemEnvio = "WhatsApp código de confirmação: " + token;

                //salvar os dados de validação
                Preferencias preferencias = new Preferencias( Login.this );
                preferencias.salvarUserPreferencias(nomeUser, foneSemFormatacao, token);

                //envio de SMS
                boolean enviadoSMS = enviaSMS( "+" + foneSemFormatacao, mensagemEnvio );

                //verifica se o SMS foi enviado

                if( enviadoSMS ){

                    Intent intent = new Intent( Login.this, Validador.class );
                    startActivity( intent );
                    finish();

                }else{
                    Toast.makeText(Login.this, "Problemas para enviar o SMS, tente novamente!", Toast.LENGTH_LONG ).show();
                }

                 */

                /*

                HashMap<String, String> usuario = preferencias.getDadosUser();
                Log.i("TOKEN ","NOME: " + usuario.get("nome") + "FONE: " + usuario.get("fone") );



            }
        });
    }
/*
    private boolean enviaSMS(String fone, String mensagem ){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage( fone, null, mensagem, null, null );

            return true;

        }catch (Exception e){
            e.printStackTrace();

            return false;
        }
    }

    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults ){

        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        for( int resultado : grantResults ){

            if( resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }

        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões negadas");
        builder.setMessage("É necessario aceitar as permissões para poder utilizar este App!");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    */

    }

    private void verificarUserLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if ( autenticacao.getCurrentUser() != null ){
            abrirTelaPrincipal();
        }

    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){

                    identificadorUsuarioLogado = Base64Custom.codificarBase64( usuario.getEmail() );

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child( identificadorUsuarioLogado );

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue( Usuario.class );

                            Preferencias preferencias = new Preferencias( Login.this );
                            preferencias.salvarDados( identificadorUsuarioLogado, usuarioRecuperado.getNome() );

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    firebase.addListenerForSingleValueEvent( valueEventListenerUsuario );

                    abrirTelaPrincipal();
                    Toast.makeText( Login.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText( Login.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent( Login.this, MainActivity.class );
        startActivity( intent );
        finish();
    }

    public void abrirCriarUsuario(View View){

        Intent intent = new Intent(Login.this, criar_usuario.class);
        startActivity( intent );

    }
}
