package be.vinci.pae.bizz.biz;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.business.ucc.TypeUcc;
import be.vinci.pae.exceptions.BizConflictException;
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
public class TypesUccTest {

  private TypeUcc ucc;
  private TypeDto typeDto;
  private TypeDto typeDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(TypeUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Nested
  @DisplayName(value = "Insert type")
  public class TypeTest {

    @BeforeEach
    void setUp() {
      typeDtoTest = myBiz.getTypeDto();
      typeDtoTest.setIdType(1);
      typeDtoTest.setLibelle("testLibelle");
    }

    @DisplayName(value = "Libelle vide")
    @Test
    public void testInsertTypeAvecLibelleVide() {
      Assertions.assertThrows(BizException.class, () -> {
        typeDto = ucc.insert("");
      });
    }

    @DisplayName(value = "Libelle déjà present")
    @Test
    public void testInsertTypeLibelleDejaPresent() {
      Assertions.assertThrows(BizConflictException.class, () -> {
        typeDto = ucc.insert("type1");
      });
    }

    @DisplayName(value = "Ajout de type OK")
    @Test
    public void testInsertTypeOK() throws SQLException {
      typeDto = ucc.insert("testLibelle");
      Assertions.assertEquals(typeDto.getIdType(), typeDtoTest.getIdType());
    }

  }

  @Nested
  @DisplayName(value = "list type")
  public class ListType {

    @BeforeEach
    void setUp() {
      typeDtoTest = myBiz.getTypeDto();
    }

    @DisplayName(value = "test list get all types")
    @Test
    public void testListerUsersEnAttente() throws SQLException {
      assertNotNull(ucc.getAllTypes());
    }
  }


  @Nested
  @DisplayName(value = "getOne du type ")
  public class TestgetOne {

    @BeforeEach
    void setUp() {
      typeDtoTest = myBiz.getTypeDto();
      typeDtoTest.setIdType(1);
    }

    @DisplayName(value = "getOne du type ")
    @Test
    public void testGetOne() throws SQLException {
      typeDto = ucc.getOne(1);
      Assertions.assertEquals(typeDto.getIdType(), typeDtoTest.getIdType());
    }

    @DisplayName(value = "info manquantes dans le getOne du type")
    @Test
    public void infoManquantes_testGetOne() {
      Assertions.assertThrows(BizException.class, () -> {
        typeDto = ucc.getOne(0);
      });
    }
  }
}