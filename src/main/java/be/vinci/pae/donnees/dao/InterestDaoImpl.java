package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.InterestDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InterestDaoImpl implements InterestDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;


  /**
   * Permet d'inserer un interet dans la db.
   *
   * @param idUser            l'id du user
   * @param idObject          l'id de l'objet
   * @param dateDisponibilite la date a laquelle on est disponible
   * @param appel             true si on souhaite être appellé, false sinon
   * @return l'interet inserer en db
   */
  @Override
  public InterestDto insertInterest(int idUser, int idObject, String dateDisponibilite,
      boolean appel) {
    String query =
        "INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite,"
            + " appel) "
            + "VALUES(DEFAULT, ?, ?, ?, ?)";
    InterestDto myInterest = myBizFactory.getInterestDto();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      pr.setInt(2, idObject);
      pr.setString(3, dateDisponibilite);
      pr.setBoolean(4, appel);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        myInterest.setIdInterest(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insert Interest");
    }

    return myInterest;
  }

  /**
   * Permet d'inserer un interet dans la db.
   *
   * @param idUser   l'id du user
   * @param idObject l'id de l'objet
   * @param appel    true si on souhaite être appellé, false sinon
   * @return l'interet inserer en db
   */
  @Override
  public InterestDto insertInterest(int idUser, int idObject, boolean appel) {
    String query =
        "INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel) "
            + "VALUES(DEFAULT , ?, ? ,NULL ,?)";
    InterestDto myInterest = myBizFactory.getInterestDto();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      pr.setInt(2, idObject);
      pr.setBoolean(3, appel);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        myInterest.setIdInterest(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insert Interest");
    }

    return myInterest;
  }

  /**
   * Permet de recuperer un interet sur base de l'id de l'objet et du membre.
   *
   * @param idUser   l'id du user
   * @param idObject l'id de l'objet
   * @return l'interet correspondant
   */
  @Override
  public boolean getInterest(int idUser, int idObject) {
    String query = "SELECT * FROM projetPAE.interets WHERE membre = ? AND objet = ?";

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      pr.setInt(2, idObject);

      rs = pr.executeQuery();

      if (!rs.next()) {
        return false;
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Probleme avec la méthode getInterest");
    }

    return true;
  }

  /**
   * Permet de recuperer tout les interets d'un membre sur base de son id.
   *
   * @param idUser l'id du membre
   * @return la liste de tous ses interets
   */
  @Override
  public List<InterestDto> getAllInterestForUserObject(int idUser) {
    String query =
        "SELECT i.* FROM projetPAE.interets i, projetPAE.objets o, projetPAE.membres m "
            + "WHERE o.membre_offreur = ? AND i.objet = o.id_objet"
            + " AND i.membre = m.id_membre";

    List<InterestDto> listInterest = new ArrayList<>();
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      //pr.setBoolean(2, vueNotif);
      ResultSet rs = pr.executeQuery();

      while (rs.next()) {
        InterestDto interest = myBizFactory.getInterestDto();
        fillInterest(rs, interest);
        listInterest.add(interest);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode getAllInterestForUserObject");
    }

    return listInterest;
  }

  /**
   * Peremt de recuprer tout les interets d'un objet sur base de son id.
   *
   * @param idObject l'id de l'objet
   * @return la liste de tous ses interets
   */
  @Override
  public List<InterestDto> getObjectInterest(int idObject) {
    String query = "SELECT i.* FROM projetPAE.interets i WHERE i.objet = ?";

    List<InterestDto> listInterest;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      listInterest = createInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode getObjectInterest");
    }

    return listInterest;
  }

  /**
   * Permet de recuperer tout les interets d'un membre sur base de son id.
   *
   * @param idUser l'id du membre
   * @return la liste de tous ses interets
   */
  @Override
  public List<InterestDto> getAllInterestFromUser(int idUser) {
    String query = "SELECT i.* FROM projetPAE.interets i WHERE i.membre = ?";

    List<InterestDto> listInterest;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      listInterest = createInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode getAllInterestFromUser");
    }

    return listInterest;
  }

  /**
   * Permet de creer une liste d'interets.
   *
   * @param pr le preparestatement
   * @return la liste d'interets
   */
  @Override
  public List<InterestDto> createInterestList(PreparedStatement pr) {
    List<InterestDto> interests = new ArrayList<>();

    try {
      ResultSet rs = pr.executeQuery();
      while (rs.next()) {
        InterestDto interest = myBizFactory.getInterestDto();
        fillInterest(rs, interest);
        interests.add(interest);
      }

      rs.close();
      return interests;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode createInterestList");
    }
  }

  /**
   * Permet de remplir l'intertDTO de tous ses attributs.
   *
   * @param rs          le resultset
   * @param interestDto l'interet à remplir
   * @throws SQLException si qqchose se passe mal
   */
  private void fillInterest(ResultSet rs, InterestDto interestDto) throws SQLException {
    interestDto.setIdInterest(rs.getInt(1));
    interestDto.setUser(rs.getInt(2));
    interestDto.setObject(rs.getInt(3));
    interestDto.setDateDisponibilite(rs.getString(4));
    interestDto.setAppel(rs.getBoolean(5));
  }

}
