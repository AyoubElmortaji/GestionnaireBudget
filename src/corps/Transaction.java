package corps;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Transaction implements Serializable {
    private final double montant;
    private final String description;
    private final Categorie categorie;
    private final LocalDate date;

    public Transaction(double montant, String description, Categorie categorie, LocalDate date) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut être vide");
        }
        if (categorie == null) {
            throw new IllegalArgumentException("Une catégorie doit être spécifiée");
        }
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }

        this.montant = montant;
        this.description = description;
        this.categorie = categorie;
        this.date = date;
    }
    public double getMontant() {
        return montant;
    }
    public String getDescription() {
        return description;
        }
    public Categorie getCategorie() {
        return categorie;
    }
    public LocalDate getDate()
    {
        return date;
    }
    public abstract boolean estDepense();
}