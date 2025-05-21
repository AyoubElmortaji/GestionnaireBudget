package corps;

import java.time.LocalDate;

public class Depense extends Transaction {
    public Depense(double montant, String description, Categorie categorie, LocalDate date) {
        super(montant, description, categorie, date);
    }

    @Override
    public boolean estDepense() {
        return true;
    }
}