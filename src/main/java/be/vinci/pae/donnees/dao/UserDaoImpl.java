package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.biz.User;
import be.vinci.pae.business.biz.UserImpl;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;


  /**
   * Permet de recuperer le user apd de son pseudo.
   *
   * @param pseudo le pseudo
   * @return le user recherche
   */
  @Override
  public UserDto getOne(String pseudo) {
    String query = "SELECT m.* FROM projetPAE.membres m WHERE m.pseudo = ?";

    UserDto user = myBizFactory.getUserDTO();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, pseudo);
      rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      fillUser(user, rs);
    } catch (SQLException e) {
      throw new FatalException("Problème dans la méthode getOne");
    }

    return user;
  }

  /**
   * Permet de recuperer le user apd de son id.
   *
   * @param id le pseudo
   * @return le user recherche
   */
  @Override
  public UserDto getOne(int id) {
    String query = "SELECT m.* FROM projetPAE.membres m WHERE m.id_membre = ?";

    UserDto user = myBizFactory.getUserDTO();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, id);
      rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      fillUser(user, rs);
    } catch (SQLException e) {
      throw new FatalException("Problème dans la méthode getOne");
    }

    return user;
  }

  /**
   * Permet de remplir le UserDTO de tous ses attributs.
   *
   * @param user le userDto à remplir
   * @param rs   le resultset
   * @throws SQLException si qqchose se passe mal
   */
  private void fillUser(UserDto user, ResultSet rs) throws SQLException {
    user.setIdUser(rs.getInt(1));
    user.setPseudo(rs.getString(2));
    user.setNom(rs.getString(3));
    user.setPrenom(rs.getString(4));
    user.setAdmin(rs.getBoolean(5));
    user.setTelephone(rs.getString(6));
    user.setEtatInscription(rs.getString(7));
    user.setRaisonRefus(rs.getString(8));
    user.setIdAdresse(rs.getInt(9));
    user.setMotDePasse(rs.getString(10));

    rs.close();
  }

  /**
   * Permet de verifier si le pseudo a deja ete utilse par un autre user.
   *
   * @param pseudo le pseudo
   * @return true si oui, false sinon
   */
  @Override
  public boolean pseudoExiste(String pseudo) {
    String query = "SELECT * FROM projetPAE.membres WHERE pseudo = ?";

    boolean exists = false;

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, pseudo);
      rs = pr.executeQuery();

      if (rs.next()) {
        exists = true;
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode pseudoExiste");
    }
    return exists;
  }

  /**
   * Permet d'inserer un user dans la DB.
   *
   * @param pseudo     le pseudo
   * @param prenom     le prenom
   * @param nom        le nom
   * @param motDePasse le mot de passe
   * @param idAdresse  l'id de l'adresse
   * @return myUser le mebre créer
   */
  @Override
  public UserDto inscription(String pseudo, String prenom, String nom, String motDePasse,
      int idAdresse) throws SQLException {
    String query =
        "INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, "
            + "etat_inscription, raison_refus, id_adresse, mot_de_passe) "
            + "VALUES(DEFAULT, ?, ?, ?, FALSE, DEFAULT, 'Inscrit', DEFAULT, ?, ?)";

    UserDto myUser = myBizFactory.getUserDTO();
    User user = new UserImpl();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {

      pr.setString(1, pseudo);
      pr.setString(2, nom);
      pr.setString(3, prenom);
      pr.setInt(4, idAdresse);
      pr.setString(5, user.hashMotDePasse(motDePasse));
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        myUser.setIdUser(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode Inscription");
    }

    return myUser;
  }

  /**
   * Permet de recuperer la liste des User dont l'inscription est refusée.
   *
   * @return la liste de User
   */
  @Override
  public List<UserDto> findAllUserDeclined() {
    String query = "SELECT * FROM projetPAE.membres m WHERE m.etat_inscription = 'Refusé'";
    List<UserDto> list;

    try (PreparedStatement pr = dal.createStatement(query)) {
      list = createListUser(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode findAllUserDeclined");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste des User dont l'inscription est en attente.
   *
   * @return la liste de User
   */
  @Override
  public List<UserDto> findAllUserWaiting() {
    String query = "SELECT * FROM projetPAE.membres m WHERE m.etat_inscription = 'Inscrit'";
    List<UserDto> list;

    try (PreparedStatement pr = dal.createStatement(query)) {
      list = createListUser(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode findAllUserWaiting");
    }

    return list;
  }

  /**
   * Permet de recuperer la liste des User dont l'inscription est confirmée.
   *
   * @return la liste de User
   */
  @Override
  public List<UserDto> findAllUserConfirmed() {
    String query = "SELECT * FROM projetPAE.membres m WHERE m.etat_inscription = 'Confirmé'"
        + " OR m.etat_inscription = 'Empeché'";
    List<UserDto> list;

    try (PreparedStatement pr = dal.createStatement(query)) {
      list = createListUser(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode findAllUserConfirmed");
    }

    return list;
  }

  /**
   * Permet de changer l'etat d'inscription d'un membre en "refusé".
   *
   * @param pseudo      le pseudo du membre
   * @param raisonRefus la raison du refus
   */
  @Override
  public void setEtatInscriptionRefusee(String raisonRefus, String pseudo) {
    String query = "UPDATE projetPAE.membres SET etat_inscription = 'Refusé',"
        + " raison_refus = ? WHERE pseudo = ?";
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, raisonRefus);
      pr.setString(2, pseudo);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode setEtatInscriptionRefusee");
    }
  }

  /**
   * Permet de changer l'etat d'inscription d'un membre en "confirmé".
   *
   * @param pseudo le pseudo du membre
   */
  @Override
  public void setEtatInscriptionConfirmee(String pseudo) {

    String query = "UPDATE projetPAE.membres SET etat_inscription = 'Confirmé',"
        + "raison_refus = null  WHERE pseudo = ?";

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, pseudo);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode setEtatInscriptionConfirmee");
    }
  }

  /**
   * Permet de changer l'etat d'inscription d'un membre en "empeché".
   *
   * @param id l'id du membre
   */
  @Override
  public void annulerParticipation(int id) {
    String query = "UPDATE projetPAE.membres "
        + "SET etat_inscription = 'Empeché' "
        + "WHERE id_membre = ?";
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, id);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode annulerParticipation");
    }

  }

  /**
   * Permet de donner le role d'admin a un user.
   *
   * @param pseudo le pseudo du user
   */
  @Override
  public void setAdmin(String pseudo) {
    String query = "UPDATE projetPAE.membres SET admin = TRUE WHERE pseudo = ?";
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, pseudo);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode setAdmin");
    }
  }

  /**
   * Permet de changer le numero de téléphone d'un membre.
   *
   * @param id     l'id du membre
   * @param numero le numero du membre
   */
  @Override
  public void setNumeroTelephone(int id, String numero) {
    String query = "UPDATE projetPAE.membres SET telephone = ? WHERE id_membre = ?";
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, numero);
      pr.setInt(2, id);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode setNumeroTelephone");
    }
  }

  /**
   * Permet de recuperer la liste complète des User.
   *
   * @return la liste de User
   */
  @Override
  public List<UserDto> findAllUsers() {
    String query = "SELECT * FROM projetPAE.membres m";
    List<UserDto> list;

    try (PreparedStatement pr = dal.createStatement(query)) {
      list = createListUser(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode findAllUsers");
    }

    return list;
  }

  /**
   * Permet d'avoir une liste de User qui correspond à la preparedStatement passé en paramètres.
   *
   * @param pr a PrepareStatement
   * @return list la liste de User
   */
  @Override
  public List<UserDto> createListUser(PreparedStatement pr) {
    List<UserDto> list = new ArrayList<>();

    try {
      ResultSet rs = pr.executeQuery();
      while (rs.next()) {
        UserDto myUser = myBizFactory.getUserDTO();
        myUser.setIdUser(rs.getInt(1));
        myUser.setPseudo(rs.getString(2));
        myUser.setNom(rs.getString(3));
        myUser.setPrenom(rs.getString(4));
        myUser.setAdmin(rs.getBoolean(5));
        if (rs.getString(6) != null) {
          myUser.setTelephone(rs.getString(6));
        }
        myUser.setEtatInscription(rs.getString(7));
        if (rs.getString(7) != null) {
          myUser.setRaisonRefus(rs.getString(8));
        }
        myUser.setIdAdresse(rs.getInt(9));
        myUser.setMotDePasse(rs.getString(10));
        list.add(myUser);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode createListUser");
    }

    return list;
  }

}
