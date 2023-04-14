/** Pacotes **/
package TP02.classes;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/** Classe ArquivoArvore **/
public class ArvoreArq {
    /* Atributos */
    private final String pathArvore = "TP02/data/arvoreB.db"; // path do arquivo
    private RandomAccessFile rafArvore; // arquivo
    private int ordem; // ordem da arvore B
    private ArvoreB arvore;

    /* Construtor */  
    public ArvoreArq() {
        this(8);
    }
    public ArvoreArq(int ordem) {
        this.ordem = ordem;
        this.arvore = new ArvoreB(ordem);
        
        // abrir ou criar arquivo
        try{
            rafArvore = new RandomAccessFile(pathArvore, "rw");
            
            // criar arquivo se nao existir
            if(!exists()){
                // escreve endereco da pagina raiz no arquivo
                rafArvore.seek(0);
                rafArvore.writeLong(-1);
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
            
            // le endereco da raiz e imprime
            rafArvore.seek(0);
            long pontRaiz = rafArvore.readLong();
            System.out.println("0 | raiz = " + pontRaiz + " |");

            // le e imprime paginas
            long pos = rafArvore.getFilePointer();
            while(pos != rafArvore.length()){
                Pagina pag = lePagina(pos);
                System.out.println(pos + " " + pag);

                pos = rafArvore.getFilePointer(); 
            }   
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }   
    }
    /**
     * Escreve arvore B em arquivo, colocando endereco da raiz primeiro, depois as paginas
     * em suas respectivas posicoes (chama funcao recursiva que escreve as paginas)
     */
    private void escreveArvore() {
        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
            rafArvore.seek(0);

            // escreve arvore
            if(arvore.getRaiz() == null){ // verifica se raiz existe
                rafArvore.writeLong(-1);
            } else{
                // escreve endereco da raiz
                rafArvore.writeLong(arvore.getRaiz().getPosArq());

                // escreve paginas no arquivo
                escrevePaginas(arvore.getRaiz(), arvore.getRaiz().getPosArq());
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    /**
     * Escreve paginas, recursivamente, em arquivo, passando do objeto Pagina para bytes
     * @param pag Pagina atual
     * @param pos endereco em arquivo da pagina atual
     */
    private void escrevePaginas(Pagina pag, long pos) {
        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
            
            if(pag != null){
                byte[] pagByte = pag.toByteArray();
                rafArvore.seek(pos);
                rafArvore.write(pagByte);
                
                // pagina nao eh folha => chama funcao p/filhas 
                if(!pag.isFolha()){
                    for(int j = 0; j < (pag.getN()+1); j++){
                        escrevePaginas(pag.getFilha(j), pag.getFilha(j).getPosArq());
                    }
                }
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    
    private void leArvore() {
        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
            
            // le endereco da raiz
            rafArvore.seek(0);
            long pontRaiz = rafArvore.readLong();

            if(pontRaiz == -1){ // verifica se raiz existe
                arvore.setRaiz(null);
            } else{
                // le raiz
                Pagina raiz = lePagina(pontRaiz);
                arvore.setRaiz(raiz);
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    
    private Pagina lePagina(long pos){
        Pagina pag = null;

        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");

            if(pos > 0 && pos != rafArvore.length()){
                rafArvore.seek(pos);

                // setar posicao em arquivo da pagina
                pag = new Pagina((ordem-1), false);
                pag.setPosArq(pos);

                // le se pagina eh folha ou nao
                byte bFolha = rafArvore.readByte();
                if(bFolha == '*') pag.setFolha(true);

                // le qtd de chaves
                pag.setN(rafArvore.readInt());
                
                // le ponteiros p/filhas e pares chave/endereco
                int i = 0;
                while(i < (ordem-1)){
                    long posFilha = rafArvore.readLong();
                    if(posFilha == -1){ // nao tem filha
                        pag.setFilha(i, null);
                    } else{ // le pagina filha (recursivamente)
                        Pagina filha = lePagina(posFilha);
                        pag.setFilha(i, filha);
                    } 
                    
                    pag.setChave(i, rafArvore.readInt());
                    pag.setEndereco(i, rafArvore.readLong());
                    i++;
                }
                long posFilha = rafArvore.readLong();
                if(posFilha == -1){ // nao tem filha
                    pag.setFilha(i, null);
                } else{ // le pagina filha (recursivamente)
                    Pagina filha = lePagina(posFilha);
                    pag.setFilha(i, filha);
                }
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return pag;
    }
        /* CRUD do indice */
   
    public boolean create(int chave, long endereco) {
        boolean sucesso = false;

        try{
            // abre arquivo
            rafArvore = new RandomAccessFile(pathArvore, "rw");
            
            // le endereco da raiz
            rafArvore.seek(0);
            long pontRaiz = rafArvore.readLong();
            
            // se arvore nao estiver vazia, le do arquivo
            if(pontRaiz != -1){
                leArvore(); 
            }
            
            // faz insercao na arvore
            sucesso = arvore.inserir(chave, endereco);
            
            // escreve de volta no arquivo
            escreveArvore();
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }

    // TODO: implementar Read e Delete

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
