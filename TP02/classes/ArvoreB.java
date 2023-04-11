/** Pacotes **/
package TP02.classes;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/** Classe ArvoreB **/
public class ArvoreB {
    /* Atributos */
    private final String pathArvore = "TP02/data/arvoreB.db"; // path do arquivo
    private RandomAccessFile rafArvore; // arquivo
    private int ordem; // ordem da arvore B (qtd max de filhos)

    /* Construtores */  
    public ArvoreB() {
        this(8);
    }
    public ArvoreB(int ordem) {
        this.ordem = ordem;
        
        // abrir ou criar arquivo
        try{
            rafArvore = new RandomAccessFile(pathArvore, "rw");
            
            // criar arquivo se nao existir
            if(!exists()){
                // escreve endereco da pagina raiz no arquivo
                rafArvore.seek(0);
                long raiz = -1;
                rafArvore.writeLong(raiz);
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }

    /* Metodos */
        /* Manipulacao do arquivo */
    /**
     * Verifica se arquivo ja existe 
     * @return true se sim, false se nao
     */
    public boolean exists() {
        boolean existe = false;

        try{
            if(rafArvore.length() != 0)
                existe = true;
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return existe;
    }
    /**
     * Fecha arquivo RandomAcessFile
     */
    public void close() {
        try{
            rafArvore.close();
        } catch(IOException ioe){
            System.err.println("Erro ao fechar arquivo");
            ioe.printStackTrace();
        }
    }
    /**
     * Deleta arquivo RandomAcessFile 
     * @return true se conseguir deletar, false caso contrario
     */
    public boolean deleteFile() {
        boolean sucesso = false;
        
        File file = new File(pathArvore);
        
        if(file.delete())
            sucesso = true;

        return sucesso;
    }
        /* Funcoes auxiliares */
    /**
     * Imprime a arvore B da forma como esta armazenada em arquivo
     */
    public void print() {
        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");

            // le e imprime endereco da pagina raiz
            rafArvore.seek(0);           
            long raiz = rafArvore.readLong();
            System.out.println("0 | raiz = " + raiz + " |");

            // imprime paginas
            long pos = rafArvore.getFilePointer();
            while(pos != rafArvore.length()){
                Pagina pagina = new Pagina(ordem - 1);
                byte[] byteArray = new byte[pagina.getTamPag()];
                
                rafArvore.read(byteArray);
                pagina.fromByteArray(byteArray);
                System.out.println(pos + " " + pagina);

                pos = rafArvore.getFilePointer();
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }   
    }
        /* CRUD do indice */
    /**
     * 
     * @param chave
     * @param endereco
     * @return
     */
    public boolean create(int chave, long endereco) {
        boolean sucesso = false;

        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
            
            // recupera endereco da pagina raiz no arquivo
            rafArvore.seek(0);
            long raiz = rafArvore.readLong();

            if(raiz == -1){ // raiz nao existe
                // cria primeira pagina (raiz)
                Pagina pagRaiz = new Pagina(ordem - 1);
                pagRaiz.setPar(0, chave,endereco);
                
                // escreve endereco da pagina raiz no arquivo
                rafArvore.seek(0);
                raiz = Long.BYTES;
                rafArvore.writeLong(raiz);
                // escreve pagina raiz no arquivo
                byte[] byteArray = pagRaiz.toByteArray();
                rafArvore.write(byteArray);
            } else{
                // vai p/endereco da pagina raiz
                rafArvore.seek(raiz);

                // le pagina do arquivo
                Pagina pagRaiz = new Pagina(ordem - 1);
                byte[] byteArray = new byte[pagRaiz.getTamPag()];
                rafArvore.read(byteArray);
                pagRaiz.fromByteArray(byteArray);

                if(pagRaiz.getN() < (ordem-1)){ // tem espaco na raiz
                    for(int i = 0; i < pagRaiz.getN(); i++){
                        
                    }
                } else{ // faz recursao p/inserir

                }
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }
    /**
     * 
     * @param chave
     * @return
     */
    public long read(int chave) {
        long endereco = -1;

        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
       
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return endereco;
    }
    /**
     * 
     * @param chave
     * @return
     */
    public boolean delete(int chave) {
        boolean sucesso = false;

        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
       
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }
}
