package app.daazi.v5.appclientevip.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import app.daazi.v5.appclientevip.R;
import app.daazi.v5.appclientevip.api.AppUtil;
import app.daazi.v5.appclientevip.controller.ClientePFController;
import app.daazi.v5.appclientevip.model.Cliente;
import app.daazi.v5.appclientevip.model.ClientePF;

public class ClientePessoaFisicaActivity extends AppCompatActivity {

    Cliente novoVIP;
    ClientePF novoClientePF;
    ClientePFController controller;

    private SharedPreferences preferences;

    EditText editCPF;
    EditText editNomeCompleto;

    Button btnSalvarContinuar;
    Button btnVoltar;
    Button btnCancelar;

    boolean isFormularioOK, isPessoaFisica;

    int clienteID;
    int ultimoIDClientePessoaPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_pessoa_fisica_card);

        initFormulario();

        btnSalvarContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFormularioOK = validarFormulario()) {

                    novoClientePF.setCpf(editCPF.getText().toString());
                    novoClientePF.setNomeCompleto(editNomeCompleto.getText().toString());
                    novoClientePF.setClienteID(clienteID);

                    controller.incluir(novoClientePF);

                    ultimoIDClientePessoaPF = controller.getUltimoID();

                    salvarSharedPreferences();

                    Intent intent;

                    if (isPessoaFisica)

                        intent = new Intent(ClientePessoaFisicaActivity.this,
                                CreadencialDeAcessoActivity.class);

                    else
                        intent = new Intent(ClientePessoaFisicaActivity.this,
                                ClientePessoaJuridicaActivity.class);


                    startActivity(intent);


                }

            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClientePessoaFisicaActivity.this,
                        LoginActivity.class);
                startActivity(intent);


            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new FancyAlertDialog.Builder(ClientePessoaFisicaActivity.this)
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
                                Toast.makeText(getApplicationContext(), "Cancelado com sucesso....", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getApplicationContext(), "Continue seu cadastro...", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build();

            }
        });
    }

    private void initFormulario() {

        editCPF = findViewById(R.id.editCPF);
        editNomeCompleto = findViewById(R.id.editNomeCompleto);

        btnSalvarContinuar = findViewById(R.id.btnSalvarContinuar);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnCancelar = findViewById(R.id.btnCancelar);

        novoClientePF = new ClientePF();
        novoVIP = new Cliente();
        controller = new ClientePFController(this);

        restaurarSharedPreferences();

    }

    private boolean validarFormulario() {

        boolean retorno = true;

        String cpf = editCPF.getText().toString();

        if (TextUtils.isEmpty(cpf)) {
            editCPF.setError("*");
            editCPF.requestFocus();
            retorno = false;
        }

        if (!AppUtil.isCPF(cpf)){

            editCPF.setError("*");
            editCPF.requestFocus();

            retorno = false;

            Toast.makeText(this,"CPF Inválido, tente novamente...",Toast.LENGTH_LONG).show();

        }else {
            editCPF.setText(AppUtil.mascaraCPF(editCPF.getText().toString()));
        }

        if (TextUtils.isEmpty(editNomeCompleto.getText().toString())) {
            editNomeCompleto.setError("*");
            editNomeCompleto.requestFocus();
            retorno = false;
        }


        return retorno;

    }

    private void salvarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();

        dados.putString("cpf", editCPF.getText().toString());
        dados.putString("nomeCompleto", editNomeCompleto.getText().toString());
        dados.putInt("ultimoIDClientePessoaPF", ultimoIDClientePessoaPF);
        dados.apply();

    }

    private void restaurarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        isPessoaFisica = preferences.getBoolean("pessoaFisica", true);
        clienteID = preferences.getInt("clienteID", -1);
    }
}
