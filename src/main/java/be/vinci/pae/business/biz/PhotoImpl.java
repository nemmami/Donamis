package be.vinci.pae.business.biz;

public class PhotoImpl implements Photo {

  private int idPhoto;
  private String nomPhoto;

  public PhotoImpl(int idPhoto, String nomPhoto) {
    this.idPhoto = idPhoto;
    this.nomPhoto = nomPhoto;
  }

  public PhotoImpl() {
  }

  @Override
  public int getIdPhoto() {
    return idPhoto;
  }

  @Override
  public void setIdPhoto(int idPhoto) {
    this.idPhoto = idPhoto;
  }

  @Override
  public String getNomPhoto() {
    return nomPhoto;
  }

  @Override
  public void setNomPhoto(String nomPhoto) {
    this.nomPhoto = nomPhoto;
  }
}
