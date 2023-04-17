package be.vinci.pae.business.ucc;

import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.AddressDao;
import be.vinci.pae.donnees.dao.UserDao;
import be.vinci.pae.exceptions.BizConflictException;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.BizForbiddenException;
import be.vinci.pae.exceptions.BizNotFoundException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class UserUccImpl implements UserUcc {

  @Inject
  private AddressDao adressDao;

  @Inject
  private UserDao userDao;

  @Inject
  private DalServices dal;


  /**
   * Permet de se connecter à la DB.
   *
   * @param pseudo     le pseudo
   * @param motDePasse le mot de passe
   * @return newUserDto le user qui se connecte
   * @throws SQLException          si qqchose se passe mal
   * @throws BizException          si un des parametres est vide ou null
   * @throws BizNotFoundException  si les identifiants sont incorrectes
   * @throws BizForbiddenException si le membre n'est pas autorisé à se connecter
   */
  @Override
  public UserDto seConnecter(String pseudo, String motDePasse) throws SQLException {

    if (pseudo == null || motDePasse == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (pseudo.isBlank() || motDePasse.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    UserDto newUserDto = null;

    try {
      newUserDto = userDao.getOne(pseudo);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la connexion d'un membre");
    }

    if (newUserDto == null) {
      throw new BizNotFoundException("Pseudo incorrect");
    }

    User user = (User) newUserDto;

    if (!user.checkMotDePasse(motDePasse)) {
      throw new BizNotFoundException("Mot de passe incorrect");
    } else if (newUserDto.getEtatInscription().equals("Inscrit")
        || newUserDto.getEtatInscription().equals("Refusé")) {
      throw new BizForbiddenException("Votre inscription est toujours en attente/est refusé");
    }

    return newUserDto;
  }

  /**
   * Permet d'inscrire un user.
   *
   * @param pseudo     le pseudo
   * @param prenom     le prenom
   * @param nom        le nom
   * @param motDePasse le mot de passe
   * @param rue        la rue
   * @param numero     le numero
   * @param boite      la boite
   * @param codePostal le code postal
   * @param ville      la ville
   * @return le user inscrit
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si un des parametres est vide ou null
   * @throws BizConflictException si il existe déjà un membre avec se pseudo
   */
  @Override
  public UserDto inscription(String pseudo, String prenom, String nom, String motDePasse,
      String rue,
      int numero, String boite, int codePostal, String ville) throws SQLException {

    if (pseudo == null || prenom == null || nom == null || motDePasse == null || rue == null
        || ville == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (numero <= 0 || codePostal <= 0 || pseudo.isBlank() || prenom.isBlank() || nom.isBlank()
        || motDePasse.isBlank() || rue.isBlank() || ville.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    UserDto newUserDto;

    try {
      if (userDao.pseudoExiste(pseudo)) {
        dal.commitTransaction();
        throw new BizConflictException(
            "Veuillez choisir un autre pseudo, celui-ci est déjà utilisé");
      }

      int idAdresse = adressDao.findOne(rue, numero, boite, codePostal, ville);

      if (idAdresse == -1) {
        adressDao.insert(rue, numero, boite, codePostal, ville);
        idAdresse = adressDao.findOne(rue, numero, boite, codePostal, ville);
      }

      newUserDto = userDao.inscription(pseudo, prenom, nom, motDePasse, idAdresse);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de l'ajout d'un membre");
    }

    return newUserDto;
  }

  /**
   * Récuperer un user selon son pseudo.
   *
   * @param pseudo le pseudo
   * @return le user en param
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si un des parametres est vide ou null
   */
  @Override
  public UserDto getOne(String pseudo) throws SQLException {

    if (pseudo == null || pseudo.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    UserDto user = null;

    try {
      user = userDao.getOne(pseudo);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode getOne");
    }
    return user;
  }

  /**
   * Récuperer un user selon son id.
   *
   * @param id l'id du membre
   * @return le user
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si un des parametres est vide ou null
   */
  @Override
  public UserDto getOne(int id) throws SQLException {

    if (id <= 0) {
      throw new BizException("Certaines informations sont fausse");
    }

    dal.startTransaction();
    UserDto user = null;

    try {
      user = userDao.getOne(id);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode getOne");
    }
    return user;
  }


  /**
   * Permet de recuperer la liste de tout les users dont l'inscription a été refusé.
   *
   * @return la liste de UserDto.
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<UserDto> getAllUserDeclined() throws SQLException {
    dal.startTransaction();
    List<UserDto> list = null;

    try {
      list = userDao.findAllUserDeclined();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode getAllUserDeclined");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste de tout les users dont l'inscription est en attente.
   *
   * @return la liste de UserDto.
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<UserDto> getAllUserWaiting() throws SQLException {
    dal.startTransaction();
    List<UserDto> list = null;

    try {
      list = userDao.findAllUserWaiting();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode getAllUserWaiting");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste de tout les users dont l'inscription est confirée.
   *
   * @return la liste de UserDto.
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<UserDto> getAllUserConfirmed() throws SQLException {
    dal.startTransaction();
    List<UserDto> list = null;

    try {
      list = userDao.findAllUserConfirmed();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode getAllUserConfirmed");
    }

    return list;
  }

  /**
   * Permet de changer l'etat d'inscription d'un membre en "refusé".
   *
   * @param raisonRefus la raison du refus
   * @param pseudo      le pseudo du membre
   * @return le membre modifié
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si un des parametres est vide ou null
   * @throws BizException         si l'etat ne peut pas etre changé
   * @throws BizNotFoundException si le pseudo ne correspond a aucun membre
   */
  @Override
  public UserDto setEtatInscriptionRefusee(String raisonRefus, String pseudo) throws SQLException {

    if (raisonRefus == null || pseudo == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (raisonRefus.isBlank() || pseudo.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();

    try {
      UserDto newUserDto = userDao.getOne(pseudo);
      if (newUserDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Pseudo incorrect");
      }

      if (!newUserDto.getEtatInscription().equals("Inscrit")) {
        dal.commitTransaction();
        throw new BizException("L'état d'inscription doit uniquement etre inscrit");
      }

      userDao.setEtatInscriptionRefusee(raisonRefus, pseudo);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode setEtatInscriptionRefusee");
    }

    return getOne(pseudo);
  }

  /**
   * Permet de changer l'etat d'inscription d'un membre en "confirmé".
   *
   * @param pseudo le pseudo du membre
   * @return le membre modifié
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si un des parametres est vide ou null
   * @throws BizException         si l'etat ne peut pas etre changé
   * @throws BizNotFoundException si le pseudo ne correspond a aucun membre
   */
  @Override
  public UserDto setEtatInscriptionConfirmee(String pseudo) throws SQLException {

    if (pseudo == null || pseudo.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();

    try {
      UserDto newUserDto = userDao.getOne(pseudo);
      if (newUserDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Pseudo incorrect");
      }

      if (newUserDto.getEtatInscription().equals("Confirmé")) {
        dal.commitTransaction();
        throw new BizException("L'état d'inscription ne peut pas déjà etre confirmé");
      }

      userDao.setEtatInscriptionConfirmee(pseudo);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode setEtatInscriptionConfirmee");
    }

    return getOne(pseudo);
  }

  /**
   * Permet de changer donner le droit d'admin à un membre.
   *
   * @param pseudo le pseudo du membre
   * @return le membre modifié
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si un des parametres est vide ou null
   * @throws BizException         si le membre est déjà admin
   * @throws BizNotFoundException si le pseudo ne correspond a aucun membre
   */
  @Override
  public UserDto setAdmin(String pseudo) throws SQLException {

    if (pseudo == null || pseudo.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();

    try {
      UserDto newUserDto = userDao.getOne(pseudo);
      if (newUserDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Pseudo incorrect");
      }

      if (newUserDto.isAdmin()) {
        dal.commitTransaction();
        throw new BizException("Le membre ne peut pas déjà être admin");
      }

      userDao.setAdmin(pseudo);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode setAdmin");
    }

    return getOne(pseudo);
  }

  /**
   * Permet de changer l'etat d'inscription d'un membre en "empeché".
   *
   * @param id l'id du membre
   * @return le membre modifié
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si id est nul
   * @throws BizException         si l'etat ne peut pas etre changé
   * @throws BizNotFoundException si le pseudo ne correspond a aucun membre
   */
  @Override
  public UserDto annulerParticipation(int id) throws SQLException {

    if (id <= 0) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();

    try {
      UserDto newUserDto = userDao.getOne(id);
      if (newUserDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Pseudo incorrect");
      }

      if (!newUserDto.getEtatInscription().equals("Confirmé")) {
        dal.commitTransaction();
        throw new BizException(
            "Le membre doit obligatoirement etre confirmé pour effectuer ce changement d'etat");
      }

      userDao.annulerParticipation(id);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode annuler participation");
    }

    return getOne(id);
  }

  /**
   * Permet de changer le numero de télephone d'un membre.
   *
   * @param id     l'id' du membre
   * @param numero le numero de téléphone
   * @return le membre modifié
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si un des parametres est vide ou null
   * @throws BizNotFoundException si le pseudo ne correspond a aucun membre
   */
  @Override
  public UserDto setNumeroTelephone(int id, String numero) throws SQLException {

    if (id <= 0 || numero == null || numero.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (numero.length() != 10) {
      throw new BizException("Mauvais format de numéro de téléphone");
    }

    dal.startTransaction();

    try {
      UserDto newUserDto = userDao.getOne(id);
      if (newUserDto == null) {
        dal.commitTransaction();
        throw new BizNotFoundException("Pseudo incorrect");
      }

      userDao.setNumeroTelephone(id, numero);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode setNumeroTelephone");
    }

    return getOne(id);
  }

  /**
   * Permet de recuperer la liste de tout les users.
   *
   * @return la liste de UserDto.
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<UserDto> getAllUsers() throws SQLException {
    dal.startTransaction();
    List<UserDto> list = null;

    try {
      list = userDao.findAllUsers();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Problème avec la méthode getAllUsers");
    }

    return list;
  }

}
