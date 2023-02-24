/** Pacotes **/
package TP01.classes;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/** Classe CRUD **/
public class CRUD {

    RandomAccessFile arq;

    CRUD() throws FileNotFoundException{
        this.arq = new RandomAccessFile("Teste" , "rw");
    }
    CRUD(String nome) throws FileNotFoundException{
        this.arq = new RandomAccessFile(nome , "rw");
    }

    public static void create(Musica obj){
        
    }
}
