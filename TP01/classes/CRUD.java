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
        
        try{
            arq.seek(0);
            arq.writeInt(0); // ultimoID inicial
        } catch(IOException ioe){
            System.err.println("Erro ao inicializar CRUD");
            ioe.printStackTrace();
        }
    }

    /* Métodos */
    /**
     * Fecha arquivo (RandomAcessFile)
     */
    public void close() {
        try{
            arq.close();
        } catch(IOException ioe){
            System.err.println("Erro ao fechar arquivo");
            ioe.printStackTrace();
        }
    }
    /**
     * Cria um registro de uma musica, lendo o último ID registrado para setar o ID atual,
     * atualizando o valor ao final 
     * @param obj Musica a ser registrada no arquivo 
     */
    public void create(Musica obj) {
        int ultimoID = -1;
        byte[] objectData;
        long pos;

        try{
            arq.seek(0); // início do arquivo

            // lê ID do último registro em arquivo (0 se estiver vazio)
            ultimoID = arq.readInt();
            ultimoID++;
            obj.setID(ultimoID);

            // cria registro como array de bytes do objeto
            objectData = obj.toByteArray();
            pos = arq.length();
            arq.seek(pos); // fim do arquivo
            
            arq.writeByte(' '); // lapide
            arq.writeInt(objectData.length); // tamanho do registro (bytes)
            arq.write(objectData);

            arq.seek(0); // início do arquivo
            arq.writeInt(ultimoID);
        } catch(IOException ioe){
            System.err.println("Erro de leitura/escrita ao criar registro no arquivo");
            ioe.printStackTrace();
        }
    }
    /**
     * 
     * @param ID da musica a ser lida
     * @return objeto Musica lido do arquivo
     */
    public Musica read(int ID) {
        Musica obj = null;
       
        try{
            boolean found = false;
            long pos, arqLen = arq.length();
            byte lapide;
            int regSize, regID;

            // posiciona ponteiro no início, pula cabeçalho e salva posição
            arq.seek(0); 
            arq.skipBytes(Integer.BYTES);
            pos = arq.getFilePointer(); 

            while(!found && pos != arqLen){
                lapide = arq.readByte();
                regSize = arq.readInt();
                regID = arq.readInt();

                if(regID == ID){
                    found = true;

                    if(lapide == ' '){
                        // retorna para ID
                        arq.seek(pos + 1 + Integer.BYTES); 

                        // lê registro em bytes e converte para objeto 
                        byte[] data = new byte[regSize];
                        arq.read(data);
                        obj = new Musica();
                        obj.fromByteArray(data);
                    } else{
                        System.out.println("Registro pesquisado ja foi excluido");
                    }
                } else{
                    arq.skipBytes(regSize - Integer.BYTES);
                }
                
                pos = arq.getFilePointer(); // início do próximo registro (lápide)
            }
        } catch(IOException ioe){
            System.err.println("Erro de leitura/escrita ao ler registro no arquivo");
            ioe.printStackTrace();
        }

        return obj;
    }
    /**
     * 
     * @param obj
     */
    public boolean update(int id , int itn) {
        boolean resp = false;
        return resp;
    }
    /**
     * 
     * @param ID
     */
    public boolean delete(int ID) {
        boolean found = false;
        
        try{
            long pos, arqLen = arq.length();
            byte lapide;
            int regSize, regID;

            // posiciona ponteiro no início, pula cabeçalho e salva posição
            arq.seek(0); 
            arq.skipBytes(Integer.BYTES);
            pos = arq.getFilePointer(); 

            while(!found && pos != arqLen){
                lapide = arq.readByte();
                regSize = arq.readInt();

                if(lapide == ' '){ // verifica se registro não foi removido
                    regID = arq.readInt();

                    // verifica se é o ID do registro a ser removido
                    if(regID == ID){ 
                        arq.seek(pos); // retorna para posição da lápide
                        arq.writeByte('*');
                        found = true;
                    } else{
                        arq.skipBytes(regSize - Integer.BYTES);
                    }
                } else{
                    arq.skipBytes(regSize); // pula bytes do registro atual
                }
                
                pos = arq.getFilePointer(); // início do próximo registro (lápide)
            }
        } catch(IOException ioe){
            System.err.println("Erro de leitura/escrita ao ler registro no arquivo");
            ioe.printStackTrace();
        }

        return found;
    }
}
