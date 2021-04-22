package br.com.cursoandroid.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import br.com.cursoandroid.whatsapp.R;
import br.com.cursoandroid.whatsapp.helper.Preferencias;

public class Validador extends AppCompatActivity {

    private EditText codigoValidador;
    private Button botaoValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        /*

        codigoValidador = findViewById(R.id.cod_validar_id);
        botaoValidar = findViewById(R.id.botao_validar_id);

        //mascara do codigo de validacao
        SimpleMaskFormatter simpleMasKCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher( codigoValidador, simpleMasKCodigoValidacao );
        codigoValidador.addTextChangedListener( mascaraCodigoValidacao );
        botaoValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recuperar dados das preferencias de usuarios
                Preferencias preferencias = new Preferencias( Validador.this );
                HashMap<String, String> usuario = preferencias.getDadosUser();

                String tokenGerado = usuario.get("token");
                String tokenDigitado = codigoValidador.getText().toString();

                //validar o token
                if(tokenDigitado.equals( tokenGerado )){
                    Toast.makeText(Validador.this, "Token VALIDADO", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Validador.this, "Token NÃO VALIDADO", Toast.LENGTH_LONG).show();
                }

            }
        });


         */
    }
}
