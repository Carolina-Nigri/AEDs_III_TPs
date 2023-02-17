package TP01.classes;

import java.text.SimpleDateFormat;
import java.util.*;

public class Musica {
    public int duration_ms;
    public Date release_date;
    public String track_id;
    public String name;
    public ArrayList<String> artist = new ArrayList<String>();

    public void readCSV(String line)throws Exception{
        int tamanho = line.length();
        int i = 0;

        boolean foundDuration = false;
        String durantionString = "";

        boolean foundDate = false;
        String dateString = "";

        boolean foundTrack = false;
        String trackString = "";

        boolean foundName = false;
        String nameString = "";

        String artistString = "";

        while(!foundDuration){
            
            if(line.charAt(i) != ','){
                durantionString += line.charAt(i);
            }
            else{
                foundDuration = true;
            }
            i++;
        }
        String[] durationSplit = durantionString.split(".0");
        duration_ms = Integer.parseInt(durationSplit[0]);
        while(!foundDate){

            if(line.charAt(i) != ','){
                dateString += line.charAt(i);
            }
            else{
                foundDate = true;
            }
            i++;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        release_date = formatter.parse(dateString);

        while(!foundTrack){
            if(line.charAt(i) != ','){
                trackString += line.charAt(i);
            }
            else{
                foundTrack = true;
            }
            i++;
        }
        track_id = trackString;

        int aux = i;
        
        if(line.charAt(aux) == '\"'){
            i++;
            while(!foundName){
                if(line.charAt(i) != '\"'){
                    nameString+= line.charAt(i);
                }
                else{
                    foundName = true;
                }
                i++;
            }
            i++;        
        }
        else{
            while(!foundName){
                if(line.charAt(i) != ','){
                    nameString += line.charAt(i);
                }
                else{
                    foundName = true;
                }
                i++;
            }
        }
        name = nameString;

        while(i < tamanho){
            if(line.charAt(i)!= '[' && line.charAt(i) != ']' && line.charAt(i) != '\'' && line.charAt(i) != '\"'){
                artistString += line.charAt(i);
            }
            i++;
        }        


        String[] artistas = artistString.split(", ");
        for(int j = 0; j < artistas.length;j++){
            String palavra = "";
            for(int k = 0; k < artistas[j].length(); k++){
                palavra+= artistas[j].charAt(k);
            }
            artist.add(palavra);
        }

    }

    public static void main(String[] args)throws Exception {
        Scanner sc = new Scanner(System.in);
        String line1 = sc.nextLine();
        Musica ms1 = new Musica();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        ms1.readCSV(line1);

        System.out.println("Duracao em ms - " + ms1.duration_ms);
        System.out.println("Data de Lancamento - " + formatter.format(ms1.release_date));
        System.out.println("Id da Musica - " + ms1.track_id);
        System.out.println("Nome da Musica - " + ms1.name);
        System.out.println("Artistas:");        
        for(int i = 0; i < ms1.artist.size(); i++){
            System.out.println("    " + ms1.artist.get(i));
        }
        sc.close();
    }
}
