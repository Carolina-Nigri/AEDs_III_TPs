/** Pacotes **/
package TP02.classes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** Classe Pagina **/
public class Pagina {
    /* Atributos */
    private int n, // numero de chaves atuais
                nMax, // numero maximo de chaves
                tamPag; // tamanho fixo da pagina (bytes)
    private long pos; // pos da pagina no arquivo
    private int[] chaves; // chaves armazenadas
    private long[] enderecos, // enderecos das chaves
                   filhas; // enderecos das paginas filhas

    /* Getters e Setters */ 
    public int getN(){
        return this.n;
    }
    public int getTamPag() {
        return tamPag;
    }
    public void setPar(int i, int chave, long endereco) {
        this.chaves[i] = chave;
        this.enderecos[i] = endereco;
        
        n++;
    }
    public void setPos(long pos){
        this.pos = pos;
    }
    public long getPos(){
        return this.pos;
    }
    public int getChave(int i) {
        return chaves[i];
    }
    public long getEndereco(int i) {
        return enderecos[i];
    }

/* 
    public long getFilha(int i) {
        return filhas[i];
    }
    public void setFilha(int i, long enderecoFilha) {
        this.filhas[i] = enderecoFilha;
    }
 */

    /* Construtor */  
    public Pagina(int nMax) {
        this(nMax, -1);
    }
    public Pagina(int nMax, long pos) {
        this.n = 0;
        this.nMax = nMax;
        this.pos = pos;

        // cria arrays p/armazenar max de chaves
        this.chaves = new int[nMax];
        this.enderecos = new long[nMax];
        for(int i = 0; i < nMax; i++){
            this.chaves[i] = -1;
            this.enderecos[i] = -1;
        }
        
        // cria array p/ponteiros das filhas (max => ordem = nMax + 1)
        this.filhas = new long[nMax+1];
        for(int i = 0; i < (nMax + 1); i++){
            this.filhas[i] = -1;
        }

        // tamanho da pagina = n (int) + (nMax) chaves e enderecos + (nMax+1) ponteiros
        this.tamPag = (Integer.BYTES) + (nMax * (Integer.BYTES + Long.BYTES)) + ((nMax + 1) * Long.BYTES);
    }

    /* Metodos */
        /* Basicos */
    /**
     * @return atributos da classe como string
     */  
    @Override
    public String toString() {
        String str = "| " + n;

        int i = 0;
        while(i < n){ // espaco de chaves e enderecos preenchidos 
            str += " | P(" + filhas[i] + ") | " + chaves[i] + " | " + enderecos[i];
            i++;
        }
        str += " | P(" + filhas[i] + ")";
        // /* 
        // imprimir se necessario, evitar p/nao ficar mta coisa na tela
        while(i < nMax){ // espaco de chaves e enderecos nao preenchidos 
            str += " | " + chaves[i] + " | " + enderecos[i];
            i++;
            str += " | P(" + filhas[i] + ")";
        }
        // */

        str += " |";

        return str;
    }
    /**
     * Converte objeto da classe para um array de bytes, escrevendo seus atributos
     * @return Byte array do objeto
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        try{
            dos.writeInt(n);
            
            int i = 0;
            while(i < nMax){
                dos.writeLong(filhas[i]);
                dos.writeInt(chaves[i]);
                dos.writeLong(enderecos[i]);
                i++;
            }
            dos.writeLong(filhas[i]);
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
        
        return baos.toByteArray();
    }
    /**
     * Converte um array de bytes para os atributos da classe, atribuindo
     * ao objeto corrente
     * @param byteArray array de bytes de um objeto
     */
    public void fromByteArray(byte[] ba) {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        try{
            n = dis.readInt();
            
            int i = 0;
            while(i < nMax){
                filhas[i] = dis.readLong();
                chaves[i] = dis.readInt();
                enderecos[i] = dis.readLong();
                i++;
            }
            filhas[i] = dis.readLong();
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
        /* Manipulacao da Arvore */
    /**
     * Deleta par da posicao passada da pagina, colocando valores de chave e endereco 
     * como -1 e diminuindo valor de n
     * @param i int posicao do par
     */
    public void deletePar(int i) {
        chaves[i] = -1;
        enderecos[i] = -1;
        n--;        
    }
    /**
     * 
     * @return
     */
    public boolean hasFilhas() {
        boolean temFilhas = false;
        
        int i = 0;
        while(i < filhas.length && !temFilhas){
            if(filhas[i] != -1)
                temFilhas = true;
            
            i++;
        }

        return temFilhas;
    }
    /**
     * 
     * @param chave
     * @param endereco
     */
    public void inserir(int chave, long endereco) {
        n++;
        
        int i = 0;
        while(i < n){
            if(chaves[i] == -1){
                chaves[i] = chave;
                enderecos[i] = endereco;
            } else if(chave < chaves[i]){
                int chaveTmp = chaves[i];
                long enderecoTmp = enderecos[i];
                
                chaves[i] = chave;
                enderecos[i] = endereco;

                chave = chaveTmp;
                endereco = enderecoTmp;
            }

            i++;
        }
    }
}
