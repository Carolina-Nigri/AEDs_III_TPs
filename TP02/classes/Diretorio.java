/** Pacotes **/
package TP02.classes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** Classe Diretorio **/
public class Diretorio {
    /* Atributos */
    private int pGlobal; // profundidade global
    private long[] enderecos; // array de enderecos dos buckets

    /* Getters e Setters */
    public int getPGlobal() {
        return this.pGlobal;
    }
    public void setPGlobal(int pGlobal) {
        this.pGlobal = pGlobal;
    }

    /* Construtores */
    public Diretorio() {
        this(1);
    }
    public Diretorio(int pGlobal) {
        this.pGlobal = pGlobal; // profundidade comeca em 1
        this.enderecos = new long[ (int)Math.pow(2, pGlobal) ];
        
        for(int i = 0; i < this.enderecos.length; i++){
            this.enderecos[i] = 0; // TODO: verificar como enderecos devem ser inicializados
        }
    }
    
    /* Metodos */  
    @Override
    public String toString() {
        String str = "pg = " + pGlobal;
        
        int n = (int) Math.pow(2, pGlobal);
        for(int i = 0; i < n; i++){
            str += "\n" + i + ": " + enderecos[i];
        }

        return str;
    }
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        try{
            dos.writeInt(this.pGlobal);
            
            for(int i = 0; i < this.enderecos.length; i++){
                dos.writeLong(this.enderecos[i]);
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
            pGlobal = dis.readInt();
            
            int n = (int) Math.pow(2, pGlobal);
            enderecos = new long[n];
            
            for(int i = 0; i < n; i++){
                enderecos[i] = dis.readLong();
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }

// TODO: organizar daqui pra baixo  
    public int hash(int chave) {
        return chave % (int) Math.pow(2, pGlobal);
    }
    public int hash2(int chave, int pl) { // cálculo do hash para profundidade local
        return chave % (int) Math.pow(2, pl);
    }
    public long enderecoBucket(int pos) {
        long endereco = -1; 

        if(pos < Math.pow(2, pGlobal) && pos >= 0)
            endereco = enderecos[pos];
        
        return endereco;
    }
    public boolean atualizarEndereco(int p, long e) {
        boolean sucesso = false;

        // testar se a profundidade existe
        if(p > Math.pow(2, pGlobal))
            return false;
        // atualizar
        enderecos[p] = e;

        return sucesso;
    }
    public boolean aumentarGlobal() {
        if (pGlobal >= 127)
            return false;
        pGlobal++;
        int n1 = (int) Math.pow(2, pGlobal - 1); // metade
        int n2 = (int) Math.pow(2, pGlobal); // tamanho total
        // novo tamanho
        long[] newEnderecos = new long[n2];
        int i = 0;
        // colocar primeira metade dos enderecos
        while (i < n1) {
            newEnderecos[i] = enderecos[i];
            i++;
        }
        // colocar segunda metade dos enderecos
        while (i < n2) {
            newEnderecos[i] = enderecos[i - n1];
            i++;
        }
        enderecos = newEnderecos;
        return true;
    }
}
