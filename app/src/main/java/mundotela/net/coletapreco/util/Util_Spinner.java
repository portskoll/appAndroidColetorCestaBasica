package mundotela.net.coletapreco.util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import mundotela.net.coletapreco.R;

/**
 * Created by Henrique on 27/08/2016.
 */
public class Util_Spinner {

    private Spinner spinner;
    private TextView txt;
    private String[] st;


    public Util_Spinner(Spinner spinner,String[] st,TextView txt) {
        this.spinner = spinner;
        this.st = st;
        this.txt = txt;
    }

    public void setSpiner (Context ctx) {

        final Spinner spinner_util = spinner;
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_dropdown_item,st);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_util.setAdapter(adaptador);
        spinner_util.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt.setText(spinner_util.getSelectedItem().toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
