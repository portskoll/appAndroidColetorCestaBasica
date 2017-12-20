package mundotela.net.coletapreco.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mundotela.net.coletapreco.domain.CadProduto;
import mundotela.net.coletapreco.domain.ListaColeta;

/**
 * Created by Henrique on 12/09/2016.
 */
public class ListaColetaDB extends SQLiteOpenHelper {

    private static final String TAG = "sql_CP_DB";
    public static final String NOME_BANCO = "listacoleta.sqlite";
    private static final int VERSAO_BANCO = 1;

    public ListaColetaDB(Context context) {
        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"criando a tabela ListaColeta");
        db.execSQL("create table if not exists listacoleta (_id integer primary key autoincrement,cod_coleta text,tipo_coleta text,cesta_coleta text,produto text, qtde_t text, qtde_f text);");
        Log.d(TAG,"A tabela foi criada");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




    //Insere novo registro ou atualiza
    public long save(ListaColeta coleta) {
        long id = coleta.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("produto",coleta.produto);
            values.put("cod_coleta",coleta.cod_coleta);
            values.put("tipo_coleta",coleta.tipo_coleta);
            values.put("cesta_coleta",coleta.cesta_coleta);
            values.put("qtde_t",coleta.qtde_t);
            values.put("qtde_f",coleta.qtde_f);



            if(id != 0){
                String _id = String.valueOf(coleta.id);
                String[] whereArgs = new String[]{_id};
                //update produto set values = ... where _id=?
                int count = db.update("listacoleta",values,"_id=?",whereArgs);
                return count;


            }else {
                //insert into produto values (...)
                id = db.insert("listacoleta","",values);
                return id;
            }


        }finally {
            db.close();
        }
    }

    //consulta a lista com todos os registros
    public List<ListaColeta> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from cadproduto
            Cursor c = db.query("listacoleta",null,null,null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }

    //consulta a lista por codigo
    public List<ListaColeta> findbyCod(String cod){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from cadproduto where cod = ?;
            Cursor c = db.query("listacoleta",null,"cod_coleta ='"+cod+"'",null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }

    //deleta todos os registros
    public int delete() {
        SQLiteDatabase db = getWritableDatabase();
        try {
           int count = db.delete("listacoleta",null,null);

            Log.i(TAG,"Deletou [ " + count + " ] registros");
            return count;

        }finally {
            db.close();
        }
    }


    private List<ListaColeta> toList(Cursor c) {
        List<ListaColeta> listaColetas = new ArrayList<ListaColeta>();
        if (c.moveToFirst()){
            do{
                ListaColeta listaColeta = new ListaColeta();
                listaColetas.add(listaColeta);
                //Recupera os atributos da categoria
                listaColeta.id = c.getLong(c.getColumnIndex("_id"));
                listaColeta.produto = c.getString(c.getColumnIndex("produto"));
                listaColeta.cod_coleta = c.getString(c.getColumnIndex("cod_coleta"));
                listaColeta.tipo_coleta = c.getString(c.getColumnIndex("tipo_coleta"));
                listaColeta.cesta_coleta = c.getString(c.getColumnIndex("cesta_coleta"));
                listaColeta.qtde_t = c.getString(c.getColumnIndex("qtde_t"));
                listaColeta.qtde_f = c.getString(c.getColumnIndex("qtde_f"));


            }while (c.moveToNext());
        }

        return listaColetas;
    }





    //Executa um sql
    public void execSQL(String sql){
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        }finally {
            db.close();
        }
    }

    //Executa um sql
    public void execSQL(String sql, Object[] args){
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql,args);
        }finally {
            db.close();
        }
    }


}