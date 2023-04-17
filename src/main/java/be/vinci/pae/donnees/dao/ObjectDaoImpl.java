package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObjectDaoImpl implements ObjectDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;


  /**
   * Permet de recuperer un objet sur base de son id.
   *
   * @param idObject l'id de l'objet
   * @return l'objet qu'on recherche
   */
  @Override
  public ObjectDto getObject(int idObject) {
    String query = "SELECT o.* FROM projetPAE.objets o WHERE o.id_objet = ?";

    ObjectDto object = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      ResultSet rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      fillObject(rs, object);

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode getObject ");
    }

    return object;
  }

  /**
   * Permet de recuperer les 6 derniers objets offert.
   *
   * @return listObject la liste d'objects
   */
  @Override
  public List<ObjectDto> getLastObjects() {
    String query =
        "SELECT o.*, max(o.date) FROM projetPAE.objets o\n WHERE "
            + " o.etat = 'Offert' OR o.etat = 'Interet marqué'\n"
            + "GROUP BY o.date,titre, id_objet, type, description, photo,"
            + " plage_horaire, membre_offreur, etat, nbr_interesse\n" + "ORDER BY o.date DESC "
            + "LIMIT 6";

    List<ObjectDto> listObject;
    try (PreparedStatement pr = dal.createStatement(query)) {
      listObject = createObjectsList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode getLastObjects");
    }

    return listObject;
  }

  /**
   * Permet de recuperer tous les objets.
   *
   * @return listObject la liste d'objects
   */
  @Override
  public List<ObjectDto> getAllObjects() {
    String query =
        "SELECT o.* FROM projetPAE.objets o ORDER BY o.etat = 'Offert'"
            + " OR o.etat = 'Interet marqué' DESC, o.date DESC";

    List<ObjectDto> listObject;
    try (PreparedStatement pr = dal.createStatement(query)) {
      listObject = createObjectsList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode getAllObjects");
    }

    return listObject;
  }

  /**
   * Permet de recuperer tous les objets offert.
   *
   * @return listObject la liste d'objects
   */
  @Override
  public List<ObjectDto> getAllObjectsOffered() {
    String query =
        "SELECT o.* FROM projetPAE.objets o WHERE o.etat = 'Offert'"
            + " OR o.etat = 'Interet marqué' ORDER BY o.date DESC";

    List<ObjectDto> listObject;
    try (PreparedStatement pr = dal.createStatement(query)) {
      listObject = createObjectsList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème avec la méthode getAllObjectsOffered");
    }

    return listObject;
  }

  /**
   * Permet de recuperer les objets d'un membre.
   *
   * @param idUser l'id du membre
   * @return listObject la liste d'objects
   */
  @Override
  public List<ObjectDto> getAllObjectsFromUsers(int idUser) {
    String query =
        "SELECT o.* FROM projetPAE.objets o WHERE o.membre_offreur = ? ORDER BY o.date DESC";

    List<ObjectDto> listObject;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      listObject = createObjectsList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode getAllObjectsFromUsers ");
    }
    return listObject;
  }

  /**
   * Permet de recuperer les objets dont le receveur a été choisi d'un membre.
   *
   * @param idUser l'id du membre
   * @return listObject la liste d'objects
   */
  @Override
  public List<ObjectDto> getAllConfirmedObjectsFromUsers(int idUser) {
    String query =
        "SELECT o.* FROM projetPAE.objets o WHERE o.membre_offreur = ?"
            + " AND o.etat = 'Receveur Choisi' ORDER BY o.date DESC";

    List<ObjectDto> listObject;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idUser);
      listObject = createObjectsList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode getAllConfirmedObjectsFromUsers ");
    }
    return listObject;
  }

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
   */
  @Override
  public ObjectDto insertObject(int idType, String titre, String description, String photo,
      String plageHoraire, int membreOffreur) {
    String query = "INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date,"
        + "plage_horaire, membre_offreur, etat, nbr_interesse) "
        + "VALUES(DEFAULT, ?, ?, ?, ?, NOW(), ?, ?, 'Offert', 0)";

    ObjectDto myObject = myBizFactory.getObjectDto();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idType);
      pr.setString(2, titre);
      pr.setString(3, description);
      pr.setString(4, photo);
      pr.setString(5, plageHoraire);
      pr.setInt(6, membreOffreur);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        myObject.setIdObjet(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insert Objet");
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
   */
  @Override
  public ObjectDto insertObject(int idType, String titre, String description, String plageHoraire,
      int membreOffreur) {
    String query = "INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date,"
        + "plage_horaire, membre_offreur, etat, nbr_interesse) "
        + "VALUES(DEFAULT, ?, ?, ?, NULL, NOW(), ?, ?, 'Offert', 0)";

    ObjectDto myObject = myBizFactory.getObjectDto();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idType);
      pr.setString(2, titre);
      pr.setString(3, description);
      pr.setString(4, plageHoraire);
      pr.setInt(5, membreOffreur);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        myObject.setIdObjet(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insert Objet");
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
   */
  @Override
  public ObjectDto updateObject(int idObject, String titre, String description,
      String plageHoraire) {

    String query = "UPDATE projetPAE.objets  SET titre =? , "
        + "description =? , plage_horaire =?  WHERE id_objet=?";

    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, titre);
      pr.setString(2, description);
      pr.setString(3, plageHoraire);
      pr.setInt(4, idObject);

      pr.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw new FatalException("Problème dans la methode updateObject");
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
   */
  @Override
  public ObjectDto updateObjectWithPhoto(int idObject, String titre, String description,
      String plageHoraire, String photo) {

    String query = "UPDATE projetPAE.objets  SET titre =? , "
        + "description =? , plage_horaire =? , photo=?  WHERE id_objet=?";

    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, titre);
      pr.setString(2, description);
      pr.setString(3, plageHoraire);
      pr.setString(4, photo);
      pr.setInt(5, idObject);

      pr.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw new FatalException("Problème dans la methode updateObjectWithPhoto");
    }

    return myObject;
  }


  /**
   * Permet de créer une liste d'objets.
   *
   * @param pr le preparedstatement
   * @return la liste de l'objet
   */
  @Override
  public List<ObjectDto> createObjectsList(PreparedStatement pr) {
    List<ObjectDto> objects = new ArrayList<>();

    try {
      ResultSet rs = pr.executeQuery();
      while (rs.next()) {
        ObjectDto object = myBizFactory.getObjectDto();
        fillObject(rs, object);
        objects.add(object);
      }

      rs.close();
      return objects;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode createObjectList");
    }
  }

  /**
   * Permet de changer l'etat de l'objet en "annulé".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   */
  @Override
  public ObjectDto annulerOffre(int idObject) {
    String query = "Update projetPAE.objets o SET etat='Annulé' WHERE o.id_objet = ?"
        + "AND (o.etat = 'Offert' OR o.etat = 'Receveur Choisi' OR o.etat = 'Interet marqué')";
    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      throw new FatalException("problème dans la methode annuler offre ");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "receveur chosi".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   */
  @Override
  public ObjectDto setEtatReceveurChoisi(int idObject) {
    String query = "Update projetPAE.objets o SET etat='Receveur Choisi' WHERE o.id_objet = ?";
    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      throw new FatalException("problème dans la methode setEtatReceveurChoisi");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "donné".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   */
  @Override
  public ObjectDto setEtatDonne(int idObject) {
    String query = "Update projetPAE.objets o SET etat='Donné' WHERE o.id_objet = ?"
        + "AND o.etat='Receveur Choisi'";
    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      throw new FatalException("problème dans la methode setEtatDonne ");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "offert" (le reposter).
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   */
  @Override
  public ObjectDto reposterObjet(int idObject) {
    String query = "Update projetPAE.objets o SET etat='Offert' WHERE o.id_objet = ?";
    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      throw new FatalException("problème dans la methode reposterObjet ");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "interet marqué" (le reposter).
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   */
  @Override
  public ObjectDto reposterObjetInteretMarquer(int idObject) {
    String query = "Update projetPAE.objets o SET etat='Interet marqué' WHERE o.id_objet = ?";
    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      throw new FatalException("problème dans la methode reposterObjetInteretMarquer ");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "offert".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   */
  @Override
  public ObjectDto setEtatOffert(int idObject) {
    String query = "Update projetPAE.objets o SET etat='Offert' WHERE o.id_objet = ?"
        + "AND o.etat='Receveur Choisi'";
    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      throw new FatalException("problème dans la methode setEtatOffert");
    }

    return myObject;
  }

  /**
   * Permet de changer l'etat de l'objet en "interet marqué".
   *
   * @param idObject l'id de l'objet
   * @return l'objet changé
   */
  @Override
  public ObjectDto setEtatInteretMarquer(int idObject) {
    String query = "Update projetPAE.objets o SET etat='Interet marqué' WHERE o.id_objet = ?";
    ObjectDto myObject = myBizFactory.getObjectDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      throw new FatalException("problème dans la methode setEtatInteretMarquer");
    }

    return myObject;
  }

  /**
   * Permet d'incrementer le nombre de like d'un objet.
   *
   * @param idObject l'id de l'objet
   * @return l'objet modifié
   */
  @Override
  public ObjectDto updateNombreInteresses(int idObject) {
    String query = "UPDATE projetPAE.objets "
        + "SET nbr_interesse = nbr_interesse+1, etat = 'Interet marqué'"
        + "WHERE id_objet = ?";

    ObjectDto myObject = myBizFactory.getObjectDto();
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idObject);
      pr.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw new FatalException("Problème dans la methode updateNombreInteresses");
    }

    return myObject;
  }

  /**
   * Permet de remplir l'objectDTO de tous ses attributs.
   *
   * @param rs     le resultset
   * @param object l'objetDto a remplir
   * @throws SQLException si qqchose se passe mal
   */
  private void fillObject(ResultSet rs, ObjectDto object) throws SQLException {
    object.setIdObjet(rs.getInt(1));
    object.setIdType(rs.getInt(2));
    object.setTitre(rs.getString(3));
    object.setDescription(rs.getString(4));
    object.setPhoto(rs.getString(5));
    object.setDate(rs.getDate(6));
    object.setPlageHoraire(rs.getString(7));
    object.setMembre_offreur(rs.getInt(8));
    object.setEtat(rs.getString(9));
    object.setNbrInteresse(rs.getInt(10));
  }


}
