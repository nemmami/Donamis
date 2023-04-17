package be.vinci.pae.business.biz;

import org.mindrot.jbcrypt.BCrypt;

public class UserImpl implements User {

  private int idUser;
  private String pseudo;
  private String nom;
  private String prenom;
  private boolean admin;
  private String telephone;
  private String etatInscription;
  private String raisonRefus;
  private int idAdresse;
  private String motDePasse;

  /**
   * Class construire d'un user.
   */
  public UserImpl(int idUser, String pseudo, String nom, String prenom, boolean admin,
      String telephone, String etatInscription, String raisonRefus,
      int idAdresse, String motDePasse) {
    this.idUser = idUser;
    this.pseudo = pseudo;
    this.nom = nom;
    this.prenom = prenom;
    this.admin = admin;
    this.telephone = telephone;
    this.etatInscription = etatInscription;
    this.raisonRefus = raisonRefus;
    this.idAdresse = idAdresse;
    this.motDePasse = motDePasse;
  }

  public UserImpl() {
  }

  @Override
  public int getIdUser() {
    return idUser;
  }

  @Override
  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  @Override
  public String getPseudo() {
    return pseudo;
  }

  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public boolean isAdmin() {
    return admin;
  }

  @Override
  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  @Override
  public String getTelephone() {
    return telephone;
  }

  @Override
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  @Override
  public String getEtatInscription() {
    return etatInscription;
  }

  @Override
  public void setEtatInscription(String etatInscription) {
    this.etatInscription = etatInscription;
  }

  @Override
  public String getRaisonRefus() {
    return raisonRefus;
  }

  @Override
  public void setRaisonRefus(String raisonRefus) {
    this.raisonRefus = raisonRefus;
  }

  @Override
  public int getIdAdresse() {
    return idAdresse;
  }

  @Override
  public void setIdAdresse(int idAdresse) {
    this.idAdresse = idAdresse;
  }

  @Override
  public String getMotDePasse() {
    return motDePasse;
  }

  @Override
  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
  }

  @Override
  public boolean checkMotDePasse(String motDePasse) {
    return BCrypt.checkpw(motDePasse, this.motDePasse);
  }

  @Override
  public String hashMotDePasse(String motDePasse) {
    return BCrypt.hashpw(motDePasse, BCrypt.gensalt());
  }

}
