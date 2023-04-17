package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.ObjectDao;
import be.vinci.pae.donnees.dao.TypeDao;
import be.vinci.pae.donnees.dao.UserDao;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.BizNotFoundException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class ObjectUccImpl implements ObjectUcc {

  @Inject
  private ObjectDao objectDao;

  @Inject
  private TypeDao typeDao;

  @Inject
  private UserDao userDao;

  @Inject
  private DalServices dal;

  /**
   * Permet d'inserer un objet dans la DB.
   *
   * @param idType        le type
   * @param titre         le titre
   * @param description   la description
   * @param photo         la photo
   * @param plageHoraire  plage horaire
   * @param membreOffreur le membre
   * @return myObject
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si un des attributs est vide ou null
   * @throws BizNotFoundException si idType ne correspond a aucun type
   * @throws BizNotFoundException si membreOffreur ne correspond a aucun membre
   */
  @Override
  public ObjectDto insertObject(int idType, String titre, String description, String photo,
      String plageHoraire, int membreOffreur) throws SQLException {

    if (titre == null || description == null || plageHoraire == null || photo == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (idType <= 0 || titre.isBlank() || description.isBlank() || plageHoraire.isBlank() || photo
        .isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    ObjectDto myObject;

    try {

      if (typeDao.getOne(idType) == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("L'id de ce type n'existe pas");
      }

      if (userDao.getOne(membreOffreur) == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("L'id de ce membre n'existe pas");
      }

      myObject = objectDao.insertObject(idType, titre, description, photo,
          plageHoraire, membreOffreur);

      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode insertObject");
    }

    return myObject;
  }

  /**
   * Permet d'inserer un objet dans la DB.
   *
   * @param idType        le type
   * @param titre         le titre
   * @param description   la description
   * @param plageHoraire  plage horaire
   * @param membreOffreur le membre
   * @return myObject
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si un des attributs est vide ou null
   * @throws BizNotFoundException si idType ne correspond a aucun type
   * @throws BizNotFoundException si membreOffreur ne correspond a aucun membre
   */
  @Override
  public ObjectDto insertObjectWithoutPhoto(int idType, String titre, String description,
      String plageHoraire, int membreOffreur) throws SQLException {

    if (titre == null || description == null || plageHoraire == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (idType <= 0 || titre.isBlank() || description.isBlank() || plageHoraire.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    ObjectDto myObject;

    try {
      if (typeDao.getOne(idType) == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("L'id de ce type n'existe pas");
      }

      if (userDao.getOne(membreOffreur) == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("L'id de ce membre n'existe pas");
      }

      myObject = objectDao.insertObject(idType, titre, description,
          plageHoraire, membreOffreur);

      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode insertObjectWithoutPhoto");
    }

    return myObject;
  }

  /**
   * Permet de mettre les informations d'un objet à jour.
   *
   * @param idObject     l'id de l'objet
   * @param titre        le titre l'objet
   * @param description  la description
   * @param plageHoraire la plage horaire
   * @return l'objet modifié
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si un des attributs est vide ou null
   */
  @Override
  public ObjectDto updateObject(int idObject, String titre, String description,
      String plageHoraire) throws SQLException {

    if (titre == null || description == null || plageHoraire == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (idObject <= 0 || titre.isBlank() || description.isBlank() || plageHoraire.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    ObjectDto myObject;

    try {
      myObject = objectDao.updateObject(idObject, titre, description, plageHoraire);

      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode updateObject");
    }

    return myObject;
  }

  /**
   * Permet de mettre les informations d'un objet à jour.
   *
   * @param idObject     l'id de l'objet
   * @param titre        le titre l'objet
   * @param description  la description
   * @param plageHoraire la plage horaire
   * @param photo        la photo
   * @return l'objet modifié
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si un des attributs est vide ou null
   */
  @Override
  public ObjectDto updateObjectWithPhoto(int idObject, String titre, String description,
      String plageHoraire, String photo) throws SQLException {

    if (titre == null || description == null || plageHoraire == null || photo == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (idObject <= 0 || titre.isBlank() || description.isBlank() || plageHoraire.isBlank() || photo
        .isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    ObjectDto myObject;

    try {
      myObject = objectDao.updateObjectWithPhoto(idObject, titre, description, plageHoraire,
          photo);

      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode updateObjectWithPhoto");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "annulé".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si idObject est nul ou si l'objet ne peut pas être annulé
   * @throws BizNotFoundException si idObject ne correspond a aucun objet
   */
  @Override
  public ObjectDto cancelOffer(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Informations incorrects");
    }

    dal.startTransaction();
    ObjectDto myObject;

    try {
      ObjectDto objectDto = objectDao.getObject(idObject);
      if (objectDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Cet objet n'existe pas");
      }

      if (!objectDto.getEtat().equals("Offert") && !objectDto.getEtat().equals("Interet marqué")) {
        dal.commitTransaction();
        throw new BizException(
            "L'objet doit obligatoirement etre offert pour etre annulé");
      }

      myObject = objectDao.annulerOffre(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode cancelOffer");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "interet marqué".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si idObject est nul
   * @throws BizNotFoundException si idObject ne correspond à aucun objet
   */
  @Override
  public ObjectDto reposterObjetInteretMarquer(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    ObjectDto myObjectDto;

    try {
      ObjectDto objectDto = objectDao.getObject(idObject);
      if (objectDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Cet objet n'existe pas");
      }

      myObjectDto = objectDao.setEtatInteretMarquer(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode reposterObjetInteretMarquer (Reposter)");
    }
    return myObjectDto;
  }

  /**
   * Permet de changer l'etat de l'objet en "offert".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si idObject est nul
   * @throws BizNotFoundException si idObject ne correspond à aucun objet
   */
  @Override
  public ObjectDto reposterObjet(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    ObjectDto myObjectDto;

    try {
      ObjectDto objectDto = objectDao.getObject(idObject);
      if (objectDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Cet objet n'existe pas");
      }

      myObjectDto = objectDao.reposterObjet(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode reposterObjet (Reposter)");
    }
    return myObjectDto;
  }

  /**
   * Permet d'incrementer le nombre de like d'un objet.
   *
   * @param idObject l'id de l'objet
   * @return l'objet modifié
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idObject est nul
   */
  @Override
  public ObjectDto updateNombreInteresses(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Informations incorrects");
    }

    dal.startTransaction();
    ObjectDto object;

    try {
      object = objectDao.updateNombreInteresses(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode updateNombreInteresses");
    }

    return object;
  }

  /**
   * Permet de recuperer les données d'un objet.
   *
   * @param idObject l'id de l'objet
   * @return l'objet modifié
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idObject est nul
   */
  @Override
  public ObjectDto getObject(int idObject) throws SQLException {

    if (idObject <= 0) {
      throw new BizException("Informations incorrects");
    }

    dal.startTransaction();
    ObjectDto objectDto;

    try {
      objectDto = objectDao.getObject(idObject);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getObject");
    }

    return objectDto;
  }

  /**
   * Permet de recuperer la liste des derniers objets offerts.
   *
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<ObjectDto> getLastObjects() throws SQLException {
    dal.startTransaction();
    List<ObjectDto> list;

    try {
      list = objectDao.getLastObjects();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getLastObjects");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste de tous les objets.
   *
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<ObjectDto> getAllObjects() throws SQLException {
    dal.startTransaction();
    List<ObjectDto> list;

    try {
      list = objectDao.getAllObjects();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllObjects");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste de tous les objets offerts.
   *
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<ObjectDto> getAllObjectsOffered() throws SQLException {
    dal.startTransaction();
    List<ObjectDto> list;

    try {
      list = objectDao.getAllObjectsOffered();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllObjectsOffered");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste de tous les objets d'un membre.
   *
   * @param idUser l'id du membre
   * @return la liste de tous ses objets
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idUser est nul
   */
  @Override
  public List<ObjectDto> getAllObjectsFromUsers(int idUser) throws SQLException {

    if (idUser <= 0) {
      throw new BizException("Informations incorrects");
    }

    dal.startTransaction();
    List<ObjectDto> list;

    try {
      list = objectDao.getAllObjectsFromUsers(idUser);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllObjectsFromUsers");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste de tous les objets offerts d'un membre.
   *
   * @param idUser l'id du membre
   * @return la liste de tous ses objets
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idUser est nul
   */
  @Override
  public List<ObjectDto> getAllConfirmedObjectsFromUsers(int idUser) throws SQLException {

    if (idUser <= 0) {
      throw new BizException("Informations incorrects");
    }

    dal.startTransaction();
    List<ObjectDto> list;

    try {
      list = objectDao.getAllConfirmedObjectsFromUsers(idUser);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllConfirmedObjectsFromUsers");
    }

    return list;
  }


}
