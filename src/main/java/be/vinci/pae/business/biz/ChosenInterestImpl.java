package be.vinci.pae.business.biz;

public class ChosenInterestImpl implements ChosenInterest {

  private int idChosenInterest;
  private int idMember;
  private int idObject;
  private String etatTransaction;

  /**
   * Constructeur.
   *
   * @param idChosenInterest l'id
   * @param idMember         l'id du membre
   * @param idObject         l'id de l'objet
   * @param etatInscription  l'état de la Transaction
   */
  public ChosenInterestImpl(int idChosenInterest, int idMember, int idObject,
      String etatInscription) {
    this.idChosenInterest = idChosenInterest;
    this.idMember = idMember;
    this.idObject = idObject;
    this.etatTransaction = etatInscription;
  }

  public ChosenInterestImpl() {

  }

  @Override
  public int getIdChosenInterest() {
    return idChosenInterest;
  }

  @Override
  public void setIdChosenInterest(int idChosenInterest) {
    this.idChosenInterest = idChosenInterest;
  }

  @Override
  public int getIdMember() {
    return idMember;
  }

  @Override
  public void setIdMember(int idMember) {
    this.idMember = idMember;
  }

  @Override
  public int getIdObject() {
    return idObject;
  }

  @Override
  public void setIdObject(int idObject) {
    this.idObject = idObject;
  }

  @Override
  public String getEtatTransaction() {
    return etatTransaction;
  }

  @Override
  public void setEtatTransaction(String etatTransaction) {
    this.etatTransaction = etatTransaction;
  }

}
