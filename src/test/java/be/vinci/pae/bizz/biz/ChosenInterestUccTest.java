package be.vinci.pae.bizz.biz;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.ChosenInterestDto;
import be.vinci.pae.business.ucc.ChosenInterestUcc;
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
public class ChosenInterestUccTest {

  private ChosenInterestUcc ucc;
  private ChosenInterestDto chosenInterestDto;
  private ChosenInterestDto chosenInterestDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(ChosenInterestUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Nested
  @DisplayName(value = "tes all setters")
  public class TestAllSetter {

    @BeforeEach
    void setUp() {
      chosenInterestDtoTest = myBiz.getChosenInterestDto();
      chosenInterestDtoTest.setIdChosenInterest(1);
      chosenInterestDtoTest.setIdObject(1);
      chosenInterestDtoTest.setIdMember(1);
    }

    @DisplayName(value = "setEtatTransactionVenu OK")
    @Test
    public void testSetEtatTransactionVenuOK() throws SQLException {
      int idObject = 1;
      chosenInterestDto = ucc.setEtatTransactionVenu(idObject);
      Assertions.assertEquals(chosenInterestDto.getIdObject(), chosenInterestDtoTest.getIdObject());
    }

    @DisplayName(value = "information manquantes dans setEtatTransactionVenu")
    @Test
    public void testInfoManquantes_setEtatTransactionVenu() {
      int idObject = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = ucc.setEtatTransactionVenu(idObject);
      });
    }

    @DisplayName(value = "setEtatTransactionPasVenu OK")
    @Test
    public void testSetEtatTransactionPasVenuOK() throws SQLException {
      int idObject = 1;
      chosenInterestDto = ucc.setEtatTransactionPasVenu(idObject);
      Assertions.assertEquals(chosenInterestDto.getIdObject(), chosenInterestDtoTest.getIdObject());
    }

    @DisplayName(value = "information manquantes dans setEtatTransactionPasVenu")
    @Test
    public void testInfoManquantes_setEtatTransactionPasVenu() {
      int idObject = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = ucc.setEtatTransactionPasVenu(idObject);
      });
    }

  }

  @Nested
  @DisplayName(value = "test InsertChosenInterest")
  public class InsertChosenInterest {

    @BeforeEach
    void setUp() {
      chosenInterestDtoTest = myBiz.getChosenInterestDto();
      chosenInterestDtoTest.setIdChosenInterest(1);
      chosenInterestDtoTest.setIdObject(1);
      chosenInterestDtoTest.setIdMember(1);
    }

    @DisplayName(value = "insertChosenInterest OK")
    @Test
    public void insertChosenInterestOK() throws SQLException {
      chosenInterestDto = ucc.insertChosenInterest(1, 1);
      Assertions.assertEquals(chosenInterestDto.getIdMember(), chosenInterestDtoTest.getIdMember());
    }

    @DisplayName(value = "information fausses dans insertChosenInterest")
    @Test
    public void testInfoManquantes_insertChosenInterestIdMemberFaux() {

      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = ucc.insertChosenInterest(0, 2);
      });
    }

    @DisplayName(value = "information fausses dans insertChosenInterest")
    @Test
    public void testInfoManquantes_insertChosenInterestIdObjectFaux() {

      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = ucc.insertChosenInterest(1, 0);
      });
    }


  }

  @Nested
  @DisplayName(value = "tes all getters")
  public class TestAllGetters {

    @BeforeEach
    void setUp() {
      chosenInterestDtoTest = myBiz.getChosenInterestDto();
      chosenInterestDtoTest.setIdChosenInterest(1);
      chosenInterestDtoTest.setIdObject(1);
      chosenInterestDtoTest.setIdMember(1);
    }


    @DisplayName(value = "test getGivenObjects")
    @Test
    public void testgetGivenObjects() throws SQLException {
      int idUser = 1;
      List<ChosenInterestDto> choseInterest = ucc.getGivenObjects(idUser);
      assertNotNull(choseInterest);
      assertTrue(choseInterest.size() == 3);
    }

    @DisplayName(value = "information manquantes dans getGivenObjects")
    @Test
    public void testInfoManquantes_getGivenObjects() {
      int idUser = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = (ChosenInterestDto) ucc.getGivenObjects(idUser);
      });
    }


    @DisplayName(value = "test getReceivedObjects")
    @Test
    public void testGetReceivedObjects() throws SQLException {
      int idMember = 1;
      List<ChosenInterestDto> choseInterest = ucc.getReceivedObjects(idMember);
      assertNotNull(choseInterest);
      assertTrue(choseInterest.size() == 3);

    }

    @DisplayName(value = "information manquantes dans getReceivedObjects")
    @Test
    public void testInfoManquantes_getReceivedObjects() {
      int idMember = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = (ChosenInterestDto) ucc.getReceivedObjects(idMember);
      });
    }


    @DisplayName(value = "test getNeverCameObjects")
    @Test
    public void testGetNeverCameObjects() throws SQLException {
      int idMember = 1;
      List<ChosenInterestDto> choseInterest = ucc.getNeverCameObjects(idMember);
      assertNotNull(choseInterest);
      assertTrue(choseInterest.size() == 3);

    }

    @DisplayName(value = "information manquantes dans getNeverCameObjects")
    @Test
    public void testInfoManquantes_getNeverCameObjects() {
      int idMember = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = (ChosenInterestDto) ucc.getNeverCameObjects(idMember);
      });
    }


    @DisplayName(value = "test getAllChosenInterestMember")
    @Test
    public void testGetAllChosenInterestMember() throws SQLException {
      int idMember = 1;
      List<ChosenInterestDto> choseInterest = ucc.getAllChosenInterestMember(idMember);
      assertNotNull(choseInterest);
      assertTrue(choseInterest.size() == 3);

    }

    @DisplayName(value = "information manquantes dans getAllChosenInterestMember")
    @Test
    public void testInfoManquantes_getAllChosenInterestMember() {
      int idMember = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = (ChosenInterestDto) ucc.getAllChosenInterestMember(idMember);
      });
    }


    @DisplayName(value = "test getChosenInterestByMember")
    @Test
    public void testGetChosenInterestByMember() throws SQLException {
      int idUser = 1;
      List<ChosenInterestDto> choseInterest = ucc.getChosenInterestByMember(idUser);
      assertNotNull(choseInterest);
      assertTrue(choseInterest.size() == 3);

    }

    @DisplayName(value = "information manquantes dans getChosenInterestByMember")
    @Test
    public void testInfoManquantes_getChosenInterestByMember() {
      int idUser = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = (ChosenInterestDto) ucc.getAllChosenInterestMember(idUser);
      });
    }

    @DisplayName(value = "test getChosenInterest")
    @Test
    public void testGetChosenInterest() throws SQLException {
      int idMember = 1;
      List<ChosenInterestDto> choseInterest = ucc.getChosenInterest(idMember);
      assertNotNull(choseInterest);
      assertTrue(choseInterest.size() == 3);

    }

    @DisplayName(value = "information manquantes dans getChosenInterest")
    @Test
    public void testInfoManquantes_getChosenInterest() {
      int idMember = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = (ChosenInterestDto) ucc.getChosenInterest(idMember);
      });
    }


    @DisplayName(value = "getChosenInterestByObject OK")
    @Test
    public void testGetChosenInterestByObjectOK() throws SQLException {
      int idObject = 1;
      chosenInterestDto = ucc.getChosenInterestByObject(idObject);
      Assertions.assertEquals(chosenInterestDto.getIdObject(), chosenInterestDtoTest.getIdObject());
    }

    @DisplayName(value = "information manquantes dans getChosenInterestByObject")
    @Test
    public void testInfoManquantes_getChosenInterestByObject() {
      int idObject = 0;
      Assertions.assertThrows(BizException.class, () -> {
        chosenInterestDto = ucc.getChosenInterestByObject(idObject);
      });
    }
  }


}
