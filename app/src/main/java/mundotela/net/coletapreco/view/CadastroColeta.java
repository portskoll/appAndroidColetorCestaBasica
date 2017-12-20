package mundotela.net.coletapreco.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import livroandroid.lib.utils.IntentUtils;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.db.CadColetaDB;
import mundotela.net.coletapreco.db.ListaColetaDB;
import mundotela.net.coletapreco.db.ListaProdutoDB;
import mundotela.net.coletapreco.domain.CadColeta;
import mundotela.net.coletapreco.domain.ListaColeta;
import mundotela.net.coletapreco.domain.ListaProduto;
import mundotela.net.coletapreco.util.TempAux;
import mundotela.net.coletapreco.util.TempCadastro;
//import static com.google.android.gms.analytics.internal.zzy.c;
//import static com.google.android.gms.analytics.internal.zzy.e;
//import static com.google.android.gms.analytics.internal.zzy.v;

public class CadastroColeta extends AppCompatActivity implements Runnable{

    private static EditText data_coleta;
    private EditText codBarras;
    private EditText preco;
    private TextView marca_coleta;
    private ImageView img;
    private String cod;
    private String codP;
    private Handler h;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_coleta);




        //pega o codigo enviado
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        cod = args.getString("cod");
        codP = args.getString("codP");
        Log.d("CODP",codP);

        marca_coleta = (TextView) findViewById(R.id.txt_marca_status);
        data_coleta = (EditText) findViewById(R.id.edt_data_coleta);
        preco = (EditText) findViewById(R.id.edt_preco_coleta);
        codBarras = (EditText) findViewById(R.id.edt_cod_cad_coleta);



            img = (ImageView) findViewById(R.id.img_cad_coleta);

        //Banner AdMob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        setData_txt();
        try {



            ListaColetaDB db = new ListaColetaDB(this);
            List<ListaColeta> item = db.findbyCod(cod);
            String produto = item.get(0).produto;
            String tipo = item.get(0).tipo_coleta;
            String cesta = item.get(0).cesta_coleta;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(tipo +" - "+ cesta);
            getSupportActionBar().setSubtitle(produto);
            if (codP.equals("-1")){
                if(TempAux.deOnde == 2){
                    marca_coleta.setText(TempCadastro.getMarca_produto());
                    codBarras.setText(TempCadastro.getCod());
                }
                TempAux.deOnde = 0;
            }else {
                CadColetaDB dbP = new CadColetaDB(this);
                List<CadColeta> itemP = dbP.findbyCod(codP);
                String data_ = itemP.get(0).data_coleta;
                String valor_ = itemP.get(0).valor_coleta;
                preco.setText(valor_);
                data_coleta.setText(data_);
                codBarras.setText(codP);
                getCod(codP);
            }


        }catch (Exception e){
            Toast.makeText(this,"Erro ao ler o banco de Coleta" + e.getMessage(),Toast.LENGTH_SHORT).show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Erro");
            getSupportActionBar().setSubtitle("Banco Coleta");
        }



    }

    //verifica cadastro
    private boolean CADok (){
        String marca = marca_coleta.getText().toString();
        String data = data_coleta.getText().toString();
        String valorpreco = preco.getText().toString();
        String cProd = codBarras.getText().toString();
        ListaProdutoDB db = new ListaProdutoDB(this);
        int busca = db.findbyCod(cProd).size();

        if(marca.contains("Marca_Status")|| marca.contains("Produto não encontrado") ){
            Toast.makeText(this,"Escolha um produto cadastrado",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!data.contains("/") || data.length() <8 ){
            Toast.makeText(this,"Data Invalida",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (valorpreco.length() < 2) {
            Toast.makeText(this,"Preço invalido",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (cProd.length() < 2 || busca < 1) {
            Toast.makeText(this,"Erro produto",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_coleta,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.mSalvaColeta) {

            if (CADok()) {
                CadColeta c = new CadColeta();
                c.cod_produto = codBarras.getText().toString();
                c.valor_coleta = preco.getText().toString();
                c.cod_coletax = cod;
                String dataStr = data_coleta.getText().toString();

                c.data_coleta = dataStr;
                Log.d("data",c.data_coleta);

                CadColetaDB db = new CadColetaDB(this);
                //verivica se ja exite o codigo do produto
                int temCod = db.findbyCod(c.cod_produto).size();
                //salva no banco do celular
                db.save(c,temCod);

                Intent it = new Intent(this,ListaCadColeta.class);
                Bundle envia = new Bundle();
                envia.putString("cod",cod);
                it.putExtras(envia);
                startActivity(it);
                finish();
            }


            return true;
        }

        if(id == R.id.mMostra_listaCadColeta){
            Intent it = new Intent(this,ListaCadColeta.class);
            Bundle envia = new Bundle();
            envia.putString("cod",cod);
            it.putExtras(envia);
            startActivity(it);
            finish();
        }

        if(id == R.id.mEnviar_listaCadColeta){
            Intent it = new Intent(this,EnviaColeta.class);
            startActivity(it);
            finish();
        }

        if (id == android.R.id.home) {


                Intent it = new Intent(this,PainelColeta.class);
                Bundle envia = new Bundle();
                envia.putString("modo","mC");
                it.putExtras(envia);
                startActivity(it);
                finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getCod (String s){//retorna os dados lidos pelo codigo do produto
        int ok = 0;

        try {
            ListaProdutoDB dbP = new ListaProdutoDB(this);


            if (s.length() >= 2) {
                List<ListaProduto> itemP =dbP.findbyCod(s);
                ok = itemP.size();
                if (ok > 0) {
                    String marca = itemP.get(0).marca_nome;
                    String prod = itemP.get(0).codProduto;
                    cod = itemP.get(0).cod_listaColeta;
                    String url =  itemP.get(0).urlFotoProduto;
                    Log.d("busca compara",marca +"produto > "+ prod + "=="+ s);
                    marca_coleta.setText(marca);
                    ListaColetaDB db = new ListaColetaDB(this);
                    List<ListaColeta> item = db.findbyCod(cod);
                    String produto = item.get(0).produto;
                    String tipo = item.get(0).tipo_coleta;
                    String cesta = item.get(0).cesta_coleta;

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(tipo +" - "+ cesta);
                    getSupportActionBar().setSubtitle(produto);
                    h = new Handler();
                    h.post(this);
                    //Picasso.with(this).load(url).into(img);
                    return 0;
                }else {

                    marca_coleta.setText("Produto não encontrado");
                    return 1;


                }
            }

        }catch (Exception e) {
            marca_coleta.setText("----ERROR----");
            return 0;
        }
        return 0;
    }

    public void BuscaCodigo(View view) {

        if (getCod(codBarras.getText().toString()) == 1){

            Snackbar snackbar;
            snackbar = Snackbar.make(view,"Produto", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("Cadastrar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(getBaseContext(),CadastroProdutos.class);
                    Bundle envia = new Bundle();
                    envia.putString("cod",cod);
                    TempCadastro.setCod(codBarras.getText().toString());
                    it.putExtras(envia);
                    startActivity(it);
                    finish();
                    TempAux.deOnde = 1;
                }
            });

        }


    }

    public void qr_busca(View view) {

        Intent it = new Intent("com.google.zxing.client.android.SCAN");
        if(IntentUtils.isAvailable(this,it)){
            startActivityForResult(it,0);
        }else {



            //AndroidUtils.alertDialog(this,"Atenção","Instale o app Barcode Scanner");

            try {

                //it = new Intent(Intent.ACTION_VIEW);
                //it.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
                //startActivity(it);

                Uri uri = Uri.parse("market://details?id=com.google.zxing.client.android");
                Intent it1 = new Intent(Intent.ACTION_VIEW,uri);

                startActivity(it1);

            }catch (Exception e){



                AndroidUtils.alertDialog(this,"Atenção -- Instale o APP ","Para poder ler o codigo de barras do produto, vc precisa instalar o app Barcode Scanner (Zxing)\n "+ e.getMessage());

            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        if(requestCode == 0) {
            if (resultCode == RESULT_OK){
                String codigo = it.getStringExtra("SCAN_RESULT");
                codBarras.setText(codigo);
                getCod(codigo);

            }
        }

        // super.onActivityResult(requestCode, resultCode, it);
    }

    @Override
    public void run() {

        ListaProdutoDB dbP = new ListaProdutoDB(this);
            List<ListaProduto> itemP =dbP.findbyCod(codBarras.getText().toString());

                String prod = itemP.get(0).codProduto;

        File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+prod+".jpg");
        if(file.exists()) {

            Bitmap b = decodeImageFile(file,200,200);
            img.setImageBitmap(b);
        }


    }



    //pega a data e poe na caixa de texto
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            data_coleta.setText(new StringBuilder().append(day).append("/").append(month+1).append("/").append(year));
        }
    }

    public void showDatePickerDialog(View view) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }

    private void setData_txt(){
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        data_coleta.setText(new StringBuilder().append(day).append("/").append(month+1).append("/").append(year));


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
}
