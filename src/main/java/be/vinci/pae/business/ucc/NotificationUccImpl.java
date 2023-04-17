package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.NotificationDao;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class NotificationUccImpl implements NotificationUcc {

  @Inject
  private NotificationDao notificationDao;

  @Inject
  private DalServices dal;

  /**
   * Permet d'inserer une nouvelle notification en db.
   *
   * @param libelle  le texte de la notification
   * @param idMembre l'id du membre concerné
   * @param type     le type de la notification
   * @return la notification inserer en db
   */
  @Override
  public NotificationDto insertNotification(String libelle, int idMembre, String type)
      throws SQLException {
    dal.startTransaction();
    NotificationDto notificationDto;
    try {
      notificationDto = notificationDao.insertNotification(libelle, idMembre, type);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new BizException("Erreur lors de la methode insertNotification");
    }
    return notificationDto;
  }

  /**
   * Permet de recuperer la liste de toutes les notifications d'un membre sur base de son id.
   *
   * @param idUser l'id du membre
   * @return la liste de notifications
   */
  @Override
  public List<NotificationDto> getAllNotificationFromUser(int idUser) throws SQLException {

    if (idUser <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    List<NotificationDto> list;

    try {
      list = notificationDao.getAllNotificationFromUser(idUser);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllInterestFromUser");
    }

    return list;
  }

  /**
   * Permet de savoir si il y a de nouvelles notifications.
   *
   * @param idUser l'id du membre
   * @return true si oui, false sinon
   */
  @Override
  public boolean isNewNotification(int idUser) throws SQLException {
    if (idUser <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();

    boolean isNew = false;
    try {
      isNew = notificationDao.isNewNotification(idUser);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode isNewNotification");
    }

    return isNew;
  }

  /**
   * Permet de changer l'etat de l'attribut "notif vue" à true.
   *
   * @param idNotif l'id de la notification
   */
  @Override
  public void setNotifVue(int idNotif) throws SQLException {
    if (idNotif <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();

    try {
      notificationDao.setNotifVue(idNotif);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode setNotifVue");
    }
  }

}
