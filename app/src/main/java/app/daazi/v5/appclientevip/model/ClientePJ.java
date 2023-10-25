package app.daazi.v5.appclientevip.model;

public class ClientePJ extends ClientePF{

    private int id;
    private int clientePFID;
    private String cnpj;
    private String razaoSocial;
    private String dataAbertura;
    private boolean simplesNacional;
    private boolean mei;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getClientePFID() {
        return clientePFID;
    }

    public void setClientePFID(int clientePFID) {
        this.clientePFID = clientePFID;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public boolean isSimplesNacional() {
        return simplesNacional;
    }

    public void setSimplesNacional(boolean simplesNacional) {
        this.simplesNacional = simplesNacional;
    }

    public boolean isMei() {
        return mei;
    }

    public void setMei(boolean mei) {
        this.mei = mei;
    }
}
