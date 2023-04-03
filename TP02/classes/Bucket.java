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
                tamPar, // tamanho fixo do par chave e endereco (bytes)
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
    public int getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }
    public int getNMax() {
        return nMax;
    }
    public void setNMax(int nMax) {
        this.nMax = nMax;
    }
    public int getTamPar() {
        return tamPar;
    }
    public void setTamPar(int tamPar) {
        this.tamPar = tamPar;
    }
    public int getTamBucket() {
        return tamBucket;
    }
    public void setTamBucket(int tamBucket) {
        this.tamBucket = tamBucket;
    }
    public int[] getChaves() {
        return chaves;
    }
    public void setChaves(int[] chaves) {
        this.chaves = chaves;
    }
    public long[] getEnderecos() {
        return enderecos;
    }
    public void setEnderecos(long[] enderecos) {
        this.enderecos = enderecos;
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

        this.tamPar = Integer.BYTES + Long.BYTES; // chave: ID (int) + end.: (long)
        // tamanho do bucket = pLocal (int) + n (int) + (nMax pares)
        this.tamBucket = (2 * Integer.BYTES) + (tamPar * nMax);
    }

    /* Metodos */
    @Override
    public String toString() {
        String str = "pl = " + pLocal +
                "\nqtd(chaves) = " + n + "\n| ";

        int i = 0;
        while(i < n){ // espaco de chaves e enderecos preenchidos 
            str += chaves[i] + " => " + enderecos[i] + " | ";
            i++;
        }
        while(i < nMax){ // espaco de chaves e enderecos nao preenchidos 
            str += "NULL => NULL | ";
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

// TODO: organizar daqui pra baixo
    public boolean empty() {
        return n == 0;
    }
    public boolean full() {
        return n == nMax;
    }
    public boolean create(int c, long d) {
        if (full())
            return false;
        int i = n - 1;
        // empurrar pares
        while (i >= 0 && c < chaves[i]) {
            chaves[i + 1] = chaves[i];
            enderecos[i + 1] = enderecos[i];
            i--;
        }
        // colocar par
        i++;
        chaves[i] = c;
        enderecos[i] = d;
        n++;
        return true;
    }
    public long read(int c) {
        if (empty())
            return -1;
        int i = 0;
        // pesquisa linear no bucket
        while (i < n && c > chaves[i])
            i++;
        if (i < n && c == chaves[i])
            return enderecos[i];
        else
            return -1;
    }
    public boolean update(int c, long d) {
        if (empty())
            return false;
        int i = 0;
        // pesquisa linear no bucket
        while (i < n && c > chaves[i])
            i++;
        if (i < n && c == chaves[i]) {
            // trocar valor de endereco
            enderecos[i] = d;
            return true;
        } else
            return false;
    }
    public boolean delete(int c) {
        if (empty())
            return false;
        int i = 0;
        // pesquisa linear no bucket
        while (i < n && c > chaves[i])
            i++;
        if (c == chaves[i]) {
            // realocar pares
            while (i < n - 1) {
                chaves[i] = chaves[i + 1];
                enderecos[i] = enderecos[i + 1];
                i++;
            }
            n--;
            return true;
        } else
            return false;
    }
}
