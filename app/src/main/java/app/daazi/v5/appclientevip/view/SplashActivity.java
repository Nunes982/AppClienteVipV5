package app.daazi.v5.appclientevip.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import app.daazi.v5.appclientevip.R;
import app.daazi.v5.appclientevip.api.AppUtil;

public class SplashActivity extends AppCompatActivity {

    // Use qualquer número
    public static final int APP_PERMISSOES = 2020;

    // Array String com a lista de permissões necessárias
    String[] permissoesRequeridas = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private SharedPreferences preferences;

    boolean isLembrarSenha = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        salvarSharedPreferences();
        restaurarSharedPreferences();

        if (validarPermissoes()) {

            iniciarAplicativo();

        } else {

            Toast.makeText(this, "As permissões para o app Cliente Vip estão pendentes... ", Toast.LENGTH_LONG).show();
            finish();

        }

    }

    private void iniciarAplicativo() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;

                if (isLembrarSenha) {

                    intent = new Intent(SplashActivity.this, MainActivity.class);

                } else {

                    intent = new Intent(SplashActivity.this, LoginActivity.class);

                }

                startActivity(intent);
                finish();
            }
        }, AppUtil.TIME_SPLASH);
    }

    private void salvarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();

    }

    private void restaurarSharedPreferences() {

        preferences = getSharedPreferences(AppUtil.PREF_APP, MODE_PRIVATE);
        isLembrarSenha = preferences.getBoolean("loginAutomatico", false);

    }

    // métodos para ativar permissões

    private boolean validarPermissoes() {

        int result;
        // Array para verificar se as permissões forão autorizadas
        List<String> permissoesRequeridas = new ArrayList<>();

        // Adiciona as permissões que o app necessita
        for (String permissao : this.permissoesRequeridas) {
            result = ContextCompat.checkSelfPermission(SplashActivity.this, permissao);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissoesRequeridas.add(permissao);
            }
        }

        // Caso o array não esteja vazio, significa que tem permissões
        // para serem autorizadas
        if (!permissoesRequeridas.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), APP_PERMISSOES);
            return false;
        } else {
            iniciarAplicativo();
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case APP_PERMISSOES: {
                if (grantResults.length > 0) {
                    String permissoesNegadasPeloUsuario = "";

                    for (String permissao : permissoesRequeridas) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissoesNegadasPeloUsuario += "\n" + permissao;
                        }
                    }

                    if (permissoesNegadasPeloUsuario.length() > 0) {

                        Toast.makeText(SplashActivity.this, "Permissões negadas pelo usuário.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(SplashActivity.this, "Todas as Permissões autorizadas pelo usuário.", Toast.LENGTH_LONG).show();

                        iniciarAplicativo();
                        break;
                    }
                }

            }
            default:
                iniciarAplicativo();
                break;
        }
    }

}
