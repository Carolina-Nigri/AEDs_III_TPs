/** Pacotes **/
package TP01.classes;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/** Classe Musica **/
public class Musica {
    /* Atributos */
        // TODO: Criar IDs
    // protected int ID;
    protected int duration_ms;
    protected Date release_date;
    protected String track_id, name;
    protected ArrayList<String> artists = new ArrayList<String>();

    /* Getters e Setters */ 
    public int getDuration_ms() {
        return duration_ms;
    }
    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }
    public Date getRelease_date() {
        return release_date;
    }
    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }
    public String getTrack_id() {
        return track_id;
    }
    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<String> getArtists() {
        return artists;
    }
    public void setArtists(ArrayList<String> artists) {
        this.artists = artists;
    }

    /* Construtores */  
    public Musica() { }
    public Musica( int duration_ms, Date release_date, String track_id, String name, 
                    ArrayList<String> artists ) {
        this.duration_ms = duration_ms;
        this.release_date = release_date;
        this.track_id = track_id;
        this.name = name;
        this.artists = artists;
    }

    /* Métodos */
    /**
     * Faz o parse de uma linha do arquivo CSV, atribuindo valores a um objeto da 
     * classe Musica a partir dos atributos lidos e separados
     * @param line String de uma linha do CSV 
     */
    public void readCSV(String line) {
        final int TAM = line.length();
        int i = 0; // index da linha

        // Booleanos e Strings p/cada atributo
            // TODO: Sugestão - usar só um booleano "found"
        boolean foundDuration = false, foundDate = false, 
                foundTrack = false, foundName = false;
        String durationString = "", dateString = "", artistsString = "";

        this.track_id = this.name = ""; // inicializa atributos do tipo String
        
        /*-----------------------------------------------------------------*/
        // Procura atributo duration_ms
        while(!foundDuration){
            // TODO: Sugestão - transformar esses while em uma função reaproveitável
            if(line.charAt(i) != ','){ // add caracteres na string
                durationString += line.charAt(i);
            } else{ // achou fim
                foundDuration = true;
            }
            i++; // próximo index
       
        }
        
        this.duration_ms = Integer.parseInt(durationString); // transforma em int
        /*-----------------------------------------------------------------*/
        // Procura atributo release_date
        while(!foundDate){

            if(line.charAt(i) != ','){ // add caracteres na string
                dateString += line.charAt(i);
            } else{ // achou fim
                foundDate = true;
            }
            i++; // próximo index

        }

        // Cria formato de data e transforma string em Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            release_date = sdf.parse(dateString);
        } catch(ParseException pe){
            pe.printStackTrace();
        }
        /*-----------------------------------------------------------------*/
        // Procura atributo track_id
        while(!foundTrack){
           
            if(line.charAt(i) != ','){ // add caracteres na string
                this.track_id += line.charAt(i);
            } else{ // achou fim
                foundTrack = true;
            }
            i++; // próximo index

        }
        /*-----------------------------------------------------------------*/
        // Procura atributo name
        if(line.charAt(i) == '\"'){ // verifica se nome está entre aspas => contém vírgula
            i++; // pula a " abrindo

            while(!foundName){

                if(line.charAt(i) != '\"'){ // add caracteres na string
                    this.name += line.charAt(i);
                } else{ // achou fim
                    foundName = true;
                }
                i++; // próximo index

            }
            i++; // pula a vírgula      
        }
        else{ // parse normal pela vírgula
            while(!foundName){

                if(line.charAt(i) != ','){ // add caracteres na string
                    this.name += line.charAt(i);
                } else{ // achou fim
                    foundName = true;
                }
                i++; // próximo index

            }
        }
        /*-----------------------------------------------------------------*/
        // Procura atributo artists
        while(i < TAM){ // vai até o fim da linha
            if( line.charAt(i) != '[' && line.charAt(i) != ']' && 
                line.charAt(i) != '\'' && line.charAt(i) != '\"' ){
                    
                artistsString += line.charAt(i); // add caracteres na string
            }
            i++; // próximo index
        }        

        // Add artistas separados por vírgula ao ArrayList
        String[] artistsArray = artistsString.split(", ");
        for(int j = 0; j < artistsArray.length; j++){
            this.artists.add(artistsArray[j]);
        }
    } // fim readCSV(String)

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("=====Musica 1=====");
        
        String line1 = sc.nextLine();
        Musica ms1 = new Musica();

        ms1.readCSV(line1);

        System.out.println("Duracao em ms: " + ms1.duration_ms);
        System.out.println("Data de Lancamento: " + formatter.format(ms1.release_date));
        System.out.println("Id da Musica: " + ms1.track_id);
        System.out.println("Nome da Musica: " + ms1.name);
        System.out.println("Artistas:");        
        for(int i = 0; i < ms1.artists.size(); i++){
            System.out.println("    " + ms1.artists.get(i));
        }

        System.out.println("=====Musica 2=====");
        
        String line2 = sc.nextLine();       
        Musica ms2 = new Musica();

        ms2.readCSV(line2);

        System.out.println("Duracao em ms: " + ms2.duration_ms);
        System.out.println("Data de Lancamento: " + formatter.format(ms2.release_date));
        System.out.println("Id da Musica: " + ms2.track_id);
        System.out.println("Nome da Musica: " + ms2.name);
        System.out.println("Artistas:");        
        for(int i = 0; i < ms2.artists.size(); i++){
            System.out.println("    " + ms2.artists.get(i));
        }

        sc.close();
    }
}
