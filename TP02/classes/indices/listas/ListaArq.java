/** Pacotes **/
package TP02.classes.indices.listas;
import TP02.classes.Musica;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/** Classe ListaArq **/
public class ListaArq {
    /* Atributos */
    private final String pathNomes = "TP02/data/listaNomes.db", // paths dos arquivos
                         pathArtistas = "TP02/data/listaArtistas.db"; 
    private RandomAccessFile rafNomes, // arquivos 
                             rafArtistas;

    /* Construtor */
    public ListaArq() {
        // abrir ou criar arquivos 
        try{
            rafNomes = new RandomAccessFile(pathNomes, "rw");
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");

            // criar arquivos se nao existirem
            if(!exists()){

            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        } 
    }

    /* Metodos */
        /* Manipulacao dos arquivos */
    /**
     * Verifica se arquivos ja existem 
     * @return true se sim, false se nao
     */
    public boolean exists() {
        boolean existem = false;

        try{
            if(rafNomes.length() != 0 && rafArtistas.length() != 0)
                existem = true;
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return existem;
    }
    /**
     * Fecha arquivos RandomAcessFile
     */
    public void close() {
        try{
            rafNomes.close();
            rafArtistas.close();
        } catch(IOException ioe){
            System.err.println("Erro ao fechar arquivos");
            ioe.printStackTrace();
        }
    }
    /**
     * Deleta arquivos RandomAcessFile 
     * @return true se conseguir deletar, false caso contrario
     */
    public boolean deleteFiles() {
        boolean sucesso = false;
        
        File dir = new File(pathNomes);
        File bucket = new File(pathArtistas);
        
        if(dir.delete() && bucket.delete())
            sucesso = true;

        return sucesso;
    }
        /* Funcoes auxiliares */
    /**
     * Imprime lista invertida de nomes do arquivo
     */
    public void printNomes() {
        
    }
    /**
     * Imprime lista invertida de artistas do arquivo
     */
    public void printArtistas() {
        
    }
        /* Operacoes nas listas */
    /**
     * 
     * @return true se encontrar, false caso contrario
     */
    public boolean pesquisar() {
        boolean achou = false;

        try{
            // abre arquivos
            rafNomes = new RandomAccessFile(pathNomes, "rw");
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");
            
            
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return achou;
    }
    /**
     * 
     * @param obj Musica que foi criada
     * @param endereco long posicao no arquivo de dados da musica
     * @return true se conseguir criar listas, false se nao
     */
    public boolean create(Musica obj, long endereco) {
        boolean sucesso = false;

        try{
            // abre arquivos
            rafNomes = new RandomAccessFile(pathNomes, "rw");
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");
            
            
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }
    /**
     * 
     * @param chave int (ID) a ser deletado
     * @return true se conseguir deletar chave, false caso contrario
     */
    public boolean delete(int chave) {
        boolean sucesso = false;

        try{
            // abre arquivos
            rafNomes = new RandomAccessFile(pathNomes, "rw");
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");
            
            
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    } 
}
