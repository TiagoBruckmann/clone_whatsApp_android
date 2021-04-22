package br.com.cursoandroid.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.cursoandroid.whatsapp.R;
import br.com.cursoandroid.whatsapp.activity.Conversas;
import br.com.cursoandroid.whatsapp.adapter.ContatoAdapter;
import br.com.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.cursoandroid.whatsapp.helper.Preferencias;
import br.com.cursoandroid.whatsapp.modelo.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference fireBase;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        fireBase.addValueEventListener( valueEventListenerContatos );
    }

    @Override
    public void onStop() {
        super.onStop();
        fireBase.removeEventListener( valueEventListenerContatos );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //instancia contatos
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //montar o listview e adapter
        listView = view.findViewById(R.id.lv_contatos);
        /*
        adapter = new ArrayAdapter(
                getActivity(),
                R.layout.lista_contato,
                contatos
        );

         */

        adapter = new ContatoAdapter(getActivity(), contatos);

        listView.setAdapter( adapter );

        //recuperar contatos do firebase
        Preferencias preferencias = new Preferencias( getActivity() );
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        fireBase = ConfiguracaoFirebase.getFirebase().child("Contatos").child( identificadorUsuarioLogado );

        //listagem de recuperação de contatos
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //limpar lista
                contatos.clear();
                //listar contatos
                for( DataSnapshot dados: dataSnapshot.getChildren() ){

                    Contato contato = dados.getValue( Contato.class );
                    contatos.add( contato );
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Conversas.class);
                //recupera dados a serem exibidos
                Contato contato = contatos.get(position);
                //enviado dados para conversas activity
                intent.putExtra("nome", contato.getNome() );
                intent.putExtra("email", contato.getEmail() );
                startActivity(intent);
            }
        });

        return view;

    }
}
