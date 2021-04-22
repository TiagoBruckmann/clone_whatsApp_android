package br.com.cursoandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.cursoandroid.whatsapp.R;
import br.com.cursoandroid.whatsapp.adapter.TabAdapter;
import br.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.cursoandroid.whatsapp.helper.Preferencias;
import br.com.cursoandroid.whatsapp.helper.SlidingTabLayout;
import br.com.cursoandroid.whatsapp.modelo.Contato;
import br.com.cursoandroid.whatsapp.modelo.Usuario;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth usuarioAutenticacao;
    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String identificadorContato;
    private DatabaseReference fireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar( toolbar );

        slidingTabLayout = findViewById(R.id.stl_labs);
        viewPager = findViewById(R.id.vp_pagina);

        //configurar sliding tabs
        slidingTabLayout.setDistributeEvenly( true );
        slidingTabLayout.setSelectedIndicatorColors( ContextCompat.getColor(this, R.color.colorAccent) );

        //configurar o adapter
        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter( tabAdapter );

        slidingTabLayout.setViewPager( viewPager );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_configuracoes:
                return true;
            case R.id.item_adicionar:
                abrirCadContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadContato(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //configurações do dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuario");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView( editText );

        //configurar botoes
        alertDialog.setPositiveButton("cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();

                //validar se o o email foi digitado
                if ( emailContato.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o E-mail", Toast.LENGTH_LONG).show();
                }else{

                    //verifica se o usuario já ta cadastrado no app
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    //recuperar do banco
                    fireBase = ConfiguracaoFirebase.getFirebase().child("usuarios").child( identificadorContato );

                    fireBase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if ( dataSnapshot.getValue() != null ){

                                //recuperar dados do usuario adicionado
                                Usuario usuarioContato =  dataSnapshot.getValue( Usuario.class );

                                //recuperar identificador do usuario logado
                                Preferencias preferencias = new Preferencias( MainActivity.this );
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                fireBase = ConfiguracaoFirebase.getFirebase();
                                fireBase = fireBase.child("Contatos").child( identificadorUsuarioLogado )
                                        .child( identificadorContato );

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario( identificadorContato );
                                contato.setEmail( usuarioContato.getEmail() );
                                contato.setNome( usuarioContato.getNome() );

                                fireBase.setValue( contato );
                            }else{
                                Toast.makeText(MainActivity.this, "Usuario não possui um cadastro",
                                        Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    private void deslogarUsuario() {
        usuarioAutenticacao.signOut();
        Intent intent = new Intent( MainActivity.this, Login.class );
        startActivity( intent );
        finish();
    }
}
