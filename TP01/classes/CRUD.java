/** Pacotes **/
package TP01.classes;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Classe CRUD **/
public class CRUD {
    /* Atributos */
    protected RandomAccessFile arq;
    protected String nome_arq;

    /* Construtores */
    public CRUD() throws FileNotFoundException { 
        this("teste");
    }
    public CRUD(String nome_arq) throws FileNotFoundException {
        this.nome_arq = nome_arq; 
        this.arq = new RandomAccessFile((this.nome_arq + ".db"), "rw");
    }

    /* Métodos */
    public void close() throws IOException {
        arq.close();
    }
    public void create(Musica obj) {
        
    }
    public void read(int ID) {
        
    }
    public void update(Musica obj) {
        
    }
    public void delete(int ID) {
        try{
            boolean removido = false;
            long pos, arqLen = arq.length();
            byte lapide;
            int regSize, regID;

            // posiciona ponteiro no início, pula cabeçalho e salva posição
            arq.seek(0); 
            arq.skipBytes(Integer.BYTES);
            pos = arq.getFilePointer(); 

            while(!removido && pos != arqLen){
                lapide = arq.readByte();
                regSize = arq.readInt();

                if(lapide == ' '){ // verifica se registro ainda não foi removido
                    regID = arq.readInt();

                    // verifica se é o ID do registro a ser removido
                    if(regID == ID){ 
                        arq.seek(pos); // retorna para posição da lápide
                        arq.writeByte('*');
                        removido = true;
                    }
                }

                arq.skipBytes(Integer.BYTES + regSize); // pula bytes do registro atual
                pos = arq.getFilePointer(); // início do próximo registro (lápide)
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
