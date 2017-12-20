package mundotela.net.coletapreco.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.net.URI;

import mundotela.net.coletapreco.R;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        getSupportActionBar().setTitle("App IFSP Barretos");
        getSupportActionBar().setSubtitle("Faça sua escolha abaixo");
    }

    public void pesquisa(View view) {

        Uri uri = Uri.parse("http://ifpesquisa.mundotela.net");
        Intent it = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(it);

    }

    public void coleta(View view) {
        Intent it = new Intent(this,Splash.class);
        startActivity(it);

    }
}
