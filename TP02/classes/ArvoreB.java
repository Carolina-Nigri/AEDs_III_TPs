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
    private int nMax; // numero maximo de chaves p/pagina da arvore B (ordem - 1)

    /* Construtores */  
    public ArvoreB() {
        this(8);
    }
    public ArvoreB(int ordem) {
        this.ordem = ordem;
        this.nMax = ordem - 1;
        
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
                Pagina pagina = new Pagina(nMax);
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
            Pagina pagRaiz = null;

            if(raiz == -1){ // raiz nao existe
                // endereco da pagina raiz
                raiz = Long.BYTES;
                
                // cria primeira pagina (raiz)
                pagRaiz = new Pagina(nMax, raiz);
                pagRaiz.setPar(0, chave, endereco);
            } else{
                // vai p/endereco da pagina raiz
                rafArvore.seek(raiz);

                // le pagina raiz do arquivo
                pagRaiz = new Pagina(nMax, raiz);
                byte[] byteArray = new byte[pagRaiz.getTamPag()];
                rafArvore.read(byteArray);
                pagRaiz.fromByteArray(byteArray);
                
                if(!pagRaiz.hasFilhas() && pagRaiz.getN() < nMax){ // insere na raiz
                    pagRaiz.inserir(chave, endereco);
                } else{ // chama insercao recursiva
                    pagRaiz = inserir(chave, endereco, raiz);
                }
            }

            // escreve endereco da pagina raiz no arquivo
            rafArvore.seek(0);
            rafArvore.writeLong(pagRaiz.getPos());

            // escreve pagina raiz no arquivo
            rafArvore.seek(pagRaiz.getPos());
            byte[] byteArray = pagRaiz.toByteArray();
            rafArvore.write(byteArray);
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }
    private Pagina inserir(int chave, long endereco, long pag) {
        Pagina pagina = null;

        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
           
            // le pagina
            rafArvore.seek(pag);
            pagina = new Pagina(nMax, pag);
            byte[] byteArray = new byte[pagina.getTamPag()];
            rafArvore.read(byteArray);
            pagina.fromByteArray(byteArray);
            
            if(pagina.getN() > nMax){ // nao tem espaco na pagina
                // boolean achou = false;
                // int i = 0;
                // while(i < nMax && !achou){
                //     if(chave > pagina.getChave(i)){
                //         i++;
                //     } else{
                //         achou = true;
                //     }
                // }
            }

        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        } 

        return pagina;
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
