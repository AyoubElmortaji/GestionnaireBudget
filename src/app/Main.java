package app;

import corps.*;
import exceptions.*;
import utils.Sauvegarder;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static GestionnaireBudget gestionnaire = GestionnaireBudget.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        chargerDonnees();
        bouclePrincipale();
    }

    private static void chargerDonnees() {
        try {
            GestionnaireBudget gestionnaireCharge = Sauvegarder.charger();
            if (gestionnaireCharge != null) {
                gestionnaire = gestionnaireCharge;
                System.out.println("✅ Données chargées avec succès");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Aucune donnée sauvegardée trouvée");
        }
    }

    private static void bouclePrincipale() {
        boolean quitter = false;
        while (!quitter) {
            if (gestionnaire.estConnecte()) {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1. Gérer le budget");
                System.out.println("2. Ajouter un budget mensuel");
                System.out.println("3. Se déconnecter");
                System.out.println("4. Quitter");
                System.out.print("Votre choix : ");

                String choix = scanner.nextLine();
                switch (choix) {
                    case "1":
                        gererBudget();
                        break;
                    case "2":
                        initialiserBudgetUtilisateur();
                        System.out.println("✅ Nouveau budget mensuel ajouté avec succès");
                        break;
                    case "3":
                        gestionnaire.seDeconnecter();
                        System.out.println("✅ Déconnexion réussie");
                        break;
                    case "4":
                        quitter = true;
                        sauvegarderEtQuitter();
                        break;
                    default:
                        System.out.println("❌ Choix invalide");
                }
            } else {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1. Se connecter");
                System.out.println("2. S'inscrire");
                System.out.println("3. Quitter");
                System.out.print("Votre choix : ");

                String choix = scanner.nextLine();
                switch (choix) {
                    case "1":
                        connecterUtilisateur();
                        break;
                    case "2":
                        inscrireUtilisateur();
                        break;
                    case "3":
                        quitter = true;
                        sauvegarderEtQuitter();
                        break;
                    default:
                        System.out.println("❌ Choix invalide");
                }
            }
        }
    }

    private static void connecterUtilisateur() {
        System.out.print("Nom d'utilisateur : ");
        String nom = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();

        try {
            gestionnaire.seConnecter(nom, mdp);
            System.out.println("✅ Connexion réussie");
        } catch (AuthentificationException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void inscrireUtilisateur() {
        System.out.print("Nom d'utilisateur : ");
        String nom = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();

        try {
            gestionnaire.inscrireUtilisateur(nom, mdp);
            initialiserBudgetUtilisateur();
            System.out.println("✅ Inscription réussie");
        } catch (SaisieInvalideException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void initialiserBudgetUtilisateur() {
        try {
            System.out.println("\n=== CONFIGURATION DU BUDGET ===");
            System.out.print("Mois (1-12) : ");
            int mois = Integer.parseInt(scanner.nextLine());
            if (mois < 1 || mois > 12) {
                throw new SaisieInvalideException("Mois invalide : doit être entre 1 et 12.");
            }

            System.out.print("Année : ");
            int annee = Integer.parseInt(scanner.nextLine());

            System.out.print("Budget max : ");
            double budgetMax = Double.parseDouble(scanner.nextLine());
            if (budgetMax <= 0) {
                throw new SaisieInvalideException("Budget max invalide : doit être positif.");
            }

            BudgetMensuel budget = new BudgetMensuel(mois, annee, budgetMax);
            gestionnaire.getUtilisateurCourant().ajouterBudget(budget);
        } catch (NumberFormatException e) {
            System.out.println("❌ Saisie invalide : entrez un nombre.");
        } catch (SaisieInvalideException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void gererBudget() {
        // Code existant inchangé
        boolean retour = false;
        while (!retour) {
            System.out.println("\n=== GESTION BUDGET ===");
            System.out.println("1. Ajouter une transaction");
            System.out.println("2. Afficher le solde");
            System.out.println("3. Lister les transactions");
            System.out.println("4. Voir dépenses par catégorie");
            System.out.println("5. Voir toutes les dépenses");
            System.out.println("6. Gérer les catégories");
            System.out.println("7. Consulter un budget antérieur");
            System.out.println("8. Retour au menu principal");
            System.out.print("Votre choix : ");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1": ajouterTransaction(); break;
                case "2": afficherSolde(); break;
                case "3": listerTransactions(); break;
                case "4": afficherDepensesParCategorie(); break;
                case "5": afficherToutesDepenses(); break;
                case "6": gererCategories(); break;
                case "7": consulterBudgetHistorique(); break;
                case "8":
                    retour = true;
                    System.out.println("✅ Retour au menu principal");
                    break;
                default: System.out.println("❌ Choix invalide");
            }
        }
    }

    private static void ajouterTransaction() {
        // Code existant inchangé
        System.out.println("\nType de transaction :");
        System.out.println("1. Dépense");
        System.out.println("2. Revenu");
        System.out.print("Votre choix : ");
        String type = scanner.nextLine();

        try {
            System.out.print("Montant : ");
            double montant = Double.parseDouble(scanner.nextLine());
            if (montant <= 0) {
                throw new SaisieInvalideException("Montant invalide : doit être positif.");
            }

            System.out.print("Description : ");
            String description = scanner.nextLine();

            List<Categorie> categories = gestionnaire.getUtilisateurCourant().getCategories();
            if (!categories.isEmpty()) {
                System.out.println("Catégories existantes :");
                categories.forEach(c -> System.out.println("- " + c.getNom()));
            }

            System.out.print("Catégorie : ");
            String nomCategorie = scanner.nextLine();

            Categorie categorie = categories.stream()
                    .filter(c -> c.getNom().equalsIgnoreCase(nomCategorie))
                    .findFirst()
                    .orElse(null);

            if (categorie == null) {
                categorie = new Categorie(nomCategorie);
                gestionnaire.getUtilisateurCourant().ajouterCategorie(categorie);
                System.out.println("✅ Nouvelle catégorie '" + nomCategorie + "' ajoutée automatiquement");
            }

            System.out.print("Date (AAAA-MM-JJ) [laissez vide pour aujourd'hui] : ");
            String dateInput = scanner.nextLine();
            LocalDate date = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);

            Transaction transaction = type.equals("1")
                    ? new Depense(montant, description, categorie, date)
                    : new Revenu(montant, description, categorie, date);

            gestionnaire.getUtilisateurCourant().getBudgetCourant().ajouterTransaction(transaction);
            System.out.println("✅ Transaction enregistrée");

            if (gestionnaire.getUtilisateurCourant().getBudgetCourant().aDepasseBudget()) {
                System.out.println("⚠️ Attention : vous avez dépassé votre budget !");
            }
        } catch (DateTimeParseException e) {
            System.out.println("❌ Format de date incorrect (utilisez AAAA-MM-JJ).");
        } catch (NumberFormatException e) {
            System.out.println("❌ Saisie invalide : entrez un nombre.");
        } catch (SaisieInvalideException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (DepassementBudgetException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void afficherSolde() {
        // Code existant inchangé
        BudgetMensuel budget = gestionnaire.getUtilisateurCourant().getBudgetCourant();
        System.out.printf("\n=== BUDGET %d/%d ===\n", budget.getMois(), budget.getAnnee());
        System.out.printf("Budget max : %.2f€\n", budget.getBudgetMax());
        System.out.printf("Solde actuel : %.2f€\n", budget.getSolde());
        System.out.printf("Dépenses totales : %.2f€\n", budget.getTotalDepenses());
        System.out.printf("Revenus totaux : %.2f€\n",
                budget.getTransactions().stream()
                        .filter(t -> !t.estDepense())
                        .mapToDouble(Transaction::getMontant)
                        .sum());
        System.out.printf("Budget dépassé : %s\n", budget.aDepasseBudget() ? "OUI ⚠️" : "NON ✅");
    }

    private static void listerTransactions() {
        // Code existant inchangé
        List<Transaction> transactions = gestionnaire.getUtilisateurCourant()
                .getBudgetCourant()
                .getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction enregistrée");
        } else {
            System.out.println("\n=== LISTE DES TRANSACTIONS ===");
            transactions.forEach(t -> {
                String type = t.estDepense() ? "Dépense" : "Revenu";
                System.out.printf("[%s] %.2f€ - %s (%s) - %s\n",
                        type, t.getMontant(), t.getDescription(),
                        t.getCategorie().getNom(), t.getDate());
            });
        }
    }

    private static void afficherDepensesParCategorie() {
        // Code existant inchangé
        System.out.print("\nCatégorie à filtrer : ");
        String nomCategorie = scanner.nextLine();
        Categorie categorie = new Categorie(nomCategorie);

        List<Depense> depenses = gestionnaire.getUtilisateurCourant()
                .getBudgetCourant()
                .getDepensesParCategorie(categorie);

        if (depenses.isEmpty()) {
            System.out.println("Aucune dépense pour cette catégorie");
        } else {
            System.out.println("\n=== DÉPENSES (" + nomCategorie + ") ===");
            depenses.forEach(d ->
                    System.out.printf("- %.2f€ - %s - %s\n",
                            d.getMontant(), d.getDescription(), d.getDate()));
            System.out.printf("Total : %.2f€\n",
                    depenses.stream().mapToDouble(Depense::getMontant).sum());
        }
    }

    private static void afficherToutesDepenses() {
        // Code existant inchangé
        List<Depense> depenses = gestionnaire.getUtilisateurCourant()
                .getBudgetCourant()
                .getDepenses();

        if (depenses.isEmpty()) {
            System.out.println("Aucune dépense enregistrée");
        } else {
            System.out.println("\n=== TOUTES LES DÉPENSES ===");
            depenses.forEach(d ->
                    System.out.printf("- %.2f€ - %s (%s) - %s\n",
                            d.getMontant(), d.getDescription(),
                            d.getCategorie().getNom(), d.getDate()));
            System.out.printf("\nTotal dépenses : %.2f€\n",
                    depenses.stream().mapToDouble(Depense::getMontant).sum());
        }
    }

    private static void gererCategories() {
        // Code existant inchangé
        boolean retour = false;
        while (!retour) {
            System.out.println("\n=== GESTION CATÉGORIES ===");
            System.out.println("1. Ajouter une catégorie");
            System.out.println("2. Lister mes catégories");
            System.out.println("3. Retour");
            System.out.print("Votre choix : ");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    System.out.print("Nom de la catégorie : ");
                    String nom = scanner.nextLine();
                    if (nom == null || nom.trim().isEmpty()) {
                        System.out.println("❌ Nom de catégorie invalide : ne peut pas être vide.");
                    } else {
                        gestionnaire.getUtilisateurCourant().ajouterCategorie(new Categorie(nom));
                        System.out.println("✅ Catégorie ajoutée");
                    }
                    break;
                case "2":
                    List<Categorie> categories = gestionnaire.getUtilisateurCourant().getCategories();
                    if (categories.isEmpty()) {
                        System.out.println("Aucune catégorie créée");
                    } else {
                        System.out.println("\n=== MES CATÉGORIES ===");
                        categories.forEach(c -> System.out.println("- " + c.getNom()));
                    }
                    break;
                case "3":
                    retour = true;
                    break;
                default:
                    System.out.println("❌ Choix invalide");
            }
        }
    }

    private static void consulterBudgetHistorique() {
        // Code existant inchangé
        try {
            System.out.print("\nMois (1-12) : ");
            int mois = Integer.parseInt(scanner.nextLine());
            if (mois < 1 || mois > 12) {
                throw new SaisieInvalideException("Mois invalide : doit être entre 1 et 12.");
            }

            System.out.print("Année : ");
            int annee = Integer.parseInt(scanner.nextLine());

            BudgetMensuel budget = gestionnaire.getUtilisateurCourant().getBudgetPour(mois, annee);
            if (budget != null) {
                System.out.printf("\n=== BUDGET %d/%d ===\n", budget.getMois(), budget.getAnnee());
                System.out.printf("Budget max : %.2f€\n", budget.getBudgetMax());
                System.out.printf("Solde final : %.2f€\n", budget.getSolde());
                System.out.printf("Dépenses totales : %.2f€\n", budget.getTotalDepenses());
            } else {
                System.out.println("❌ Aucun budget trouvé pour cette période");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Saisie invalide : entrez un nombre.");
        } catch (SaisieInvalideException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void sauvegarderEtQuitter() {
        try {
            Sauvegarder.sauvegarder(gestionnaire); // Assure-toi que c'est gestionnaire, pas un User
            System.out.println("✅ Données sauvegardées");
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la sauvegarde");
        }
        scanner.close();
        System.out.println("👋 Fermeture de l'application");
    }
}