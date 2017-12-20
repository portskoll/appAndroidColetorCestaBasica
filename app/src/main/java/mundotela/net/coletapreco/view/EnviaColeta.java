package mundotela.net.coletapreco.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import livroandroid.lib.utils.AndroidUtils;
import livroandroid.lib.utils.HttpHelper;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.db.CadColetaDB;
import mundotela.net.coletapreco.db.ListaProdutoDB;
import mundotela.net.coletapreco.db.MensagemDB;
import mundotela.net.coletapreco.domain.ListaProduto;
import mundotela.net.coletapreco.util.Pref_Salva;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
//import static com.google.android.gms.analytics.internal.zzy.C;
//import static com.google.android.gms.analytics.internal.zzy.d;

public class EnviaColeta extends AppCompatActivity implements Runnable{

    private Handler h;
    private CadColetaDB db;
    private MensagemDB dbMem;
    private ProgressBar pb_item;
    private ProgressBar pb_total;
    private TextView status;
    private ImageView img;
    private Button btn_enviar;
    private String _cod;
    private String _data;
    private String _valor;
    private String _codUser;
    private String _verCod;
    private int TotalBanco;
    private int item = 0;
    private String chave = "30be91";
    private final String URL = "http://ifspcoletor.mundotela.net/app/cad_coleta.php";
    private android.support.v7.app.ActionBar actionBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envia_coleta);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Enviar Coleta");
        db = new CadColetaDB(this);
        TotalBanco =  db.findAll().size();
        dbMem = new MensagemDB(this);


        Log.d("Banco", String.valueOf(TotalBanco));
        img = (ImageView) findViewById(R.id.img_foto);
        status = (TextView) findViewById(R.id.txt_status_envia_coleta);
        status.setText("Registros a enviar : " + TotalBanco);
        btn_enviar = (Button) findViewById(R.id.btn_enviarColeta);
        pb_item = (ProgressBar) findViewById(R.id.pb_enviar_coleta_item);
        pb_item.setVisibility(View.INVISIBLE);
        pb_total = (ProgressBar) findViewById(R.id.pb_enviar_coleta_total);
        pb_total.setMax(TotalBanco);
        h = new Handler();
        _codUser = Pref_Salva.getUser(this,"chave");
        Log.d("USER",_codUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recycler_view, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            Intent it = new Intent(this,Splash.class);
            startActivity(it);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        try {
            String prod = db.findAll().get(item).cod_produto.toString();

            File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+prod+".jpg");
            if(file.exists()) {

                Bitmap b = decodeImageFile(file,200,200);
                img.setImageBitmap(b);
            }

            EnviarColeta taskColeta = new EnviarColeta(this);
            taskColeta.execute();

        }catch (Exception e) {
            status.setText("Nada a enviar...");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }





    }


    public void enviarColeta(View view) {

        if (AndroidUtils.isNetworkAvailable(this)){
            h.post(this);
        }else {
            status.setText("Sem conexão com a Internet.");
        }



    }

    //metodo para tratar o bitmap para não dar estouro de memoria
    public static Bitmap decodeImageFile(File f, int WIDTH, int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    private class EnviarColeta extends AsyncTask<Void,Void,String>{

        Context context;

        public EnviarColeta(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String data_temp = db.findAll().get(item).data_coleta.toString();
            _cod=  db.findAll().get(item).cod_produto.toString();



            //transforma o formato da data para ser inserida no banco
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date data = null;
            try {
                data = new java.sql.Date(format.parse(data_temp).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = System.currentTimeMillis();
            Log.d("milis",String.valueOf(millis));
            _verCod = String.valueOf(millis);
            _data= data.toString();
            _valor =  db.findAll().get(item).valor_coleta.toString();
            pb_item.setVisibility(View.VISIBLE);
            btn_enviar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.contains("OK")){
                item++;
                pb_total.setProgress(item);
                status.setText("Envio [ "+ item+" / "+TotalBanco +" ]");


            }
            if(item < TotalBanco) {
                h.postDelayed((Runnable)context,200);

            }else {
                pb_item.setVisibility(View.INVISIBLE);
                status.setText("Enviado com sucesso...");
                db.deleteAll();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("Coleta Enviada...");

            }

        }

        @Override
        protected String doInBackground(Void... params) {

            String retorno;
            String c = dbMem.findAll().get(0).codColeta;
            String s = dbMem.findAll().get(0).codSuper;

            Map<String,String> post = new HashMap<String,String>();

            post.put("chave",chave);
            post.put("codProduto",_cod);
            post.put("valor",_valor);
            post.put("data",_data);
            post.put("codUser",_codUser);
            post.put("codColeta",c);
            post.put("codSmercado",s);
            post.put("verCod",_verCod);

            try {

                retorno = HttpHelper.doPost(URL,post,"UTF-8");

            }catch (IOException e) {
                Log.e("EnvioColeta",e.getMessage(),e);
                return null;
            }

            //retorno = "OK";
            return retorno;

        }
    }
}
