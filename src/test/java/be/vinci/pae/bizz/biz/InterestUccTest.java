package be.vinci.pae.bizz.biz;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.InterestDto;
import be.vinci.pae.business.ucc.InterestUcc;
import be.vinci.pae.exceptions.BizConflictException;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.BizNotFoundException;
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
public class InterestUccTest {

  private InterestUcc ucc;
  private InterestDto interestDto;
  private InterestDto interestDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(InterestUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Nested
  @DisplayName(value = "Insert Interet")
  public class InterestTest {

    @BeforeEach
    void setUp() {
      interestDtoTest = myBiz.getInterestDto();
      interestDtoTest.setIdInterest(1);
      interestDtoTest.setUser(1);
      interestDtoTest.setObject(2);
      interestDtoTest.setDateDisponibilite("Le samedi a 18h");
      interestDtoTest.setAppel(false);
    }


    @DisplayName(value = "idnfo fausses iUser = 0")
    @Test
    public void testInsert_infoFausses() {
      Assertions.assertThrows(BizException.class, () -> {
        interestDto = ucc.insertInterest(0, 1, "du mercredi a vendredi", true);
      });
    }

    @DisplayName(value = "idnfo fausses idObjet = 0")
    @Test
    public void testInsert_infoFausses2() {
      Assertions.assertThrows(BizException.class, () -> {
        interestDto = ucc.insertInterest(1, 0, "du mercredi a vendredi", true);
      });
    }

    @DisplayName(value = "idnfo fausses date de disponibilite est vide")
    @Test
    public void testInsert_infoFausses3() {
      Assertions.assertThrows(BizException.class, () -> {
        interestDto = ucc.insertInterest(1, 1, " ", true);
      });
    }

    @DisplayName(value = "idnfo fausses date de disponibilite est null")
    @Test
    public void testInsert_infoFausses4() {
      Assertions.assertThrows(BizException.class, () -> {
        interestDto = ucc.insertInterest(1, 1, null, true);
      });
    }

    @DisplayName(value = "idnfo fausses date de numero est null")
    @Test
    public void testInsert_infoFausses5() {
      Assertions.assertThrows(BizException.class, () -> {
        interestDto = ucc.insertInterest(1, 1, "mercredi", true, null);
      });
    }


    @DisplayName(value = "idUser inexistant")
    @Test
    public void testInsertInterestUserNotFound() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        interestDto = ucc.insertInterest(2, 1, "du mercredi a vendredi", true);
      });
    }

    @DisplayName(value = "idObject inexistant")
    @Test
    public void testInsertInterestObjectNotFound() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        interestDto = ucc.insertInterest(1, 7, "du mercredi a vendredi", true);
      });
    }

    @DisplayName(value = "Interet déjà ajouter")
    @Test
    public void testInsertInterestAlreadyAdded() {
      Assertions.assertThrows(BizConflictException.class, () -> {
        interestDto = ucc.insertInterest(1, 2, "du mercredi a vendredi", true);
      });
    }


  }

  @Nested
  @DisplayName(value = "list interest")
  public class TestInterestList {

    @BeforeEach
    void setUp() {
      interestDtoTest = myBiz.getInterestDto();
    }

    @DisplayName(value = "test list getObjectInterest")
    @Test
    public void testGetObjectInterest() throws SQLException {
      int idUser = 1;
      List<InterestDto> interest = ucc.getObjectInterest(idUser);
      assertNotNull(interest);
      assertTrue(interest.size() == 3);
    }

    @DisplayName(value = "test list getObjectInterest fausses information")
    @Test
    public void testGetObjectInterestFausse() {
      int idUser = 0;
      Assertions.assertThrows(BizException.class, () -> {
        ucc.getObjectInterest(idUser);
      });

    }

    @DisplayName(value = "test list getAllInterestFromUser")
    @Test
    public void testGetAllInterestFromUser() throws SQLException {
      int idUser = 1;
      List<InterestDto> interest = ucc.getAllInterestFromUser(idUser);
      assertNotNull(interest);
      assertTrue(interest.size() == 3);
    }

    @DisplayName(value = "test list getObjectInterest fausses information")
    @Test
    public void testGetAllInterestFromUserFausse() {
      int idUser = 0;
      Assertions.assertThrows(BizException.class, () -> {
        ucc.getAllInterestFromUser(idUser);
      });

    }

    @DisplayName(value = "test list getAllInterestForUserObject")
    @Test
    public void testgetAllInterestForUserObjectOK() throws SQLException {
      int idUser = 1;
      List<InterestDto> interest = ucc.getAllInterestForUserObject(idUser);
      assertNotNull(interest);
      assertTrue(interest.size() == 3);
    }

    @DisplayName(value = "test list getAllInterestForUserObject fausses information")
    @Test
    public void testGetAllInterestForUserObjectFausseInfo() {
      int idUser = 0;
      Assertions.assertThrows(BizException.class, () -> {
        ucc.getAllInterestForUserObject(idUser);
      });

    }
  }

}
