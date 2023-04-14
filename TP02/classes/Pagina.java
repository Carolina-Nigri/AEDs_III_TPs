/** Pacotes **/
package TP02.classes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** Classe Pagina **/
class Pagina {
    /* Atributos */
    private boolean folha; // indica se a pagina eh folha ou nao
    private int nMax, // qtd max de chaves
                n, // qtd atual de chaves 
                tamPag; // tamanho fixo da pagina (bytes)
    private long posArq; // posicao da pagina em arquivo
    private int[] chaves;
    private long[] enderecos;
    private Pagina[] filhas;

    /* Getters e Setters */ 
    public boolean isFolha() {
        return folha;
    }
    public void setFolha(boolean folha) {
        this.folha = folha;
    }
    public int getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }
    public long getPosArq() {
        return this.posArq;
    }
    public void setPosArq(long posArq) {
        this.posArq = posArq;
    }
    public int getChave(int i) {
        return chaves[i];
    }
    public void setChave(int i, int chave) {
        this.chaves[i] = chave;
    }
    public long getEndereco(int i) {
        return enderecos[i];
    }
    public void setEndereco(int i, long endereco) {
        this.enderecos[i] = endereco;
    }
    public Pagina getFilha(int i) {
        return filhas[i];
    }
    public void setFilha(int i, Pagina filha) {
        this.filhas[i] = filha;
    }   

    /* Construtor */
    public Pagina(int nMax, boolean folha) {
        this.folha = folha;
        this.nMax = nMax;
        this.n = 0;

        // tamanho da pagina = folha + n + (nMax) chaves e enderecos + (nMax+1) filhas
        this.tamPag = 1 + Integer.BYTES + (nMax * (Integer.BYTES+Long.BYTES)) + ((nMax+1) * Long.BYTES); 
        this.posArq = -1;

        this.chaves = new int[nMax];
        this.enderecos = new long[nMax];
        for(int i = 0; i < nMax; i++){
            this.chaves[i] = -1;
            this.enderecos[i] = -1;
        }

        this.filhas = new Pagina[nMax+1];
        for(int i = 0; i < (nMax+1); i++){
            this.filhas[i] = null;
        }
    }

    /* Metodos */
       /* Manipulacao da Arvore */
    /**
     * Insere chave na pagina, procurando posicao de forma a manter ordenada
     * @param chave identificador a ser inserido
     * @param endereco posicao em arquivo da chave
     */
    public void inserir(int chave, long endereco) {
        int i = n - 1; 
        // copia chaves maiores p/proximas posicoes
        while(i >= 0 && chave < chaves[i]){
            chaves[i+1] = chaves[i];
            enderecos[i+1] = enderecos[i];
            i--;
        }
        // achou posicao de insercao da chave
        chaves[i+1] = chave;
        enderecos[i+1] = endereco; 
        n++;
    }
    /**
     * Achar em qual pagina filha deve-se inserir a chave, retornando sua posicao 
     * @param chave identificador a ser inserido
     * @return int posicao da pagina filha
     */
    public int acharPosFilha(int chave) {
        int i = n - 1; 
        while(i >= 0 && chave < chaves[i]){
            i--;
        }
        i++; // posicao da pagina filha

        return i;
    }
}
