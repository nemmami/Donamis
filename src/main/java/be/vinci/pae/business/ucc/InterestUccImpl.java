package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.InterestDto;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.InterestDao;
import be.vinci.pae.donnees.dao.ObjectDao;
import be.vinci.pae.donnees.dao.UserDao;
import be.vinci.pae.exceptions.BizConflictException;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.BizNotFoundException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class InterestUccImpl implements InterestUcc {

  @Inject
  private InterestDao interestDao;

  @Inject
  private ObjectDao objectDao;

  @Inject
  private UserDao userDao;

  @Inject
  private DalServices dal;


  /**
   * Permet d'inserer un interet dans la db.
   *
   * @param idUser            l'id du user
   * @param idObject          l'id de l'objet
   * @param dateDisponibilite la date a laquelle on est disponible
   * @param appel             true si on souhaite être appellé, false sinon
   * @param numero            le numéro de telephone du user
   * @return l'interet inserer en db
   * @throws SQLException         si qqchose se passe mal
   * @throws BizConflictException si un des paramètres est vide ou null
   * @throws BizNotFoundException si aucun user correspond à l'idUser
   */
  @Override
  public InterestDto insertInterest(int idUser, int idObject, String dateDisponibilite,
      boolean appel, String numero) throws SQLException {

    if (dateDisponibilite == null || numero == null) {
      throw new BizException("Certaines informations sont fausses");
    }

    if (idUser <= 0 || idObject <= 0 || dateDisponibilite.isBlank() || numero.isBlank()) {
      throw new BizException("Certaines informations sont fausses");
    }

    if (numero.length() != 10) {
      throw new BizException("Mauvais format de numéro de téléphone");
    }

    dal.startTransaction();
    InterestDto myInterest;

    try {
      checkPreconditionInsertInterest(idUser, idObject);

      myInterest = interestDao.insertInterest(idUser, idObject, dateDisponibilite,
          appel);

      UserDto newUserDto = userDao.getOne(idUser);
      if (newUserDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Id non conforme");
      }
      userDao.setNumeroTelephone(idUser, numero);

      objectDao.updateNombreInteresses(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode insertInterest");
    }

    return myInterest;
  }

  /**
   * Permet d'inserer un interet dans la db.
   *
   * @param idUser            l'id du user
   * @param idObject          l'id de l'objet
   * @param dateDisponibilite la date a laquelle on est disponible
   * @param appel             true si on souhaite être appellé, false sinon
   * @return l'interet inserer en db
   * @throws SQLException         si qqchose se passe mal
   * @throws BizConflictException si un des paramètres est vide ou null
   */
  @Override
  public InterestDto insertInterest(int idUser, int idObject, String dateDisponibilite,
      boolean appel) throws SQLException {

    if (dateDisponibilite == null) {
      throw new BizException("Certaines informations sont fausses");
    }

    if (idUser <= 0 || idObject <= 0 || dateDisponibilite.isBlank()) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    InterestDto myInterest;

    try {
      checkPreconditionInsertInterest(idUser, idObject);

      myInterest = interestDao.insertInterest(idUser, idObject, dateDisponibilite, appel);
      objectDao.updateNombreInteresses(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode insertInterest");
    }

    return myInterest;
  }

  /**
   * Permet de recuperer tout les interets d'un objet.
   *
   * @param idObject l'id de l'objet
   * @return la liste de tous les interets
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idObject est nul
   */
  @Override
  public List<InterestDto> getObjectInterest(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<InterestDto> list;

    try {
      list = interestDao.getObjectInterest(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getObjectInterest");
    }

    return list;
  }

  /**
   * Permet de recuperer tout les interets d'un membre.
   *
   * @param idUser l'id du membre
   * @return la liste de tous les interets
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idUser est nul
   */
  @Override
  public List<InterestDto> getAllInterestFromUser(int idUser) throws SQLException {

    if (idUser <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<InterestDto> list;

    try {
      list = interestDao.getObjectInterest(idUser);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getObjectInterest");
    }

    return list;
  }

  /**
   * Permet de recuperer tout les interets d'un membre.
   *
   * @param idUser l'id du membre
   * @return la liste de tous les interets
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idUser est nul
   */
  @Override
  public List<InterestDto> getAllInterestForUserObject(int idUser)
      throws SQLException {

    if (idUser <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<InterestDto> list;

    try {
      list = interestDao.getAllInterestForUserObject(idUser);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllInterestForUserObject");
    }

    return list;
  }

  /**
   * Permet de verifier toutes les precondition nécessaire à l'ajout d'un interet.
   *
   * @param idUser   l'id du user
   * @param idObject l'id de l'objet
   * @throws SQLException si qqchose se passe mal
   */
  private void checkPreconditionInsertInterest(int idUser, int idObject) throws SQLException {
    if (userDao.getOne(idUser) == null) {
      dal.commitTransaction();
      throw new BizNotFoundException("L'id de ce membre n'existe pas");
    }

    if (objectDao.getObject(idObject) == null) {
      dal.commitTransaction();
      throw new BizNotFoundException("L'id de cet objet n'existe pas");
    }

    if (interestDao.getInterest(idUser, idObject)) {
      dal.commitTransaction();
      throw new BizConflictException("Vous avez déjà liker cette objet");
    }

    if (userDao.getOne(idUser).getIdUser() == objectDao.getObject(idObject).getMembre_offreur()) {
      dal.commitTransaction();
      throw new BizConflictException("Vous ne pouvze pas liker votre propre objet");
    }
  }

}