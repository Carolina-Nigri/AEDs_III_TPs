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
        this.arq.close();
    }
    public void create(Musica obj) {
        
    }
    public void read(int ID) {
        
    }
    public void update(int ID) {
        
    }
    public void delete(int ID) {
        
    }
}
