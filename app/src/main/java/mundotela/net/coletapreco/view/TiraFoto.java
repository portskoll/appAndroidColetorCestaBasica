package mundotela.net.coletapreco.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import livroandroid.lib.utils.AndroidUtils;
import mundotela.net.coletapreco.BuildConfig;
import mundotela.net.coletapreco.R;
import mundotela.net.coletapreco.util.PermissaFile;
import  mundotela.net.coletapreco.util.TempCadastro;
import livroandroid.lib.utils.ImageResizeUtils;
import livroandroid.lib.utils.SDCardUtils;


public class TiraFoto extends AppCompatActivity {

    private ImageView img;
    private File file;
    private StorageReference mountainsRef;
    private StorageReference mountainImagesRef;
    private StorageReference storageRef;
    private ProgressBar pb;
    private TextView status;
    private boolean enviar = false;
    private Boolean fotoEnviada = false;
    private Button OK;
    private String cod = "";
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tira_foto);
        PermissaFile.verifyStoragePermissions(this);//Cria um pergunta para permitir gravar no Android 6

        img = (ImageView)findViewById(R.id.img_foto_cad_emp);
        pb = (ProgressBar) findViewById(R.id.pb_enviar_foto_emp);
        pb.setVisibility(View.INVISIBLE);
        status = (TextView) findViewById(R.id.txt_status_foto);
        OK = (Button)findViewById(R.id.btn_foto_emp_OK);
        OK.setVisibility(View.INVISIBLE);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cadastro");
        actionBar.setSubtitle("Tirar Foto");
        //pega o codigo enviado
        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        cod = args.getString("cod");


        //para quando girar a tela
        if(savedInstanceState != null) {
            file = (File) savedInstanceState.getSerializable("file");
            showImage(file);
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl("gs://meusfilmes-eda8a.appspot.com");


// Create a reference to "mountains.jpg"
        //mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
        mountainImagesRef = storageRef.child("coleta_produtos/mountains.jpg");

// While the file names are the same, the references point to different files
        // mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        // mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("file",file);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_foto,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.mbate_foto){
            //cria o caminho do arquivo no sdCard

            file = SDCardUtils.getPrivateFile(getBaseContext(),"foto_produto_"+TempCadastro.getCod()+".jpg", Environment.DIRECTORY_PICTURES);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //codigo abaixo foi implementado por causa da versao androi 7.1.1
            Uri photoURI = FileProvider.getUriForFile(TiraFoto.this, BuildConfig.APPLICATION_ID + ".provider",file);
            //i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(i,0);
            fotoEnviada = false;
            OK.setVisibility(View.INVISIBLE);

            //Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", createImageFile());

        }
        if(id == R.id.menvia_foto) {

            if (enviar) {
                if (fotoEnviada) {
                    status.setText("Esta foto já foi enviada...");
                } else {


                        status.setText("A foto foi salva no celular...");
                        //TempCadastro.setUrlFoto(file.getAbsolutePath());
                        TempCadastro.setUrlFoto("foto_produto_"+TempCadastro.getCod()+".jpg");
                        OK.setVisibility(View.VISIBLE);
                        fotoEnviada = true;

                    /*}else {
                        // Create a reference to 'images/mountains.jpg'
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
                                fotoEnviada = true;
                                OK.setVisibility(View.VISIBLE);

                            }
                        });
                    }*/


                }//fim do else
            } else {
                status.setText("Tire a foto antes de enviar...");
            }



        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && file != null) {
            showImage(file);
            status.setText("Para enviar a foto click no botão Enviar");
            enviar = true;
        }


    }



    public void ok(View view) {
        Intent it = new Intent(getBaseContext(),CadastroProdutos.class);
        Bundle envia = new Bundle();
        envia.putString("cod",cod);
        it.putExtras(envia);
        startActivity(it);
        finish();


    }
}
