/** Pacotes **/
package TP02.classes.indices.listas;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** Classe LinhaLista **/
public class LinhaLista {
    /* Atributos */
    private String termo;
    private int n;
    private long[] ocorrencias;

    /* Getters e Setters */
    public String getTermo() {
        return termo;
    }
    public void setTermo(String termo) {
        this.termo = termo;
    }
    public long getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }
    public long getOcorrencia(int i) {
        return ocorrencias[i];
    }
    public void setOcorrencia(int i, long ocorrencia) {
        this.ocorrencias[i] = ocorrencia;
    }

    /* Construtores */
    public LinhaLista(String termo){
        this.termo = termo;
        this.n = 0;
        this.ocorrencias = null;
    }
    
    /* Metodos */
        /* Basicos */
    /**
     * @return atributos da classe como string
     */  
    @Override
    public String toString() {
        String str = "" + termo + " | n = " + n;

        for(int i = 0; i < n; i++){
            str += " | " + ocorrencias[i];
        }

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
            dos.writeUTF(termo);
            dos.writeInt(n);
            for(int i = 0; i < n; i++){
                dos.writeLong(ocorrencias[i]);
            }
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
            termo = dis.readUTF();
            n = dis.readInt();
            for(int i = 0; i < n; i++){
                ocorrencias[i] = dis.readLong();
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
}
