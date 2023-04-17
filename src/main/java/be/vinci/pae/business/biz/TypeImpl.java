package be.vinci.pae.business.biz;

public class TypeImpl implements Type {

  private int idType;
  private String libelle;

  public TypeImpl(int idType, String libelle) {
    this.idType = idType;
    this.libelle = libelle;
  }

  public TypeImpl() {

  }

  @Override
  public int getIdType() {
    return idType;
  }

  @Override
  public String getLibelle() {
    return libelle;
  }

  @Override
  public void setIdType(int idType) {
    this.idType = idType;
  }

  @Override
  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }
}
