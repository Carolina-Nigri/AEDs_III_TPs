/** Pacotes **/
package TP02.classes.indices.listas;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/** Classe Lista Invertida **/
public class ListaInvertida {
    /* Atributos */
    private ArrayList<LinhaLista> linhas;

    /* Getters e Setters */
    public int getSize(){
        return linhas.size();
    }

    /* Construtores */
    public ListaInvertida() {
        this.linhas = new ArrayList<LinhaLista>();
    }

    /* Metodos */
        /* Basicos */
    /**
     * Imprime a lista invertida
     */
    public void print() {
        System.out.println("termos = " + linhas.size());

        for(int i = 0; i < linhas.size(); i++){
            System.out.println(linhas.get(i));
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
            dos.writeInt(linhas.size());

            for(int i = 0; i < linhas.size(); i++){
                byte[] byteArray = linhas.get(i).toByteArray();
                dos.write(byteArray);
            }
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
            int n = dis.readInt();

            for(int i = 0; i < n; i++){
                LinhaLista lin = new LinhaLista(dis.readUTF());
                int linN = dis.readInt();
                for(int j = 0; j < linN; j++){
                    lin.setOcorrencia(j, dis.readLong());
                }

                linhas.add(i, lin);
            }
        } catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
        /* Manipulacao da lista */
    /**
     * Pesquisa termos da string na lista invertida, retornando os enderecos das ocorrencias
     * @param query String a ser pesquisada
     * @return ArrayList<Long> enderecos dos registros onde se encontraram os termos
     */
    public ArrayList<Long> pesquisar(String query) {
        ArrayList<Long> enderecos = new ArrayList<Long>();

        if(linhas.size() != 0){ // lista nao esta vazia
            // String[] termos = query.split(" ");

            // // copia enderecos iguais (AND das duas pesquisas)
            // int i = 0, j = 0, k = 0;
            // while(i < endNomes.size() && j < endArtistas.size()){
            //     if(endNomes.get(i) == endArtistas.get(j)){
            //         enderecos.add(k, endNomes.get(i));
            //         i++; j++; k++;
            //     } else if(endNomes.get(i) < endArtistas.get(j)){
            //         i++;
            //     } else{
            //         j++;
            //     }
            // }
        }

        return enderecos;
    }
    /**
     * Insere o par termo e endereco na lista invertida, retornando se houve sucesso
     * @param termo String a ser inserida
     * @param endereco long posicao em arquivo do registro onde aparece o termo
     * @return true se conseguir inserir termo, false caso contrario
     */
    public boolean inserir(String termo, long endereco) {
        boolean inseriu = false;

        if(linhas.size() == 0){ // lista vazia => insere direto
            LinhaLista tmp = new LinhaLista(termo);
            tmp.setOcorrencia(0, endereco);
            linhas.add(tmp);

            inseriu = true;
        } else{ // lista ja tem pelo menos uma linha
            // acha pos de insercao do termo (ordenados)
            int i = 0;
            while(i < linhas.size() && linhas.get(i).getTermo().compareTo(termo) < 0){
                i++;
            }  
            
            if(i != linhas.size()){
                LinhaLista tmp = linhas.get(i);

                // termo ja existe => acrescenta ocorrencia no novo endereco
                if(tmp.getTermo().equals(termo)){
                    // acha pos de insercao do endereco (ordenados)
                    int j = 0;
                    while(j < tmp.getSize() && tmp.getOcorrencia(j) < endereco){
                        j++;
                    }  

                    // add endereco
                    if(j == tmp.getSize())
                        tmp.setOcorrencia(j, endereco); 
                    else if(tmp.getOcorrencia(j) != endereco)
                        tmp.setOcorrencia(j, endereco); 

                    inseriu = true;
                } else{ // criar linha no lugar da linha i e arrastar as seguintes
                    tmp = new LinhaLista(termo);
                    tmp.setOcorrencia(0, endereco);
                    linhas.add(i, tmp);
                    
                    inseriu = true;
                }
            } else{ // cria novo termo no final (linha nova)
                LinhaLista tmp = new LinhaLista(termo);
                tmp.setOcorrencia(0, endereco);
                linhas.add(tmp);
                
                inseriu = true;
            } 
        }

        return inseriu;
    }
    /**
     * Remove as ocorrencias relacionadas ao registro cujo endereco foi passado. Se o termo
     * so tiver ocorrencia nesse endereco, ele eh retirado por completo, senao somente a 
     * ocorrencia do termo nesse endereco eh retirada 
     * @param endereco long posicao em arquivo do registro
     * @return true se conseguir remover endereco, false caso contrario
     */
    public boolean remover(long endereco) {
        boolean removeu = false;

        if(linhas.size() != 0){ // lista nao esta vazia
            int i = 0;
            while(i < linhas.size()){
                LinhaLista tmp = linhas.get(i);
                
                // faz uma pesquisa binaria pelo endereco
                int dir = (tmp.getSize() - 1), esq = 0, meio = 0;
                boolean achou = false;
                while(esq <= dir && !achou){
                    meio = (esq + dir) / 2;
                    
                    if(endereco == tmp.getOcorrencia(meio))
                        achou = true;
                    else if(endereco > tmp.getOcorrencia(meio))
                        esq = meio + 1;
                    else
                        dir = meio - 1;
                }

                if(achou){
                    // remove endereco
                    tmp.delOcorrencia(meio);

                    // termo nao possui outras ocorrencias => remove
                    if(tmp.getSize() == 0)
                        linhas.remove(i);

                    removeu = true;
                }

                i++;
            }  
        }

        return removeu;
    }
}
