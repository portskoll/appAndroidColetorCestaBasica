package mundotela.net.coletapreco.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import livroandroid.lib.utils.AndroidUtils;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.servicos.VerificaCadColetor;
import mundotela.net.coletapreco.util.Pref_Salva;

public class Login extends AppCompatActivity implements Runnable{


    private EditText email;
    private ProgressBar pb;
    private TextView status;
    private String URL ="http://ifspcoletor.mundotela.net/app/verifica_user.php";
    private String chave = "30be91";
    private String post_email;
    private Handler h;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.edt_email_coletor);
        status =(TextView)findViewById(R.id.txt_status);
        pb = (ProgressBar) findViewById(R.id.pb_coletor);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ativação do App");
        h = new Handler();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ativacao,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == R.id.mAtivar) {

            if(AndroidUtils.isNetworkAvailable(this)) {
                post_email = email.getText().toString();
                if (post_email.equals("")){
                    AndroidUtils.alertDialog(this,"ATENÇÂO !","Digite seu número de prontuario e click em ativar.Para poder entrar no APP.");
                }else {
                    VerificaCadColetor Atask = new VerificaCadColetor(this,pb,status,chave,post_email,URL);
                    Atask.execute();
                    h.postDelayed(this,1000);

                }

            }else {
                AndroidUtils.alertDialog(this,"ERRO CONEXÃO !","Você precisa estar conectado a Internet para completar esta operação.");
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        if (Pref_Salva.getAtivo(getApplicationContext(),"chave").contains("ATIVO")){
            //Pref_Salva.setAtivo(this,"chave","ATIVO");
            //Pref_Salva.setUser(this,"chave",post_email);
            Intent it = new Intent(this,Splash.class);
            startActivity(it);
            finish();
            Log.d("ATIVACAO",Pref_Salva.getAtivo(getApplicationContext(),"chave"));
        }else {
            h.postDelayed(this,1000);
        }

    }
}
