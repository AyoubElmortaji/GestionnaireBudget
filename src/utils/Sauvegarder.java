package utils;

import corps.GestionnaireBudget;
import java.io.*;

public class Sauvegarder {
    private static final String FICHIER_SAUVEGARDE = "gestionnaireBudget.ser";

    public static void sauvegarder(GestionnaireBudget gestionnaire) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_SAUVEGARDE))) {
            oos.writeObject(gestionnaire);
        }
    }

    public static GestionnaireBudget charger() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_SAUVEGARDE))) {
            return (GestionnaireBudget) ois.readObject();
        } catch (FileNotFoundException e) {
            return null; // Pas de fichier existant
        }
    }
}