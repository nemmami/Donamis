package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.donnees.dao.UserDao;
import be.vinci.pae.exceptions.BizException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class MockUserDao implements UserDao {

  @Inject
  private BizFactory myBiz;

  /**
   * Recupere un user.
   *
   * @param pseudo un pseudo
   * @return le user
   */
  public UserDto getOne(String pseudo) {
    if (pseudo == null || pseudo.equals("")) {
      throw new BizException("Le pseudo est vide !");
    } else if (pseudo.equals("test1")) {
      UserDto userDto = myBiz.getUserDTO();
      userDto.setIdUser(0);
      userDto.setPseudo("test1");
      userDto.setNom("dupond");
      userDto.setPrenom("julien");
      userDto.setEtatInscription("Confirmé");
      userDto.setMotDePasse(BCrypt.hashpw("1234", BCrypt.gensalt()));
      return userDto;
    } else if (pseudo.equals("test2")) {
      UserDto userDto = myBiz.getUserDTO();
      userDto.setIdUser(1);
      userDto.setPseudo("test2");
      userDto.setNom("dupond");
      userDto.setPrenom("julien");
      userDto.setEtatInscription("Inscrit");
      userDto.setMotDePasse(BCrypt.hashpw("1234", BCrypt.gensalt()));
      userDto.setAdmin(false);
      return userDto;
    } else if (pseudo.equals("test3")) {
      UserDto userDto = myBiz.getUserDTO();
      userDto.setIdUser(2);
      userDto.setPseudo("test3");
      userDto.setNom("dupond");
      userDto.setPrenom("julien");
      userDto.setEtatInscription("Refusé");
      userDto.setMotDePasse(BCrypt.hashpw("1234", BCrypt.gensalt()));
      return userDto;
    } else if (pseudo.equals("test4")) {
      UserDto userDto = myBiz.getUserDTO();
      userDto.setIdUser(3);
      userDto.setAdmin(true);
      return userDto;
    }
    return null;
  }

  /**
   * getOne user.
   *
   * @param id l'id du membre
   * @return le user
   */
  public UserDto getOne(int id) {
    if (id <= 0) {
      throw new BizException("Certaines informations sont fausse");
    }

    if (id == 1) {
      UserDto userDto = myBiz.getUserDTO();
      userDto.setIdUser(1);
      userDto.setPseudo("test1");
      userDto.setNom("dupond");
      userDto.setPrenom("julien");
      userDto.setEtatInscription("Confirmé");
      userDto.setMotDePasse(BCrypt.hashpw("1234", BCrypt.gensalt()));
      return userDto;
    }
    return null;
  }

  public boolean pseudoExiste(String pseudo) {
    return pseudo.equals("test2");
  }

  @Override
  public List<UserDto> findAllUsers() {
    List<UserDto> listUser = new ArrayList<UserDto>();

    for (int i = 0; i < 5; i++) {
      UserDto user = myBiz.getUserDTO();
      listUser.add(user);
    }
    return listUser;

  }

  /**
   * Inscrit un user.
   *
   * @param pseudo     le pseudo
   * @param prenom     le prenom
   * @param nom        le nom
   * @param motDePasse le mot de passe
   * @param idAdresse  l'id de l'adresse
   * @return le user
   */
  public UserDto inscription(String pseudo, String prenom, String nom, String motDePasse,
      int idAdresse) {

    if (pseudo == null || pseudo.equals("")) {
      throw new BizException("Le pseudo est vide !");
    } else if (prenom == null || prenom.equals("")) {
      throw new BizException("Le prenom est vide!");
    } else if (nom == null || nom.equals("")) {
      throw new BizException("Le nom est vide");
    } else if (motDePasse == null || motDePasse.equals("")) {
      throw new BizException("Le mot de passe est vide");
    } else if (pseudo.equals("test1")) {
      UserDto userDto = myBiz.getUserDTO();
      userDto.setIdUser(1);
      userDto.setPseudo("test1");
      userDto.setNom("test1");
      userDto.setPrenom("test1");
      userDto.setMotDePasse(BCrypt.hashpw("1234", BCrypt.gensalt()));
      userDto.setIdAdresse(2);
      return userDto;
    }
    return null;
  }


  public List<UserDto> createListUser(PreparedStatement pr) {
    return null;
  }

  @Override
  public List<UserDto> findAllUserWaiting() {

    List<UserDto> listUser = new ArrayList<UserDto>();

    for (int i = 0; i < 5; i++) {
      UserDto user = myBiz.getUserDTO();
      user.setEtatInscription("En attente");
      listUser.add(user);
    }
    return listUser;
  }

  @Override
  public List<UserDto> findAllUserDeclined() {

    List<UserDto> listUser = new ArrayList<UserDto>();

    for (int i = 0; i < 5; i++) {
      UserDto user = myBiz.getUserDTO();
      user.setEtatInscription("Refusé");
      listUser.add(user);
    }
    return listUser;
  }

  @Override
  public void setEtatInscriptionRefusee(String raisonRefus, String pseudo) {
    if (pseudo == null || pseudo.isBlank() || raisonRefus == null || raisonRefus.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }
    return;
  }

  @Override
  public void setEtatInscriptionConfirmee(String pseudo) {
    if (pseudo == null || pseudo.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    return;
  }

  @Override
  public void setAdmin(String pseudo) {
    if (pseudo == null || pseudo.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    return;
  }

  @Override
  public void annulerParticipation(int id) {
    if (id <= 0) {
      throw new BizException("Il manque une ou plusieurs informations");
    } else if (id == 1) {
      UserDto user = myBiz.getUserDTO();
      user.setIdUser(1);
      user.setEtatInscription("Confirmé");
    } else if (id == 2) {
      UserDto user2 = myBiz.getUserDTO();
      user2.setIdUser(1);
      user2.setEtatInscription("Annulé");
      if (!user2.getEtatInscription().equals("Confirmé")) {
        throw new BizException(
            "Le membre doit obligatoirement etre confirmé pour effectuer ce changement d'etat");
      }
    }
  }

  @Override
  public void setNumeroTelephone(int id, String numero) {
    if (id <= 0 || numero == null || numero.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    } else if (numero.length() != 10) {
      throw new BizException("Mauvais format de numéro de téléphone");
    }

    return;
  }

  @Override
  public List<UserDto> findAllUserConfirmed() {
    List<UserDto> listUser = new ArrayList<UserDto>();

    for (int i = 0; i < 5; i++) {
      UserDto user = myBiz.getUserDTO();
      user.setEtatInscription("Confirmé");
      listUser.add(user);
    }
    return listUser;


  }
}
