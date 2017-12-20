package mundotela.net.coletapreco.servicos;

import android.content.Context;
import android.os.AsyncTask;
import mundotela.net.coletapreco.db.ListaColetaDB;
import mundotela.net.coletapreco.domain.ListaColetaService;

/**
 * Created by Henrique on 12/09/2016.
 */
public class AtaskColeta extends AsyncTask<Void,Void,Void> {

    Context context;

    public AtaskColeta(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        ListaColetaDB db = new ListaColetaDB(context);
        db.delete();

        ListaColetaService.buscaJson(context);
        return null;

    }
}
