/** Pacotes **/
package TP01;
import TP01.classes.CRUD;
import TP01.classes.Musica;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/* Classe TP01 => Main */
public class TP01 {
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
    /**
     * 
     */
    public static void main(String[] args) {
        try{
            // arquivo CSV (base de dados)
            String baseFile = "TP01/data/spotify.csv";
            BufferedReader fr = new BufferedReader(new FileReader(baseFile));
            
            // arquivo RAF (registros em bytes) 
            CRUD arquivo = new CRUD("TP01/data/musicas");

            String line; // linha do CSV
            
            // lê 10 musicas (linhas) do CSV, faz parse e cria registro no arquivo
            for(int i = 0; i < 100; i++){
                line = fr.readLine();
                
                Musica musica = new Musica();
                musica.parseCSV(line);
               
                arquivo.create(musica); 
            }

            System.out.println("Base de dados carregada. 100 registros criados.");

            int opc = -1; // opcao do menu

            do{
                opc = menu();

                // executa tarefas do menu de CRUD
                switch(opc){ 
                    case 0: { // fecha arquivo e encerra programa
                        System.out.println("\n**Encerrando programa**");
                        arquivo.close();
                        break;
                    }
                    case 1: { // Create: lê atributos da musica e cria registro no arquivo
                        System.out.println("\n**Criando musica**");
                        
                        Musica msc = lerMusica();
                        arquivo.create(msc);
                        
                        System.out.println("\n" + msc);
                                                
                        break;
                    } case 2: { // Read: lê ID da música, lê musica do arquivo e imprime
                        System.out.println("\n**Lendo musica**");
                        System.out.print("ID da musica a ser lida: ");
                        
                        int readID = Integer.parseInt(br.readLine());
                        Musica msc = arquivo.read(readID);
                        if(msc != null)
                            System.out.println("\n" + msc);
                        
                        break;
                    } case 3: { // Update
                        System.out.println("\n**Atualizando musica**");
                        System.out.print("ID da musica que deve ser alterada: ");
                        
                        int updateID = Integer.parseInt(br.readLine());
                        Musica msc = arquivo.read(updateID);
                        if(msc != null){
                            System.out.println("\n" + msc);
                            Musica nova = lerAtualizacao(msc);
                                                    
                            if(arquivo.update(nova))
                                System.out.println("Musica atualizada com sucesso");
                            else
                                System.out.println("Erro ao atualizar musica");
                        } else{
                            System.out.println("Musica a ser atualizada nao encontrada");
                        }

                        break;
                    } case 4: { // Delete: lê ID da música, procura no arquivo e exclui
                        System.out.println("\n**Deletando musica**");
                        System.out.print("ID da musica que deve ser deletada: ");   
                        
                        int deleteID = Integer.parseInt(br.readLine());     
                        
                        if(arquivo.delete(deleteID))
                            System.out.println("Musica removida com sucesso");
                        else
                            System.out.println("Erro ao remover musica");
                                                    
                        break;
                    }
                }
            } while(opc != 0);
          
            fr.close();
            br.close();
        } catch(FileNotFoundException fnfe){
            System.err.println("Arquivo CSV da base de dados nao encontrado");
            fnfe.printStackTrace();
        } catch(IOException ioe){
            System.err.println("Erro de leitura na funcao principal");
            ioe.printStackTrace();
        }
    }
    /**
     * Mostra menu na tela e solicita ao usuário qual opção ele deseja executar (CRUD)
     * @return int opção lida
     */
    public static int menu() {
        System.out.println("\nCRUD - TP01");
        System.out.println("Escolha uma das opcoes:");
        System.out.println("0 - Sair");
        System.out.println("1 - Create");
        System.out.println("2 - Read");
        System.out.println("3 - Update");
        System.out.println("4 - Delete");
        
        int opc = -1;
        boolean invalido = false;
     
        try{
            do{
                System.out.print("-> ");
                opc = Integer.parseInt(br.readLine());
                invalido = (opc < 0) || (opc > 4);
                if(invalido) System.out.println("Opcao invalida! Digite novamente");
            } while(invalido);
        } catch(IOException ioe){
            System.err.println("Erro ao ler opcao do menu");
            ioe.printStackTrace();
        }

        return opc;
    }
    /**
     * Solicita ao usuário que digite os atributos da musica, criando uma instância
     * e retorna o objeto criado
     * @return objeto da musica lida
     */
    public static Musica lerMusica() {   
        int duration_ms = -1;
        String track_id = "", name = "";
        Date release_date = new Date();
        ArrayList<String> artists = new ArrayList<String>();

        try{
            // Lê duration_ms
            System.out.print("Duration_ms: ");
            duration_ms = Integer.parseInt(br.readLine());

            // Lê release_date
            System.out.print("Release_date [yyyy-MM-dd]: ");
            String stringDate = br.readLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
            try{
                release_date = sdf.parse(stringDate);
            } catch(ParseException pe){
                System.err.println("Erro ao fazer parse da data");
                pe.printStackTrace();
            }

            // Lê track_id
            System.out.print("Track_id: ");
            track_id = br.readLine();

            // Lê name
            System.out.print("Name: ");
            name = br.readLine();

            // Lê artists
            System.out.println("Artists [FIM quando terminar]:");
            String line = br.readLine();
            while( !(line.equals("FIM")) ){
                artists.add(line);
                line = br.readLine();
            }
        } catch(IOException ioe){
            System.err.println("Erro ao ler atributo da musica");
            ioe.printStackTrace();
        }
        
        Musica msc = new Musica(-1, duration_ms, release_date, track_id, name, artists);

        return msc;
    }
    /**
     * 
     * @param atual
     * @return
     */
    public static Musica lerAtualizacao(Musica atual) {
        System.out.println("Qual atributo deseja alterar?");
        System.out.println("[0] - duration_ms");
        System.out.println("[1] - release_date");
        System.out.println("[2] - track_id");
        System.out.println("[3] - name");
        System.out.println("[4] - artists");
        
        int valor = -1;
        boolean invalido =false;
        Musica nova = atual.clone();
        
        try{

            do{
                System.out.print("-> ");
                valor = Integer.parseInt(br.readLine());
                invalido = (valor < 0) || (valor > 4);
                if(invalido) System.out.println("Opcao invalida, digite novamente");
            } while(invalido);

            switch(valor){
                case 0: {
                    System.out.print("Duration_ms: ");
                    nova.setDuration_ms(Integer.parseInt(br.readLine()));    

                    break;
                } case 1: {
                    System.out.print("Release_date [yyyy-MM-dd]: ");
                    String stringDate = br.readLine();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
                    try{
                        nova.setRelease_date(sdf.parse(stringDate));
                    } catch(ParseException pe){
                        System.err.println("Erro ao fazer parse da data");
                        pe.printStackTrace();
                    }

                    break;
                } case 2: {
                    System.out.print("Track_id: ");
                    nova.setTrack_id(br.readLine());
                    
                    break;
                } case 3: {
                    System.out.print("Name: ");
                    nova.setName(br.readLine());
                    
                    break;
                } case 4: {
                    System.out.println("Artists [FIM quando terminar]:");
                    String line = br.readLine();
                    ArrayList<String> artists = new ArrayList<String>();
                    while( !(line.equals("FIM")) ){
                        artists.add(line);
                        line = br.readLine();
                    }
                    nova.setArtists(artists);
                    
                    break;
                }
            }

        } catch(IOException ioe){
            System.err.println("Erro de leitura na atualizacao da musica");
            ioe.printStackTrace();
        }

        return nova;
    }
}
