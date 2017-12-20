package mundotela.net.coletapreco.util;

import android.content.Context;
import android.content.Intent;

import mundotela.net.coletapreco.view.CadastroProdutos;

/**
 * Created by Henrique on 30/08/2016.
 */
public class TempCadastro {

    private static long id_p = 0;
    private static String cod = "";
    private static String produto = "";
    private static String marca_produto = "";
    private static String tipo_produto = "";
    private static String cesta = "";
    private static String urlFoto = "";

    public void zera_cad () {
        TempCadastro.setId_p(0);
        TempCadastro.setCod("");
        TempCadastro.setProduto("");
        TempCadastro.setMarca_produto("");
        TempCadastro.setTipo_produto("");
        TempCadastro.setCesta("");
        TempCadastro.setUrlFoto("");


    }

    public boolean ok_cad() {

        if (!TempCadastro.getCod().equals("") && !TempCadastro.getMarca_produto().equals("")) {

            return true;
        } else {

            return false;
        }
    }

    public static String getUrlFoto() {
        return urlFoto;
    }

    public static void setUrlFoto(String urlFoto) {
        TempCadastro.urlFoto = urlFoto;
    }

    public static String getCesta() {
        return cesta;
    }

    public static void setCesta(String cesta) {
        TempCadastro.cesta = cesta;
    }

    public static String getCod() {
        return cod;
    }

    public static void setCod(String cod) {
        TempCadastro.cod = cod;
    }

    public static String getMarca_produto() {
        return marca_produto;
    }

    public static void setMarca_produto(String marca_produto) {
        TempCadastro.marca_produto = marca_produto;
    }

    public static String getProduto() {
        return produto;
    }

    public static void setProduto(String produto) {
        TempCadastro.produto = produto;
    }

    public static String getTipo_produto() {
        return tipo_produto;
    }

    public static void setTipo_produto(String tipo_produto) {
        TempCadastro.tipo_produto = tipo_produto;
    }

    public static long getId_p() {
        return id_p;
    }

    public static void setId_p(long id_p) {
        TempCadastro.id_p = id_p;
    }
}
