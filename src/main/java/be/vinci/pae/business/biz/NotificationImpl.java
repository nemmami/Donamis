package be.vinci.pae.business.biz;

import java.util.Date;

public class NotificationImpl implements Notification {

  private int idNotif;
  private String libelle;
  private boolean notifVue;
  private Date dateNotif;
  private int idMembre;

  private String type;

  /**
   * Constructeur d'une notification.
   */
  public NotificationImpl(int idNotif, String libelle, boolean notifVue, Date dateNotif,
      int idMembre, String type) {
    this.idNotif = idNotif;
    this.libelle = libelle;
    this.notifVue = notifVue;
    this.dateNotif = dateNotif;
    this.idMembre = idMembre;
    this.type = type;
  }

  public NotificationImpl() {

  }

  public int getIdNotif() {
    return idNotif;
  }

  public void setIdNotif(int idNotif) {
    this.idNotif = idNotif;
  }

  public String getLibelle() {
    return libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public boolean isNotifVue() {
    return notifVue;
  }

  public void setNotifVue(boolean notfiVue) {
    this.notifVue = notfiVue;
  }

  public Date getDateNotif() {
    return dateNotif;
  }

  public void setDateNotif(Date dateNotif) {
    this.dateNotif = dateNotif;
  }

  public int getIdMembre() {
    return idMembre;
  }

  public void setIdMembre(int idMembre) {
    this.idMembre = idMembre;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
