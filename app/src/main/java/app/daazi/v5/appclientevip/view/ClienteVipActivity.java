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
import app.daazi.v5.appclientevip.controller.ClienteController;
import app.daazi.v5.appclientevip.model.Cliente;

public class ClienteVipActivity extends AppCompatActivity {

    // Declarar Objetos

    Cliente novoVip;
    ClienteController controller;

    private SharedPreferences preferences;

    // 1º passo a partir do Layout

    EditText editPrimeiroNome, editSobreNome;
    CheckBox chPessoaFisica;
    Button btnSalvarContinuar, btnCancelar;

    boolean isFormularioOK, isPessoaFisica;

    int ultimoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_vip_card);

        initFormulario();

        btnSalvarContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFormularioOK = validarFormulario()) {

                    novoVip.setPrimeiroNome(editPrimeiroNome.getText().toString());
                    novoVip.setSobreNome(editSobreNome.getText().toString());
                    novoVip.setPessoaFisica(isPessoaFisica);

                    controller.incluir(novoVip);

                    ultimoID = controller.getUltimoID();

                    salvarSharedPreferences();

                    Intent intent = new Intent(ClienteVipActivity.this,
                            ClientePessoaFisicaActivity.class);
                    startActivity(intent);


                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new FancyAlertDialog.Builder(ClienteVipActivity.this)
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

        editPrimeiroNome = findViewById(R.id.editPrimeiroNome);
        editSobreNome = findViewById(R.id.editSobreNome);
        chPessoaFisica = findViewById(R.id.chPessoaFisica);

        btnSalvarContinuar = findViewById(R.id.btnSalvarContinuar);
        btnCancelar = findViewById(R.id.btnCancelar);

        isFormularioOK = false;

        novoVip = new Cliente();

        controller = new ClienteController(this);

        restaurarSharedPreferences();

    }

    private boolean validarFormulario() {

        boolean retorno = true;

        if (TextUtils.isEmpty(editPrimeiroNome.getText().toString())) {
            editPrimeiroNome.setError("*");
            editPrimeiroNome.requestFocus();
            retorno = false;
        }

        if (TextUtils.isEmpty(editSobreNome.getText().toString())) {
            editSobreNome.setError("*");
            editSobreNome.requestFocus();
            retorno = false;
        }


        return retorno;
    }

    public void pessoaFisica(View view) {

        isPessoaFisica = chPessoaFisica.isChecked();

    }

    private void salvarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();

        dados.putString("primeiroNome", novoVip.getPrimeiroNome());
        dados.putString("sobreNome", novoVip.getSobreNome());
        dados.putBoolean("pessoaFisica", novoVip.isPessoaFisica());
        dados.putInt("clienteID", ultimoID);
        dados.apply();

    }

    private void restaurarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);

    }

}
