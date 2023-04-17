package be.vinci.pae.business.dto;

import java.util.Date;

public interface NotificationDto {

  int getIdNotif();

  void setIdNotif(int idNotif);

  String getLibelle();

  void setLibelle(String libelle);

  boolean isNotifVue();

  void setNotifVue(boolean notifVue);

  Date getDateNotif();

  void setDateNotif(Date dateNotif);

  int getIdMembre();

  void setIdMembre(int idMembre);

  String getType();

  void setType(String type);

}
