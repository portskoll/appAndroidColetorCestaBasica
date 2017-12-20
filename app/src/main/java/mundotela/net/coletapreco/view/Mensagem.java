package mundotela.net.coletapreco.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.List;

import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.adapter.CadColetaAdapter;
import mundotela.net.coletapreco.domain.CadColeta;
import mundotela.net.coletapreco.domain.CadColetaService;
import mundotela.net.coletapreco.domain.RecebeMensagem;
import mundotela.net.coletapreco.domain.RecebeMensagemService;
import mundotela.net.coletapreco.util.Pref_Salva;

import static mundotela.net.coletapreco.R.id.imageView;
import static mundotela.net.coletapreco.R.id.recyclerView;

public class Mensagem extends AppCompatActivity {

    private TextView mensagem;
    private TextView nomeColeta;
    private TextView nomeSuper;
    private TextView nomeCidade;
    private TextView endereco;
    private TextView dataInicial;
    private TextView dataFinal;
    private ImageView fotoSuper;
    private TextView status;
    private List<RecebeMensagem> mensagemList;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mensagem");
        getSupportActionBar().setSubtitle("Local da coleta");

        mensagem = (TextView)findViewById(R.id.txt_men_mensagem);
        nomeColeta = (TextView)findViewById(R.id.txt_men_nomeColeta);
        nomeCidade = (TextView)findViewById(R.id.txt_men_cidade);
        nomeSuper = (TextView)findViewById(R.id.txt_men_nomeSuper);
        endereco = (TextView)findViewById(R.id.txt_men_end_super);
        dataInicial = (TextView)findViewById(R.id.txt_men_data_i);
        dataFinal = (TextView)findViewById(R.id.txt_men_data_f);
        fotoSuper = (ImageView)findViewById(R.id.img_superMercado);
        status = (TextView)findViewById(R.id.txt_men_statusColeta);

        //Banner AdMob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




    }

    private Activity getActivity() {
        return this;
    }

    private void taskMensagem(){

        new TaskMensagem().execute();

    }

    //assinkTask
    private class TaskMensagem extends AsyncTask<Void,Void,List<RecebeMensagem>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status.setText("Buscando Mensagem");

        }

        @Override
        protected List<RecebeMensagem> doInBackground(Void... params) {

            try {
                RecebeMensagemService.buscaJson(getBaseContext());
                mensagemList = RecebeMensagemService.getBanco(getBaseContext());
                return mensagemList;
            }catch (Exception e) {

                Log.e("COLETA_TASK",e.getMessage(),e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<RecebeMensagem> mRecebida) {


            if(mRecebida != null){
                status.setText("Ativada");
                mensagem.setText(mRecebida.get(0).mensagem);
                endereco.setText(mRecebida.get(0).endereco);
                nomeCidade.setText(mRecebida.get(0).nomeCidade);
                nomeSuper.setText(mRecebida.get(0).nomeSuper);
                nomeColeta.setText(mRecebida.get(0).nomeColeta);
                dataInicial.setText(mRecebida.get(0).dataInicial);
                dataFinal.setText(mRecebida.get(0).dataFinal);
                Picasso.with(getActivity()).load(mRecebida.get(0).urlFoto).into(fotoSuper);
                Pref_Salva.setMem(getActivity(),"chave",mRecebida.get(0).codColeta);


            }else {
                status.setText("Não exite coleta ainda...");
            }
        }
    }

    public void buscarColeta(View view) {

        taskMensagem();
    }
}
