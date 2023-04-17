package be.vinci.pae.bizz.biz;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.NotificationDto;
import be.vinci.pae.business.ucc.NotificationUcc;
import be.vinci.pae.exceptions.BizException;
import java.sql.SQLException;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class NotificationUccTest {

  private NotificationUcc ucc;
  private NotificationDto notifDto;
  private NotificationDto notifDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(NotificationUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Nested
  @DisplayName(value = "Insert notification ")
  public class InsertNotif {

    @BeforeEach
    void setUp() {
      notifDtoTest = myBiz.getNotificationDto();
      notifDtoTest.setIdNotif(1);
      notifDtoTest.setLibelle("bazz a liker votre objet");
      notifDtoTest.setIdMembre(1);
      notifDtoTest.setType("like");
    }

    @DisplayName(value = "Insert notif OK")
    @Test
    public void testInsertOK() throws SQLException {
      notifDto = ucc.insertNotification("bazz a liker votre objet", 1, "du mercredi a vendredi");
      Assertions.assertEquals(notifDto.getIdNotif(), notifDtoTest.getIdNotif());
    }

  }


  @Nested
  @DisplayName(value = "liste des notification ")
  public class ListNotif {

    @BeforeEach
    void setUp() {
      notifDtoTest = myBiz.getNotificationDto();
    }

    @DisplayName(value = "test list getAllNotificationFromUser")
    @Test
    public void testGetAllNotificationFromUser() throws SQLException {
      int idUser = 1;
      List<NotificationDto> notifs = ucc.getAllNotificationFromUser(idUser);
      assertNotNull(notifs);
      assertTrue(notifs.size() == 5);

    }

    @DisplayName(value = "information manquantes dans getAllNotificationFromUser")
    @Test
    public void testInfoManquantes_getAllNotificationFromUser() throws SQLException {
      int idUser = 0;
      Assertions.assertThrows(BizException.class, () -> {
        ucc.getAllNotificationFromUser(idUser);
      });

    }
  }

  @Nested
  @DisplayName(value = "test isNewNotif && setNotifVue ")
  public class TestNotif {

    @BeforeEach
    void setUp() {
      notifDtoTest = myBiz.getNotificationDto();
      notifDtoTest.setIdNotif(1);
    }


    @DisplayName(value = "information manquantes dans setNotifVue")
    @Test
    public void testInfoManquantes_setNotifVue() throws SQLException {
      int idNotif = 0;

      Assertions.assertThrows(BizException.class, () -> {
        ucc.setNotifVue(idNotif);
      });
    }

    @DisplayName(value = "isNewnotif OK")
    @Test
    public void testisNewNotifOK() throws SQLException {

      Assertions.assertTrue(ucc.isNewNotification(1));
    }

    @DisplayName(value = "information fausse isNewNotification")
    @Test
    public void testInfoManquantes_isNewNotification() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.isNewNotification(0);
      });
    }

  }
}
