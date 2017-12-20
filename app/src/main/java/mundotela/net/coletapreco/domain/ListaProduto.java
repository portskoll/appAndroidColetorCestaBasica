package mundotela.net.coletapreco.domain;

import java.io.Serializable;

/**
 * Created by Henrique on 16/09/2016.
 */
public class ListaProduto implements Serializable{

    private static final long serialVersionUID = 1L;

    public long id;
    public String codProduto;
    public String urlFotoProduto;
    public String cod_listaColeta;
    public String marca_nome;

    @Override
    public String toString() {
        return "ListaProduto{" +
                "marca_nome='" + marca_nome + '\'' +
                '}';
    }
}
