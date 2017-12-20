package mundotela.net.coletapreco.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import mundotela.net.coletapreco.domain.CadColeta;

import static android.R.attr.id;

/**
 * Created by Henrique on 21/09/2016.
 */

public class CadColetaDB extends SQLiteOpenHelper{

    private static final String TAG = "sql_CC_DB";
    public static final String NOME_BANCO = "cad_coleta.sqlite";
    private static final int VERSAO_BANCO = 1;

    public CadColetaDB(Context context) { super(context,NOME_BANCO,null,VERSAO_BANCO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG,"criando a tabela cadColetas");
        db.execSQL("create table if not exists cadcoleta (_id integer primary key autoincrement, cod text,cod_coletax text, valor text, data text);");
        Log.d(TAG,"A tabela foi criada");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } //Insere novo registro ou atualiza
    public long save(CadColeta coleta,int temCod) {
        long id = coleta.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("cod",coleta.cod_produto);
            values.put("valor",coleta.valor_coleta);
            values.put("data",coleta.data_coleta);
            values.put("cod_coletax",coleta.cod_coletax);



            if(temCod > 0){
                String _id = String.valueOf(coleta.cod_produto);
                String[] whereArgs = new String[]{_id};
                //update produto set values = ... where _id=?
                int count = db.update("cadcoleta",values,"cod=?",whereArgs);

                Log.d("CadColeta","feito update");
                return count;


            }else {
                //insert into produto values (...)
                id = db.insert("cadcoleta","",values);
                Log.d("CadColeta","feito insert");
                return id;
            }


        }finally {
            db.close();
        }
    }


    //deleta registro

    public int delete(CadColeta coleta){
        SQLiteDatabase db = getWritableDatabase();
        try {
            //delete from cadcoleta where _id=?
            int count = db.delete("cadcoleta","_id=?",new String[]{String.valueOf(coleta.id)});
            Log.i(TAG,"Deletou [ "+ count + "] registros.");
            return count;
        }finally {
            db.close();
        }
    }

    //deleta todos os registros
    public int deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("cadcoleta",null,null);

            Log.i(TAG,"Deletou [ " + count + " ] registros");
            return count;

        }finally {
            db.close();
        }
    }

    //consulta a lista com todos os registros
    public List<CadColeta> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from cadproduto
            Cursor c = db.query("cadcoleta",null,null,null,null,null,"cod_coletax",null);
            return toList(c);
        }finally {
            db.close();
        }

    }

    //consulta a lista por codigo
    public List<CadColeta> findbyCod(String cod){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from listaproduto where cod = ?;
            Cursor c = db.query("cadcoleta",null,"cod='"+cod+"'",null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }

    //consulta a lista por codigo
    public List<CadColeta> findbyCodCol(String cod){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from listaproduto where cod = ?;
            Cursor c = db.query("cadcoleta",null,"cod_coletax='"+cod+"'",null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }

    private List<CadColeta> toList(Cursor c) {
        List<CadColeta> coletas = new ArrayList<CadColeta>();
        if (c.moveToFirst()){
            do{
                CadColeta coleta = new CadColeta();
                coletas.add(coleta);
                //Recupera os atributos do produto
                coleta.id = c.getLong(c.getColumnIndex("_id"));
                coleta.cod_produto = c.getString(c.getColumnIndex("cod"));
                coleta.valor_coleta = c.getString(c.getColumnIndex("valor"));
                coleta.data_coleta = c.getString(c.getColumnIndex("data"));
                coleta.cod_coletax = c.getString(c.getColumnIndex("cod_coletax"));

            }while (c.moveToNext());
        }

        return coletas;
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



