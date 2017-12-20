package mundotela.net.coletapreco.servicos;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import livroandroid.lib.utils.HttpHelper;
import mundotela.net.coletapreco.util.Pref_Salva;

/**
 * Created by Henrique on 25/08/2016.
 */
public class VerificaCadColetor  extends AsyncTask<Void,Void,String>{

    private Context context;
    private ProgressBar _pb;
    private TextView _txt;
    private String email;
    private String URL;
    private String chave;



    public VerificaCadColetor(Context context,ProgressBar _pb, TextView _txt, String chave, String email, String URL) {
        this.context = context;
        this._pb = _pb;
        this._txt = _txt;
        this.chave = chave;
        this.email = email;
        this.URL = URL;
    }

    public String retorno_post (String s) {

        return s;
    }

    @Override
    protected void onPreExecute() {
        _pb.setVisibility(View.VISIBLE);
        _txt.setText("Enviando Dados...");

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        String retorno=null;

        Map<String,String> post = new HashMap<String,String>();
        post.put("chave",chave);
        post.put("email_pront",email);

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

        Log.d("RetCAD",s);
        int id_retorno = Integer.parseInt(s);
        if(id_retorno > 0){
            Pref_Salva.setUser(context,"chave",s);
            Pref_Salva.setAtivo(context,"chave","ATIVO");
            _txt.setText("OK");

        }else if(s.equals("0")){
            _txt.setText("ALUNO NÂO AUTORIZADO");
        }else {
            _txt.setText("ERRO SERVIDOR");
        }

        super.onPostExecute(s);
    }
}
