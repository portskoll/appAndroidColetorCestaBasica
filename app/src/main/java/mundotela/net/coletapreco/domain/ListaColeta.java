package mundotela.net.coletapreco.domain;

import java.io.Serializable;

/**
 * Created by Henrique on 12/09/2016.
 */
public class ListaColeta implements Serializable {
    private static final long serialVersionUID = 1L;

    public long id;
    public String produto;
    public String cod_coleta;
    public String tipo_coleta;
    public String cesta_coleta;
    public String qtde_t;
    public String qtde_f;

    @Override
    public String toString() {
        return "ListaColeta{" +
                "produto='" + produto + '\'' +
                '}';
    }
}
