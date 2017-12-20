package mundotela.net.coletapreco.servicos;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by Henrique on 18/09/2016.
 */
public class AtaskFotoColeta extends AsyncTask<Void, Void, Void> {

    Context context;
    ImageView img;
    String URL;

    public AtaskFotoColeta(Context context, ImageView img, String URL) {
        this.context = context;
        this.img = img;
        this.URL = URL;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Picasso.with(context).load(URL).into(img);
        return null;
    }
}
