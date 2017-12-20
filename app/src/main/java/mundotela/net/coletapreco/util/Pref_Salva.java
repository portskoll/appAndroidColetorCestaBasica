package mundotela.net.coletapreco.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Henrique on 25/08/2016.
 */
public class Pref_Salva {

    //identificados do banco de dados desta preferencia
    public static final String PREF_ID1 = "ativacao";
    public static final String PREF_ID2 = "usuario";
    public static final String PREF_ID3 = "mensagem";




    public static void setAtivo(Context ctx,String chave,String valor) {

        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID1,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(chave,valor);
        editor.commit();
    }

    public static String getAtivo(Context ctx,String chave) {

        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID1,0);
        String s = pref.getString(chave,"PENDENTE");

        return s;
    }

    public static void setUser(Context ctx,String chave,String valor) {

        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID2,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(chave,valor);
        editor.commit();
    }

    public static String getUser(Context ctx,String chave) {

        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID2,0);
        String s = pref.getString(chave,"xxxxxxx");

        return s;
    }

    public static void setMem(Context ctx,String chave,String valor) {

        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID3,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(chave,valor);
        editor.commit();
    }

    public static String getMen(Context ctx,String chave) {

        SharedPreferences pref = ctx.getSharedPreferences(PREF_ID3,0);
        String s = pref.getString(chave,"0");

        return s;
    }


}
