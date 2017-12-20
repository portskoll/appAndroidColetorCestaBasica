package mundotela.net.coletapreco.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.adapter.CadColetaAdapter;
import mundotela.net.coletapreco.db.CadColetaDB;
import mundotela.net.coletapreco.domain.CadColeta;
import mundotela.net.coletapreco.domain.CadColetaService;

public class ListaCadColeta extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CadColeta c;
    private List<CadColeta> cColeta;
    private LinearLayout LLpb;
    private ActionMode actionMode;
    private String cod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cad_coleta);
        //pega o codigo enviado
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        cod = args.getString("cod");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cadastro");
        getSupportActionBar().setSubtitle("Coleta para enviar...");
        //RecycleView
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_listacoleta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        LLpb = (LinearLayout) findViewById(R.id.LL_prog_lista_coleta);
        taskCol();
    }


    private Activity getActivity() {
        return this;
    }

    private void taskCol(){
        new TaskColeta().execute();
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

            envia_parametros("-1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //envia bundle
    private void envia_parametros(String s) {
        Intent it = new Intent(getActivity(),CadastroColeta.class);
        Bundle envia = new Bundle();
        envia.putString("cod",cod);
        envia.putString("codP",s);
        it.putExtras(envia);
        startActivity(it);
        finish();
    }

    //OnClick em um item da lista

    private CadColetaAdapter.CadColetaOnClickListener onClickListener(){

        return new CadColetaAdapter.CadColetaOnClickListener() {
            @Override
            public void onClickCadColeta(View view, int idx) {

                c  = cColeta.get(idx);

                if (actionMode == null) {

                    envia_parametros(c.cod_produto.toString());

                    Toast.makeText(getActivity(),"click curto : " + c.cod_produto,Toast.LENGTH_LONG).show();
                }else {
                    c.selected = !c.selected;
                    updateActionModeTitle();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

            }

            @Override
            public void onLongClickCadColeta(View view, int idx) {

                if(actionMode != null) {return;}
                actionMode = startSupportActionMode(getActionModeCallback());
                c  = cColeta.get(idx);
                c.selected = true;
                recyclerView.getAdapter().notifyDataSetChanged();
                updateActionModeTitle();
                //Toast.makeText(getActivity(),"click curto : " + c.cod_produto,Toast.LENGTH_LONG).show();




            }
        };
    }

    private void updateActionModeTitle() {
        if(actionMode != null) {
            actionMode.setTitle("Selecione os itens");
            actionMode.setSubtitle(null);
            List<CadColeta> selectedColetas = getSelectedColetas();
            if(selectedColetas.size() == 1){
                actionMode.setSubtitle("1 item selecionado");
            }else if (selectedColetas.size()> 1){
                actionMode.setSubtitle(selectedColetas.size() +" itens selecionados");
            }
        }
    }

    private List<CadColeta> getSelectedColetas() {
        List<CadColeta> list = new ArrayList<>();
        for(CadColeta c : cColeta) {
            if(c.selected) {list.add(c);}

        }
        return list;
    }

    //utilizado para fazer selecao multipla
    private ActionMode.Callback getActionModeCallback() {
       return new ActionMode.Callback() {

           @Override
           public boolean onCreateActionMode(ActionMode mode, Menu menu) {

               MenuInflater inflater = getActivity().getMenuInflater();
               inflater.inflate(R.menu.menu_lista_cad_coleta,menu);
               return true;
           }

           @Override
           public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
               return true;
           }

           @Override
           public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
               List<CadColeta> selectedColetas = getSelectedColetas();
               if(item.getItemId() == R.id.delete_item_listaCadColeta){
                   CadColetaDB db = new CadColetaDB(getBaseContext());
                   try {
                       for(CadColeta c : selectedColetas){
                           db.delete(c);
                           cColeta.remove(c);
                           Toast.makeText(getActivity(),"Iten(s) excluido(s) com sucesso",Toast.LENGTH_LONG).show();
                       }
                   }finally {
                       db.close();
                   }

               }
               mode.finish();
               return true;
           }

           @Override
           public void onDestroyActionMode(ActionMode mode) {

               actionMode = null;
               for(CadColeta c : cColeta){
                   c.selected = false;
               }
                recyclerView.getAdapter().notifyDataSetChanged();
           }
       };
    }



    //assinkTask
    private class TaskColeta extends AsyncTask<Void,Void,List<CadColeta>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LLpb.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<CadColeta> doInBackground(Void... params) {

            try {
                cColeta = CadColetaService.getBanco(getBaseContext());
                return cColeta;
            }catch (Exception e) {

                Log.e("COLETA_TASK",e.getMessage(),e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<CadColeta> cadColetas) {


            if(cadColetas != null){
                LLpb.setVisibility(View.GONE);
                recyclerView.setAdapter(new CadColetaAdapter(getApplicationContext(),cadColetas,onClickListener()));
            }
        }
    }

}
