/** Pacotes **/
package TP02.classes;
import java.io.File;
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
    private int tamBucket; // tamanho do bucket

    /* Construtor */
    public HashEstendido(int tamBucket) {
        this.tamBucket = tamBucket; // setar tamanho dos buckets

        // abrir ou criar arquivos p/diretorio e buckets
        try{
            rafDiretorio = new RandomAccessFile(pathDiretorio, "rw");
            rafBucket = new RandomAccessFile(pathBucket, "rw");

            // criar se nao existir
            if(rafDiretorio.length() == 0 && rafBucket.length() == 0){
                // cria um novo diretorio, com profundidade de 1 
                Diretorio diretorio = new Diretorio();
                byte[] byteArray = diretorio.toByteArray();
                rafDiretorio.seek(0);
                rafDiretorio.write(byteArray);
                
                // cria buckets, com profundidade de 1 
                Bucket bucket = new Bucket(tamBucket);
                byteArray = bucket.toByteArray();
                rafBucket.seek(0);
                rafBucket.write(byteArray);
            }
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe.getMessage());
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    
    /* Metodos */
// TODO: organizar daqui pra baixo    
    public void print() {
        try{
            // le arquivo do diretorio de bytes pra classe 
            byte[] bd = new byte[ (int)rafDiretorio.length() ];
            rafDiretorio.seek(0);
            rafDiretorio.read(bd);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(bd);

            // imprime diretorio
            System.out.println("=======================================");
            System.out.println("Diretorio do Hash Estendido");
            System.out.println("=======================================\n");
            System.out.println(diretorio);
            // imprime buckets            
            System.out.println("\n=======================================");
            System.out.println("Buckets do Hash Estendido");
            System.out.println("=======================================\n");

            // le arquivo de buckets e imprime cada bucket
            rafBucket.seek(0);
            while(rafBucket.getFilePointer() != rafBucket.length()){
                Bucket b = new Bucket(tamBucket);
                byte[] ba = new byte[b.getTamBucket()];
                
                rafBucket.read(ba);
                b.fromByteArray(ba);
                System.out.println(b.toString());
                System.out.println("");
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    public void end() {
        try {
            rafDiretorio.close();
            rafBucket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean deleteFile() {
        File file1 = new File(pathBucket);
        boolean tmp = file1.delete();
        File file2 = new File(pathDiretorio);
        return tmp && file2.delete();
    }
    public boolean create(int chave, long dado) {
        try {
            // Carrega o diretorio
            byte[] bd = new byte[(int) rafDiretorio.length()];
            rafDiretorio.seek(0);
            rafDiretorio.read(bd);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(bd);
            // Identifica a hash do diretorio,
            int i = diretorio.hash(chave);
            // Recupera o Bucket
            long enderecoBucket = diretorio.enderecoBucket(i);
            Bucket b = new Bucket(tamBucket);
            byte[] ba = new byte[b.getTamBucket()];
            rafBucket.seek(enderecoBucket);
            rafBucket.read(ba);
            b.fromByteArray(ba);
            // Testa se a chave já não existe no Bucket
            if (b.read(chave) != -1)
                throw new Exception("Erro no Hash, chave já existente");
            // Testa se o Bucket já não está cheio
            if (!b.full()) {
                // Insere a chave no Bucket e o atualiza
                b.create(chave, dado);
                rafBucket.seek(enderecoBucket);
                rafBucket.write(b.toByteArray());
                return true;
            }
            // caso cheio continua o codigo

            // Testar se necessario duplicar diretorio
            int pl = b.getPLocal(); // pLocal bucket
            if (pl >= diretorio.getPGlobal())
                diretorio.aumentarGlobal();
            int pg = diretorio.getPGlobal(); // pGlobal diretorio
            // Cria os novos Buckets
            Bucket b1 = new Bucket(tamBucket, pl + 1);
            rafBucket.seek(enderecoBucket);
            rafBucket.write(b1.toByteArray());
            Bucket b2 = new Bucket(tamBucket, pl + 1);
            long newEndereco = rafBucket.length();
            rafBucket.seek(newEndereco);
            rafBucket.write(b2.toByteArray());
            // Atualizar os dados no diretorio
            int j = diretorio.hash2(chave, b.getPLocal());
            int aux = (int) Math.pow(2, pl);
            int max = (int) Math.pow(2, pg);
            boolean troca = false;
            for (int k = j; k < max; k += aux) {
                if (troca)
                    diretorio.atualizarEndereco(k, newEndereco);
                troca = !troca;
            }
            // Atualiza o arquivo do diretorio
            bd = diretorio.toByteArray();
            rafDiretorio.seek(0);
            rafDiretorio.write(bd);
            // Reinserir as chaves
            for (int k = 0; k < b.getN(); k++) {
                create(b.getChaves()[k], b.getEnderecos()[k]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return create(chave, dado);
    }
    public long read(int chave) {
        try {
            // Carrega o diretorio
            byte[] bd = new byte[(int) rafDiretorio.length()];
            rafDiretorio.seek(0);
            rafDiretorio.read(bd);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(bd);
            // Pegar hash
            int i = diretorio.hash(chave);
            // Recuperar o bucket
            long enderecoBucket = diretorio.enderecoBucket(i);
            Bucket b = new Bucket(tamBucket);
            byte[] ba = new byte[b.getTamBucket()];
            if (enderecoBucket > 0) {
                rafBucket.seek(enderecoBucket);
                rafBucket.read(ba);
                b.fromByteArray(ba);
                // retornar elemento dentro do bucket, caso nao exista retorna -1
                return b.read(chave);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public boolean update(int chave, long newDado) {
        try {
            // Carrega o diretorio
            byte[] bd = new byte[(int) rafDiretorio.length()];
            rafDiretorio.seek(0);
            rafDiretorio.read(bd);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(bd);
            // Identifica a hash do diretorio,
            int i = diretorio.hash(chave);
            // Recupera o Bucket
            long enderecoBucket = diretorio.enderecoBucket(i);
            Bucket b = new Bucket(tamBucket);
            byte[] ba = new byte[b.getTamBucket()];
            rafBucket.seek(enderecoBucket);
            rafBucket.read(ba);
            b.fromByteArray(ba);
            // atualizar o dado
            if (!b.update(chave, newDado))
                return false;
            // Atualiza o Bucket no arquivo
            rafBucket.seek(enderecoBucket);
            rafBucket.write(b.toByteArray());
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean delete(int chave) {
        try {
            // Carrega o diretorio
            byte[] bd = new byte[(int) rafDiretorio.length()];
            rafDiretorio.seek(0);
            rafDiretorio.read(bd);
            Diretorio diretorio = new Diretorio();
            diretorio.fromByteArray(bd);
            // Achar Hash
            int i = diretorio.hash(chave);
            // Recupera o Bucket
            long enderecoBucket = diretorio.enderecoBucket(i);
            Bucket b = new Bucket(tamBucket);
            byte[] ba = new byte[b.getTamBucket()];
            rafBucket.seek(enderecoBucket);
            rafBucket.read(ba);
            b.fromByteArray(ba);
            // Deletar chave no bucket
            if (!b.delete(chave))
                return false;
            // Atualizar o Bucket no arquivo
            rafBucket.seek(enderecoBucket);
            rafBucket.write(b.toByteArray());
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
