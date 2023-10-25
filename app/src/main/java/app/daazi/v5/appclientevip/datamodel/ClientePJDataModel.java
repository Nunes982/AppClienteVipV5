package app.daazi.v5.appclientevip.datamodel;

// MOR - Modelo Objeto Relacional - SQLserver, Oracle, Postgress

public class ClientePJDataModel {

    /**
     *     private int fk;
     *     private String cnpj;
     *     private String razaoSocial;
     *     private String dataAbertura;
     *     private boolean simplesNacional;
     *     private boolean mei;
     */


    public static final String TABELA = "clientePJ";

    public static final String ID = "id";
    public static final String FK = "clientePFID";
    public static final String CNPJ = "cnpj";
    public static final String RAZAO_SOCIAL = "razao_social";
    public static final String DATA_ABERTURA = "dataAbertura";
    public static final String SIMPLES_NACIONAL = "simplesNacional";
    public static final String MEI = "mei";

    private static final String DATA_INC = "datainc";
    private static final String DATA_ALT = "dataalt";

    private static String query;


    /**
     * CREATE TABLE clientePJ (
     *     id              INTEGER PRIMARY KEY AUTOINCREMENT,
     *     clientePFID     INTEGER,
     *     cnpj            TEXT,
     *     razao_social    TEXT,
     *     dataAbertura    TEXT,
     *     simplesNacional INTEGER,
     *     mei             INTEGER,
     *     datainc         TEXT,
     *     dataalt         TEXT,
     *     FOREIGN KEY (
     *         clientePFID
     *     )
     *     REFERENCES clientePF (id)
     * );
     * @return
     */

    public static String gerarTabela() {
        query = "CREATE TABLE " + TABELA + " ( ";
        query += ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        query += FK + " INTEGER, ";
        query += CNPJ + " TEXT, ";
        query += RAZAO_SOCIAL + " TEXT, ";
        query += DATA_ABERTURA + " TEXT, ";
        query += SIMPLES_NACIONAL + " INTEGER, ";
        query += MEI + " INTEGER, ";
        query += DATA_INC + " datetime default current_timestamp, ";
        query += DATA_ALT + " datetime default current_timestamp, ";
        query += "FOREIGN KEY(" + FK + ") REFERENCES clientePF(id) ";
        query += ")";
        return query;
    }


}
