package br.com.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import br.com.cursoandroid.whatsapp.R;
import br.com.cursoandroid.whatsapp.modelo.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatoAdapter(@NonNull Context c, @NonNull ArrayList<Contato> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //verifica se a lista esta vazia
        if ( contatos != null ){

            //iniciar objeto para montegem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //montar a view
            view = inflater.inflate( R.layout.lista_contato, parent, false );

            //recuperar elemento para exibição
            TextView nomeContato = view.findViewById(R.id.tv_nome);
            TextView emailContato = view.findViewById(R.id.tv_email);

            Contato contato = contatos.get( position );
            nomeContato.setText( contato.getNome() );
            emailContato.setText( contato.getEmail() );

        }

        return view;

    }
}
