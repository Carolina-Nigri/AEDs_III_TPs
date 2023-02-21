package TP01;
import TP01.classes.Musica;
import java.util.Scanner;

public class TP01 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line1 = sc.nextLine();
        Musica ms1 = new Musica();
                
        ms1.readCSV(line1);
        System.out.println(ms1);

        

        sc.close();
    }
}
