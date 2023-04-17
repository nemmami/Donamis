package be.vinci.pae.business.dto;

public interface AddressDto {

  int getIdAddresse();

  void setIdAddresse(int idAdresse);

  String getRue();

  void setRue(String rue);

  int getNumero();

  void setNumero(int numero);

  String getBoite();

  void setBoite(String boite);

  int getCodePostal();

  void setCodePostal(int codePostal);

  String getVille();

  void setVille(String ville);
}
