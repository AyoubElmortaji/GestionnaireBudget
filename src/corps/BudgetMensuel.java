package corps;

import exceptions.DepassementBudgetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetMensuel {
    private final int mois;
    private final int annee;
    private final double budgetMax;
    private final List<Transaction> transactions = new ArrayList<>();

    public BudgetMensuel(int mois, int annee, double budgetMax) {
        this.mois = mois;
        this.annee = annee;
        this.budgetMax = budgetMax;
    }

    public void ajouterTransaction(Transaction t) throws DepassementBudgetException {
        if (t.estDepense() && getTotalDepenses() + t.getMontant() > budgetMax) {
            throw new DepassementBudgetException("Budget dépassé !");
        }
        transactions.add(t);
    }

    public double getSolde() {
        return transactions.stream()
                .mapToDouble(t -> t.estDepense() ? -t.getMontant() : t.getMontant())
                .sum();
    }

    public double getTotalDepenses() {
        return getDepenses().stream()
                .mapToDouble(Transaction::getMontant)
                .sum();
    }

    public List<Depense> getDepensesParCategorie(Categorie c) {
        return getDepenses().stream()
                .filter(d -> d.getCategorie().equals(c))
                .collect(Collectors.toList());
    }

    public List<Depense> getDepenses() {
        return transactions.stream()
                .filter(Transaction::estDepense)
                .map(t -> (Depense)t)
                .collect(Collectors.toList());
    }

    public boolean aDepasseBudget() {
        return getTotalDepenses() > budgetMax;
    }

    // Getters
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }
    public int getMois() { return mois; }
    public int getAnnee() { return annee; }
    public double getBudgetMax() { return budgetMax; }
}