package mundotela.net.coletapreco.domain;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import livroandroid.lib.utils.HttpHelper;
import mundotela.net.coletapreco.db.MensagemDB;
import mundotela.net.coletapreco.util.Pref_Salva;
import mundotela.net.coletapreco.util.TempAux;

import static android.R.attr.id;

/**
 * Created by Henrique on 07/03/2017.
 */

public class RecebeMensagemService {

    private static final boolean LOG_ON = true;
    private static  final String TAG = "RecebendoMensagem";
    private static final String URL = "http://ifspcoletor.mundotela.net/app/json_mensagem.php";
    private static final String URLIMG = "http://ifspcoleta.mundotela.net/uploads/";



    // cria umA LISTA VINDA DO BANCO DO CELULAR
    public static List<RecebeMensagem> getBanco(Context context) {

        try {


            MensagemDB db = new MensagemDB(context);

            List<RecebeMensagem> c = db.findAll();
            return c;

        }catch (Exception e) {
            Log.e("Erro banco : " + e.getMessage(), String.valueOf(e));
            return null;
        }


    }


    //puxa lista da Internet
    public static void buscaJson(Context context) {

        try {

            Map<String,String> post = new HashMap<String,String>();
            post.put("codUser", Pref_Salva.getUser(context,"chave"));
            post.put("status", "1");

            String json = HttpHelper.doPost(URL,post,"UTF-8");
            parseJSON(context,json);


        }catch (Exception e) {
            Log.e(TAG + e.getMessage(), String.valueOf(e));

        }
    }


    private static List<RecebeMensagem> parseJSON(Context context, String json) throws IOException {


        MensagemDB db = new MensagemDB(context);

        List<RecebeMensagem> mensagem = new ArrayList<RecebeMensagem>();
        try {
            db.delete();
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonMensagens = obj.getJSONArray("carro");
            //insere produtos vindos da Web no banco local
            for (int i = 0; i < jsonMensagens.length();i++){
                JSONObject jsonlistaMensagem = jsonMensagens.getJSONObject(i);
                RecebeMensagem c = new RecebeMensagem();
                c.mensagem = jsonlistaMensagem.optString("mensagem");
                c.nomeColeta = jsonlistaMensagem.optString("nomeColeta");
                c.nomeCidade = jsonlistaMensagem.optString("nomeCidade");
                c.nomeSuper = jsonlistaMensagem.optString("nomeSuper");
                c.endereco = jsonlistaMensagem.optString("endereco");
                c.dataInicial = jsonlistaMensagem.optString("dataInicial");
                c.urlFoto = URLIMG+jsonlistaMensagem.optString("urlFoto");
                c.dataFinal = jsonlistaMensagem.optString("dataFinal");
                c.codSuper = jsonlistaMensagem.optString("codSuper");
                c.codColeta = jsonlistaMensagem.optString("codColeta");
                if(LOG_ON) {
                    Log.d(TAG,"lista : "+ c.mensagem+ " > " + c.nomeCidade + " > " +
                            c.nomeColeta+ " > " + c.endereco+ " > "+ " > " + c.nomeSuper
                            + c.dataInicial+ " > " + c.dataFinal+ " > " + c.urlFoto+ " > " + c.codSuper+ " > " + c.codColeta);
                }
                mensagem.add(c);
                //TempAux.totalJson_coleta = mensagem.size();
                try {

                    db.save(c);
                    Log.d("DB","salvou");

                }finally {
                    db.close();
                }
            }
            if(LOG_ON) {
                Log.d(TAG,mensagem.size() + "encontrados.");
            }

        } catch (JSONException e) {
            throw new IOException(e.getMessage(),e);
        }

        return mensagem;
    }










}
