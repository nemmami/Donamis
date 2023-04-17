package be.vinci.pae.business.biz;

public class InterestImpl implements Interest {

  private int idInterest;
  private int idUser;
  private int idObject;
  private String dateDisponibilite;
  private boolean appel;

  /**
   * Constructeur d'un interet.
   */
  public InterestImpl(int idInterest, int idUser, int idObject, String dateDisponibilite,
      boolean appel) {
    this.idInterest = idInterest;
    this.idUser = idUser;
    this.idObject = idObject;
    this.dateDisponibilite = dateDisponibilite;
    this.appel = appel;
  }

  public InterestImpl() {

  }

  @Override
  public int getIdInterest() {
    return idInterest;
  }

  @Override
  public void setIdInterest(int idInterest) {
    this.idInterest = idInterest;
  }

  @Override
  public int getIdUser() {
    return idUser;
  }

  @Override
  public void setUser(int idUser) {
    this.idUser = idUser;
  }

  @Override
  public int getIdObject() {
    return idObject;
  }

  @Override
  public void setObject(int idObject) {
    this.idObject = idObject;
  }

  @Override
  public String getDateDisponibilite() {
    return dateDisponibilite;
  }

  @Override
  public void setDateDisponibilite(String dateDisponibilite) {
    this.dateDisponibilite = dateDisponibilite;
  }

  @Override
  public boolean isAppel() {
    return appel;
  }

  @Override
  public void setAppel(boolean appel) {
    this.appel = appel;
  }

}
