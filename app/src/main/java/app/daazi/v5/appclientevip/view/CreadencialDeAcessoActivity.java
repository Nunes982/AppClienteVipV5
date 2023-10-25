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

public class CreadencialDeAcessoActivity extends AppCompatActivity {

    // 1º Passo

    Button btnCadastro;

    EditText editNome;
    EditText editEmail;
    EditText editSenhaA;
    EditText editSenhaB;

    CheckBox ckTermo;

    boolean isFormularioOK, isPessoaFisica;

    private SharedPreferences preferences;

    Cliente cliente;
    ClienteController controller;

    int clienteID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_card);

        // 2º Passo

        initFormulario();

        // 3º obter o evento do botão cadastrar

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 4º validar os dados digitados

                isFormularioOK = true;

                if (TextUtils.isEmpty(editNome.getText().toString())) {
                    editNome.setError("*");
                    editNome.requestFocus();
                    isFormularioOK = false;
                }

                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    editEmail.setError("*");
                    editEmail.requestFocus();
                    isFormularioOK = false;
                }

                if (TextUtils.isEmpty(editSenhaA.getText().toString())) {
                    editSenhaA.setError("*");
                    editSenhaA.requestFocus();
                    isFormularioOK = false;
                }

                if (TextUtils.isEmpty(editSenhaB.getText().toString())) {
                    editSenhaB.setError("*");
                    editSenhaB.requestFocus();
                    isFormularioOK = false;
                }

                if (!ckTermo.isChecked()) {

                    isFormularioOK = false;

                }

                // 5º depois de tudo ok, mudar de tela

                if (isFormularioOK) {

                    if (!validarSenha()) {

                        editSenhaA.setError(("*"));
                        editSenhaB.setError(("b"));
                        editSenhaA.requestFocus();

                        new FancyAlertDialog.Builder(CreadencialDeAcessoActivity.this)
                                .setTitle("ATENÇÃO!")
                                .setBackgroundColor(Color.parseColor("#303F9F"))
                                .setMessage("As senhas digitadas não são iguais, por favor tente novamente.")
                                .setPositiveBtnText("CONTINUAR")
                                .setPositiveBtnBackground(Color.parseColor("#4ECA25"))
                                .isCancellable(true)
                                .setIcon(R.mipmap.ic_launcher_round, Icon.Visible)
                                .OnPositiveClicked(new FancyAlertDialogListener() {
                                    @Override
                                    public void OnClick() {

                                    }
                                })
                                .build();

                    } else {

                        cliente.setEmail(editEmail.getText().toString());
                        cliente.setSenha(AppUtil.gerarMD5Hash(editSenhaA.getText().toString()));

                        controller.alterar(cliente);

                        salvarSharedPreferences();

                        Intent iMenuPrincipal = new Intent(CreadencialDeAcessoActivity.this, LoginActivity.class);
                        startActivity(iMenuPrincipal);
                        finish();
                    }

                }

            }
        });
    }


    private void initFormulario() {

        btnCadastro = findViewById(R.id.btnCadastro);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editSenhaA = findViewById(R.id.editSenhaA);
        editSenhaB = findViewById(R.id.editSenhaB);

        ckTermo = findViewById(R.id.ckTermo);

        isFormularioOK = false;

        cliente = new Cliente();
        controller = new ClienteController(this);

        restaurarSharedPreferences();

    }

    public void validarTermo(View view) {

        if (!ckTermo.isChecked()) {

            Toast.makeText(getApplicationContext(),
                    "É nessário aceitar os termos de uso para continuar o cadastro...",
                    Toast.LENGTH_LONG).show();
        }

    }

    public boolean validarSenha() {

        boolean retorno = false;

        int senhaA, senhaB;

        senhaA = Integer.parseInt(editSenhaA.getText().toString());
        senhaB = Integer.parseInt(editSenhaB.getText().toString());

        retorno = (senhaA == senhaB);


        return retorno;


    }

    private void salvarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();

        dados.putString("email",editEmail.getText().toString());
        dados.putString("senha",AppUtil.gerarMD5Hash(editSenhaA.getText().toString()));
        dados.apply();

    }

    private void restaurarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);

        // Objeto Cliente
        clienteID = preferences.getInt("clienteID", -1);
        String primeiroNome = preferences.getString("primeiroNome", "");
        String sobreNome = preferences.getString("sobreNome", "");
        isPessoaFisica = preferences.getBoolean("pessoaFisica", true);

        cliente.setId(clienteID);
        cliente.setPrimeiroNome(primeiroNome);
        cliente.setSobreNome(sobreNome);
        cliente.setPessoaFisica(isPessoaFisica);

        if (isPessoaFisica)
            editNome.setText(preferences.getString("nomeCompleto", "Verifique os dados"));
        else
            editNome.setText(preferences.getString("razaoSocial", "Verifique os dados"));


    }
}
