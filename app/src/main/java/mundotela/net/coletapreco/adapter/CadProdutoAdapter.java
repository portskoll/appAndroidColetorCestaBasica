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
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import livroandroid.lib.utils.SDCardUtils;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.domain.CadProduto;


/**
 * Created by Henrique on 05/09/2016.
 */
public class CadProdutoAdapter extends RecyclerView.Adapter<CadProdutoAdapter.CatServicosViewHolder>{

    protected  static final String TAG = "Cat_Servicos";
    private final List<CadProduto> catServ;
    private final Context context;
    private final CatServicosOnClickListener onClickListener;

    public interface CatServicosOnClickListener {
         void onClickCatServicos(View view, int idx);
    }

    public CadProdutoAdapter(Context context, List<CadProduto> catServ, CatServicosOnClickListener onClickListener) {

        this.context = context;
        this.catServ = catServ;
        this.onClickListener = onClickListener;
    }

    @Override
    public CatServicosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //este metodo cria a subclasse de RecycleView.ViewHolder
        //Infla a view no layout
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_lista_cad_produto,viewGroup,false);
        //Cria a classe view holder
        CatServicosViewHolder holder = new CatServicosViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CatServicosViewHolder holder, final int position) {
        //Este metodo recebe o indice do elemento, e atualiza as views que estão dentro do ViewHolder
        CadProduto c = catServ.get(position);
        //Atualiza os valores nas views
        //holder.tNome.setText(c.nome);
        holder.tCod.setText(c.cod);
        holder.tMarca.setText(c.marca);
        holder.tCod_col.setText(c.cod_col);

        //holder.tTipo.setText(c.tipo);
        //holder.tCesta.setText(c.cesta);
        holder.progress.setVisibility(View.GONE);
        /*Picasso.with(context).load(c.urlFoto).fit().into(holder.img, new com.squareup.picasso.Callback() { -- nao mostra a imagem
            @Override
            public void onSuccess() {
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progress.setVisibility(View.GONE);

            }
        });*/

        File file = SDCardUtils.getPrivateFile(context,"foto_produto_"+ c.cod+".jpg", Environment.DIRECTORY_PICTURES);

        //holder.img.setImageURI(Uri.fromFile(file));
        //Bitmap bitmap = BitmapFactory.decodeFile(c.foto_file);
        Bitmap b = decodeImageFile(file,30,30);
        holder.img.setImageBitmap(b);
        //Picasso.with(context).load(c.foto_file).into(holder.img);
        //click
        if(onClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //chama o istener para informar que clicou na categoria
                    onClickListener.onClickCatServicos(holder.view,position);
                }
            });
        }

    }

    //metodo para tratar o bitmap para não dar estouro de memoria
    public static Bitmap decodeImageFile(File f,int WIDTH,int HIGHT){
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

        return this.catServ != null ? this.catServ.size() : 0;
    }






    //SubClasse de RecyclerView.ViewHolder. Contem todas as views.
    public class CatServicosViewHolder extends RecyclerView.ViewHolder {

        //public TextView tNome;
        public TextView tCod;
        public TextView tMarca;
        public TextView tCod_col;

        //public TextView tTipo;
        //public TextView tCesta;
        ImageView img;
        ProgressBar progress;
        private View view;
        public CatServicosViewHolder(View view) {
            super(view);
            this.view = view;
            //Cria as views para salvar no ViewHolder
            //tNome = (TextView)view.findViewById(R.id.tNome);
            tCod = (TextView) view.findViewById(R.id.tCod);
            tMarca = (TextView) view.findViewById(R.id.tMarca);
            tCod_col = (TextView) view.findViewById(R.id.tcod_col);
            //tTipo = (TextView) view.findViewById(R.id.tTipo);
            //tCesta = (TextView) view.findViewById(R.id.tCesta);
            img = (ImageView)view.findViewById(R.id.img11);
            progress = (ProgressBar)view.findViewById(R.id.progress);
        }
    }
}
