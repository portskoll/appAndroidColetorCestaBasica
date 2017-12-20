package mundotela.net.coletapreco.domain;

import java.io.Serializable;

/**
 * Created by Henrique on 19/09/2016.
 */
public class CadColeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public long id;
    public String cod_produto;
    public String data_coleta;
    public String valor_coleta;
    public String cod_coletax;

    public boolean selected;

    @Override
    public String toString() {
        return "CadColeta{" +
                "cod_coletax='" + cod_coletax + '\'' +
                ", id=" + id +
                ", cod_produto='" + cod_produto + '\'' +
                ", data_coleta='" + data_coleta + '\'' +
                ", valor_coleta='" + valor_coleta + '\'' +
                ", selected=" + selected +
                '}';
    }
}
