package corps;

import exceptions.AuthentificationException;
import exceptions.SaisieInvalideException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GestionnaireBudget implements Serializable {
    private static final long serialVersionUID = 1L;
    private static GestionnaireBudget instance;
    private User utilisateurCourant;
    private final List<User> utilisateurs = new ArrayList<>();

    private GestionnaireBudget() {}

    public static synchronized GestionnaireBudget getInstance() {
        if (instance == null) {
            instance = new GestionnaireBudget();
        }
        return instance;
    }

    public void inscrireUtilisateur(String nom, String mdp) throws SaisieInvalideException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new SaisieInvalideException("Le nom d'utilisateur ne peut pas être vide.");
        }
        if (mdp == null || mdp.trim().isEmpty()) {
            throw new SaisieInvalideException("Le mot de passe ne peut pas être vide.");
        }
        User nouvelUtilisateur = new User(nom, mdp);
        utilisateurs.add(nouvelUtilisateur);
        utilisateurCourant = nouvelUtilisateur;
    }

    public void seConnecter(String nom, String mdp) throws AuthentificationException {
        for (User user : utilisateurs) {
            if (user.getNom().equals(nom)) {
                if (user.verifierMdp(mdp)) {
                    utilisateurCourant = user;
                    return;
                }
                break;
            }
        }
        throw new AuthentificationException("Nom d'utilisateur ou mot de passe incorrect");
    }

    public void seDeconnecter() {
        utilisateurCourant = null;
    }

    public boolean estConnecte() {
        return utilisateurCourant != null;
    }

    public User getUtilisateurCourant() {
        return utilisateurCourant;
    }

    public void setUtilisateurCourant(User user) {
        this.utilisateurCourant = user;
    }

    public List<User> getUtilisateurs() {
        return new ArrayList<>(utilisateurs);
    }
}