package app.daazi.v5.appclientevip.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import app.daazi.v5.appclientevip.R;
import app.daazi.v5.appclientevip.api.AppUtil;
import app.daazi.v5.appclientevip.controller.ClientePJController;
import app.daazi.v5.appclientevip.model.Cliente;
import app.daazi.v5.appclientevip.model.ClientePJ;

public class ClientePessoaJuridicaActivity extends AppCompatActivity {

    Cliente novoVIP;
    ClientePJ novoClientePJ;
    ClientePJController controller;

    private SharedPreferences preferences;

    EditText editCNPJ, editRazaoSocial, editDataAberturaPJ;

    CheckBox chSimplesNacional, chMEI;

    Button btnSalvarContinuar;
    Button btnVoltar;
    Button btnCancelar;

    boolean isFormularioOK, isSimplesNacional, isMEI;

    int ultimoIDClientePessoaPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_pessoa_juridica_card);

        initFormulario();

        btnSalvarContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFormularioOK = validarFormulario()) {

                    novoClientePJ.setClientePFID(ultimoIDClientePessoaPF);
                    novoClientePJ.setCnpj(editCNPJ.getText().toString());
                    novoClientePJ.setRazaoSocial(editRazaoSocial.getText().toString());
                    novoClientePJ.setDataAbertura(editDataAberturaPJ.getText().toString());
                    novoClientePJ.setSimplesNacional(isSimplesNacional);
                    novoClientePJ.setMei(isMEI);

                    controller.incluir(novoClientePJ);

                    salvarSharedPreferences();

                    Intent intent = new Intent(ClientePessoaJuridicaActivity.this,
                            CreadencialDeAcessoActivity.class);
                    startActivity(intent);

                }

            }
        });


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClientePessoaJuridicaActivity.this,
                        LoginActivity.class);

                startActivity(intent);

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FancyAlertDialog.Builder(ClientePessoaJuridicaActivity.this)
                        .setTitle("Confirme o Cancelamento")
                        .setBackgroundColor(Color.parseColor("#303F9F"))
                        .setMessage("Deseja realmente Cancelar?")
                        .setNegativeBtnText("NÃO")
                        .setNegativeBtnBackground(Color.parseColor("#FF4081"))
                        .setPositiveBtnText("SIM")
                        .setPositiveBtnBackground(Color.parseColor("#4ECA25"))
                        .isCancellable(true)
                        .setIcon(R.mipmap.ic_launcher_round, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getApplicationContext(),"Cancelado com sucesso....",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getApplicationContext(),"Continue seu cadastro...",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build();

            }
        });





    }

    public void simplesNacional(View view){

        isSimplesNacional = chSimplesNacional.isChecked();

    }

    public void mei(View view){

        isMEI = chMEI.isChecked();

    }

    private void initFormulario() {

        editCNPJ = findViewById(R.id.editCNPJ);
        editRazaoSocial = findViewById(R.id.editRazaoSocial);
        editDataAberturaPJ = findViewById(R.id.editDataAberturaPJ);

        chSimplesNacional = findViewById(R.id.chSimplesNacional);
        chMEI = findViewById(R.id.chMEI);

        btnSalvarContinuar = findViewById(R.id.btnSalvarContinuar);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnCancelar = findViewById(R.id.btnCancelar);

        novoClientePJ = new ClientePJ();
        novoVIP = new Cliente();
        controller = new ClientePJController(this);

        restaurarSharedPreferences();

    }

    private boolean validarFormulario() {

        boolean retorno = true;

        if(TextUtils.isEmpty(editCNPJ.getText().toString())){
            editCNPJ.setError("*");
            editCNPJ.requestFocus();

            retorno =  false;
        }

        if(!AppUtil.isCNPJ(editCNPJ.getText().toString())){

            editCNPJ.setError("*");
            editCNPJ.requestFocus();

            retorno = false;

            Toast.makeText(this, "CNPJ Inválido, tente novamente...", Toast.LENGTH_LONG).show();

        }else {
            editCNPJ.setText(AppUtil.mascaraCNPJ(editCNPJ.getText().toString()));
        }

        if(TextUtils.isEmpty(editRazaoSocial.getText().toString())){
            editRazaoSocial.setError("*");
            editRazaoSocial.requestFocus();
            retorno =  false;
        }

        if(TextUtils.isEmpty(editDataAberturaPJ.getText().toString())){
            editDataAberturaPJ.setError("*");
            editDataAberturaPJ.requestFocus();
            retorno =  false;
        }

        return  retorno;

    }

    private void salvarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();

        dados.putString("cnpj", editCNPJ.getText().toString());
        dados.putString("razaoSocial", editRazaoSocial.getText().toString());
        dados.putBoolean("simplesNacional", isSimplesNacional);
        dados.putString("dataAbertura", editDataAberturaPJ.getText().toString());
        dados.putBoolean("mei", isMEI);
        dados.putInt("ultimoIDClientePessoaPF", ultimoIDClientePessoaPF);
        dados.apply();

    }

    private void restaurarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        ultimoIDClientePessoaPF = preferences.getInt("ultimoIDClientePessoaPF", -1);

    }
}
