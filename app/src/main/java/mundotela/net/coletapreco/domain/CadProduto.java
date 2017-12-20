package mundotela.net.coletapreco.domain;

import java.io.Serializable;

/**
 * Created by Henrique on 07/09/2016.
 */
public class CadProduto implements Serializable{
    private static final long serialVersionUID = 1L;

    public long id;
    public String cod;
    public String cod_col;
    //public String nome;
    public String marca;
    //public String tipo;
    //public String cesta;
    public String foto_file;
    public String foto_url;

    @Override
    public String toString() {
        return "CadProduto{" +
                "nome='" + marca + '\'' +
                '}';
    }
}
