package app.daazi.v5.appclientevip.controller;


import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.Nullable;

import java.util.List;

import app.daazi.v5.appclientevip.api.AppDataBase;
import app.daazi.v5.appclientevip.datamodel.ClientePJDataModel;
import app.daazi.v5.appclientevip.model.ClientePJ;

public class ClientePJController extends AppDataBase {

    private static final String TABELA = ClientePJDataModel.TABELA;
    private ContentValues dados;

    public ClientePJController(@Nullable Context context) {
        super(context);
    }

    public boolean incluir(ClientePJ obj){

        dados = new ContentValues();

        // TODO: Pessoa Física ID
        dados.put(ClientePJDataModel.FK, obj.getClientePFID());
        dados.put(ClientePJDataModel.CNPJ, obj.getCnpj());
        dados.put(ClientePJDataModel.RAZAO_SOCIAL, obj.getRazaoSocial());
        dados.put(ClientePJDataModel.DATA_ABERTURA, obj.getDataAbertura());
        dados.put(ClientePJDataModel.SIMPLES_NACIONAL, obj.isSimplesNacional());
        dados.put(ClientePJDataModel.MEI, obj.isMei());

        return insert(TABELA, dados);
    }

    public boolean alterar(ClientePJ obj){

        dados = new ContentValues();

        dados.put(ClientePJDataModel.ID, obj.getId());
        dados.put(ClientePJDataModel.FK, obj.getClienteID());
        dados.put(ClientePJDataModel.CNPJ, obj.getCnpj());
        dados.put(ClientePJDataModel.RAZAO_SOCIAL, obj.getRazaoSocial());
        dados.put(ClientePJDataModel.DATA_ABERTURA, obj.getDataAbertura());
        dados.put(ClientePJDataModel.SIMPLES_NACIONAL, obj.isSimplesNacional());
        dados.put(ClientePJDataModel.MEI, obj.isMei());

        return update(TABELA, dados);

    }

    public boolean deletar(ClientePJ obj){

        return delete(TABELA, obj.getId());
    }

    public List<ClientePJ> listar(){

        return listClientesPessoaJuridica(TABELA);
    }

    public int getUltimoID(){

        return getLastPK(TABELA);
    }

    public ClientePJ getClientePJByFK(int idFK){

        // idPK é a chave primária da tabela Cliente (id)

        return getClientePJByFK(ClientePJDataModel.TABELA, idFK);

    }

}
