package app.daazi.v5.appclientevip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.daazi.v5.appclientevip.R;

public class RecuperarSenhaActivity extends AppCompatActivity {

    EditText editEmail;
    Button btnRecuperar, btnVoltar;

    private SharedPreferences preferences;

    boolean isFormularioOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha_card);

        initFormulario();

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFormularioOK = validarFormulario()) {

                    Toast.makeText(getApplicationContext(),"Sua senha foi enviada para o e-mail informado...",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RecuperarSenhaActivity.this,
                            LoginActivity.class);
                    startActivity(intent);

                }

            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecuperarSenhaActivity.this,
                        LoginActivity.class);
                startActivity(intent);


            }
        });
    }

    private void initFormulario() {

        editEmail = findViewById(R.id.editEmail);

        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnVoltar = findViewById(R.id.btnVoltar);

    }

    private boolean validarFormulario() {

        boolean retorno = true;

        if(TextUtils.isEmpty(editEmail.getText().toString())){
            editEmail.setError("*");
            editEmail.requestFocus();
            retorno =  false;
        }

        return  retorno;

    }
}
