package mundotela.net.coletapreco.domain;

import android.content.Context;
import android.util.Log;

import java.util.List;

import mundotela.net.coletapreco.db.CadColetaDB;

/**
 * Created by Henrique on 21/09/2016.
 */

public class CadColetaService {

    private static final boolean LOG_ON = true;
    private static  final String TAG = "CadColetaServico";

    public static List<CadColeta> getBanco(Context context) {

      try {
          CadColetaDB db = new CadColetaDB(context);

          List<CadColeta> lista = db.findAll();
          return lista;

      }catch(Exception e){

          Log.e("Erro banco : " + e.getMessage(), String.valueOf(e));
          return null;
      }


    }
}
