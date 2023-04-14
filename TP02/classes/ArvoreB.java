/** Pacotes **/
package TP02.classes;
import java.lang.Math;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** Classe ArvoreB **/
class ArvoreB {
    /* Atributos */
    private int nMax; // qtd max de chaves
    private Pagina raiz; // pagina raiz
    
    /* Construtor */  
    public ArvoreB(int ordem) {
        this.nMax = (ordem - 1);
        this.raiz = null;
    }

    /* Metodos */
        /* Basicos */
    /**
     * Imprime a arvore 
     */
    public void print() {
        printPagina(raiz);
    }
    public void printPagina(Pagina pag) {
        if(pag != null){
            for(int i = 0; i < pag.getN(); i++){
                System.out.print(pag.getChave(i) + " ");
            }
            System.out.println("");
            if(!pag.isFolha()){
                for(int i = 0; i < (pag.getN()+1); i++){
                    printPagina(pag.getFilha(i));
                }
            }
        }
    }
    /**
     * Converte objeto da classe para um array de bytes, escrevendo seus atributos
     * @return Byte array do objeto
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        try{

        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
        
        return baos.toByteArray();
    }
    /**
     * Converte um array de bytes para os atributos da classe, atribuindo
     * ao objeto corrente
     * @param byteArray array de bytes de um objeto
     */
    public void fromByteArray(byte[] ba) {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        try{

        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
        /* Manipulacao da Arvore */
    /**
     * Insere par chave e endereco na arvore B. Se raiz nao existir, cria e insere nela.
     * Caso contrario, verifica se raiz esta cheia e faz split antes de inserir internamente
     * ou insere internamente direto se raiz tiver espaco (nao precisa fazer split p/mudar 
     * raiz) 
     * @param chave identificador a ser inserido
     * @param endereco posicao em arquivo da chave
     */
    public void inserir(int chave, long endereco) {
        if(raiz == null){ // raiz nao existe => cria raiz
            raiz = new Pagina(nMax, true);
        } else if(raiz.getN() == nMax){ // raiz existe mas esta cheia
            // acha pagina filha onde chave pode ficar
            int posFilha = raiz.acharPosFilha(chave);
            Pagina filha = raiz.getFilha(posFilha);
            
            // raiz eh folha ou a filha tambem ta cheia => cria nova raiz
            if(filha != null && filha.getN() == nMax || raiz.isFolha())
                criaNovaRaiz(chave);
        } 

        // insere internamente (procura pagina folha)
        inserir(raiz, chave, endereco);
    }
    /**
     * Busca posicao de insercao (pagina folha) e insere par, fazendo split a partir
     * das paginas pai e filha quando necessario, para inserir em sequencia
     * @param pag Pagina atual 
     * @param chave identificador a ser inserido
     * @param endereco posicao em arquivo da chave
     */
    private void inserir(Pagina pag, int chave, long endereco) {
        if(pag.isFolha()){ // pagina eh folha => insere par
            pag.inserir(chave, endereco);
        } else{ // pagina nao eh folha
            // acha pagina filha onde chave pode ficar
            int posFilha = pag.acharPosFilha(chave);
            Pagina filha = pag.getFilha(posFilha);

            if(filha.getN() == nMax){ // nao tem espaco na pagina filha
                // faz split se necessario (nao tem nenhuma folha com espaco)
                boolean fezSplit = split(pag, filha, posFilha, chave);

                // pagina filha onde chave fica mudou (chave eh maior q a q "subiu" no split)
                if(fezSplit && chave > pag.getChave(posFilha)){
                    posFilha++;
                    filha = pag.getFilha(posFilha);
                }
            }

            // tem espaco na pagina filha (ja tinha ou fez split e liberou)
            inserir(filha, chave, endereco);
        }
    }
    /**
     * Cria nova raiz, tornando a raiz atual filha da nova e fazendo split
     * @param chave identificador a ser inserido
     */
    private void criaNovaRaiz(int chave) {
        // cria nova raiz (raiz atual vira filha da nova)
        Pagina novaRaiz = new Pagina(nMax, false);
        novaRaiz.setFilha(0, raiz);
        raiz = novaRaiz;

        // faz split 
        split(raiz, raiz.getFilha(0), 0, chave);
    }
    /**
     * Verifica condicoes de split na arvore B, se existir alguma folha que tem espaco nao 
     * faz split (retorna falso), caso contrario faz, recursivamente, a partir da pagina pai
     * @param pai Pagina pai 
     * @param filha Pagina filha
     * @param posFilha posicao da Pagina filha
     * @param chave identificador a ser inserido
     * @return true se fizer split, false se nao
     */
    private boolean split(Pagina pai, Pagina filha, int posFilha, int chave) {
        boolean fazSplit = true;
        
        // nao esta criando nova raiz (n = 0) e filha nao eh folha
        if(pai.getN() != 0 && !filha.isFolha()){
            // acha pagina neta onde chave pode ficar
            int posNeta = filha.acharPosFilha(chave);
            Pagina neta = filha.getFilha(posNeta);

            // verifica, recursivamente, se alguma neta tem espaco (logo folha tem espaco)
            if(neta != null && neta.getN() == nMax){
                fazSplit = split(filha, neta, posNeta, chave);
            } else{ // neta tem espaco
                fazSplit = false;
            }
        }
        
        // pode fazer split a partir do pai (nao esta cheio)
        if(fazSplit && pai.getN() < nMax){
            split(pai, filha, posFilha);
        }

        return fazSplit;
    }
    /**
     * Faz split na arvore B, reorganizando chaves entre as paginas pai, filha e a nova
     * @param pai Pagina pai 
     * @param filha Pagina filha
     * @param posFilha posicao da Pagina filha
     */
    private void split(Pagina pai, Pagina filha, int posFilha) {
        // cria nova pagina filha
        Pagina novaFilha = new Pagina(nMax, filha.isFolha());
        int meio = (int) Math.ceil(nMax / 2.0);

        // copia chaves maiores da pagina filha (meio ate o fim) p/nova filha
        for(int i = 0; i < (meio-1); i++){
            // insere pares chave e endereco na novaFilha
            novaFilha.setChave(i, filha.getChave(i+meio));
            novaFilha.setEndereco(i, filha.getEndereco(i+meio));
            // remove pares chave e endereco da filha
            filha.setChave(i+meio, -1);
            filha.setEndereco(i+meio, -1);
        }
        // atualiza qtd de chaves na filha e novaFilha
        novaFilha.setN(meio-1);
        filha.setN(meio);

        // pagina filha nao eh folha (tem ponteiros de filhas p/copiar)
        if(!filha.isFolha()){
            // copia ponteiros das filhas da pagina filha (meio ate o fim) p/nova filha
            for(int i = 0; i < meio; i++){
                novaFilha.setFilha(i, filha.getFilha(i+meio));
                filha.setFilha(i+meio, null);
            } 
        }

        // liga nova filha ao pai
        pai.setFilha((posFilha+1), novaFilha);

        // insere pares chave e endereco no pai (sobe c/o do meio)
        pai.setChave(posFilha, filha.getChave(meio-1));
        pai.setEndereco(posFilha, filha.getEndereco(meio-1));
        // remove pares chave e endereco da filha
        filha.setChave((meio-1), -1);
        filha.setEndereco((meio-1), -1);
        // atualiza qtd de chaves
        pai.setN(pai.getN()+1); 
        filha.setN(filha.getN()-1);
    }
}
