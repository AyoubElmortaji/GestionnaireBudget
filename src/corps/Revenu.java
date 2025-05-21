package corps;

import java.time.LocalDate;

public class Revenu extends Transaction {
    public Revenu(double montant, String description, Categorie categorie, LocalDate date) {
        super(montant, description, categorie, date);
    }

    @Override
    public boolean estDepense() {
        return false;
    }
}