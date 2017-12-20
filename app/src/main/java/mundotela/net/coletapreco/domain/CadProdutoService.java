package mundotela.net.coletapreco.domain;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import livroandroid.lib.utils.HttpHelper;
import mundotela.net.coletapreco.db.CadProdudoDB;

/**
 * Created by Henrique on 06/09/2016.
 */
public class CadProdutoService {

    private static final boolean LOG_ON = true;
    private static  final String TAG = "catServServico";
    //private static final String URL = "http://app.mundotela.net/teste/teste.json";

    public static List<CadProduto> getcat(Context context, String tipo) {

        try {
            //String json = readFile(context,tipo);
            //String json = HttpHelper.doGet(URL);
            CadProdudoDB db = new CadProdudoDB(context);

            List<CadProduto> cat = db.findAll();
            return cat;

        }catch (Exception e) {
            Log.e("Erro banco : " + e.getMessage(), String.valueOf(e));
            return null;
        }


    }

    /*private static List<CadProduto> parseJSON(Context context, String json) throws IOException{
        List<CadProduto> cat = new ArrayList<CadProduto>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonCats = obj.getJSONArray("carro");
            //insere cada carro na lista
            for (int i = 0; i < jsonCats.length();i++){
                JSONObject jsonCat = jsonCats.getJSONObject(i);
                CadProduto c = new CadProduto();
                c.id = jsonCat.optString("id");
                c.tipo = jsonCat.optString("tipo");
                c.nome = jsonCat.optString("nome");
                c.urlFoto = jsonCat.optString("urlFoto");
                if(LOG_ON) {
                    Log.d(TAG,"CAT : " + c.nome + " > " + c.urlFoto);
                }
                cat.add(c);
            }
            if(LOG_ON) {
                Log.d(TAG,cat.size() + "encontrados.");
            }

        } catch (JSONException e) {
            throw new IOException(e.getMessage(),e);
        }

        return cat;
    }*/

    /*private static String readFile(Context context, String tipo)  throws IOException{
        if("s".equals(tipo)){
            return FileUtils.readRawFileString(context, R.raw.teste,"UTF-8");
        }
        return null;
    }*/
}
