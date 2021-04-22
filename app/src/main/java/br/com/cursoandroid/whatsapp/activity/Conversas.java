package br.com.cursoandroid.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.cursoandroid.whatsapp.R;
import br.com.cursoandroid.whatsapp.adapter.MensagemAdapter;
import br.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.cursoandroid.whatsapp.helper.Preferencias;
import br.com.cursoandroid.whatsapp.modelo.Conversa;
import br.com.cursoandroid.whatsapp.modelo.Mensagem;

public class Conversas extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagens;

    //dados destinatario
    private String nomeUserDestinatario;
    private String cd_usuarioDestinatario;

    //dados do remetente
    private String cd_usuarioRementente;
    private String nomeUsuarioRemetente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);

        toolbar = findViewById(R.id.tb_conversas);
        editMensagem = findViewById(R.id.mensagem_id);
        btMensagem = findViewById(R.id.botao_enviar_msg_id);
        listView = findViewById(R.id.lv_conversas);

        //dados do usuario logado
        Preferencias preferencias = new Preferencias(Conversas.this);
        cd_usuarioRementente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getNome();

        //transfere dados de uma activity para outra
        Bundle extra = getIntent().getExtras();

        if ( extra != null ){
            nomeUserDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            cd_usuarioDestinatario = Base64Custom.codificarBase64( emailDestinatario );
        }

        //configurar a toolbar
        toolbar.setTitle(nomeUserDestinatario);
        toolbar.setNavigationIcon(R.drawable.icone_voltar);
        setSupportActionBar( toolbar );

        //montar listagem e adapter das conversas
        mensagens = new ArrayList<>();
       adapter = new MensagemAdapter(Conversas.this, mensagens);
        /*
        adapter = new ArrayAdapter(
                Conversas.this,
                android.R.layout.simple_list_item_1,
                mensagens
        );

         */

        listView.setAdapter( adapter );

        //recuperar as mensagens do firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child( cd_usuarioRementente )
                .child( cd_usuarioDestinatario );

        //criar listagens para mensagens
        valueEventListenerMensagens = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //limpar mensagens
                mensagens.clear();

                //recupera as mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Mensagem mensagem = dados.getValue( Mensagem.class );
                    mensagens.add(mensagem );
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener( valueEventListenerMensagens );


        //enviar mensagem
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoMensagem = editMensagem.getText().toString();

                if ( textoMensagem.isEmpty() ){
                    Toast.makeText(Conversas.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();
                }else{

                    Mensagem mensagem = new Mensagem();
                    mensagem.setCd_usuario( cd_usuarioRementente );
                    mensagem.setDs_mensagem( textoMensagem );

                    //salvar mensagem para o remetente
                    Boolean retornoMensagemRemetente = salvarMensagem(cd_usuarioRementente, cd_usuarioDestinatario, mensagem);
                    if ( !retornoMensagemRemetente ) {

                        Toast.makeText(Conversas.this,
                                "Problemas ao enviar mensagens, tente novamente!",
                                Toast.LENGTH_LONG).show();

                    }else{

                        //salvar mensagem para o destinatario
                        Boolean retornoMensagemDestinatario = salvarMensagem(cd_usuarioDestinatario, cd_usuarioRementente, mensagem);

                        if ( !retornoMensagemDestinatario ) {

                            Toast.makeText(Conversas.this,
                                    "Problemas ao enviar mensagens, tente novamente!",
                                    Toast.LENGTH_LONG).show();

                        }

                    }

                    //salvar conversa para o remetente

                    Conversa conversa = new Conversa();
                    conversa.setCd_usuario( cd_usuarioDestinatario );
                    conversa.setNm_usuario( nomeUserDestinatario );
                    conversa.setDs_mensagem( textoMensagem );

                    Boolean retornoConversaRemetente = salvarConversa(cd_usuarioRementente, cd_usuarioDestinatario, conversa );
                    if ( !retornoConversaRemetente ) {

                        Toast.makeText(Conversas.this,
                                "Problemas ao enviar mensagens, tente novamente!",
                                Toast.LENGTH_LONG).show();

                    }else{

                        //salvar conversa para destinatario

                        conversa = new Conversa();
                        conversa.setCd_usuario( cd_usuarioRementente );
                        conversa.setNm_usuario( nomeUsuarioRemetente );
                        conversa.setDs_mensagem( textoMensagem );

                        Boolean retornoConversaDestinatario = salvarConversa( cd_usuarioDestinatario, cd_usuarioRementente, conversa );
                        if ( !retornoConversaDestinatario ) {

                            Toast.makeText(Conversas.this,
                                    "Problemas ao enviar mensagens, tente novamente!",
                                    Toast.LENGTH_LONG).show();

                        }

                    }



                editMensagem.setText("");

                }

            }
        });
    }

    public boolean salvarMensagem(String cdRemetente, String cdDestinatario, Mensagem mensagem){

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");

            firebase.child(cdRemetente).child(cdDestinatario).push().setValue( mensagem );

            return true;

        }catch ( Exception e ){
            e.printStackTrace();
            return false;
        }

    }

    private boolean salvarConversa(String cdRemetente, String cdDestinatario, Conversa conversa){

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("Conversas");
            firebase.child( cdRemetente )
                    .child( cdDestinatario )
                    .setValue( conversa );

            return true;

        }catch ( Exception e ){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener( valueEventListenerMensagens );
    }
}
