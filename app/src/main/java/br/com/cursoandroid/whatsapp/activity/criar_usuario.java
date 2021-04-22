package br.com.cursoandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.cursoandroid.whatsapp.R;
import br.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.cursoandroid.whatsapp.helper.Preferencias;
import br.com.cursoandroid.whatsapp.modelo.Usuario;

public class criar_usuario extends AppCompatActivity {

    private EditText nomeUser;
    private EditText emailUser;
    private EditText senhaUser;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);

        nomeUser = findViewById(R.id.criar_user_id);
        emailUser = findViewById(R.id.criar_email_id);
        senhaUser = findViewById(R.id.criar_senha_id);
        botaoCadastrar = findViewById(R.id.bt_cadastrar_user_id);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setNome( nomeUser.getText().toString() );
                usuario.setEmail( emailUser.getText().toString() );
                usuario.setSenha( senhaUser.getText().toString() );
                cadastrarUsuario();

            }
        });

    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(criar_usuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    Toast.makeText( criar_usuario.this, "Sucesso ao cadastrar Usuario", Toast.LENGTH_LONG ).show();

                    String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setCd_usuario( identificadorUsuario );
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias( criar_usuario.this );
                    preferencias.salvarDados( identificadorUsuario, usuario.getNome() );

                    abrirLoginUser();

                }else{

                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, com letras e numeros!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado é invalido, insira um novo ou digite um e-mail valido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Este e-mail já esta cadastrado";
                    } catch (Exception e) {
                        erroExcecao = "Ao cadastrar usuario!";
                        e.printStackTrace();
                    }

                    Toast.makeText(criar_usuario.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void abrirLoginUser(){
        Intent intent = new Intent(criar_usuario.this, Login.class);
        startActivity( intent );
        finish();
    }

}
