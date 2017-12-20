package mundotela.net.coletapreco.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mundotela.net.coletapreco.domain.ListaColeta;
import mundotela.net.coletapreco.domain.ListaProduto;

/**
 * Created by Henrique on 16/09/2016.
 */

public class ListaProdutoDB extends SQLiteOpenHelper {
    private static final String TAG = "sql_produto_DB";
    public static final String NOME_BANCO = "listaproduto.sqlite";
    private static final int VERSAO_BANCO = 1;

    public ListaProdutoDB(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "criando a tabela ListaProduto");
        db.execSQL("create table if not exists listaproduto (_id integer primary key autoincrement,codProduto text,urlFotoPoduto text,cod_listaColeta text,marca_nome text);");
        Log.d(TAG, "A tabela foi criada");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //Insere novo registro ou atualiza
    public long save(ListaProduto produto) {
        long id = produto.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("codProduto", produto.codProduto);
            values.put("urlFotoPoduto", produto.urlFotoProduto);
            values.put("cod_listaColeta", produto.cod_listaColeta);
            values.put("marca_nome", produto.marca_nome);


            if (id != 0) {
                String _id = String.valueOf(produto.id);
                String[] whereArgs = new String[]{_id};
                //update produto set values = ... where _id=?
                int count = db.update("listaproduto", values, "_id=?", whereArgs);
                return count;


            } else {
                //insert into produto values (...)
                id = db.insert("listaproduto", "", values);
                return id;
            }


        } finally {
            db.close();
        }
    }


    //consulta a lista com todos os registros
    public List<ListaProduto> findAll() {
        SQLiteDatabase db = getWritableDatabase();

        try {
            //select * from cadproduto
            Cursor c = db.query("listaproduto", null, null, null, null, null, null, null);

            return toList(c);
        } finally {
            db.close();
        }

    }




    //consulta a lista por codigo
    public List<ListaProduto> findbyCod(String cod){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from listaproduto where cod = ?;
            Cursor c = db.query("listaproduto",null,"codProduto ='"+cod+"'",null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }


    private List<ListaProduto> toList(Cursor c) {
        List<ListaProduto> listaProdutos = new ArrayList<ListaProduto>();

        if (c.moveToFirst()) {
            do {
                ListaProduto listaproduto = new ListaProduto();
                listaProdutos.add(listaproduto);
                //Recupera os atributos da categoria
                listaproduto.id = c.getLong(c.getColumnIndex("_id"));
                listaproduto.codProduto = c.getString(c.getColumnIndex("codProduto"));
                listaproduto.urlFotoProduto = c.getString(c.getColumnIndex("urlFotoPoduto"));
                listaproduto.cod_listaColeta = c.getString(c.getColumnIndex("cod_listaColeta"));
                listaproduto.marca_nome = c.getString(c.getColumnIndex("marca_nome"));

            } while (c.moveToNext());
        }

        return listaProdutos;
    }

    //deleta todos os registros
    public int delete() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("listaproduto", null, null);

            Log.i(TAG, "Deletou [ " + count + " ] registros");
            return count;

        } finally {
            db.close();
        }
    }


    //Executa um sql
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    //Executa um sql
    public void execSQL(String sql, Object[] args) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, args);
        } finally {
            db.close();
        }
    }
}