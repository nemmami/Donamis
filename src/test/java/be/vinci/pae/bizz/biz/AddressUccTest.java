package be.vinci.pae.bizz.biz;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.business.ucc.AddressUcc;
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
public class AddressUccTest {

  private AddressUcc ucc;
  private AddressDto addressDto;
  private AddressDto addressDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(AddressUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Nested
  @DisplayName(value = "Insert Address")
  public class InsertAddressTest {

    @BeforeEach
    void setUp() {
      addressDtoTest = myBiz.getAddresseDTO();
      addressDtoTest.setIdAddresse(1);
      addressDtoTest.setBoite("");
      addressDtoTest.setCodePostal(1080);
      addressDtoTest.setNumero(1);
      addressDtoTest.setRue("Rue du test");
      addressDtoTest.setVille("Ville du test");
    }

    @DisplayName(value = "Rue vide")
    @Test
    public void testInsertAddressRueVide() {
      Assertions.assertThrows(BizException.class, () -> {
        addressDto = ucc.ajouterAdresse("", 1, "", 1080, "Ville du test");
      });
    }

    @DisplayName(value = "Rue null")
    @Test
    public void testInsertAddressRueNull() {
      Assertions.assertThrows(BizException.class, () -> {
        addressDto = ucc.ajouterAdresse(null, 1, "", 1080, "Ville du test");
      });
    }

    @DisplayName(value = "Numero nul")
    @Test
    public void testInsertAddressNumeroNul() {
      Assertions.assertThrows(BizException.class, () -> {
        addressDto = ucc.ajouterAdresse("Rue du test", 0, "", 1080, "Ville du test");
      });
    }

    @DisplayName(value = "Code postal nul")
    @Test
    public void testInsertAddressCodePostalNul() {
      Assertions.assertThrows(BizException.class, () -> {
        addressDto = ucc.ajouterAdresse("Rue du test", 1, "", 0, "Ville du test");
      });
    }

    @DisplayName(value = "Ville vide")
    @Test
    public void testInsertAddressVilleVide() {
      Assertions.assertThrows(BizException.class, () -> {
        addressDto = ucc.ajouterAdresse("Rue du test", 1, "", 1080, "");
      });
    }

    @DisplayName(value = "Ville null")
    @Test
    public void testInsertAddressVilleNull() {
      Assertions.assertThrows(BizException.class, () -> {
        addressDto = ucc.ajouterAdresse("Rue du test", 1, "", 1080, null);
      });
    }

    @DisplayName(value = "Insert address OK")
    @Test
    public void testInsertAddressOK() throws SQLException {
      addressDto = ucc.ajouterAdresse("Rue du test", 1, "", 1080, "Ville du test");
      Assertions.assertEquals(addressDto.getIdAddresse(), addressDtoTest.getIdAddresse());
    }

  }

  @Nested
  @DisplayName(value = "fFind one adress")
  public class FindOneAdress {

    @BeforeEach
    void setUp() {
      addressDtoTest = myBiz.getAddresseDTO();
      addressDtoTest.setIdAddresse(1);
    }

    @DisplayName(value = "find one address OK")
    @Test
    public void testFindOneAdressOK() throws SQLException {
      addressDto = ucc.findOne(1);
      Assertions.assertEquals(addressDto.getIdAddresse(), addressDtoTest.getIdAddresse());
    }


    @DisplayName(value = "informations incorrectes pour find One adress")
    @Test
    public void findOneAdress_infoIncorrect() {
      Assertions.assertThrows(BizException.class, () -> {
        addressDto = ucc.findOne(-1);
      });
    }

    @DisplayName(value = "findAddress OK")
    @Test
    public void testFindAdressOK() throws SQLException {
      int id = 1;
      addressDto = ucc.findOne(id);
      ucc.findAdresse("rue", 23, "23", 1030, "Bruxelles");
      Assertions.assertEquals(addressDto.getIdAddresse(), addressDtoTest.getIdAddresse());
    }

    @DisplayName(value = "find adress avec rue qui est null")
    @Test
    public void findAdress_infoIncorrect() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse(null, 23, "23", 1030, "Bruxelles");

      });
    }

    @DisplayName(value = "find adress avec boite qui est null")
    @Test
    public void findAdress_infoIncorrect2() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse("rue", 23, null, 1030, "Bruxelles");

      });
    }

    @DisplayName(value = "find adress avec ville qui est null")
    @Test
    public void findAdress_infoIncorrect3() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse("rue", 23, "23", 1030, null);

      });
    }

    @DisplayName(value = "findAdresse manque d'un ou plusieurs info -> codepostal")
    @Test
    public void findAdress_infoManquantes() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse("rue", 23, "23", 0, "Bruxelles");

      });
    }

    @DisplayName(value = "findAdresse manque d'un ou plusieurs info -> numero")
    @Test
    public void findAdress_infoManquantes2() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse("rue", 0, "23", 23, "Bruxelles");

      });
    }

    @DisplayName(value = "findAdresse manque d'un ou plusieurs info -> rue")
    @Test
    public void findAdress_infoManquantes3() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse(" ", 21, "23", 23, "Bruxelles");

      });
    }

    @DisplayName(value = "findAdresse manque d'un ou plusieurs info -> boite")
    @Test
    public void findAdress_infoManquantes4() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse("rue", 21, " ", 23, "Bruxelles");

      });
    }

    @DisplayName(value = "findAdresse manque d'un ou plusieurs info -> ville")
    @Test
    public void findAdress_infoManquantes5() {
      Assertions.assertThrows(BizException.class, () -> {
        ucc.findAdresse("rue", 21, "23", 23, " ");

      });
    }
  }
}
