/** Pacotes **/
package TP02.classes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** Classe Bucket **/
public class Bucket {
    /* Atributos */
    private int pLocal, // profundidade local
                n, // numero de chaves atuais
                nMax, // numero maximo de chaves
                tamBucket; // tamanho fixo do bucket (bytes)
    private int[] chaves; // chaves armazenadas
    private long[] enderecos; // enderecos das chaves

    /* Getters e Setters */
    public int getPLocal() {
        return this.pLocal;
    }
    public void setPLocal(int pLocal) {
        this.pLocal = pLocal;
    }
    public int getN(){
        return this.n;
    }
    public int getTamBucket() {
        return tamBucket;
    }
    public int getChave(int i) {
        return chaves[i];
    }
    public void setChave(int i, int chave) {
        this.chaves[i] = chave;
        n++;
    }
    public long getEndereco(int i) {
        return enderecos[i];
    }
    public void setEndereco(int i, long endereco) {
        this.enderecos[i] = endereco;
    }

    /* Construtores */
    public Bucket(int nMax) {
        this(nMax, 1);
    }
    public Bucket(int nMax, int pLocal) {
        this.pLocal = pLocal;
        this.n = 0;
        this.nMax = nMax;

        // cria arrays p/armazenar max de chaves (tamanho do bucket)
        this.chaves = new int[nMax];
        this.enderecos = new long[nMax];
        for(int i = 0; i < nMax; i++){
            this.chaves[i] = -1;
            this.enderecos[i] = -1;
        }

        // tamanho bucket = pLocal (int) + n (int) + (nMax pares)
        this.tamBucket = (2 * Integer.BYTES) + (nMax * (Integer.BYTES + Long.BYTES));
    }

    /* Metodos */
    @Override
    public String toString() {
        String str = "pl = " + pLocal +
                     "\nn = " + n + "\n";

        int i = 0;
        while(i < n){ // espaco de chaves e enderecos preenchidos 
            str += "| " + chaves[i] + " => " + enderecos[i] + " ";
            i++;
        }
        while(i < nMax){ // espaco de chaves e enderecos nao preenchidos 
            str += "| " + chaves[i] + " => " + enderecos[i] + " ";
            i++;
        }
        
        return str;
    }
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        try{
            dos.writeInt(pLocal);
            dos.writeInt(n);
            
            for(int i = 0; i < nMax; i++) {
                dos.writeInt(chaves[i]);
                dos.writeLong(enderecos[i]);
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
        
        return baos.toByteArray();
    }
    public void fromByteArray(byte[] ba) {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        try{
            pLocal = dis.readInt();
            n = dis.readInt();
            
            for(int i = 0; i < nMax; i++) {
                chaves[i] = dis.readInt();
                enderecos[i] = dis.readLong();
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    
    public void deletePar(int i) {
        chaves[i] = -1;
        enderecos[i] = -1;
        n--;        
    }
    public void reorganizarChaves() {
        int[] chavesTmp = new int[n];
        long[] enderecosTmp = new long[n];
        
        // copia so chaves e enderecos validos, limpa o resto
        int i = 0, j = 0;
        while(i < nMax){
            if(chaves[i] != -1){
                chavesTmp[j] =  chaves[i];
                enderecosTmp[j] = enderecos[i];
                j++;
                
                chaves[i] = -1;
                enderecos[i] = -1;
            } 
              
            i++;
        }

        // copia tmps de volta pros originais
        for(int k = 0; k < n; k++){
            chaves[k] = chavesTmp[k];
            enderecos[k] = enderecosTmp[k];
        }
    }
}
