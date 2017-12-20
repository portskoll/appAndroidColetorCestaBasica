package mundotela.net.coletapreco.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.adapter.ListaColetaAdapter;
import mundotela.net.coletapreco.domain.ListaColeta;
import mundotela.net.coletapreco.domain.ListaColetaService;
import mundotela.net.coletapreco.servicos.AtaskColeta;
import mundotela.net.coletapreco.servicos.AtaskSplah;

public class PainelColeta extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ListaColeta p;
    private List<ListaColeta> cServ;
    private LinearLayout LLpb;
    String modo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_coleta);

        //pega o modo de trabalho
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        modo = args.getString("modo");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (modo.equals("mP")){
            getSupportActionBar().setTitle("Cadastro de Produtos");
            getSupportActionBar().setSubtitle("Lista de produtos");
        }
        if (modo.equals("mC")){
            getSupportActionBar().setTitle("Coleta de Preços");
            getSupportActionBar().setSubtitle("Lista de produtos");
        }

        //RecycleView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_coleta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        LLpb = (LinearLayout) findViewById(R.id.LL_prog_lista_coleta);
        taskCAT();
        //AtaskSplah teste = new AtaskSplah(this);
        //teste.execute();

    }

    //onClick Lista Coleta
    private ListaColetaAdapter.ListaColetaOnClickListener onClickListaColeta() {

        return new ListaColetaAdapter.ListaColetaOnClickListener() {

            @Override
            public void onClickListaColeta(View view, int idx) {
                p = cServ.get(idx);
                Toast.makeText(getBaseContext(),p.produto,Toast.LENGTH_SHORT ).show();


                if (modo.equals("mC")){

                    Intent it = new Intent(getBaseContext(),CadastroColeta.class);
                    Bundle envia = new Bundle();
                    envia.putString("cod",p.cod_coleta);
                    envia.putString("codP","-1");
                    it.putExtras(envia);
                    startActivity(it);
                    finish();
                }

                if (modo.equals("mP")){

                    Intent it = new Intent(getBaseContext(),CadastroProdutos.class);
                    Bundle envia = new Bundle();
                    envia.putString("cod",p.cod_coleta);
                    it.putExtras(envia);
                    startActivity(it);
                    finish();

                }



            }
        };
    }
    private Activity getActivity() {
        return this;
    }

    private void taskCAT(){
        new GetCatTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coleta,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if ( id == R.id.mRefresColeta) {
           if (AndroidUtils.isNetworkAvailable(this)) {
               AtaskColeta t = new AtaskColeta(this);
               t.execute();
               taskCAT();
           }else {
               AndroidUtils.alertDialog(this,"Erro Internet","Você precisa estar conectado para atualizar a lista.");
           }



        }

        if (id == android.R.id.home) {

            Intent it = new Intent(this,Splash.class);
            startActivity(it);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //assinkTask
    private class GetCatTask extends AsyncTask<Void,Void,List<ListaColeta>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LLpb.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<ListaColeta> doInBackground(Void... params) {

            try {
                cServ = ListaColetaService.getBanco(getApplicationContext());
                return cServ;
            }catch (Exception e){
                Log.e("Coleta_TASK",e.getMessage(),e);
                return null;
            }



        }

        @Override
        protected void onPostExecute(List<ListaColeta> ListaColeta) {
            if(ListaColeta != null){
                LLpb.setVisibility(View.GONE);
                recyclerView.setAdapter(new ListaColetaAdapter(getApplicationContext(),ListaColeta,onClickListaColeta()));
            }
        }
    }



}