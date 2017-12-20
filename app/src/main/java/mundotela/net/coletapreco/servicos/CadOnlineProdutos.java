package mundotela.net.coletapreco.servicos;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import livroandroid.lib.utils.HttpHelper;
import mundotela.net.coletapreco.db.CadProdudoDB;

/**
 * Created by Henrique on 31/08/2016.
 */
public class CadOnlineProdutos extends AsyncTask<Void,Void,String> {

    private ProgressBar _pb;
    private String codProduto;
    private String produto;
    private String marca;
    private String tipo;
    private String cesta;
    private String urlFoto;
    private String URL;
    private String chave;
    private String codUser;
    private TextView _txt;
    private Context context;

    @Override
    protected void onPreExecute() {
        _pb.setVisibility(View.VISIBLE);
        _txt.setText("Enviando Dados...");

        super.onPreExecute();
    }

    public CadOnlineProdutos(Context context,ProgressBar _pb, TextView _txt, String cesta, String chave, String codProduto, String codUser, String marca, String produto, String tipo, String URL, String urlFoto) {
        this._pb = _pb;
        this._txt = _txt;
        this.cesta = cesta;
        this.chave = chave;
        this.codProduto = codProduto;
        this.codUser = codUser;
        this.marca = marca;
        this.produto = produto;
        this.tipo = tipo;
        this.URL = URL;
        this.urlFoto = urlFoto;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {

        String retorno=null;

        Map<String,String> post = new HashMap<String,String>();

        post.put("chave",chave);
        post.put("codProduto",codProduto);
        post.put("produto",produto);
        post.put("marca",marca);
        //post.put("tipo",tipo);
       // post.put("cesta",cesta);
        post.put("foto",urlFoto);
        post.put("codUser",codUser);





        try {

            retorno = HttpHelper.doPost(URL,post,"UTF-8");

        }catch (IOException e) {
            Log.e(URL,e.getMessage(),e);
            return null;
        }

        return retorno;
    }

    @Override
    protected void onPostExecute(String s) {
        _pb.setVisibility(View.INVISIBLE);
        if(s.equals("CADASTRADO_COM_SUCESSO")){
            _txt.setText(s);
            try {
                CadProdudoDB db = new CadProdudoDB(context);
                db.deleteByCod(codProduto);
            }catch (Exception e) {
                Log.e("erroB",e.getMessage());
                _txt.setText("Erro no Banco do celeular...");
            }


        }else if(s.equals("VALOR_JA_EXISTE")){
            _txt.setText("Esse Código já está cadastrado");
        }else {
            _txt.setText("ERRO SERVIDOR");
        }

        super.onPostExecute(s);
    }


}
