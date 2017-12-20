package mundotela.net.coletapreco.view;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import livroandroid.lib.utils.ImageResizeUtils;
import livroandroid.lib.utils.IntentUtils;
import livroandroid.lib.utils.SDCardUtils;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.db.CadProdudoDB;
import mundotela.net.coletapreco.db.ListaColetaDB;
import mundotela.net.coletapreco.db.ListaMarcaDB;
import mundotela.net.coletapreco.domain.CadProduto;
import mundotela.net.coletapreco.domain.ListaColeta;
import mundotela.net.coletapreco.domain.ListaMarca;
import mundotela.net.coletapreco.servicos.CadOnlineProdutos;
import mundotela.net.coletapreco.util.Pref_Salva;
import mundotela.net.coletapreco.util.TempAux;
import mundotela.net.coletapreco.util.TempCadastro;

//import static com.google.android.gms.analytics.internal.zzy.c;

public class CadastroProdutos extends AppCompatActivity {

    private TextView txt_produto;
    //private Spinner spinner;
    private TextView txt_marca;
    private TextView txt_tipo_produto;
    private TextView txt_cesta;
    private EditText edt_cod_prod;
    private AutoCompleteTextView autoMarca;
    private ImageView img;
    private File file;
    private ImageButton imgB;
    private ProgressBar pb;
    private TextView status;
    private String URL ="http://ifspcoletor.mundotela.net/app/cad_produto.php";
    private String chave = "30be91";
    private String cod = "";
    private List<ListaMarca> lista;
    private String[] lMarca;
    private StorageReference mountainImagesRef;
    private StorageReference storageRef;
    private Button voltaColeta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadstro_produtos);

        //pega o codigo enviado
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        cod = args.getString("cod");

        //Banner AdMob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        voltaColeta = (Button) findViewById(R.id.btn_coleta_volta);



        try {

            ListaColetaDB db = new ListaColetaDB(this);
            List<ListaColeta> item = db.findbyCod(cod);
            String produto = item.get(0).produto;
            String tipo = item.get(0).tipo_coleta;
            String cesta = item.get(0).cesta_coleta;

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(tipo +"-"+ cesta);
            getSupportActionBar().setSubtitle(produto);

            txt_produto = (TextView) findViewById(R.id.txt_produto);
            txt_produto.setText(produto);
            //spinner = (Spinner) findViewById(R.id.spinner);
            //txt_marca = (TextView)findViewById(R.id.txt_marca_produto);
            //txt_marca.setText(TempCadastro.getMarca_produto());
            autoMarca = (AutoCompleteTextView) findViewById(R.id.txtauto_marca_cadProduto);
            autoMarca.setText(TempCadastro.getMarca_produto());
            ListaMarcaDB db_marca = new ListaMarcaDB(this);
            lista = db_marca.findAll();
            lMarca = new String[lista.size()];
            for(int i=0;i < lista.size() ;i++){
                lMarca[i]=lista.get(i).marca;
            }
            setAutoMarca(lMarca);

            txt_tipo_produto = (TextView)findViewById(R.id.txt_tipo_produto);
            txt_tipo_produto.setText(tipo);
            txt_cesta = (TextView)findViewById(R.id.txt_cesta);
            txt_cesta.setText(cesta);
            edt_cod_prod = (EditText)findViewById(R.id.edt_cod_produto);
            edt_cod_prod.setText(TempCadastro.getCod());


        }catch (Exception e){
            Toast.makeText(this,"Erro ao ler o banco de Coleta" + e.getMessage(),Toast.LENGTH_SHORT).show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Erro");
            getSupportActionBar().setSubtitle("Banco Coleta");
        }
        imgB = (ImageButton) findViewById(R.id.imgB_qrcode);
        file = SDCardUtils.getPrivateFile(getBaseContext(),"foto_produto_"+TempCadastro.getCod()+".jpg", Environment.DIRECTORY_PICTURES);
        img = (ImageView)findViewById(R.id.img_foto_produto);
        edt_cod_prod = (EditText)findViewById(R.id.edt_cod_produto);
        edt_cod_prod.setText(TempCadastro.getCod());
        pb = (ProgressBar)findViewById(R.id.pb_envia_cad_online);
        status = (TextView)findViewById(R.id.txt_status_cad_online);
        //escolheMarcaProduto(ListaCatMarcas.tM_2);

        if(file.exists()) {
            img.setVisibility(View.VISIBLE);
            imgB.setVisibility(View.GONE);
            showImage(file);
        }


    }
    //insere alista no autocompleteTexto
    private void setAutoMarca (String[] lista){
        ArrayAdapter <String> adptador = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,lista);
        autoMarca.setAdapter(adptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tipo_marca,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

                TempCadastro NCad = new TempCadastro();
                NCad.zera_cad();
                Intent it = new Intent(this,PainelColeta.class);
                Bundle envia = new Bundle();
                envia.putString("modo","mP");
                it.putExtras(envia);
                startActivity(it);
                finish();

            return true;
        }

        if ( id == R.id.mTirafoto) {

            TempCadastro CAD = new TempCadastro();
            TempCadastro.setCod(edt_cod_prod.getText().toString());
            TempCadastro.setProduto(txt_produto.getText().toString());
            TempCadastro.setMarca_produto(autoMarca.getText().toString());
            TempCadastro.setTipo_produto(txt_tipo_produto.getText().toString());
            TempCadastro.setCesta(txt_cesta.getText().toString());

            if (CAD.ok_cad() ){
                Intent it = new Intent(getBaseContext(),TiraFoto.class);
                Bundle envia = new Bundle();
                envia.putString("cod",cod);
                it.putExtras(envia);
                startActivity(it);
                finish();


            }else {
                AndroidUtils.alertDialog(this,"**Atenção**","Preencha todo o cadastro  de produtos antes de tirar a foto...");
            }
        }

        if ( id == R.id.mEnviarCad) {

            TempCadastro CADv = new TempCadastro();
            if (CADv.ok_cad() ) {

                if (AndroidUtils.isNetworkAvailable(getBaseContext())){

                    //pega a url da foto

                    // Create a reference to 'images/mountains.jpg'
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    storageRef = storage.getReferenceFromUrl("gs://meusfilmes-eda8a.appspot.com");

                    // Create a reference to 'images/mountains.jpg'
                    //mountainImagesRef = storageRef.child("coleta_produtos/mountains.jpg");


                    pb.setVisibility(View.VISIBLE);
                    status.setText("Enviando a foto para nuvem...");
                    //i = PrefCadEmpresa.getEmpID(getApplicationContext(),"id");

                    mountainImagesRef = storageRef.child("coleta_produtos/foto_produto_"+TempCadastro.getCod()+".jpg");

                    // Get the data from an ImageView as bytes
                    img.setDrawingCacheEnabled(true);
                    img.buildDrawingCache();
                    Bitmap bitmap = img.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = mountainImagesRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata();// contains file metadata such as size, content-type, and download URL.
                            String foto_emp = String.valueOf(taskSnapshot.getDownloadUrl());
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            pb.setVisibility(View.INVISIBLE);
                            TempCadastro.setUrlFoto(foto_emp);
                            Log.d("urlFirebase", foto_emp);
                            status.setText("Foto enviada com Sucesso.");

                            CadOnlineProdutos enviaCad = new CadOnlineProdutos(CadastroProdutos.this,pb, status, TempCadastro.getCesta(), chave, TempCadastro.getCod(), Pref_Salva.getUser(CadastroProdutos.this,"chave"),
                                    autoMarca.getText().toString(), cod, TempCadastro.getTipo_produto(), URL, TempCadastro.getUrlFoto());
                            enviaCad.execute();
                        }
                    });




                }else {


                    CadProduto p = new CadProduto();
                    p.id = TempCadastro.getId_p();
                    p.cod = TempCadastro.getCod();
                    //p.nome = TempCadastro.getProduto();
                    //p.tipo = TempCadastro.getTipo_produto();
                    p.marca = autoMarca.getText().toString();
                    p.cod_col = cod;
                   // p.cesta = TempCadastro.getCesta();
                    p.foto_file = TempCadastro.getUrlFoto();
                    p.foto_url = "pendente";

                    CadProdudoDB db = new CadProdudoDB(this);
                    try {
                        int temCod = db.findbyCod(p.cod).size();
                        if (temCod > 0){
                            db.save(p,temCod);
                            status.setText("Dados atualizados neste dispositivo...");
                            if(TempAux.deOnde == 1){
                                voltaColeta.setVisibility(View.VISIBLE);
                            }
                        }else {
                            db.save(p,temCod);
                            status.setText("Dados salvos neste dispositivo...");
                            if(TempAux.deOnde == 1){
                                voltaColeta.setVisibility(View.VISIBLE);
                            }
                        }

                    }finally {
                        db.close();
                    }
                }

            }else {
                AndroidUtils.alertDialog(this,"**Atenção**","Tire a foto do produto antes de enviar os dados...");
            }
        }



        if ( id == R.id.mListaCad) {

            Intent it = new Intent(this,ListaCadProduto.class);
            Bundle envia = new Bundle();
            envia.putString("cod",cod);
            it.putExtras(envia);
            startActivity(it);
            finish();


        }





        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        if(requestCode == 0) {
            if (resultCode == RESULT_OK){
                String codigo = it.getStringExtra("SCAN_RESULT");
                edt_cod_prod.setText(codigo);
            }
        }

       // super.onActivityResult(requestCode, resultCode, it);
    }


    private void showImage(File file) {
        if(file != null && file.exists()) {
            Log.d("foto",file.getAbsolutePath());
            int w = img.getWidth();
            int h = img.getHeight();
            //redimenciona a imagem
            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(file),w,h,false);
            img.setImageBitmap(bitmap);
        }
    }

    public void codigoBarras(View view){

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

    public void voltaColeta(View view) {

        Intent it = new Intent(getBaseContext(),CadastroColeta.class);
        Bundle envia = new Bundle();
        envia.putString("cod",cod);
        envia.putString("codP","-1");
        it.putExtras(envia);
        startActivity(it);
        finish();
        TempAux.deOnde = 2;
    }
}
