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

import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.adapter.CadProdutoAdapter;
import mundotela.net.coletapreco.db.CadProdudoDB;
import mundotela.net.coletapreco.domain.CadProduto;
import mundotela.net.coletapreco.domain.CadProdutoService;
import mundotela.net.coletapreco.util.TempCadastro;

public class ListaCadProduto extends AppCompatActivity {

    private RecyclerView recyclerView;
    CadProduto p;
    List<CadProduto> cServ;
    LinearLayout LLpb;
    private String cod = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cad_produto);
        //pega o codigo enviado
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        cod = args.getString("cod");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cadastro");
        getSupportActionBar().setSubtitle("Produtos para enviar...");
        //RecycleView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        LLpb = (LinearLayout) findViewById(R.id.LL_prog_lista_prod);
        //CatServicos e Adaptador
        //List<CadProduto> cServ = CadProdutoService.getcat(getBaseContext(),"s");
        //recyclerView.setAdapter(new CadProdutoAdapter(this,cServ,onClickCatServicos()));
        taskCAT();
    }

    //onClick CatServicos
    private CadProdutoAdapter.CatServicosOnClickListener onClickCatServicos() {

        return new CadProdutoAdapter.CatServicosOnClickListener() {
            @Override
            public void onClickCatServicos(View view, int idx) {
                //List<CatServicos> cServ = CatServicos.getCatServicos();

                p = cServ.get(idx);
                String codP =p.cod;
                CadProdudoDB dbP = new CadProdudoDB(getApplicationContext());
                TempCadastro.setUrlFoto(dbP.findbyCod(codP).get(0).foto_file);
                TempCadastro.setCod(dbP.findbyCod(codP).get(0).cod);
                TempCadastro.setMarca_produto(dbP.findbyCod(codP).get(0).marca);
                Intent it = new Intent(getBaseContext(),CadastroProdutos.class);
                Bundle envia = new Bundle();
                envia.putString("cod",p.cod_col);
                it.putExtras(envia);
                startActivity(it);
                finish();

                Toast.makeText(getBaseContext(),"Produto : " + p.cod,Toast.LENGTH_SHORT ).show();
            }
        };
    }
    private Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recycler_view, menu);
        return true;
    }

    private void taskCAT(){
        new GetCatTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            Intent it = new Intent(getBaseContext(),CadastroProdutos.class);
            Bundle envia = new Bundle();
            envia.putString("cod",cod);
            it.putExtras(envia);
            startActivity(it);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //assinkTask
    private class GetCatTask extends AsyncTask<Void,Void,List<CadProduto>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LLpb.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<CadProduto> doInBackground(Void... params) {

            try {
                cServ = CadProdutoService.getcat(getApplicationContext(),"s");
                return cServ;
            }catch (Exception e){
                Log.e("CAT_TASK",e.getMessage(),e);
                return null;
            }



        }

        @Override
        protected void onPostExecute(List<CadProduto> cadProdutos) {
            if(cadProdutos != null){
                LLpb.setVisibility(View.GONE);
                recyclerView.setAdapter(new CadProdutoAdapter(getApplicationContext(), cadProdutos,onClickCatServicos()));
            }
        }
    }
}
