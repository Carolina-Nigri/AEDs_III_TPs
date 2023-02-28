package TP01.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Sort {
    private static long pointer; // long para armazenar ponteiro de raf
    private static String path = "TP01/data/spotify.csv/";
    private static int len = 30;
    private static int files = 2;
    private static Musica[] bloco;

    private static void create2nTmpFiles() {
        for (int i = 0; i < files * 2; i++) {
            try {
                RandomAccessFile raf = new RandomAccessFile(path + "tmp" + (i + 1) + ".db", "rw");
                raf.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void delete2nTmpFiles() {
        for (int i = 0; i < files * 2; i++) {
            File file = new File(path + "tmp" + (i + 1) + ".db");
            file.delete();
        }
    }

    private static void swap(int i, int j) {
        Musica tmp = bloco[i].clone();
        bloco[i] = bloco[j].clone();
        bloco[j] = tmp.clone();
    }


    private static void quicksort(int esq, int dir) {
        int i = esq, j = dir;
        int pivo = bloco[(dir + esq) / 2].getDuration_ms();
        while (i <= j) {
            while (bloco[i].getDuration_ms() < pivo)
                i++;
            while (bloco[j].getDuration_ms() > pivo)
                j--;
            if (i <= j) {
                swap(i, j);
                i++;
                j--;
            }
        }
        if (esq < j)
            quicksort(esq, j);
        if (i < dir)
            quicksort(i, dir);
    }


}
