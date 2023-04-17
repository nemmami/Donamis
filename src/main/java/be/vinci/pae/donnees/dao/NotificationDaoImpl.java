package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.NotificationDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDaoImpl implements NotificationDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;

  /**
   * Permet d'inserer une nouvelle notification en db.
   *
   * @param libelle  le texte de la notification
   * @param idMembre l'id du membre concerné
   * @param type     le type de la notification
   * @return la notification inserer en db
   */
  @Override
  public NotificationDto insertNotification(String libelle, int idMembre, String type) {
    String query =
        "INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, "
            + "date_notif, id_membre, type) "
            + "VALUES(DEFAULT, ?, FALSE, NOW(), ?, ?)";
    NotificationDto myNotification = myBizFactory.getNotificationDto();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, libelle);
      pr.setInt(2, idMembre);
      pr.setString(3, type);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        myNotification.setIdNotif(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insert Notification");
    }

    return myNotification;
  }

  /**
   * Permet de changer l'etat de l'attribut "notif vue" à true.
   *
   * @param idNotif l'id de la notification
   */
  @Override
  public void setNotifVue(int idNotif) {
    String query = "UPDATE projetPAE.notifications SET notif_vue = TRUE WHERE id_notif = ?";
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idNotif);
      pr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("probleme avec la methode setNotifVue");
    }
  }

  /**
   * Permet de savoir si il y a de nouvelles notifications.
   *
   * @param idMembre l'id du membre
   * @return true si oui, false sinon
   */
  @Override
  public boolean isNewNotification(int idMembre) {
    String query = "SELECT * FROM projetPAE.notifications n"
        + " WHERE n.id_membre = ? AND n.notif_vue = FALSE";
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idMembre);
      ResultSet rs = pr.executeQuery();

      if (rs.next()) {
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("probleme avec la methode isNewNotification");
    }
    return false;
  }

  /**
   * Permet de recuperer la liste de toutes les notifications d'un membre sur base de son id.
   *
   * @param idMembre l'id du membre
   * @return la liste de notifications
   */
  @Override
  public List<NotificationDto> getAllNotificationFromUser(int idMembre) {
    String query =
        "SELECT * FROM projetPAE.notifications n WHERE n.id_membre = ? ORDER BY n.date_notif DESC";

    List<NotificationDto> listNotification = new ArrayList<>();
    ResultSet rs;

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idMembre);
      rs = pr.executeQuery();

      while (rs.next()) {
        NotificationDto myNotification = myBizFactory.getNotificationDto();
        fillNotification(rs, myNotification);
        listNotification.add(myNotification);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Probleme avec la methode getAllNotificationFromUser");
    }

    return listNotification;
  }

  /**
   * Permet de creer une liste de notifications.
   *
   * @param pr le preparestatment
   * @return la liste de notifications
   */
  @Override
  public List<NotificationDto> createNotificationList(PreparedStatement pr) {
    List<NotificationDto> notifications = new ArrayList<>();

    try {
      ResultSet rs = pr.executeQuery();
      while (rs.next()) {
        NotificationDto myNotification = myBizFactory.getNotificationDto();
        fillNotification(rs, myNotification);
        notifications.add(myNotification);
      }

      rs.close();
      return notifications;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode createInterestList");
    }
  }

  /**
   * Permet de remplir la notificationDTO de tous ses attributs.
   *
   * @param rs              le resultset
   * @param notificationDto la notification à remplir
   * @throws SQLException si qqchose se passe mal
   */
  private void fillNotification(ResultSet rs, NotificationDto notificationDto) throws SQLException {
    notificationDto.setIdNotif(rs.getInt(1));
    notificationDto.setLibelle(rs.getString(2));
    notificationDto.setNotifVue(rs.getBoolean(3));
    notificationDto.setDateNotif(rs.getDate(4));
    notificationDto.setIdMembre(rs.getInt(5));
    notificationDto.setType(rs.getString(6));
  }

}
