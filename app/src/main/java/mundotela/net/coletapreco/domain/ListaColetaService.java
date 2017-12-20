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
import mundotela.net.coletapreco.db.ListaColetaDB;
import mundotela.net.coletapreco.util.TempAux;

/**
 * Created by Henrique on 12/09/2016.
 */
public class ListaColetaService {

    private static final boolean LOG_ON = true;
    private static  final String TAG = "ColetaServ";
    private static final String URL = "http://ifspcoletor.mundotela.net/app/json_lista_coleta.php";


    // cria umA LISTA VINDA DO BANCO DO CELULAR
    public static List<ListaColeta> getBanco(Context context) {

        try {

            ListaColetaDB db = new ListaColetaDB(context);

            List<ListaColeta> c = db.findAll();
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


    private static List<ListaColeta> parseJSON(Context context, String json) throws IOException {


        ListaColetaDB db = new ListaColetaDB(context);

        List<ListaColeta> cat = new ArrayList<ListaColeta>();
        try {
            db.delete();
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonCats = obj.getJSONArray("carro");
            //insere produtos vindos da Web no banco local
            for (int i = 0; i < jsonCats.length();i++){
                JSONObject jsonlistaColeta = jsonCats.getJSONObject(i);
                ListaColeta c = new ListaColeta();
                c.produto = jsonlistaColeta.optString("produto");
                c.cod_coleta = jsonlistaColeta.optString("id");
                c.tipo_coleta = jsonlistaColeta.optString("tipo");
                c.cesta_coleta = jsonlistaColeta.optString("cesta");
                c.qtde_t = jsonlistaColeta.optString("qtde");
                c.qtde_f = "0";
                if(LOG_ON) {
                    Log.d(TAG,"lista : "+ c.cod_coleta+ " > " + c.produto + " > " + c.qtde_t);
                }
                cat.add(c);
                TempAux.totalJson_coleta = cat.size();
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
