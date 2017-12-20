package mundotela.net.coletapreco.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import livroandroid.lib.utils.AndroidUtils;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.db.ListaProdutoDB;
import mundotela.net.coletapreco.domain.ListaColetaService;
import mundotela.net.coletapreco.domain.ListaMarcaService;
import mundotela.net.coletapreco.domain.ListaProdutoService;
import mundotela.net.coletapreco.util.PermissaFile;
import mundotela.net.coletapreco.util.Pref_Salva;
import mundotela.net.coletapreco.util.TempAux;

public class Splash extends AppCompatActivity implements Runnable{

    private ProgressBar pb;
    private TextView st;
    private int contador = 0;

    boolean local = false;
    String ID_PlayerTV = "";
    //ProgressBar pb;
    // declare the dialog as a member field of your activity
    ProgressDialog mProgressDialog;
    int u = 0;
    Handler h;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pb = (ProgressBar) findViewById(R.id.pbSplash);
        st = (TextView)findViewById(R.id.txt_status_splash);
        PermissaFile.verifyStoragePermissions(this);//Cria um pergunta para permitir gravar no Android 6



        if(Pref_Salva.getAtivo(getApplicationContext(),"chave").equals("ATIVO")){
            contador = 1;
        }else {
            contador = 2;
        }


        if (contador == 1) {

            getSupportActionBar().setTitle("Escolha o modo");
            getSupportActionBar().setSubtitle("Faça sua escolha no menu ao lado > >");
            h = new Handler();
            h.post(this);

        }

        if (contador == 2) {

            getSupportActionBar().hide();
           if (AndroidUtils.isNetworkAvailable(this)){
               AtaskSplah task = new AtaskSplah(this);
               task.execute();
           }else {
               st.setText("Sem conexão com a Internet...");
           }



        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash,menu);
        return true;
    }

    // modo de envio do parametro Produto ou coleta
    private void mod (String m) {

        int mensagem = Integer.parseInt(Pref_Salva.getMen(this,"chave"));
        if(mensagem > 0) {
            Intent it = new Intent(this,PainelColeta.class);
            Bundle envia = new Bundle();
            envia.putString("modo",m);
            it.putExtras(envia);
            startActivity(it);
            finish();
        }else if(mensagem == 0){
            AndroidUtils.alertDialog(this,"Atenção","Escolha opção Receber Coleta, para ativar a coleta de preços");
        }else {
            AndroidUtils.alertDialog(this,"Não autorizado","O periodo de coleta acabou, aguarde proxima mensagem.");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == R.id.mModoCadColeta) {
           mod("mC");
        }

        if ( id == R.id.mModoCadProduto) {
            mod("mP");
        }

        if ( id == R.id.mBuscaMenColeta) {
           Intent it = new Intent(this,Mensagem.class);
            startActivity(it);
        }

        if ( id == R.id.mAtulalizaListaProd) {

            if (AndroidUtils.isNetworkAvailable(this)){
                AtaskSplah task = new AtaskSplah(this);
                task.execute();
            }else {
                st.setText("Sem conexão com a Internet...");
            }

        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {

        ListaProdutoDB db = new ListaProdutoDB(getApplicationContext());

        if(u < db.findAll().size() ) {
            // instantiate it within the onCreate method
            mProgressDialog = new ProgressDialog(Splash.this);
            //mProgressDialog.setMessage("Baixando arquivo : " + db.findAll().get(u).url_video);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("Atualizando Imagens...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);

            // execute this when the downloader must be fired

            File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+db.findAll().get(u).codProduto+".jpg");
            if(file.exists()){

                Log.d("Arquivo Existe : ","Ele Existe");
                u++;
                h.post(Splash.this);

            }else {
                final DownloadTask downloadTask = new DownloadTask(Splash.this,android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"+db.findAll().get(u).codProduto+".jpg");
                downloadTask.execute(db.findAll().get(u).urlFotoProduto);

                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        downloadTask.cancel(true);
                    }
                });
            }

        }else {
            //Intent it = new Intent(getApplicationContext(),PlayerTV.class);
            //startActivity(it);
            //finish();
        }
    }


    private class AtaskSplah extends AsyncTask<Void,Void,String> {

        Context  context;


        public AtaskSplah(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            st.setText("Buscando dados...");

        }


        @Override
        protected String doInBackground(Void... params) {

            ListaColetaService.buscaJson(context);
            String resposta1 = String.valueOf(TempAux.totalJson_coleta);
            ListaMarcaService.buscaJson(context);
            String resposta2 = String.valueOf(TempAux.totalJson_marca);
            ListaProdutoService.buscaJson(context);
            String resposta3 = String.valueOf(TempAux.totalJson_produto);

            return "\n\n\nEncontradas e salvas : \n" + "Coletas  : "+ resposta1 + "\nMarcas : "+ resposta2 + "\nProdutos : "+ resposta3  ;
        }



        @Override
        protected void onPostExecute(String s) {
           super.onPostExecute(s);
            pb.setVisibility(View.INVISIBLE);
            st.setText(s);
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
            if(contador==2) {
                Intent it = new Intent(context,Login.class);
                startActivity(it);
                finish();
            }



        }
    }




    public class DownloadTask  extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private String end_jpg = "";

        public DownloadTask(Context context,String sd) {
            this.context = context;
            this.end_jpg = sd;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                ListaProdutoDB p = new ListaProdutoDB(getApplicationContext());
                input = connection.getInputStream();
                output = new FileOutputStream(end_jpg);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null){
                //Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
                Log.e("Download error: ",result);
                h.post(Splash.this);
            }else {

                u++;
                h.post(Splash.this);
            }
        }
    }
}

