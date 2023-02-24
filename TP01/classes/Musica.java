/** Pacotes **/
package TP01.classes;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/** Classe Musica **/
public class Musica {
    /* Atributos */
    protected int ID;
    protected int duration_ms;
    protected Date release_date;
    protected String track_id, name;
    protected ArrayList<String> artists;

    /* Getters e Setters */ 
    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
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
    public Musica() { 
        this.ID = -1;
        this.duration_ms = -1;
        this.release_date = new Date();
        this.track_id = new String();
        this.name = new String();
        this.artists = new ArrayList<String>();
    }
    public Musica( int ID, int duration_ms, Date release_date, String track_id, String name, 
                   ArrayList<String> artists ) {
        this.ID = ID;
        this.duration_ms = duration_ms;
        this.release_date = release_date;
        this.track_id = track_id;
        this.name = name;
        this.artists = artists;
    }

    /* Métodos */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Duration_ms: "+this.duration_ms+
               "\nRelease_date: "+sdf.format(this.release_date)+
               "\nTrack_id: "+this.track_id+
               "\nName: "+this.name+
               "\nArtists: "+this.artists.toString();
    }
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        dos.writeInt(this.ID);
        dos.writeInt(this.duration_ms);
        
        dos.writeUTF(sdf.format(this.release_date)); // TODO: alterar depois
        
        dos.writeUTF(this.track_id);
        dos.writeInt(this.name.length());
        dos.writeUTF(this.name);

        dos.writeInt(artists.size());
        for(String artist : this.artists){
            dos.writeUTF(artist);
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte ba[]) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        this.ID = dis.readInt();
        this.duration_ms = dis.readInt();
        this.release_date = sdf.parse(dis.readUTF()); // TODO: alterar depois
        this.track_id = dis.readUTF();
        this.name = dis.readUTF();
    
        int num_artists = dis.readInt();
        for(int i = 0; i < num_artists; i++){
            artists.set(i, dis.readUTF());
        }
    }
    /**
     * Faz o parse de uma linha do arquivo CSV, atribuindo valores a um objeto da 
     * classe Musica a partir dos atributos lidos e separados
     * @param line String de uma linha do CSV 
     */
    public void readCSV(String line) {
        final int TAM = line.length();
        int index = 0;

        // Strings p/salvar cada atributo
        String durationString = "", dateString = "", artistsString = "";
        this.track_id = this.name = ""; // inicializa atributos do tipo String
        
        Boolean found = false;
    
        // Procura atributo duration_ms
        while(!found){
            
            if(line.charAt(index) != ','){ // add caracteres na string
                durationString += line.charAt(index);
            } else{ // achou fim
                found = true;
            }
            index++; // próximo index
       
        }
        this.duration_ms = Integer.parseInt(durationString); // transforma em int
        
        // Procura atributo release_date
        found = false;
        while(!found){
            
            if(line.charAt(index) != ','){ // add caracteres na string
                dateString += line.charAt(index);
            } else{ // achou fim
                found = true;
            }
            index++; // próximo index
       
        }
        // Cria formato de data e transforma string em Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            release_date = sdf.parse(dateString);
        } catch(ParseException pe){
            pe.printStackTrace();
        }
    
        // Procura atributo track_id
        found = false;
        while(!found){
            
            if(line.charAt(index) != ','){ // add caracteres na string
                this.track_id += line.charAt(index);
            } else{ // achou fim
                found = true;
            }
            index++; // próximo index
       
        }
    
        // Procura atributo name
        found = false;
        if(line.charAt(index) == '\"'){ // verifica se nome está entre aspas => contém vírgula
            index++; // pula a " abrindo

            while(!found){

                if(line.charAt(index) != '\"'){ // add caracteres na string
                    this.name += line.charAt(index);
                } else{ // achou fim
                    found = true;
                }
                index++; // próximo index

            }
            index++; // pula a vírgula      
        }
        else{ // parse normal pela vírgula
            while(!found){
                
                if(line.charAt(index) != ','){ // add caracteres na string
                    this.name += line.charAt(index);
                } else{ // achou fim
                    found = true;
                }
                index++; // próximo index
        
            }
        }
    
        // Procura atributo artists
        while(index < TAM){ // vai até o fim da linha
            if( line.charAt(index) != '[' && line.charAt(index) != ']' && 
                line.charAt(index) != '\'' && line.charAt(index) != '\"' ){
                    
                artistsString += line.charAt(index); // add caracteres na string
            }
            index++; // próximo index
        }        
        // Add artistas separados por vírgula ao ArrayList
        String[] artistsArray = artistsString.split(", ");
        for(int j = 0; j < artistsArray.length; j++){
            this.artists.add(artistsArray[j]);
        }
    } // fim readCSV(String)
} // fim classe Musica
