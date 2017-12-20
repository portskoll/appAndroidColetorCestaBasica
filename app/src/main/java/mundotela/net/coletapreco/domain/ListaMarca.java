package mundotela.net.coletapreco.domain;

/**
 * Created by Henrique on 15/09/2016.
 */
import java.io.Serializable;

/**
 * Created by Henrique on 12/09/2016.
 */
public class ListaMarca implements Serializable {
    private static final long serialVersionUID = 1L;

    public long id;
    public String cod_marca;
    public String marca;

    @Override
    public String toString() {
        return "ListaMarca{" +
                "marca='" + marca + '\'' +
                '}';
    }
}
