package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.NotificationDto;
import be.vinci.pae.donnees.dao.NotificationDao;
import be.vinci.pae.exceptions.BizException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;


public class MockNotificationDao implements NotificationDao {

  @Inject
  private BizFactory myBiz;

  @Override
  public NotificationDto insertNotification(String libelle, int idMembre, String type) {

    if (idMembre == 1) {
      NotificationDto notifDto = myBiz.getNotificationDto();
      notifDto.setIdNotif(1);
      notifDto.setLibelle("bazz a liker votre objet");
      notifDto.setIdMembre(1);
      notifDto.setType("like");
      return notifDto;
    }

    return null;
  }

  @Override
  public List<NotificationDto> createNotificationList(PreparedStatement pr) {
    return null;
  }

  @Override
  public List<NotificationDto> getAllNotificationFromUser(int idMembre) {

    List<NotificationDto> listNotif = new ArrayList<NotificationDto>();
    if (idMembre <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    if (idMembre == 1) {
      for (int i = 0; i < 5; i++) {
        NotificationDto notif = myBiz.getNotificationDto();
        listNotif.add(notif);
      }
    }

    return listNotif;
  }

  @Override
  public void setNotifVue(int idNotif) {
    if (idNotif <= 0) {
      throw new BizException("Certaines informations sont fausses");
    } else {
      NotificationDto notifDto = myBiz.getNotificationDto();
      notifDto.setIdNotif(1);
    }
  }

  @Override
  public boolean isNewNotification(int idMembre) {
    if (idMembre <= 0) {
      throw new BizException("Certaines informations sont fausses");
    } else if (idMembre == 1) {
      return true;
    } else {
      return false;
    }

  }
}
