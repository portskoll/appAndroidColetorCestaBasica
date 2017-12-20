package mundotela.net.coletapreco.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.db.CadColetaDB;
import mundotela.net.coletapreco.domain.ListaColeta;

//import static com.google.android.gms.analytics.internal.zzy.e;
//import static com.google.android.gms.analytics.internal.zzy.r;

/**
 * Created by Henrique on 13/09/2016.
 */
public class ListaColetaAdapter extends RecyclerView.Adapter<ListaColetaAdapter.ListaColetaViewHolder> {

    protected  static final String TAG = "Adpter_";
    private final List<ListaColeta> coleta;
    private final Context context;
    private final ListaColetaOnClickListener onClickListener;

    public interface ListaColetaOnClickListener {
        void onClickListaColeta(View view, int idx);
    }

    public ListaColetaAdapter(Context context,List<ListaColeta> coleta,ListaColetaOnClickListener onClickListener) {
        this.coleta = coleta;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public ListaColetaAdapter.ListaColetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adpter_lista_coleta,viewGroup,false);
        ListaColetaViewHolder holder = new ListaColetaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ListaColetaAdapter.ListaColetaViewHolder holder, final int position) {

        ListaColeta c = coleta.get(position);
        holder.tcproduto.setText(c.produto);
        holder.tccodColeta.setText(c.cod_coleta);
        holder.tctipoColeta.setText(c.tipo_coleta);
        holder.tccestaColeta.setText(c.cesta_coleta);
        holder.tcqtde_t.setText(c.qtde_t);

        try {
            CadColetaDB db = new CadColetaDB(context);
            int temp = db.findbyCodCol(c.cod_coleta).size();
            holder.tcqtde_f.setText(String.valueOf(temp));
            int resultado = Integer.parseInt(c.qtde_t) - temp;
            if (Integer.parseInt(c.qtde_t) == 1){
                if(resultado == 0) {
                    holder.tcColetou.setBackgroundColor(Color.rgb(255,0,0));
                }else if(resultado > 0){
                    holder.tcColetou.setBackgroundColor(Color.rgb(173,226,213));
                }
            }else {
                if(resultado == 0) {
                    holder.tcColetou.setBackgroundColor(Color.rgb(255,0,0));
                }else if(resultado == 3){
                    holder.tcColetou.setBackgroundColor(Color.rgb(0,255,173));
                }else if (resultado == 2){
                    holder.tcColetou.setBackgroundColor(Color.rgb(33,255,0));
                }else if (resultado == 1){
                    holder.tcColetou.setBackgroundColor(Color.rgb(179,255,0));
                }
            }


        }catch (Exception e){

        }




        if(onClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //chama o istener para informar que clicou na categoria
                    onClickListener.onClickListaColeta(holder.view,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return this.coleta != null ? this.coleta.size() : 0;
    }


    //SubClasse de RecyclerView.ViewHolder. Contem todas as views.
    public class ListaColetaViewHolder extends RecyclerView.ViewHolder {

        public TextView tcproduto;
        public TextView tccodColeta;
        public TextView tctipoColeta;
        public TextView tccestaColeta;
        public TextView tcqtde_t;
        public TextView tcqtde_f;
        public FrameLayout tcColetou;

        private View view;
        public ListaColetaViewHolder(View view) {
            super(view);
            this.view = view;
            //Cria as views para salvar no ViewHolder
            tcproduto = (TextView)view.findViewById(R.id.txt_coleta_produto);
            tccodColeta = (TextView)view.findViewById(R.id.txt_coleta_cod);
            tctipoColeta = (TextView)view.findViewById(R.id.txt_coleta_tipo);
            tccestaColeta = (TextView)view.findViewById(R.id.txt_coleta_cesta);
            tcqtde_t = (TextView) view.findViewById(R.id.txt_coleta_qtde_t);
            tcqtde_f = (TextView) view.findViewById(R.id.txt_coleta_qtde_f);
            tcColetou = (FrameLayout) view.findViewById(R.id.Coleta_qtde);

        }
    }
}
