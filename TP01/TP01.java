package TP01;
import TP01.classes.CRUD;
import TP01.classes.Musica;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

public class TP01 {

    public static int menu(){
        int resp = -1;
        Scanner sc = new Scanner(System.in);
        
        System.out.println("CRUD - TP01");
        System.out.println("Escolha Uma das Opcoes");
        System.out.println("1 - Create");
        System.out.println("2 - Update");
        System.out.println("3 - Delete");
        System.out.println("0 - Sair");

        System.out.print("-> ");
        resp =  sc.nextInt();

        sc.close();
        return resp;
    }

    public static Musica lerMusica(int id)throws Exception{
        Scanner sc = new Scanner(System.in);
        Musica msc = new Musica();

        msc.setID(id);

        System.out.println("Digite o duration_ms da musica");
        int duration_ms = Integer.parseInt(sc.nextLine());
        msc.setDuration_ms(duration_ms);

        System.out.println("Digite o release_date da musica [yyyy-MM-dd]");
        Date release_date;
        String StringDate = sc.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
        release_date = sdf.parse(StringDate);
        msc.setRelease_date(release_date);

        System.out.println("Digite o trackID da musica");
        String track_id = sc.nextLine();
        msc.setTrack_id(track_id);

        System.out.println("Digite o trackID da musica");
        String name = sc.nextLine();
        msc.setName(name);

        System.out.println("Digite o numero de artistas da musica");
        ArrayList<String> artistas = new ArrayList<String>();
        int num_artists = Integer.parseInt(sc.nextLine()); 
        for(int i = 0; i < num_artists; i++){
            artistas.add(sc.nextLine());
        }
        msc.setArtists(artistas);

        sc.close();
        return msc;
    }

    public static void main(String[] args)throws Exception {
        Scanner sc = new Scanner(System.in);
        int opc = -1;
        int id = 0;

        CRUD arqivo = new CRUD("Teste");

        String baseFile = ".../data/spotfy.csv";
        FileInputStream fstream = new FileInputStream(baseFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String line;

        for(int i = 0; i < 10; i++){
            line = br.readLine();
            Musica musica = new Musica();
            musica.readCSV(line);
            musica.setID(id);
            arqivo.create(musica);
            id++;
        }
        
        do{
            opc = menu();
            switch(opc){
                case 0:
                    arqivo.close();
                    break;
                case 1:
                    Musica msc = lerMusica(id);
                    arqivo.create(msc);
                    id++;
                    break;
                case 2:
                    System.out.println("Qual o id da musica que deve ser alterado ");
                    int altera = Integer.parseInt(sc.nextLine());
                    arqivo.update(altera);
                    break;
                case 3:
                    System.out.println("Qual o id da musica que deve ser deletado ");   
                    int deleta = Integer.parseInt(sc.nextLine());     
                    arqivo.delete(deleta);
                    break;
                default:
                    System.out.println("Valor Invalido ");   

            }

        }while(opc != 0);

        br.close();
        sc.close();
    }
}
