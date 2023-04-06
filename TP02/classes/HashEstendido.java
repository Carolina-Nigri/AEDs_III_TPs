/** Pacotes **/
package TP02.classes;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Classe HashEstendido **/
public class HashEstendido {
    /* Atributos */
    private String pathDiretorio = "TP02/data/diretorioHash.db", // paths dos arquivos hash
                   pathBucket = "TP02/data/bucketHash.db"; 
    private RandomAccessFile rafDiretorio, // arquivos 
                             rafBucket;
    private int qtdChaves; // qtd de chaves do bucket

    /* Construtor */
    public HashEstendido(int qtdChaves) {
        this.qtdChaves = qtdChaves; // setar tamanho dos buckets

        // abrir ou criar arquivos p/diretorio e buckets
        try{
            rafDiretorio = new RandomAccessFile(pathDiretorio, "rw");
            rafBucket = new RandomAccessFile(pathBucket, "rw");

            // criar se nao existir
            if(rafDiretorio.length() == 0 && rafBucket.length() == 0){
                // cria um novo diretorio, com profundidade de 1 
                Diretorio diretorio = new Diretorio();
                
                // cria buckets, com profundidade de 1 e liga ponteiros do diretorio
                rafBucket.seek(0);
                for(int i = 0; i < 2; i++){
                    diretorio.setEndereco(i,rafBucket.getFilePointer());
                    
                    Bucket bucket = new Bucket(qtdChaves);
                    byte[] byteArray = bucket.toByteArray();
                    rafBucket.write(byteArray);
                }

                // escreve diretorio no arquivo
                rafDiretorio.seek(0);
                byte[] byteArray = diretorio.toByteArray();
                rafDiretorio.write(byteArray);
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    
    /* Metodos */
    public int hash(int chave, int p) {
        return chave % (int) Math.pow(2, p);
    }
    public void aumentarPGlobal() {
        try{
            // abre arquivos
            rafDiretorio = new RandomAccessFile(pathDiretorio, "rw");
            rafBucket = new RandomAccessFile(pathBucket, "rw");

            // le arquivo do diretorio de bytes pra classe 
            byte[] byteArray = new byte[ (int)rafDiretorio.length() ];
            rafDiretorio.seek(0);
            rafDiretorio.read(byteArray);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(byteArray);

            // aumentar pGlobal e ajustar ponteiros
            diretorio.aumentarP();

            // reescrever no arquivo
            rafDiretorio.seek(0);
            byteArray = diretorio.toByteArray();
            rafDiretorio.write(byteArray);
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }        
    }
    public void aumentarPLocal(int bucket) {
        try{
            // abre arquivos
            rafDiretorio = new RandomAccessFile(pathDiretorio, "rw");
            rafBucket = new RandomAccessFile(pathBucket, "rw");

            // le arquivo do diretorio de bytes pra classe 
            byte[] byteArray = new byte[ (int)rafDiretorio.length() ];
            rafDiretorio.seek(0);
            rafDiretorio.read(byteArray);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(byteArray);

            // pega endereco do bucket atual do diretorio
            long posBAtual = diretorio.getEndereco(bucket);

            // vai p/posicao do bucket atual e cria objeto
            rafBucket.seek(posBAtual);
            Bucket bAtual = new Bucket(qtdChaves);
            byteArray = new byte[bAtual.getTamBucket()];
            rafBucket.read(byteArray);
            bAtual.fromByteArray(byteArray);
            int pL = bAtual.getPLocal(); // pega pLocal atual
            
            // muda ponteiro no diretorio do bucket novo
            long posBNovo = rafBucket.length();
            int bucketNovo = bucket + (int)Math.pow(2, pL);
            rafDiretorio.seek(Integer.BYTES + bucketNovo * Long.BYTES);
            rafDiretorio.writeLong(posBNovo);
            
            // aumenta pLocal e cria novo bucket
            pL++; 
            bAtual.setPLocal(pL);
            Bucket bNovo = new Bucket(qtdChaves, pL);

            // organizar chaves
            int chave = -1;

            for(int i = 0, j = 0; i < qtdChaves; i++){
                chave = bAtual.getChave(i);
                int pos = hash(chave, pL); // calcula novo hash

                if(pos != bucket){ // chave tem que mudar de bucket
                    bNovo.setChave(j, chave);
                    bNovo.setEndereco(j, bAtual.getEndereco(i));

                    bAtual.deletePar(i);

                    j++;
                }
            }

            bAtual.reorganizarChaves();

            // escreve buckets de volta no arquivo
            rafBucket.seek(posBAtual);
            byteArray = bAtual.toByteArray();
            rafBucket.write(byteArray);
            rafBucket.seek(posBNovo);
            byteArray = bNovo.toByteArray();
            rafBucket.write(byteArray);
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }        
    }
    public void printHash() {
        try{
            // le arquivo do diretorio de bytes pra classe 
            byte[] byteArray = new byte[ (int)rafDiretorio.length() ];
            rafDiretorio.seek(0);
            rafDiretorio.read(byteArray);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(byteArray);

            // imprime diretorio
            System.out.println("\nDiretorio:");
            System.out.println(diretorio);
            
            // imprime buckets            
            System.out.println("\nBuckets (parte preenchida):\n");

            // le arquivo de buckets e imprime cada bucket
            rafBucket.seek(0);
            while(rafBucket.getFilePointer() != rafBucket.length()){
                Bucket bucket = new Bucket(qtdChaves);
                byteArray = new byte[bucket.getTamBucket()];
                
                rafBucket.read(byteArray);
                bucket.fromByteArray(byteArray);
                System.out.println(bucket);
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    public void create(int chave, long endereco) {
        try{
            // abre arquivos
            rafDiretorio = new RandomAccessFile(pathDiretorio, "rw");
            rafBucket = new RandomAccessFile(pathBucket, "rw");

            // le pGlobal do diretorio e salva pos
            rafDiretorio.seek(0);
            int pG = rafDiretorio.readInt();
            long posDir = rafDiretorio.getFilePointer();

            // calcula funcao hash da chave e le endereco do bucket onde deve inserir
            int bucket = hash(chave, pG);
            posDir = posDir + bucket * Long.BYTES;
            rafDiretorio.seek(posDir);
            long posBucket = rafDiretorio.readLong();
            
            // vai p/posicao do bucket p/inserir chave e le pLocal e qtd de chaves
            rafBucket.seek(posBucket);
            int pL = rafBucket.readInt();
            int n = rafBucket.readInt();
            int tamPar = Integer.BYTES + Long.BYTES;

            if(n < qtdChaves){ // tem espaco no bucket => escreve dados normalmente
                // atualiza n
                n++;
                rafBucket.seek(posBucket + Integer.BYTES);
                rafBucket.writeInt(n);

                // procura posicao de insercao
                posBucket = rafBucket.getFilePointer() + (n-1 * tamPar);
                rafBucket.seek(posBucket);
                rafBucket.writeInt(chave);
                rafBucket.writeLong(endereco);
            } else{ // NAO tem espaco no bucket
                if(pL < pG){ // pGlobal ja aumentou => aumentarPLocal
                    aumentarPLocal(bucket);
                    pL++;

                    // posiciona no endereco do bucket a inserir
                    rafDiretorio.seek(posDir);
                } else{ // aumentar pGlobal
                    aumentarPGlobal();
                    pG++;
                    aumentarPLocal(bucket);
                    pL++;
                    
                    // recalcula hash e le endereco do novo bucket onde deve inserir
                    bucket = hash(chave, pG); 
                    posDir = Integer.BYTES + bucket * Long.BYTES;
                    rafDiretorio.seek(posDir);
                }

                // atualiza endereco do bucket e procura posicao de insercao
                posBucket = rafDiretorio.readLong();
                rafBucket.seek(posBucket + Integer.BYTES);
                n = rafBucket.readInt();

                // atualiza n
                n++;
                rafBucket.seek(posBucket + Integer.BYTES);
                rafBucket.writeInt(n);

                // procura posicao de insercao
                posBucket = rafBucket.getFilePointer() + (n-1 * tamPar);
                rafBucket.seek(posBucket);
                rafBucket.writeInt(chave);
                rafBucket.writeLong(endereco);
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
}
