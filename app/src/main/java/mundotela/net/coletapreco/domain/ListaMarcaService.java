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
import mundotela.net.coletapreco.db.ListaMarcaDB;
import mundotela.net.coletapreco.util.TempAux;

/**
 * Created by Henrique on 12/09/2016.
 */
public class ListaMarcaService {

    private static final boolean LOG_ON = true;
    private static  final String TAG = "ServicoMarca";
    private static final String URL = "http://ifspcoletor.mundotela.net/app/json_lista_marca.php";


    // cria umA LISTA VINDA DO BANCO DO CELULAR
    public static List<ListaMarca> getBanco(Context context) {

        try {

            ListaMarcaDB db = new ListaMarcaDB(context);

            List<ListaMarca> c = db.findAll();
            return c;

        }catch (Exception e) {
            Log.e("Erro banco : " + e.getMessage(), String.valueOf(e));
            return null;
        }


    }


    //puxa lista da Internet
    public static void buscaJson(Context context) {

        try {

            String json = HttpHelper.doGet(URL);
            parseJSON(context,json);


        }catch (Exception e) {
            Log.e("Erro ao ler as cat : " + e.getMessage(), String.valueOf(e));

        }
    }


    private static List<ListaMarca> parseJSON(Context context, String json) throws IOException {


        ListaMarcaDB db = new ListaMarcaDB(context);


        List<ListaMarca> cat = new ArrayList<ListaMarca>();
        try {
            db.delete();
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonCats = obj.getJSONArray("carro");
            //insere produtos vindos da Web no banco local
            for (int i = 0; i < jsonCats.length();i++){
                JSONObject jsonlistaMarca = jsonCats.getJSONObject(i);
                ListaMarca c = new ListaMarca();
                c.cod_marca = jsonlistaMarca.optString("cod_marca");
                c.marca = jsonlistaMarca.optString("marca");

                if(LOG_ON) {
                    Log.d(TAG,"lista : "+ c.cod_marca+ " > " + c.marca );
                }
                cat.add(c);
                TempAux.totalJson_marca = cat.size();
                try {

                    db.save(c);
                    Log.d("DB","salvou");

                }finally {
                    db.close();
                }
            }
            if(LOG_ON) {
                Log.d(TAG,cat.size() + "encontrados.");
            }

        } catch (JSONException e) {
            throw new IOException(e.getMessage(),e);
        }

        return cat;
    }


}
