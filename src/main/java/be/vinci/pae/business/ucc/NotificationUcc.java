package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDto;
import java.sql.SQLException;
import java.util.List;

public interface NotificationUcc {

  NotificationDto insertNotification(String libelle, int idMembre, String type) throws SQLException;

  List<NotificationDto> getAllNotificationFromUser(int idUser) throws SQLException;

  boolean isNewNotification(int idUser) throws SQLException;

  void setNotifVue(int idNotif) throws SQLException;

}
