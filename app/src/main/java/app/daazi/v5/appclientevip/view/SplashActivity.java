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
    public static final int APP_PERMISSOES = 2023;

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

        if (validarPermissoes()){

            iniciarAplicativo();

        }else {

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

                    intent = new Intent(
                            SplashActivity.this,
                            MainActivity.class
                    );

                } else {

                    intent
                            = new Intent(
                            SplashActivity.this,
                            LoginActivity.class
                    );

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
        // Lista para armazenar permissões que precisam ser solicitadas
        List<String> permissoesRequeridas = new ArrayList<>();

        // Verificar se as permissões são concedidas
        for (String permissao : this.permissoesRequeridas) {
            int result = ContextCompat.checkSelfPermission(SplashActivity.this, permissao);
            if (result != PackageManager.PERMISSION_GRANTED) {
                // Se a permissão não foi concedida, adicioná-la à lista de permissões para solicitar
                permissoesRequeridas.add(permissao);
            }
        }

        // Se houver permissões para solicitar, solicite-as
        if (!permissoesRequeridas.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]), APP_PERMISSOES);
            return false;
        }

        // Se nenhuma permissão precisa ser solicitada, retorne true
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case APP_PERMISSOES: {
                boolean todasAsPermissoesAutorizadas = true;

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        todasAsPermissoesAutorizadas = false;
                        Toast.makeText(SplashActivity.this, "Permissão negada: " + permissions[i], Toast.LENGTH_LONG).show();
                    }
                }

                if (todasAsPermissoesAutorizadas) {
                    Toast.makeText(SplashActivity.this, "Todas as permissões autorizadas pelo usuário.", Toast.LENGTH_LONG).show();
                    iniciarAplicativo();
                }
            }
        }
    }


}
