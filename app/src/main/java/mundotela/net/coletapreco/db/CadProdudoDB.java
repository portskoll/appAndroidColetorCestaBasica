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


/**
 * Created by Henrique on 07/09/2016.
 */
public class CadProdudoDB extends SQLiteOpenHelper{

    private static final String TAG = "sql_CP_DB";
    public static final String NOME_BANCO = "cad_prod.sqlite";
    private static final int VERSAO_BANCO = 1;

    public CadProdudoDB(Context context) {
        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"criando a tabela cadProdutos");
        db.execSQL("create table if not exists cadproduto (_id integer primary key autoincrement,cod text, cod_col text, marca text,foto_file text, foto_url text);");
        Log.d(TAG,"A tabela foi criada");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




    //Insere novo registro ou atualiza
    public long save(CadProduto produto,int temCod) {
        long id = produto.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("cod",produto.cod);
            //values.put("nome",produto.nome);
            values.put("cod_col",produto.cod_col);
            values.put("marca",produto.marca);
            //values.put("tipo",produto.tipo);
            //values.put("cesta",produto.cesta);
            values.put("foto_file",produto.foto_file);
            values.put("foto_url",produto.foto_url);



            if(temCod > 0){
                String _id = String.valueOf(produto.cod);
                String[] whereArgs = new String[]{_id};
                //update produto set values = ... where _id=?
               // int count = db.update("cadproduto",values,"_id=?",whereArgs);
                int count = db.update("cadproduto",values,"cod=?",whereArgs);

                Log.d("CadP","feito update");
                return count;


            }else {
                //insert into produto values (...)
                id = db.insert("cadproduto","",values);
                Log.d("CadP","feito insert");
                return id;
            }


        }finally {
            db.close();
        }
    }

    //deleta todos os registros
    public int deleteByCod(String cod) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("cadproduto","cod=?",new String[]{cod});

            Log.i(TAG,"Deletou [ " + count + " ] registros");
            return count;

        }finally {
            db.close();
        }
    }

    //consulta a lista com todos os registros
    public List<CadProduto> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from cadproduto
            Cursor c = db.query("cadproduto",null,null,null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }

    //consulta a lista por codigo
    public List<CadProduto> findbyCod(String cod){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from listaproduto where cod = ?;
            Cursor c = db.query("cadproduto",null,"cod='"+cod+"'",null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }



    private List<CadProduto> toList(Cursor c) {
        List<CadProduto> produtos = new ArrayList<CadProduto>();
        if (c.moveToFirst()){
            do{
                CadProduto produto = new CadProduto();
               produtos.add(produto);
                //Recupera os atributos do produto
                produto.id = c.getLong(c.getColumnIndex("_id"));
                produto.cod = c.getString(c.getColumnIndex("cod"));
                produto.cod_col = c.getString(c.getColumnIndex("cod_col"));
                //produto.nome = c.getString(c.getColumnIndex("nome"));
                produto.marca = c.getString(c.getColumnIndex("marca"));
                //produto.tipo = c.getString(c.getColumnIndex("tipo"));
                //produto.cesta = c.getString(c.getColumnIndex("cesta"));
                produto.foto_file = c.getString(c.getColumnIndex("foto_file"));
                produto.foto_url = c.getString(c.getColumnIndex("foto_url"));

            }while (c.moveToNext());
        }

        return produtos;
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
