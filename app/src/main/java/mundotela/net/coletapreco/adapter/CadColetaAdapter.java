package mundotela.net.coletapreco.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import livroandroid.lib.utils.SDCardUtils;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.db.ListaColetaDB;
import mundotela.net.coletapreco.db.ListaProdutoDB;
import mundotela.net.coletapreco.domain.CadColeta;
import mundotela.net.coletapreco.util.TempCadastro;

//import static com.google.android.gms.analytics.internal.zzy.a;
//import static com.google.android.gms.analytics.internal.zzy.f;

/**
 * Created by Henrique on 21/09/2016.
 */

public class CadColetaAdapter extends RecyclerView.Adapter<CadColetaAdapter.CadColetaViewHolder>{


    protected static final String TAG = "AdpterColeta";
    private final List<CadColeta> cadColeta;
    private final Context context;
    private final CadColetaOnClickListener onClickListener;
    private File file;

    public CadColetaAdapter(Context context, List<CadColeta> cadColeta, CadColetaOnClickListener onClickListener) {
        this.cadColeta = cadColeta;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public interface CadColetaOnClickListener{
        void onClickCadColeta(View view, int idx);
        void onLongClickCadColeta(View view, int idx);
    }


    @Override
    public CadColetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adpter_lista_cad_coleta,viewGroup,false);
        CadColetaViewHolder holder = new CadColetaViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CadColetaViewHolder holder,final int position) {

        CadColeta c = cadColeta.get(position);
        holder.cc_cod.setText(c.cod_produto);
        holder.cc_valor.setText("R$ "+c.valor_coleta);

        try {
            ListaColetaDB db_c = new ListaColetaDB(context);
            String _produto = db_c.findbyCod(c.cod_coletax).get(0).produto;
            holder.cc_produto.setText(_produto);

        }catch (Exception e) {
            holder.cc_produto.setText("ERRO");
        }

        try {
            ListaProdutoDB db_p = new ListaProdutoDB(context);
            String _marca = db_p.findbyCod(c.cod_produto).get(0).marca_nome;
            holder.cc_marca.setText(_marca);

        }catch (Exception e) {
            holder.cc_produto.setText("ERRO");
        }



        //pinta o fundo de azul se a linha estiver selecionada
        int corFundo = context.getResources().getColor(c.selected ? R.color.colorPrimary : R.color.white );
        holder.cc_imgProd.setBackgroundColor(corFundo);
        int corFonte = context.getResources().getColor(c.selected ? R.color.white : R.color.colorPrimary );
        holder.cc_cod.setTextColor(corFonte);


        File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+c.cod_produto+".jpg");

        if(file.exists()) {

            Bitmap b = decodeImageFile(file,30,30);
            holder.cc_imgProd.setImageBitmap(b);

        }
        //click normal
        if(onClickListener != null){

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  onClickListener.onClickCadColeta(holder.view,position);
               }
           });
       }

       //click Longo
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickListener.onLongClickCadColeta(holder.view,position);
                return true;
            }
        });


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

    @Override
    public int getItemCount() {
        return this.cadColeta != null ? this.cadColeta.size() : 0;
    }

    public class CadColetaViewHolder extends RecyclerView.ViewHolder {

        public TextView cc_cod;
        public TextView cc_valor;
        public TextView cc_produto;
        public TextView cc_marca;
        public ImageView cc_imgProd;
        private View view;

        public CadColetaViewHolder(View view) {

            super(view);
            this.view = view;
            cc_cod = (TextView)view.findViewById(R.id.txt_codProduto_lista_cadColeta);
            cc_valor = (TextView)view.findViewById(R.id.txt_valor_lista_cadColeta);
            cc_produto = (TextView)view.findViewById(R.id.txt_produto_listaCadColeta);
            cc_marca = (TextView) view.findViewById(R.id.txt_marca_listaCadColeta);
            cc_imgProd = (ImageView)view.findViewById(R.id.img_cc_codProduto);



        }
    }


    }


