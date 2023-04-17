package be.vinci.pae.business.dto;

public interface InterestDto {

  int getIdInterest();

  void setIdInterest(int idInterest);

  int getIdUser();

  void setUser(int idUser);

  int getIdObject();

  void setObject(int idObject);

  String getDateDisponibilite();

  void setDateDisponibilite(String dateDisponibilite);

  boolean isAppel();

  void setAppel(boolean appel);


}
