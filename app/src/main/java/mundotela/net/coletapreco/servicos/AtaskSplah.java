package mundotela.net.coletapreco.servicos;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mundotela.net.coletapreco.db.ListaColetaDB;
import mundotela.net.coletapreco.domain.ListaColeta;

/**
 * Created by Henrique on 14/09/2016.
 */
public class AtaskSplah extends AsyncTask<Void,Void,Void>{


    Context context;

    public AtaskSplah(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ListaColetaDB db = new ListaColetaDB(context);
        List<ListaColeta> c = db.findbyCod("29");
        String p = c.get(0).produto;
        String t = c.get(0).tipo_coleta;
        String ce = c.get(0).cesta_coleta;

        Log.d("busca koll",p + " " + t +" "+ ce);

        return null;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
