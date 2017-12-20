package mundotela.net.coletapreco.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import mundotela.net.coletapreco.domain.RecebeMensagem;

/**
 * Created by Henrique on 07/03/2017.
 */

public class MensagemDB extends SQLiteOpenHelper{

    private static final String TAG = "sql_MEM_DB";
    public static final String NOME_BANCO = "mensagem.sqlite";
    private static final int VERSAO_BANCO = 1;

    public MensagemDB(Context context) {
        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG,"criando a tabela Mensagem");
        db.execSQL("create table if not exists mensagemDB (_id integer primary key autoincrement,mensagem text,endereco text,urlFoto text,nomeColeta text,nomeSuper text, nomeCidade text, dataInicial text, dataFinal text, codSuper text, codColeta text);");
        Log.d(TAG,"A tabela foi criada");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    //Insere novo registro ou atualiza
    public long save(RecebeMensagem recebeMensagem) {
        long id = recebeMensagem.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("mensagem",recebeMensagem.mensagem);
            values.put("endereco",recebeMensagem.endereco);
            values.put("urlFoto",recebeMensagem.urlFoto);
            values.put("nomeColeta",recebeMensagem.nomeColeta);
            values.put("nomeCidade",recebeMensagem.nomeCidade);
            values.put("nomeSuper",recebeMensagem.nomeSuper);
            values.put("dataInicial",recebeMensagem.dataInicial);
            values.put("dataFinal",recebeMensagem.dataFinal);
            values.put("codSuper",recebeMensagem.codSuper);
            values.put("codColeta",recebeMensagem.codColeta);




            if(id != 0){
                String _id = String.valueOf(recebeMensagem.id);
                String[] whereArgs = new String[]{_id};
                //update produto set values = ... where _id=?
                int count = db.update("mensagemDB",values,"_id=?",whereArgs);
                return count;


            }else {
                //insert into produto values (...)
                id = db.insert("mensagemDB","",values);
                return id;
            }


        }finally {
            db.close();
        }
    }

    //consulta a lista com todos os registros
    public List<RecebeMensagem> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //select * from mensagemDB
            Cursor c = db.query("mensagemDB",null,null,null,null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }

    }


    private List<RecebeMensagem> toList(Cursor c) {
        List<RecebeMensagem> recebeMensagems = new ArrayList<RecebeMensagem>();
        if (c.moveToFirst()){
            do{
                RecebeMensagem recebeMensagem = new RecebeMensagem();
                recebeMensagems.add(recebeMensagem);
                //Recupera os atributos da mensagem
                recebeMensagem.id = c.getLong(c.getColumnIndex("_id"));
                recebeMensagem.mensagem = c.getString(c.getColumnIndex("mensagem"));
                recebeMensagem.endereco = c.getString(c.getColumnIndex("endereco"));
                recebeMensagem.urlFoto = c.getString(c.getColumnIndex("urlFoto"));
                recebeMensagem.nomeColeta = c.getString(c.getColumnIndex("nomeColeta"));
                recebeMensagem.nomeCidade = c.getString(c.getColumnIndex("nomeCidade"));
                recebeMensagem.nomeSuper = c.getString(c.getColumnIndex("nomeSuper"));
                recebeMensagem.dataInicial = c.getString(c.getColumnIndex("dataInicial"));
                recebeMensagem.dataFinal = c.getString(c.getColumnIndex("dataFinal"));
                recebeMensagem.codColeta = c.getString(c.getColumnIndex("codColeta"));
                recebeMensagem.codSuper = c.getString(c.getColumnIndex("codSuper"));

            }while (c.moveToNext());
        }

        return recebeMensagems;
    }

    //deleta todos os registros
    public int delete() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("mensagemDB",null,null);

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
