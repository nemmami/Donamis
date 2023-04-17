package be.vinci.pae.business.biz;

class AddressImpl implements Address {

  private int idAdresse;
  private String rue;
  private int numero;
  private String boite;
  private int codePostal;
  private String ville;


  @Override
  public int getIdAddresse() {
    return idAdresse;
  }

  @Override
  public void setIdAddresse(int idAdresse) {
    this.idAdresse = idAdresse;
  }

  @Override
  public String getRue() {
    return rue;
  }

  @Override
  public void setRue(String rue) {
    this.rue = rue;
  }

  @Override
  public int getNumero() {
    return numero;
  }

  @Override
  public void setNumero(int numero) {
    this.numero = numero;
  }

  @Override
  public String getBoite() {
    return boite;
  }

  @Override
  public void setBoite(String boite) {
    this.boite = boite;
  }

  @Override
  public int getCodePostal() {
    return codePostal;
  }

  @Override
  public void setCodePostal(int codePostal) {
    this.codePostal = codePostal;
  }

  @Override
  public String getVille() {
    return ville;
  }

  @Override
  public void setVille(String ville) {
    this.ville = ville;
  }
}
