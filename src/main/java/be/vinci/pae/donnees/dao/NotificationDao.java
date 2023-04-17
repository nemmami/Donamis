package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.NotificationDto;
import java.sql.PreparedStatement;
import java.util.List;

public interface NotificationDao {

  NotificationDto insertNotification(String libelle, int idMembre, String type);

  List<NotificationDto> createNotificationList(PreparedStatement pr);

  List<NotificationDto> getAllNotificationFromUser(int idMembre);

  void setNotifVue(int idNotif);

  boolean isNewNotification(int idMembre);

}
