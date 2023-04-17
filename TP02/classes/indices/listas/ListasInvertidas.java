/** Pacotes **/
package TP02.classes.indices.listas;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/** Classe ListaArq **/
public class ListasInvertidas {
    /* Atributos */
    private final String pathNomes = "TP02/data/listaNomes.db", // paths dos arquivos
                         pathArtistas = "TP02/data/listaArtistas.db"; 
    private RandomAccessFile rafNomes, // arquivos 
                             rafArtistas;

    /* Construtor */
    public ListasInvertidas() {
        // abrir ou criar arquivos 
        try{
            rafNomes = new RandomAccessFile(pathNomes, "rw");
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");

            // verifica se arquivos nao existem
            if(!exists()){
                rafNomes.seek(0);
                rafArtistas.seek(0);

                // indicador de arquivo vazio
                rafNomes.writeByte('*');
                rafArtistas.writeByte('*');
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
        try{
            // abre arquivo
            rafNomes = new RandomAccessFile(pathNomes, "rw");

            
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    /**
     * Imprime lista invertida de artistas do arquivo
     */
    public void printArtistas() {
        try{
            // abre arquivo
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");


        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
        /* Operacoes nas listas */
    /**
     * Pesquisa string nas listas invertidas, de acordo com o tipo de pesquisa desejado
     * @param query String a ser pesquisada nas listas
     * @param tipo int indicando qual pesquisa deve ser feita (nome, artistas ou os dois)
     * @return long[] enderecos dos registros em que houve ocorrencia dos termos pesquisados
     */
    public long[] pesquisar(String query, int tipo) {
        long[] enderecos = null;

        // if(tipo == 1){ // pesquisa por nome
        //     leListaNomes();
        //     enderecos = listaNomes.pesquisar(query);            
        // } else if(tipo == 2){ // pesquisa por artista
        //     leListaArtistas();
        //     enderecos = listaArtistas.pesquisar(query);            
        // } else if(tipo == 3){ // pesquisa por nome e artista
        //     leListaNomes();
        //     leListaArtistas();
            
        //     long[] endNomes = listaNomes.pesquisar(query);
        //     long[] endArtistas = listaArtistas.pesquisar(query);

        //     // copia enderecos iguais (AND das duas pesquisas)
        //     enderecos = new long[Integer.max(endNomes.length, endArtistas.length)];
        //     int i = 0, j = 0, k = 0;
        //     while(i < endNomes.length && j < endArtistas.length){
        //         if(endNomes[i] == endArtistas[j]){
        //             enderecos[k] = endNomes[i];
        //             i++; j++; k++;
        //         } else if(endNomes[i] < endArtistas[j]){
        //             i++;
        //         } else{
        //             j++;
        //         }
        //     }
        //     if(k == 0) enderecos = null;
        // }

        return enderecos;
    }
    /**
     * Cria termos nas listas invertidas, a partir do nome da musica e dos artistas, separando
     * string por espaco e inserindo nas listas os termos e o endereco da sua ocorrencia
     * @param nome String do nome da musica
     * @param artistas ArrayList<String> de artistas da musica
     * @param endereco long posicao no arquivo de dados da musica
     * @return true se conseguir criar termos nas listas, false se nao
     */
    public boolean create(String nome, ArrayList<String> artistas, long endereco) {
        boolean sucesso = false;
        
        if(createArtistas(artistas, endereco) && createNome(nome, endereco))
            sucesso = true;

        return sucesso;
    }
  
    private boolean createNome(String nome, long endereco) {
        boolean sucesso = false;
        
        try{
            // abre arquivo
            rafNomes = new RandomAccessFile(pathNomes, "rw");
            
            // organiza termos antes de fazer insercoes
            ArrayList<String> termosNomes = new ArrayList<>();
            String[] split = nome.split(" ");
            for(int i = 0; i < split.length; i++){
                termosNomes.add(split[i]);
            }
            termosNomes.sort(Comparator.naturalOrder());
            
            // verifica indicador de arquivo vazio
            rafNomes.seek(0);
            byte vazio = rafNomes.readByte();
            
            // insere novos termos na lista de nomes
            if(vazio == '*'){ // insere direto
                rafNomes.seek(0);
                rafNomes.writeByte(' ');
                for(int i = 0; i < termosNomes.size(); i++){
                    LinhaLista lin = new LinhaLista(termosNomes.get(i));
                    lin.setOcorrencia(0, endereco);
                    lin.setN(1);
                    byte[] byteArray = lin.toByteArray();
                    rafNomes.write(byteArray);
                }
            } else{ // compara com termos ja inseridos
                long pos = 0;
                while(pos != rafNomes.length()){
                    rafNomes.seek(pos);
                    
                    
                
                    pos = rafNomes.getFilePointer();
                }
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }

    private boolean createArtistas(ArrayList<String> artistas, long endereco) {
        boolean sucesso = false;
        
        try{
            // abre arquivo
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");
            
            // organiza termos antes de fazer insercoes
            ArrayList<String> termosArtistas = new ArrayList<>();
            for(int i = 0; i < artistas.size(); i++){
                String[] split = artistas.get(i).split(" ");
                for(int j = 0; j < split.length; j++){
                    termosArtistas.add(split[i]);
                }
            }
            termosArtistas.sort(Comparator.naturalOrder());

            // verifica indicador de arquivo vazio
            rafArtistas.seek(0);
            byte vazio = rafArtistas.readByte();
            
            // insere novos termos na lista de artistas
            if(vazio == '*'){ // insere direto
                rafArtistas.seek(0);
                rafArtistas.writeByte(' ');
                for(int i = 0; i < termosArtistas.size(); i++){
                    LinhaLista lin = new LinhaLista(termosArtistas.get(i));
                    lin.setOcorrencia(0, endereco);
                    lin.setN(1);
                    byte[] byteArray = lin.toByteArray();
                    rafArtistas.write(byteArray);
                }
            } else{ // compara com termos ja inseridos
                long pos = 0;
                while(pos != rafArtistas.length()){
                    rafArtistas.seek(pos);
                    
                    
                
                    pos = rafArtistas.getFilePointer();
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
     * Remove termos das listas invertidas, que tem sua ocorrencia no registro do endereco 
     * passado
     * @param endereco long posicao no arquivo de dados da musica
     * @return true se conseguir deletar termos, false caso contrario
     */
    public boolean delete(long endereco) {
        boolean sucesso = false;
        
        if(deleteArtistas(endereco) && deleteNome(endereco))
            sucesso = true;

        return sucesso;
    } 

    private boolean deleteNome(long endereco) {
        boolean sucesso = false;
        
        try{
            // abre arquivo
            rafNomes = new RandomAccessFile(pathNomes, "rw");
            
            // verifica indicador de arquivo vazio
            rafNomes.seek(0);
            byte vazio = rafNomes.readByte();
            
            if(vazio == '*'){
                System.out.println("Lista de nomes esta vazia.");
            } else{ // procura ocorrencias do endereco a deletar e remove
                long pos = 0;
                while(pos != rafNomes.length()){
                    rafNomes.seek(pos);
                    
                    
                
                    pos = rafNomes.getFilePointer();
                }
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }

    private boolean deleteArtistas(long endereco) {
        boolean sucesso = false;
        
        try{
            // abre arquivo
            rafArtistas = new RandomAccessFile(pathArtistas, "rw");
            
            // verifica indicador de arquivo vazio
            rafArtistas.seek(0);
            byte vazio = rafArtistas.readByte();
            
            if(vazio == '*'){
                System.out.println("Lista de artistas esta vazia.");
            } else{ // procura ocorrencias do endereco a deletar e remove
                long pos = 0;
                while(pos != rafArtistas.length()){
                    rafArtistas.seek(pos);
                    
                    
                
                    pos = rafArtistas.getFilePointer();
                }
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }

        return sucesso;
    }
}
