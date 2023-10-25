package app.daazi.v5.appclientevip.datamodel;

// MOR - Modelo Objeto Relacional - SQLserver, Oracle, Postgress

public class ClientePFDataModel {

    /**
     *     private int fk;
     *     private String cpf;
     *     private String nomeCompleto;
     */

    public static final String TABELA = "clientePF";

    public static final String ID = "id";
    public static final String FK = "clienteID";
    public static final String CPF = "cpf";
    public static final String NOME_COMPLETO = "nomeCompleto";

    private static final String DATA_INC = "datainc";
    private static final String DATA_ALT = "dataalt";

    private static String query;

    /**
     * CREATE TABLE clientePF (
     *     id           INTEGER PRIMARY KEY AUTOINCREMENT,
     *     clienteID    INTEGER,
     *     cpf          TEXT,
     *     nomeCompleto TEXT,
     *     datainc      TEXT,
     *     dataalt      TEXT,
     *     FOREIGN KEY (
     *         clienteID
     *     )
     *     REFERENCES cliente (id)
     * );
     * @return
     */

    public static String gerarTabela() {
        query = "CREATE TABLE " + TABELA + " ( ";
        query += ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        query += FK + " INTEGER, ";
        query += CPF + " TEXT, ";
        query += NOME_COMPLETO + " TEXT, ";
        query += DATA_INC + " datetime default current_timestamp, ";
        query += DATA_ALT + " datetime default current_timestamp, ";
        query += "FOREIGN KEY(" + FK + ") REFERENCES cliente(id) ";
        query += ")";
        return query;
    }

}
