package app.daazi.v5.appclientevip.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.daazi.v5.appclientevip.R;
import app.daazi.v5.appclientevip.api.ClienteAdapter;
import app.daazi.v5.appclientevip.controller.ClienteController;
import app.daazi.v5.appclientevip.model.Cliente;

public class ConsultarClientesActivity extends AppCompatActivity {

    List<Cliente> clientes;

    ClienteAdapter adapter;

    Cliente obj;
    ClienteController controller;

    RecyclerView rvClientesVip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_clientes);

        rvClientesVip = findViewById(R.id.rvClientesVip);

        controller = new ClienteController(getApplicationContext());

        clientes = controller.listar();

        adapter = new ClienteAdapter(clientes, getApplicationContext());

        rvClientesVip.setAdapter(adapter);

        rvClientesVip.setLayoutManager(new LinearLayoutManager(this));

    }
}
