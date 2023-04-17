package be.vinci.pae.business.dto;

import java.util.Date;

public interface ObjectDto {

  int getIdObjet();

  void setIdObjet(int idObjet);

  int getIdType();

  void setIdType(int idType);

  String getTitre();

  void setTitre(String titre);

  String getDescription();

  void setDescription(String description);

  String getPhoto();

  void setPhoto(String photo);

  Date getDate();

  void setDate(Date date);

  String getPlageHoraire();

  void setPlageHoraire(String plageHoraire);

  int getMembre_offreur();

  void setMembre_offreur(int membreOffreur);

  String getEtat();

  void setEtat(String etat);

  int getNbrInteresse();

  void setNbrInteresse(int nbrInteresse);


}
