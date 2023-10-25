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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.squareup.picasso.Picasso;

import app.daazi.v5.appclientevip.R;
import app.daazi.v5.appclientevip.api.AppUtil;
import app.daazi.v5.appclientevip.controller.ClienteController;
import app.daazi.v5.appclientevip.model.Cliente;

public class LoginActivity extends AppCompatActivity {

    // Declarar Objetos

    Cliente cliente;

    private SharedPreferences preferences;

    // 1º passo a partir do Layout

    TextView txtRecuperarSenha, txtLerPolitica, btnSejaVip;
    EditText editEmail, editSenha;
    CheckBox chLembrar;
    Button btnAcessar;

    ImageView imgBackground, imgLogo;

    boolean isFormularioOK, isLembrarSenha;

    ClienteController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_novo);

        initFormulario();

        loadImagens();

        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFormularioOK = validarFormulario()) {

                    if (validarDadosDoUsuario()) {

                        salvarSharedPreferences();

                        Intent intent =
                                new Intent(LoginActivity.this,
                                        MainActivity.class);

                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Verifique os dados....", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnSejaVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(LoginActivity.this,
                                ClienteVipActivity.class);

                startActivity(intent);
                finish();

            }
        });


        txtRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "Carregando tela de recuperação de Senha...",
                        Toast.LENGTH_LONG).show();
                ;
            }
        });

        txtLerPolitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FancyAlertDialog.Builder(LoginActivity.this)
                        .setTitle("Política de Privacidade & Termos de Uso")
                        .setBackgroundColor(Color.parseColor("#303F9F"))
                        .setMessage("texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto")
                        .setNegativeBtnText("Discordo")
                        .setNegativeBtnBackground(Color.parseColor("#FF4081"))
                        .setPositiveBtnText("Concordo")
                        .setPositiveBtnBackground(Color.parseColor("#4ECA25"))
                        .isCancellable(true)
                        .setIcon(R.mipmap.ic_launcher_round, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getApplicationContext(), "Obrigado, seja bem vindo e conclua o seu cadastro para usar o aplicativo...", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getApplicationContext(), "Lamentamos, mas é necessário concordar com a política de privacidade e termos de uso para se cadastrar no aplicativo...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .build();

            }
        });


    }

    private boolean validarFormulario() {

        boolean retorno = true;

        if (TextUtils.isEmpty(editEmail.getText().toString())) {
            editEmail.setError("*");
            editEmail.requestFocus();
            retorno = false;
        }

        if (TextUtils.isEmpty(editSenha.getText().toString())) {
            editSenha.setError("*");
            editSenha.requestFocus();
            retorno = false;
        }

        return retorno;
    }

    private void initFormulario() {

        txtRecuperarSenha = findViewById(R.id.txtRecurarSenha);
        txtLerPolitica = findViewById(R.id.txtLerPolitica);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        chLembrar = findViewById(R.id.ckLembrar);
        btnAcessar = findViewById(R.id.btnAcessar);
        btnSejaVip = findViewById(R.id.btnSejaVip);

        imgBackground = findViewById(R.id.imgBackground);
        imgLogo = findViewById(R.id.imgLogo);

        isFormularioOK = false;

        controller = new ClienteController(getApplicationContext());

        cliente = new Cliente();


        cliente.setId(20);
        //cliente.setPrimeiroNome("Nome "+i);
        //cliente.setSobreNome("Sobre nome "+i);
        //cliente.setEmail(i+"@teste.com");
        //cliente.setSenha(i+"_12345");
        //cliente.setPessoaFisica(false);

        // controller.incluir(cliente);

        // controller.alterar(cliente);
        controller.deletar(cliente);

        // List<Cliente> clientes = controller.listar();

        restaurarSharedPreferences();


    }

    private  void loadImagens(){


        Picasso.get()
                .load(AppUtil.URL_IMG_BACKGROUD)
                .placeholder(R.drawable.carregando_animacao)
                .into(imgBackground);

        Picasso.get().load(AppUtil.URL_IMG_LOGO).placeholder(R.drawable.carregando_animacao).into(imgLogo);

    }

    public void lembrarSenha(View view) {

        isLembrarSenha = chLembrar.isChecked();

    }

    // TODO: Implementar Validação senha MD5
    public boolean validarDadosDoUsuario() {

        boolean retorno = false;
        //comparar as senhas

        String senhaDigitadaPura = editSenha.getText().toString();
        String senhaMD5 = cliente.getSenha();

        if (senhaMD5.equals(AppUtil.gerarMD5Hash(senhaDigitadaPura))){
            retorno = true;
        }

        return retorno;

    }

    private void salvarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();

        dados.putBoolean("loginAutomatico", isLembrarSenha);
        dados.putString("emailCliente", editEmail.getText().toString());
        dados.apply();

    }

    private void restaurarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);

        cliente.setEmail(preferences.getString("email", "teste@teste.com"));
        cliente.setSenha(preferences.getString("senha", "12345"));
        cliente.setPrimeiroNome(preferences.getString("primeiroNome", "Cliente"));
        cliente.setSobreNome(preferences.getString("sobreNome", "Fake"));
        cliente.setPessoaFisica(preferences.getBoolean("pessoaFisica", true));

        isLembrarSenha = preferences.getBoolean("loginAutomatico", false);

    }
}
