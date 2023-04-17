package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ChosenInterestDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.ChosenInterestDao;
import be.vinci.pae.donnees.dao.ObjectDao;
import be.vinci.pae.exceptions.BizConflictException;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class ChosenInterestUccImpl implements ChosenInterestUcc {

  @Inject
  private ChosenInterestDao chosenInterestDao;

  @Inject
  private ObjectDao objectDao;

  @Inject
  private DalServices dal;

  /**
   * Permet d'inserer un interet choisi.
   *
   * @param idMembre l'id du membre
   * @param idObject l'id de l'objet
   * @return l'interet choisi
   * @throws SQLException         si il se passe qqchose de mal
   * @throws BizException         si idObject ou idMembre est nul
   * @throws BizConflictException si il existe déjà un interet choisi correspondant au membre et à
   *                              l'objet dont l'etat est "En attente"
   */
  @Override
  public ChosenInterestDto insertChosenInterest(int idMembre, int idObject) throws SQLException {

    if (idMembre <= 0 || idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    ChosenInterestDto chosenInterestDto;

    try {
      List<ChosenInterestDto> chosen;
      chosen = chosenInterestDao.getChosenInterest(idObject);
      for (ChosenInterestDto c : chosen) {
        if (c.getIdMember() == idMembre && c.getEtatTransaction().equals("En attente")) {
          dal.commitTransaction();
          throw new BizConflictException("Vous avez déja essayer de choisir cette personne");
        }
      }

      chosenInterestDto = chosenInterestDao.insertChosenInterest(idMembre, idObject);
      objectDao.setEtatReceveurChoisi(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode insertChosenInterest");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de changer l'etat de la transaction si le receveur est venu.
   *
   * @param idObject l'id de l'objet
   * @return chosenInterestDto l'interet choisi
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idObject est nul
   */
  @Override
  public ChosenInterestDto setEtatTransactionVenu(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    ChosenInterestDto chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.setEtatTransactionVenu(idObject);
      objectDao.setEtatDonne(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      e.printStackTrace();
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode setEtatTransactionVenu");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de changer l'etat de la transaction si le receveur n'est pas venu.
   *
   * @param idObject l'id de l'objet
   * @return chosenInterestDto l'interet choisi
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idObject est nul
   */
  @Override
  public ChosenInterestDto setEtatTransactionPasVenu(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    ChosenInterestDto chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.setEtatTransactionPasVenu(idObject);
      //objectDao.setEtatOffert(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode setEtatTransactionPasVenu");
    }
    return chosenInterestDto;
  }

  /**
   * Permet de recuperer une liste d'interet choisi.
   *
   * @param idObject l'id de l'objet
   * @return l'interet choisi
   * @throws SQLException si il se passe qqchose de mal
   * @throws BizException si idObject est nul
   */
  @Override
  public List<ChosenInterestDto> getChosenInterest(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<ChosenInterestDto> chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.getChosenInterest(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getChosenInterest");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer l'interet choisi de l'objet.
   *
   * @param idObject l'id de l'objet
   * @return l'interet choisi
   * @throws SQLException si il se passe qqchose de mal
   * @throws BizException si idObject est nul
   */
  @Override
  public ChosenInterestDto getChosenInterestByObject(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    ChosenInterestDto chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.getChosenInterestByObject(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getChosenInterestByObject");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer une liste d'interet choisi pour un membre.
   *
   * @param idMember l'id de l'objet
   * @return l'interet choisi
   * @throws SQLException si il se passe qqchose de mal
   * @throws BizException si idMembre est nul
   */
  @Override
  public List<ChosenInterestDto> getAllChosenInterestMember(int idMember) throws SQLException {

    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<ChosenInterestDto> chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.getAllChosenInterestMember(idMember);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllChosenInterestMember");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer une liste d'interet choisi pour un membre selon la notif.
   *
   * @param idUser l'id de l'user
   * @return l'interet choisi
   * @throws SQLException si il se passe qqchose de mal
   * @throws BizException si idUser est nul
   */
  @Override
  public List<ChosenInterestDto> getChosenInterestByMember(int idUser)
      throws SQLException {

    if (idUser <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<ChosenInterestDto> chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.getChosenInterestByMember(idUser);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getChosenInterestByMember");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer la liste de tout les objets qu'un membre a donné sur base de son id.
   *
   * @param idMember l'id de l'objet
   * @return l'interet choisi
   * @throws SQLException si il se passe qqchose de mal
   * @throws BizException si idMembre est nul
   */
  @Override
  public List<ChosenInterestDto> getGivenObjects(int idMember) throws SQLException {

    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<ChosenInterestDto> chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.getGivenObjects(idMember);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getGivenObjects");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer la liste de tout les objets qu'un membre a reçu sur base de son id.
   *
   * @param idMember l'id de l'objet
   * @return l'interet choisi
   * @throws SQLException si il se passe qqchose de mal
   * @throws BizException si idMembre est nul
   */
  @Override
  public List<ChosenInterestDto> getReceivedObjects(int idMember) throws SQLException {

    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<ChosenInterestDto> chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.getReceivedObjects(idMember);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getReceivedObjects");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer la liste de tout les objets qu'un membre n'est pas venu chercher sur base
   * de son id.
   *
   * @param idMember l'id de l'objet
   * @return l'interet choisi
   * @throws SQLException si il se passe qqchose de mal
   * @throws BizException si idMembre est nul
   */
  @Override
  public List<ChosenInterestDto> getNeverCameObjects(int idMember) throws SQLException {

    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<ChosenInterestDto> chosenInterestDto;

    try {
      chosenInterestDto = chosenInterestDao.getNeverCameObjects(idMember);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getNeverCameObjects");
    }

    return chosenInterestDto;
  }

}