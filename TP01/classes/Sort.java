/** Pacotes **/
package TP01.classes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/** Classe Sort **/
public class Sort {
    /* Atributos (constantes) */
    protected final String PATH = "TP01/data/";
    protected final int TAM = 10; // qtd de registros p/bloco
    protected final int ARQ = 4; // qtd de arquivos

    /* Getters */
    public String getPATH() {
        return PATH;
    }
    public int getTAM() {
        return TAM;
    }
    public int getARQ() {
        return ARQ;
    }

    /* Métodos */
        /* Intercalações balanceadas */
    /**
     * 
     * @param pathCRUD String do caminho do arquivo original a ser ordenado
     */
    public void intercalaComum(String pathCRUD) {
        createTmpFiles();

        distribuicao(pathCRUD);

        // intercalações

        if(!deleteTmpFiles())
            System.err.println("Erro ao deletar arquivos temporarios");
    }
    public void intercalaBlocosVar(String pathCRUD) {
        
    }
    public void intercalaSubstituicao(String pathCRUD) {
        
    }
        /* Auxiliares */
    /**
     * Cria arquivos temporarios (qtd = ARQ) 
     */
    private void createTmpFiles() {
        for(int i = 1; i <= ARQ; i++){
            try{
                RandomAccessFile tmp = new RandomAccessFile(PATH + "tmp"+i+".db", "rw");
                tmp.close();
            } catch(FileNotFoundException fnfe){
                System.err.println("Caminho de arquivo temporario nao encontrado");
                fnfe.printStackTrace();
            } catch(IOException ioe){
                System.err.println("Erro de I/O ao criar arquivo temporario para ordenacao");
                ioe.printStackTrace();
            }
        }
    }
    /**
     * Deleta arquivos temporarios (qtd = ARQ) 
     * @return true se conseguir deletar, false caso contrario
     */
    private boolean deleteTmpFiles() {
        boolean sucesso = false;

        for(int i = 1; i <= ARQ; i++){
            File tmp = new File(PATH + "tmp"+i+".db");
        
            if(tmp.delete())
                sucesso = true;
        }

        return sucesso;
    }
    /**
     * Distribui blocos de Musica nos ARQ/2 arquivos temporários, de forma alternada
     * @param pathCRUD String do caminho do arquivo original a ser ordenado 
     */
    private void distribuicao(String pathCRUD) {
        RandomAccessFile[] tmp = new RandomAccessFile[ARQ];

        // Abre metade dos arquivos tmp 
        try{ 
            for(int i = 0; i < (ARQ/2); i++){
                tmp[i] = new RandomAccessFile(PATH + "tmp"+(i+1)+".db", "rw");
                tmp[i].seek(0);
            }
        } catch(FileNotFoundException fnfe){
            System.err.println("Caminho de arquivo temporario nao encontrado");
            fnfe.printStackTrace();
        } catch(IOException ioe){
            System.err.println("Erro de I/O ao buscar no arquivo temporario na distribuicao");
            ioe.printStackTrace();
        }

        // Lê blocos do arquivo CRUD e salva nos temporários de forma intercalada
        try{
            CRUD crud = new CRUD(pathCRUD);
            long posCrud = Integer.BYTES; // posicao atual no arquivo original
            long crudLen = crud.arq.length() - 1;

            while(posCrud < crudLen){ // lê até fim do arquivo 
                // intercala arquivos tmp pra escrever blocos
                for(int i = 0; i < (ARQ/2) && posCrud < crudLen; i++){
                    // lê um bloco
                    ArrayList<Musica> bloco = crud.getBlock(posCrud, TAM);
                   
                    // ordena em memória principal
                    quicksort( bloco, 0, (bloco.size() - 1) );
                    
                    // escreve bloco no arquivo tmp
                    int j = 0;
                    while(j < bloco.size()){
                        byte[] regByte = bloco.get(j).toByteArray();
                        
                        int regSize = regByte.length;
                        posCrud += 1 + Integer.BYTES + regSize; // atualiza pos no arquivo CRUD
                        
                        tmp[i].write(regByte);

                        j++;
                    }
                }
            }
        } catch(FileNotFoundException fnfe){
            System.err.println("Arquivo original nao encontrado");
            fnfe.printStackTrace();
        } catch(IOException ioe){
            System.err.println("Erro de I/O ao ler do arquivo original na distribuicao");
            ioe.printStackTrace();
        }
    }
    /**
     * Quicksort de um bloco de Musica em memoria principal
     * @param bloco array de Musica
     * @param esq index mais à esquerda (início do array)
     * @param dir index mais à direita (fim do array)
     */
    private void quicksort(ArrayList<Musica> bloco, int esq, int dir) {
        int i = esq, j = dir;
        Musica pivo = bloco.get( (dir + esq) / 2 );

        while(i <= j){
            while(bloco.get(i).getDuration_ms() < pivo.getDuration_ms())
                i++;
            while(bloco.get(j).getDuration_ms() > pivo.getDuration_ms())
                j--;
            
            if(i <= j){
                swap(bloco, i, j);
                
                i++;
                j--;
            }
        }

        if(esq < j)
            quicksort(bloco, esq, j);
        if(i < dir)
            quicksort(bloco, i, dir);
    }
    /**
     * Troca musicas de indices i e j dentro do bloco
     * @param bloco array de Musica
     * @param i index de uma musica
     * @param j index de outra musica
     */
    private void swap(ArrayList<Musica> bloco, int i, int j) {
        Musica tmp = bloco.get(i).clone();

        bloco.set(i, bloco.get(j));
        bloco.set(j, tmp);
    }
}
