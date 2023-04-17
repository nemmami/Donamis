package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.ChosenInterestDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChosenInterestDaoImpl implements ChosenInterestDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;

  /**
   * Permet d'inserer un interet choisi en DB.
   *
   * @param idMembre l'id du membre
   * @param idObject l'id de l'objet
   * @return l'interet choisi
   */
  @Override
  public ChosenInterestDto insertChosenInterest(int idMembre, int idObject) {
    String query =
        "INSERT INTO projetPAE.interets_choisis(id_interet_choisi, membre,"
            + " objet, etat_transaction)"
            + "VALUES (DEFAULT, ?, ?, 'En attente')";

    ChosenInterestDto chosenInterestDto = myBizFactory.getChosenInterestDto();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idMembre);
      pr.setInt(2, idObject);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        chosenInterestDto.setIdChosenInterest(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insertChosenInterest");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer un interet choisi sur base de l'id de son objet.
   *
   * @param idObject l'id de l'objet
   * @return l'interet choisi
   */
  @Override
  public ChosenInterestDto getChosenInterestByObject(int idObject) {
    String query =
        "SELECT ci.* FROM projetPAE.interets_choisis ci "
            + "WHERE ci.objet = ? AND ci.etat_transaction = 'En attente' "
            + "ORDER BY ci.etat_transaction";

    ChosenInterestDto chosenInterestDto = myBizFactory.getChosenInterestDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      ResultSet rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      chosenInterestDto.setIdChosenInterest(rs.getInt(1));
      chosenInterestDto.setIdMember(rs.getInt(2));
      chosenInterestDto.setIdObject(rs.getInt(3));
      chosenInterestDto.setEtatTransaction(rs.getString(4));

    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode getChosenInterestByObject");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de recuperer une liste de tous les interets choisis sur base de l'id de son objet.
   *
   * @param idObject l'id de l'objet
   * @return l'interet choisi
   */
  @Override
  public List<ChosenInterestDto> getChosenInterest(int idObject) {
    String query =
        "SELECT ci.* FROM projetPAE.interets_choisis ci "
            + "WHERE ci.objet = ? ORDER BY ci.etat_transaction";

    List<ChosenInterestDto> listChosenInterest;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      listChosenInterest = createChosenInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode getChosenInterest");
    }

    return listChosenInterest;
  }

  /**
   * Permet de recuperer une liste de tous les interets choisis sur base de l'id d'un membre.
   *
   * @param idMember l'id de l'objet
   * @return l'interet choisi
   */
  @Override
  public List<ChosenInterestDto> getAllChosenInterestMember(int idMember) {
    String query =
        "SELECT ci.* FROM projetPAE.interets_choisis ci "
            + "WHERE ci.membre = ?";

    List<ChosenInterestDto> listChosenInterest;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idMember);
      listChosenInterest = createChosenInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode getAllChosenInterestMember");
    }

    return listChosenInterest;
  }

  /**
   * Permet de recuperer une liste de tous les interets choisis sur base de l'id d'un membre.
   *
   * @param idUser l'id de l'objet
   * @return l'interet choisi
   */
  @Override
  public List<ChosenInterestDto> getChosenInterestByMember(int idUser) {
    String query =
        "SELECT ci.* FROM projetPAE.interets_choisis ci "
            + "WHERE ci.membre = ? ";

    List<ChosenInterestDto> listChosenInterest;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      //pr.setBoolean(2, notifVue);
      listChosenInterest = createChosenInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode getChosenInterestByMember");
    }

    return listChosenInterest;
  }

  /**
   * Permet de recuperer la liste de tout les objets qu'un membre a donné sur base de son id.
   *
   * @param idMember l'id du membre
   * @return une liste d'interets choisis
   */
  @Override
  public List<ChosenInterestDto> getGivenObjects(int idMember) {
    String query = "SELECT ci.* FROM projetPAE.interets_choisis ci, projetPAE.objets o"
        + " WHERE ci.objet= o.id_objet AND o.membre_offreur = ? "
        + "AND ci.etat_transaction = 'Venu'";

    List<ChosenInterestDto> listChosenInterest;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idMember);
      listChosenInterest = createChosenInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode getGivenObjects");
    }

    return listChosenInterest;
  }

  /**
   * Permet de recuperer la liste de tout les objets qu'un membre a reçu sur base de son id.
   *
   * @param idMember l'id du membre
   * @return une liste d'interets choisis
   */
  @Override
  public List<ChosenInterestDto> getReceivedObjects(int idMember) {
    String query = "SELECT ci.* FROM projetPAE.interets_choisis ci WHERE ci.membre = ?"
        + "AND ci.etat_transaction = 'Venu'";

    List<ChosenInterestDto> listChosenInterest;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idMember);
      listChosenInterest = createChosenInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode getReceivedObjects");
    }

    return listChosenInterest;
  }

  /**
   * Permet de recuperer la liste de tout les objets qu'un membre n'est pas venu chercher sur base
   * de son id.
   *
   * @param idMember l'id du membre
   * @return une liste d'interets choisis
   */
  @Override
  public List<ChosenInterestDto> getNeverCameObjects(int idMember) {
    String query = "SELECT ci.* FROM projetPAE.interets_choisis ci WHERE ci.membre = ?"
        + "AND ci.etat_transaction = 'Pas venu'";

    List<ChosenInterestDto> listChosenInterest;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idMember);
      listChosenInterest = createChosenInterestList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode getNeverCameObjects");
    }

    return listChosenInterest;
  }


  /**
   * Permet de changer l'etat d'un interet choisi en "venu" sur base l'id d'un objet.
   *
   * @param idObject l'id de l'objet
   * @return l'interet choisi changé
   */
  @Override
  public ChosenInterestDto setEtatTransactionVenu(int idObject) {
    String query = "UPDATE projetPAE.interets_choisis SET etat_transaction ="
        + " 'Venu' WHERE objet = ? AND etat_transaction = 'En attente'";
    ChosenInterestDto chosenInterestDto = myBizFactory.getChosenInterestDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode setEtatTransactionVenu");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de changer l'etat d'un interet choisi en "pas venu" sur base l'id d'un objet.
   *
   * @param idObject l'id de l'objet
   * @return l'interet choisi changé
   */
  @Override
  public ChosenInterestDto setEtatTransactionPasVenu(int idObject) {
    String query = "UPDATE projetPAE.interets_choisis SET etat_transaction ="
        + " 'Pas venu' WHERE objet = ? AND etat_transaction = 'En attente'";
    ChosenInterestDto chosenInterestDto = myBizFactory.getChosenInterestDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode setEtatTransactionPasVenu");
    }

    return chosenInterestDto;
  }

  /**
   * Permet de créer une liste d'interets choisis.
   *
   * @param pr le prepareStatement
   * @return la liste d'interets choisis
   */
  private List<ChosenInterestDto> createChosenInterestList(PreparedStatement pr) {
    List<ChosenInterestDto> chosenInterests = new ArrayList<>();

    try {
      ResultSet rs = pr.executeQuery();
      while (rs.next()) {
        ChosenInterestDto chosenInterest = myBizFactory.getChosenInterestDto();
        chosenInterest.setIdChosenInterest(rs.getInt(1));
        chosenInterest.setIdMember(rs.getInt(2));
        chosenInterest.setIdObject(rs.getInt(3));
        chosenInterest.setEtatTransaction(rs.getString(4));

        chosenInterests.add(chosenInterest);
      }

      rs.close();
      return chosenInterests;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode createInterestList");
    }
  }

}