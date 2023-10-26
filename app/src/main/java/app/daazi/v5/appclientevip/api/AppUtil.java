package app.daazi.v5.appclientevip.api;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.InputMismatchException;

/**
 * Classe de apoio contendo métodos que podem
 * ser reutilizados em todo o projeto
 *
 * Criada por Marco Maddo - 01/2020
 *
 * Versão v2
 */
public class AppUtil {

    public static final int TIME_SPLASH = 3000;
    public static final String PREF_APP = "app_cliente_vip_pref";
    public static final String LOG_APP = "CLIENTE_LOG";

    public static final String URL_IMG_BACKGROUD = "http://bit.ly/daaziImgBackground";
    public static final String URL_IMG_LOGO = "http://bit.ly/daaziImgLogo";

    /**
     *
     * @return devolve a data atual
     */
    public static String getDataAtual(){

        String dia, mes, ano;

        String dataAtual = "00/00/0000";

        try {

            Calendar calendar = Calendar.getInstance();

            dia = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            mes = String.valueOf(calendar.get(Calendar.MONTH)+1);
            ano = String.valueOf(calendar.get(Calendar.YEAR));

           // p1 : p2 : p2

            dia = (Calendar.DAY_OF_MONTH<10) ? "0"+dia : dia;

            dia = (dia.length()>2) ? dia.substring(1,3) : dia;

            int mesAtual = (Calendar.MONTH)+1;

            mes = (mesAtual<=9) ? "0"+mes : mes;

            mes = (mes.length()>2) ? mes.substring(1,3) : mes;

            dataAtual = dia+"/"+mes+"/"+ano;

            return dataAtual;


        }catch (Exception e){


        }

        return dataAtual;

    }

    /**
     *
     * @return devolve a hora atual
     */
    public static String getHoraAtual(){

        String horaAtual = "00:00:00";

        String hora, minuto, segudo;

        try {

            Calendar calendar = Calendar.getInstance();

            int iHora = calendar.get(Calendar.HOUR_OF_DAY);
            int iMinuto = calendar.get(Calendar.MINUTE);
            int iSegundo = calendar.get(Calendar.SECOND);


            hora = (iHora <= 9) ? "0"+iHora : Integer.toString(iHora);
            minuto = (iMinuto <= 9) ? "0"+iMinuto : Integer.toString(iMinuto);
            segudo = (iSegundo <= 9) ? "0"+iSegundo : Integer.toString(iSegundo);

            horaAtual = hora+":"+minuto+":"+segudo;

            return horaAtual;


        }catch (Exception e){

        }

        return horaAtual;

    }


    public static boolean isCNPJ(String CNPJ) {
        if (CNPJ == null || CNPJ.length() != 14) {
            return false;
        }

        int[] digits = new int[14];
        for (int i = 0; i < 14; i++) {
            digits[i] = Character.getNumericValue(CNPJ.charAt(i));
        }

        // Calcula o primeiro dígito verificador
        int sum = 0;
        int weight = 5;
        for (int i = 0; i < 12; i++) {
            sum += digits[i] * weight;
            weight--;
            if (weight < 2) {
                weight = 9;
            }
        }
        int remainder = sum % 11;
        int digit13 = (remainder < 2) ? 0 : 11 - remainder;

        // Calcula o segundo dígito verificador
        sum = 0;
        weight = 6;
        for (int i = 0; i < 13; i++) {
            sum += digits[i] * weight;
            weight--;
            if (weight < 2) {
                weight = 9;
            }
        }
        remainder = sum % 11;
        int digit14 = (remainder < 2) ? 0 : 11 - remainder;

        return digits[12] == digit13 && digits[13] == digit14;
    }

    public static boolean isCPF(String CPF) {
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {

            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {

                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48);


            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return (true);
            else return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static String mascaraCNPJ(String CNPJ) {

        String retorno;

        retorno =(CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "." +
                CNPJ.substring(5, 8) + "." + CNPJ.substring(8, 12) + "-" +
                CNPJ.substring(12, 14));

        return  retorno;
    }

    public static String mascaraCPF(String CPF) {
        return (CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." +
                CPF.substring(6, 9) + "-" + CPF.substring(9, 11));
    }

    /**
     * Gerar senha criptografada com MD5.
     * @param password
     * @return
     */
    public static String gerarMD5Hash(String password) {

        String retorno = "";

        if(!password.isEmpty()) {

            retorno = "falhou";

            try {
                // Create MD5 Hash
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(password.getBytes());
                byte messageDigest[] = digest.digest();

                StringBuffer MD5Hash = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++) {
                    String h = Integer.toHexString(0xFF & messageDigest[i]);
                    while (h.length() < 2)
                        h = "0" + h;
                    MD5Hash.append(h);
                }

                return MD5Hash.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
        return retorno;
    }

}
