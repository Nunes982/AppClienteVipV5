package app.daazi.v5.appclientevip.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import app.daazi.v5.appclientevip.controller.ClientePFController;
import app.daazi.v5.appclientevip.controller.ClientePJController;
import app.daazi.v5.appclientevip.datamodel.ClienteDataModel;
import app.daazi.v5.appclientevip.datamodel.ClientePFDataModel;
import app.daazi.v5.appclientevip.datamodel.ClientePJDataModel;
import app.daazi.v5.appclientevip.model.Cliente;
import app.daazi.v5.appclientevip.model.ClientePF;
import app.daazi.v5.appclientevip.model.ClientePJ;

public class AppDataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "clienteDB.sqlite";
    private static final int DB_VERSION = 1;

    Cursor cursor;

    SQLiteDatabase db;

    Context context;

    public AppDataBase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;

        db = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar as tabelas

        try {

            // Executar o que desejamos
            db.execSQL(ClienteDataModel.gerarTabela());

            Log.i(AppUtil.LOG_APP, "TB Cliente: " + ClienteDataModel.gerarTabela());

        } catch (SQLException e) {

            // Capturar o erro
            Log.e(AppUtil.LOG_APP, "Erro TB Cliente: " + e.getMessage());
            Log.e(AppUtil.LOG_APP, "Erro TB Cliente: " + ClienteDataModel.gerarTabela());

        }

        try {

            // Executar o que desejamos
            db.execSQL(ClientePFDataModel.gerarTabela());

            Log.i(AppUtil.LOG_APP, "TB ClientePF: " + ClientePFDataModel.gerarTabela());

        } catch (SQLException e) {

            // Capturar o erro
            Log.e(AppUtil.LOG_APP, "Erro TB ClientePF: " + e.getMessage());

        }

        try {

            // Executar o que desejamos
            db.execSQL(ClientePJDataModel.gerarTabela());

            Log.i(AppUtil.LOG_APP, "TB ClientePJ: " + ClientePJDataModel.gerarTabela());

        } catch (SQLException e) {

            // Capturar o erro
            Log.e(AppUtil.LOG_APP, "Erro TB ClientePJ: " + e.getMessage());

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizar o banco de dados
        // tabelas
    }


    /**
     * Incluir dados no banco de dados
     *
     * @return
     */
    public boolean insert(String tabela, ContentValues dados) {

        boolean sucesso = true;

        try {

            Log.i(AppUtil.LOG_APP, tabela + " insert() executado com sucesso. ");

            sucesso = db.insert(tabela, null, dados) > 0;

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, tabela + " falhou ao executar o insert(): " + e.getMessage());
        }

        return sucesso;
    }

    /**
     * Deletar dados no banco de dados
     *
     * @return
     */
    public boolean delete(String tabela, int id) {

        boolean sucesso = true;

        try {

            Log.i(AppUtil.LOG_APP, tabela + " delete() executado com sucesso. ");

            sucesso = db.delete(tabela, "id=?", new String[]{Integer.toString(id)}) > 0;

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, tabela + " falhou ao executar o delete(): " + e.getMessage());
        }

        return sucesso;
    }

    /**
     * Atualizar dados no banco de dados
     *
     * @return
     */
    public boolean update(String tabela, ContentValues dados) {

        boolean sucesso = true;

        try {

            int id = dados.getAsInteger("id");

            Log.i(AppUtil.LOG_APP, tabela + " update() executado com sucesso. ");

            sucesso = db.update(tabela, dados, "id=?", new String[]{Integer.toString(id)}) > 0;

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, tabela + " falhou ao executar o update(): " + e.getMessage());
        }

        return sucesso;
    }

    public List<Cliente> listClientes(String tabela) {

        List<Cliente> list = new ArrayList<>();

        Cliente cliente;
        ClientePFController controllerPF = new ClientePFController(context);
        ClientePJController controllerPJ = new ClientePJController(context);

        // Select no banco de dados
        // SELECT * FROM tabela

        String sql = "SELECT * FROM " + tabela;

        try {

            cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {

                do {

                    cliente = new Cliente();

                    cliente.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClienteDataModel.ID)));
                    cliente.setPrimeiroNome(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.PRIMEIRO_NOME)));
                    cliente.setSobreNome(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.SOBRE_NOME)));
                    cliente.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.EMAIL)));
                    cliente.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.SENHA)));
                    cliente.setPessoaFisica(cursor.getInt(cursor.getColumnIndexOrThrow(ClienteDataModel.PESSOA_FISICA)) == 1);


                    cliente.setClientePF(controllerPF.getClientePFByFK(cliente.getId()));

                    if (!cliente.isPessoaFisica())
                        cliente.setClientePJ(controllerPJ.getClientePJByFK(cliente.getClientePF().getId()));

                    list.add(cliente);

                } while (cursor.moveToNext());

                Log.i(AppUtil.LOG_APP, tabela + " lista gerada com sucesso.");

            }

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, "Erro ao listar os dados: " + tabela);
            Log.e(AppUtil.LOG_APP, "Erro: " + e.getMessage());
        }

        return list;
    }

    public List<ClientePF> listClientesPessoaFisica(String tabela) {

        List<ClientePF> list = new ArrayList<>();

        ClientePF clientePF;

        String sql = "SELECT * FROM " + tabela;

        try {

            cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {

                do {

                    clientePF = new ClientePF();

                    clientePF.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePFDataModel.ID)));
                    clientePF.setClienteID(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePFDataModel.FK)));
                    clientePF.setNomeCompleto(cursor.getString(cursor.getColumnIndexOrThrow(ClientePFDataModel.NOME_COMPLETO)));
                    clientePF.setCpf(cursor.getString(cursor.getColumnIndexOrThrow(ClientePFDataModel.CPF)));

                    list.add(clientePF);

                } while (cursor.moveToNext());

                Log.i(AppUtil.LOG_APP, tabela + " lista gerada com sucesso.");

            }

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, "Erro ao listar os dados: " + tabela);
            Log.e(AppUtil.LOG_APP, "Erro: " + e.getMessage());
        }

        return list;
    }

    public List<ClientePJ> listClientesPessoaJuridica(String tabela) {

        List<ClientePJ> list = new ArrayList<>();

        ClientePJ clientePJ;

        String sql = "SELECT * FROM " + tabela;

        try {

            cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {

                do {

                    clientePJ = new ClientePJ();

                    clientePJ.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.ID)));
                    clientePJ.setClientePFID(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.FK)));
                    clientePJ.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow(ClientePJDataModel.CNPJ)));
                    clientePJ.setRazaoSocial(cursor.getString(cursor.getColumnIndexOrThrow(ClientePJDataModel.RAZAO_SOCIAL)));
                    clientePJ.setDataAbertura(cursor.getString(cursor.getColumnIndexOrThrow(ClientePJDataModel.DATA_ABERTURA)));

                    clientePJ.setSimplesNacional
                            (cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.SIMPLES_NACIONAL)) == 1);

                    clientePJ.setMei
                            (cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.MEI)) == 1);

                    list.add(clientePJ);

                } while (cursor.moveToNext());

                Log.i(AppUtil.LOG_APP, tabela + " lista gerada com sucesso.");

            }

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, "Erro ao listar os dados: " + tabela);
            Log.e(AppUtil.LOG_APP, "Erro: " + e.getMessage());
        }

        return list;
    }

    public int getLastPK(String tabela) {

        //SELECT seq FROM sqlite_sequence WHERE name="tabela"

        String sql = "SELECT seq FROM sqlite_sequence WHERE name='" + tabela + "'";

        try {

            Log.e(AppUtil.LOG_APP, "SQL RAW: " + sql);

            cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {

                do {

                    return cursor.getInt(cursor.getColumnIndexOrThrow("seq"));

                } while (cursor.moveToNext());

            }

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, "Erro recuoperando ultimo PK: " + tabela);
            Log.e(AppUtil.LOG_APP, "Erro: " + e.getMessage());
        }

        return -1;
    }

    public Cliente getClienteByID(String tabela, Cliente obj) {

        Cliente cliente = new Cliente();

        String sql = "SELECT * FROM " + tabela + " WHERE id = " + obj.getId();

        try {

            cursor = db.rawQuery(sql, null);

            if (cursor.moveToNext()) {

                cliente.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClienteDataModel.ID)));
                cliente.setPrimeiroNome(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.PRIMEIRO_NOME)));
                cliente.setSobreNome(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.SOBRE_NOME)));
                cliente.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.EMAIL)));
                cliente.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(ClienteDataModel.SENHA)));
                cliente.setPessoaFisica(cursor.getInt(cursor.getColumnIndexOrThrow(ClienteDataModel.PESSOA_FISICA)) == 1);

            }

        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, "Erro getClienteByID " + obj.getId());
            Log.e(AppUtil.LOG_APP, "Erro  " + e.getMessage());
        }

        return cliente;

    }

    public ClientePF getClientePFByFK(String tabela, int idFK) {

        ClientePF clientePF = new ClientePF();

        String sql = "SELECT * FROM " + tabela + " WHERE  " + ClientePFDataModel.FK + " = " + idFK;

        try {

            cursor = db.rawQuery(sql, null);

            if (cursor.moveToNext()) {

                clientePF.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePFDataModel.ID)));
                clientePF.setClienteID(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePFDataModel.FK)));
                clientePF.setNomeCompleto(cursor.getString(cursor.getColumnIndexOrThrow(ClientePFDataModel.NOME_COMPLETO)));
                clientePF.setCpf(cursor.getString(cursor.getColumnIndexOrThrow(ClientePFDataModel.CPF)));
                clientePF.setCpf("111.222.333-44");// CPF fake para obscurecer o real

            }


        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, "Erro getClientePFByFK " + idFK);
            Log.e(AppUtil.LOG_APP, "Erro  " + e.getMessage());
        }

        return clientePF;

    }


    public ClientePJ getClientePJByFK(String tabela, int idFK) {

        ClientePJ clientePJ = new ClientePJ();

        String sql = "SELECT * FROM " + tabela + " WHERE " + ClientePJDataModel.FK + " = " + idFK;

        try {

            cursor = db.rawQuery(sql, null);

            if (cursor.moveToNext()) {

                clientePJ.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.ID)));
                clientePJ.setClientePFID(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.FK)));
                clientePJ.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow(ClientePJDataModel.CNPJ)));
                clientePJ.setDataAbertura(cursor.getString(cursor.getColumnIndexOrThrow(ClientePJDataModel.DATA_ABERTURA)));
                clientePJ.setRazaoSocial(cursor.getString(cursor.getColumnIndexOrThrow(ClientePJDataModel.RAZAO_SOCIAL)));
                clientePJ.setSimplesNacional(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.SIMPLES_NACIONAL)) == 1);
                clientePJ.setMei(cursor.getInt(cursor.getColumnIndexOrThrow(ClientePJDataModel.MEI)) == 1);


            }


        } catch (SQLException e) {

            Log.e(AppUtil.LOG_APP, "Erro getClientePJByFK " + idFK);
            Log.e(AppUtil.LOG_APP, "Erro  " + e.getMessage());
        }

        return clientePJ;

    }

}
