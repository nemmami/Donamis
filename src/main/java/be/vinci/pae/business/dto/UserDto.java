package be.vinci.pae.business.dto;

public interface UserDto {

  int getIdUser();

  void setIdUser(int idUser);

  String getPseudo();

  void setPseudo(String pseudo);

  String getNom();

  void setNom(String nom);

  String getPrenom();

  void setPrenom(String prenom);

  boolean isAdmin();

  void setAdmin(boolean admin);

  String getTelephone();

  void setTelephone(String telephone);

  String getEtatInscription();

  void setEtatInscription(String etatInscription);

  String getRaisonRefus();

  void setRaisonRefus(String raisonRefus);

  int getIdAdresse();

  void setIdAdresse(int idAdresse);

  String getMotDePasse();

  void setMotDePasse(String motDePasse);


}
