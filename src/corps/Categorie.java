package corps;

import java.io.Serializable;

public class Categorie implements Serializable {
    private final String nom;

    public Categorie(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categorie other = (Categorie) obj;
        return nom.equalsIgnoreCase(other.nom);
    }

    @Override
    public int hashCode() {
        return nom.toLowerCase().hashCode();
    }
}