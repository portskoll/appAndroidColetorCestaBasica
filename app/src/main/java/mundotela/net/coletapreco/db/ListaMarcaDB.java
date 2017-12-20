package mundotela.net.coletapreco.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import mundotela.net.coletapreco.domain.ListaMarca;
import mundotela.net.coletapreco.util.ListaCatMarcas;

/**
 * Created by Henrique on 15/09/2016.
 */
public class ListaMarcaDB extends SQLiteOpenHelper{

    private static final String TAG = "sql_marcas_DB";
    public static final String NOME_BANCO = "listamarca.sqlite";
    private static final int VERSAO_BANCO = 1;

    public ListaMarcaDB(Context context) {
        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"criando a tabela ListaMarca");
        db.execSQL("create table if not exists listamarca (_id integer primary key autoincrement,cod_marca text,marca text);");
        Log.d(TAG,"A tabela foi criada");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //Insere novo registro ou atualiza
    public long save(ListaMarca marca) {
        long id = marca.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("cod_marca",marca.cod_marca);
            values.put("marca",marca.marca);




            if(id != 0){
                String _id = String.valueOf(marca.id);
                String[] whereArgs = new String[]{_id};
                //update marca set values = ... where _id=?
                int count = db.update("listamarca",values,"_id=?",whereArgs);
                return count;


            }else {
                //insert into marca values (...)
                id = db.insert("listamarca","",values);
                return id;
            }


        }finally {
            db.close();
        }
    }


    //consulta a lista com todos os registros
    public List<ListaMarca> findAll(){
        SQLiteDatabase db = getWritableDatabase();

        try{
            //select * from cadproduto
            Cursor c = db.query("listamarca",null,null,null,null,null,null,null);

            return toList(c);
        }finally {
            db.close();
        }

    }


    private List<ListaMarca> toList(Cursor c) {
        List<ListaMarca> listaMarcas = new ArrayList<ListaMarca>();
        int i = 0;
        if (c.moveToFirst()){
            do{
                ListaMarca listaMarca = new ListaMarca();
                listaMarcas.add(listaMarca);
                //Recupera os atributos da categoria
                listaMarca.id = c.getLong(c.getColumnIndex("_id"));
                listaMarca.cod_marca = c.getString(c.getColumnIndex("cod_marca"));
                listaMarca.marca = c.getString(c.getColumnIndex("marca"));

            }while (c.moveToNext());
        }

        return listaMarcas;
    }

    //deleta todos os registros
    public int delete() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("listamarca",null,null);

            Log.i(TAG,"Deletou [ " + count + " ] registros");
            return count;

        }finally {
            db.close();
        }
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
