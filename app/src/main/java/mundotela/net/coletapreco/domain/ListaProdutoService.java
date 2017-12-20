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
import mundotela.net.coletapreco.db.ListaProdutoDB;
import mundotela.net.coletapreco.util.TempAux;

/**
 * Created by Henrique on 17/09/2016.
 */
public class ListaProdutoService {

    private static final boolean LOG_ON = true;
    private static  final String TAG = "ServicoProduto";
    private static final String URL = "http://ifspcoletor.mundotela.net/app/json_lista_produto.php";


    // cria umA LISTA VINDA DO BANCO DO CELULAR
    public static List<ListaProduto> getBanco(Context context) {

        try {

            ListaProdutoDB db = new ListaProdutoDB(context);

            List<ListaProduto> c = db.findAll();
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


    private static List<ListaProduto> parseJSON(Context context, String json) throws IOException {


        ListaProdutoDB db = new ListaProdutoDB(context);


        List<ListaProduto> cat = new ArrayList<ListaProduto>();
        try {
            db.delete();
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonCats = obj.getJSONArray("carro");
            //insere produtos vindos da Web no banco local
            for (int i = 0; i < jsonCats.length();i++){
                JSONObject jsonlistaProduto = jsonCats.getJSONObject(i);
                ListaProduto c = new ListaProduto();
                c.codProduto = jsonlistaProduto.optString("codProduto");
                c.marca_nome = jsonlistaProduto.optString("marca_nome");
                c.cod_listaColeta = jsonlistaProduto.optString("cod_listaColeta");
                c.urlFotoProduto = jsonlistaProduto.optString("urlFotoProduto");

                if(LOG_ON) {
                    Log.d(TAG,"lista : "+ c.codProduto + " > " + c.marca_nome );
                }
                cat.add(c);
                TempAux.totalJson_produto = cat.size();
                try {

                    db.save(c);
                    Log.d("DB","salvou");

                }finally {
                    db.close();
                }
            }
            if(LOG_ON) {
                Log.d(TAG,cat.size() + " encontrados.");
            }

        } catch (JSONException e) {
            throw new IOException(e.getMessage(),e);
        }

        return cat;
    }
}
