package app.daazi.v5.appclientevip.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.ArrayList;
import java.util.List;

import app.daazi.v5.appclientevip.R;
import app.daazi.v5.appclientevip.api.AppUtil;
import app.daazi.v5.appclientevip.controller.ClienteController;
import app.daazi.v5.appclientevip.controller.ClientePFController;
import app.daazi.v5.appclientevip.controller.ClientePJController;
import app.daazi.v5.appclientevip.model.Cliente;
import app.daazi.v5.appclientevip.model.ClientePF;

public class MainActivity extends AppCompatActivity {


    Cliente cliente;
    ClientePF clientePF;
    ClienteController controller;
    ClientePFController controllerPF;
    ClientePJController controllerPJ;

    TextView txtNomeCliente;

    private SharedPreferences preferences;

    List<Cliente> clientes;
    List<String> cidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFormulario();

    }

    private void initFormulario() {

        cliente = new Cliente();
        controller = new ClienteController(this);
        controllerPF = new ClientePFController(this);
        controllerPJ = new ClientePJController(this);
        txtNomeCliente = findViewById(R.id.txtNomeCliente);

        restaurarSharedPreferences();

        txtNomeCliente.setText("Bem vindo, "+ cliente.getPrimeiroNome());
    }

    public void meusDados(View view) {

       Intent intent = new Intent(MainActivity.this, MeusDadosActivity.class);
       startActivity(intent);

    }

    public void atualizarMeusDados(View view) {

        Intent intent = new Intent(MainActivity.this, AtualizarMeusDadosActivity.class);
        startActivity(intent);

    }

    public void excluirMinhaConta(View view) {

        new FancyAlertDialog.Builder(MainActivity.this)
                .setTitle("EXCLUIR SUA CONTA")
                .setBackgroundColor(Color.parseColor("#303F9F"))
                .setMessage("Confirma EXCLUSÃO definitiva da sua conta do aplicativo?")
                .setNegativeBtnText("RETORNAR")
                .setNegativeBtnBackground(Color.parseColor("#FF4081"))
                .setPositiveBtnText("SIM")
                .setPositiveBtnBackground(Color.parseColor("#4ECA25"))
                .isCancellable(true)
                .setIcon(R.mipmap.ic_launcher_round, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                        cliente.setClientePF(controllerPF.getClientePFByFK(cliente.getId()));

                        if (!cliente.isPessoaFisica()){

                            cliente.setClientePJ(controllerPJ.getClientePJByFK(clientePF.getId()));
                            controllerPJ.deletar(cliente.getClientePJ());

                        }

                        controllerPF.deletar(cliente.getClientePF());
                        controller.deletar(cliente);

                        salvarSharedPreferences();

                        Toast.makeText(getApplicationContext(), cliente.getPrimeiroNome() + ", sua conta foi excluída, esperamos que retorne em breve...", Toast.LENGTH_SHORT).show();

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getApplicationContext(), cliente.getPrimeiroNome() + ", divirta-se com as opções do aplicativo...", Toast.LENGTH_SHORT).show();

                    }
                })
                .build();
    }

    private void buscarListaDeClientes() {

        cidades = new ArrayList<>();


        clientes = new ArrayList<>();


        cidades.add("Brasília");
        cidades.add("Campo Grande");
        cidades.add("São Paulo");
        cidades.add("Curitiba");

        for (int i = 0; i < 10; i++) {


            cliente = new Cliente();
            cliente.setPrimeiroNome("Cliente nº "+i);

            clientes.add(cliente);

        }


        for (String obj: cidades) {

            Log.i(AppUtil.LOG_APP,"Obj: "+obj);

        }

    }

    public void consultarClientesVip(View view) {

        Intent intent = new Intent(MainActivity.this, ConsultarClientesActivity.class);
        startActivity(intent);
    }

    public void sairDoAplicativo(View view) {

        new FancyAlertDialog.Builder(MainActivity.this)
                .setTitle("SAIR DO APLICATIVO")
                .setBackgroundColor(Color.parseColor("#303F9F"))
                .setMessage("Confirma sua saída do aplicativo?")
                .setNegativeBtnText("RETORNAR")
                .setNegativeBtnBackground(Color.parseColor("#FF4081"))
                .setPositiveBtnText("SIM")
                .setPositiveBtnBackground(Color.parseColor("#4ECA25"))
                .isCancellable(true)
                .setIcon(R.mipmap.ic_launcher_round, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getApplicationContext(), cliente.getPrimeiroNome() + ", volte sempre e obrigado...", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(getApplicationContext(), cliente.getPrimeiroNome() + ", divirta-se com as opções do aplicativo...", Toast.LENGTH_SHORT).show();

                    }
                })
                .build();

    }

    private void salvarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();

        dados.clear();
        dados.apply();

    }

    private void restaurarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);

        cliente.setPrimeiroNome(preferences.getString("primeiroNome", "Madoo"));
        cliente.setSobreNome(preferences.getString("sobreNome", "NULO"));
        cliente.setEmail(preferences.getString("email", "NULO"));
        cliente.setSenha(preferences.getString("senha", "NULO"));
        cliente.setPessoaFisica(preferences.getBoolean("pessoaFisica", true));
        cliente.setId(preferences.getInt("clienteID", -1));

    }
}
