package corps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private final String nom;
    private final String mdpHash;
    private final List<Categorie> categories = new ArrayList<>();
    private final List<BudgetMensuel> budgets = new ArrayList<>();
    private BudgetMensuel budgetCourant;

    public User(String nom, String mdp) {
        this.nom = nom;
        this.mdpHash = String.valueOf(mdp.hashCode());
    }

    public boolean verifierMdp(String mdp) {
        return mdpHash.equals(String.valueOf(mdp.hashCode()));
    }

    // Gestion des catégories
    public void ajouterCategorie(Categorie categorie) {
        if (!categories.contains(categorie)) {
            categories.add(categorie);
        }
    }

    public List<Categorie> getCategories() {
        return new ArrayList<>(categories);
    }

    // Gestion des budgets
    public void ajouterBudget(BudgetMensuel budget) {
        if (!budgets.contains(budget)) {
            budgets.add(budget);
        }
        this.budgetCourant = budget;
    }

    public BudgetMensuel getBudgetPour(int mois, int annee) {
        return budgets.stream()
                .filter(b -> b.getMois() == mois && b.getAnnee() == annee)
                .findFirst()
                .orElse(null);
    }

    public List<BudgetMensuel> getBudgets() {
        return new ArrayList<>(budgets);
    }

    // Getters standards
    public String getNom() { return nom; }
    public BudgetMensuel getBudgetCourant() { return budgetCourant; }
    public void setBudgetCourant(BudgetMensuel budget) { this.budgetCourant = budget; }
}