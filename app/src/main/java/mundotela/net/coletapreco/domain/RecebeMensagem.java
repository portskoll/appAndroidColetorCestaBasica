package mundotela.net.coletapreco.domain;

import java.io.Serializable;

/**
 * Created by Henrique on 07/03/2017.
 */

public class RecebeMensagem implements Serializable{

    private static final long serialVersionUID = 1L;


    public long id;
    public String mensagem;
    public String endereco;
    public String urlFoto;
    public String nomeColeta;
    public String nomeCidade;
    public String nomeSuper;
    public String dataInicial;
    public String dataFinal;
    public String codSuper;
    public String codColeta;


    @Override
    public String toString() {
        return "RecebeMensagem{" +
                "codColeta='" + codColeta + '\'' +
                ", id=" + id +
                ", mensagem='" + mensagem + '\'' +
                ", endereco='" + endereco + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", nomeColeta='" + nomeColeta + '\'' +
                ", nomeCidade='" + nomeCidade + '\'' +
                ", nomeSuper='" + nomeSuper + '\'' +
                ", dataInicial='" + dataInicial + '\'' +
                ", dataFinal='" + dataFinal + '\'' +
                ", codSuper='" + codSuper + '\'' +
                '}';
    }
}
