package be.vinci.pae.business.biz;

import java.util.Date;

public class ObjectImpl implements Object {

  private int idObjet;
  private int idType;
  private String titre;
  private String description;
  private String photo;
  private Date date;
  private String plageHoraire;
  private int membreOffreur;
  private String etat;
  private int nbrInteresse;

  /**
   * Constructeur de l'objet.
   */
  public ObjectImpl(int idObjet, int idType, String titre, String description, String photo,
      Date date, String plageHoraire, int membreOffreur, String etat, int nbrInteresse) {
    this.idObjet = idObjet;
    this.idType = idType;
    this.titre = titre;
    this.description = description;
    this.photo = photo;
    this.date = date;
    this.plageHoraire = plageHoraire;
    this.membreOffreur = membreOffreur;
    this.etat = etat;
    this.nbrInteresse = nbrInteresse;
  }

  public ObjectImpl() {

  }

  @Override
  public int getIdObjet() {
    return idObjet;
  }

  @Override
  public void setIdObjet(int idObjet) {
    this.idObjet = idObjet;
  }

  @Override
  public int getIdType() {
    return idType;
  }

  @Override
  public void setIdType(int idType) {
    this.idType = idType;
  }

  @Override
  public String getTitre() {
    return titre;
  }

  @Override
  public void setTitre(String titre) {
    this.titre = titre;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getPhoto() {
    return photo;
  }

  @Override
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public Date getDate() {
    return date;
  }

  @Override
  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public String getPlageHoraire() {
    return plageHoraire;
  }

  @Override
  public void setPlageHoraire(String plageHoraire) {
    this.plageHoraire = plageHoraire;
  }

  @Override
  public int getMembre_offreur() {
    return membreOffreur;
  }

  @Override
  public void setMembre_offreur(int membreOffreur) {
    this.membreOffreur = membreOffreur;
  }

  @Override
  public String getEtat() {
    return etat;
  }

  @Override
  public void setEtat(String etat) {
    this.etat = etat;
  }

  @Override
  public int getNbrInteresse() {
    return nbrInteresse;
  }

  @Override
  public void setNbrInteresse(int nbrInteresse) {
    this.nbrInteresse = nbrInteresse;
  }
}
