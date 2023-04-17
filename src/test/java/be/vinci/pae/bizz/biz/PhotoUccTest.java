package be.vinci.pae.bizz.biz;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.business.ucc.PhotoUcc;
import be.vinci.pae.exceptions.BizException;
import java.sql.SQLException;
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
public class PhotoUccTest {

  private PhotoUcc ucc;
  private PhotoDto photoDto;
  private PhotoDto photoDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(PhotoUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Nested
  @DisplayName(value = "test : Photo ")
  public class PhotoTest {

    @BeforeEach
    void setUp() {
      photoDtoTest = myBiz.getPhotoDto();
      photoDtoTest.setIdPhoto(1);
      //photoDtoTest.setNomPhoto("nomPhoto");
    }


    @DisplayName(value = "getPhoto test ")
    @Test
    public void testGetPhoto() throws SQLException {
      photoDto = ucc.getPhoto(1);
      Assertions.assertEquals(photoDto.getIdPhoto(), photoDtoTest.getIdPhoto());
    }

    @DisplayName(value = "info manquantes dans le getPhoto ")
    @Test
    public void infoManquantes_testGetPhoto() {
      Assertions.assertThrows(BizException.class, () -> {
        photoDto = ucc.getPhoto(0);
      });
    }


    @DisplayName(value = "getPhotoOK test ")
    @Test
    public void testGetPhotoAvecNomOK() throws SQLException {
      photoDto = ucc.getPhoto("photo1");
      Assertions.assertEquals(photoDto.getIdPhoto(), photoDtoTest.getIdPhoto());
    }

    @DisplayName(value = "info manquantes dans le getPhoto ")
    @Test
    public void infoManquantes_testGetPhotoNomNull() {
      Assertions.assertThrows(BizException.class, () -> {
        photoDto = ucc.getPhoto(null);
      });
    }

    @DisplayName(value = "info manquantes dans le getPhoto ")
    @Test
    public void infoManquantes_testGetPhotoNomVide() {
      Assertions.assertThrows(BizException.class, () -> {
        photoDto = ucc.getPhoto("");
      });
    }

    @DisplayName(value = "insertPhotoOK test ")
    @Test
    public void testInsertPhotoOK() throws SQLException {
      String nomPhoto = "photo1";
      photoDto = ucc.getPhoto(nomPhoto);
      ucc.insertPhoto(nomPhoto);
      Assertions.assertEquals(photoDto.getIdPhoto(), photoDtoTest.getIdPhoto());
    }

    @DisplayName(value = "info manquantes dans le insertPhoto photo=vide ")
    @Test
    public void testinserPhoto_infoManquantes() {
      Assertions.assertThrows(BizException.class, () -> {
        photoDto = ucc.insertPhoto(" ");
      });
    }

    @DisplayName(value = "info manquantes dans le insertPhoto photo=null ")
    @Test
    public void testinserPhoto_infoManquantesPhotoNull() {
      Assertions.assertThrows(BizException.class, () -> {
        photoDto = ucc.insertPhoto(null);
      });
    }

  }
}
