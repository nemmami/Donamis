package be.vinci.pae.bizz.biz;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.business.ucc.ObjectUcc;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.BizNotFoundException;
import java.sql.Date;
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
public class ObjectUccTest {

  private ObjectUcc ucc;
  private ObjectDto objectDto;
  private ObjectDto objectDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(ObjectUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Nested
  @DisplayName(value = "Insert object avec photo")
  public class InsertObjectTest {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
      objectDtoTest.setIdObjet(1);
      objectDtoTest.setIdType(1);
      objectDtoTest.setTitre("Lampe Ikea");
      objectDtoTest.setDescription("Lampe très belle");
      objectDtoTest.setPhoto("photo.png");
      objectDtoTest.setDate(Date.valueOf("2020-03-02"));
      objectDtoTest.setPlageHoraire("Du lundi au mercredi");
      objectDtoTest.setMembre_offreur(1);
      objectDtoTest.setEtat("Offert");
      objectDtoTest.setNbrInteresse(0);
    }

    @DisplayName(value = "Titre vide")
    @Test
    public void testInsertObjectTitreVide() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, "", "tres beau", "", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Titre null")
    @Test
    public void testInsertObjectTitreNull() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, null, "tres beau", "", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Description vide")
    @Test
    public void testInsertObjectDescriptionVide() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, "titre", "", "", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Description null")
    @Test
    public void testInsertObjectDescriptionNull() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, "titre", null, "", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Plage horaire vide")
    @Test
    public void testInsertObjectPlageHoraireVide() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, "titre", "description", "", "", 1);
      });
    }

    @DisplayName(value = "Plage horaire vide")
    @Test
    public void testInsertObjectPlageHoraireNull() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, "titre", "description", "", null, 1);
      });
    }

    @DisplayName(value = "Photo vide")
    @Test
    public void testInsertObjectPhotoVide() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, "titre", "description", "", null, 1);
      });
    }

    @DisplayName(value = "Photo vide")
    @Test
    public void testInsertObjectPhotoNull() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(1, "titre", "description", null, null, 1);
      });
    }

    @DisplayName(value = "Type inexistant")
    @Test
    public void testInsertObjectTypeInexistant() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObject(2, "titre", "tres beau", "", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Membre inexistant")
    @Test
    public void testInsertObjectMembreInexistant() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        objectDto = ucc.insertObject(1, "titre", "tres beau", "1", "lundi au lundi", 2);
      });
    }

    @DisplayName(value = "insert object OK")
    @Test
    public void testInsertObjectOK() throws SQLException {
      objectDto = ucc.insertObject(1, "Lampe Ikea", "Lampe très belle", "photo.png",
          "Du lundi au mercredi", 1);
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());
    }
  }

  @Nested
  @DisplayName(value = "Insert object avec photo")
  public class InsertObjectWithoutPhotoTest {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
      objectDtoTest.setIdObjet(1);
      objectDtoTest.setIdType(1);
      objectDtoTest.setTitre("Lampe Ikea");
      objectDtoTest.setDescription("Lampe très belle");
      objectDtoTest.setPhoto(null);
      objectDtoTest.setDate(Date.valueOf("2020-03-02"));
      objectDtoTest.setPlageHoraire("Du lundi au mercredi");
      objectDtoTest.setMembre_offreur(1);
      objectDtoTest.setEtat("Offert");
      objectDtoTest.setNbrInteresse(0);
    }

    @DisplayName(value = "Titre vide")
    @Test
    public void testInsertObjectTitreVide() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(1, "", "tres beau", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Titre null")
    @Test
    public void testInsertObjectTitreNull() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(1, null, "tres beau", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Description vide")
    @Test
    public void testInsertObjectDescriptionVide() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(1, "titre", "", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Description null")
    @Test
    public void testInsertObjectDescriptionNull() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(1, "titre", null, "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Plage horaire vide")
    @Test
    public void testInsertObjectPlageHoraireVide() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(1, "titre", "description", "", 1);
      });
    }

    @DisplayName(value = "Plage horaire vide")
    @Test
    public void testInsertObjectPlageHoraireNull() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(1, "titre", "description", null, 1);
      });
    }

    @DisplayName(value = "Type inexistant")
    @Test
    public void testInsertObjectTypeInexistant() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(2, "titre", "tres beau", "lundi au lundi", 1);
      });
    }

    @DisplayName(value = "Type inexistant")
    @Test
    public void testInsertObjectMembreInexistant() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        objectDto = ucc.insertObjectWithoutPhoto(1, "titre", "tres beau", "lundi au lundi", 2);
      });
    }

    @DisplayName(value = "insert object OK")
    @Test
    public void testInsertObjectOK() throws SQLException {
      objectDto = ucc.insertObjectWithoutPhoto(1, "Lampe Ikea", "Lampe très belle",
          "Du lundi au mercredi", 1);
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());
    }
  }

  @Nested
  @DisplayName(value = "Mis a jour d'un objet")
  public class UpdateObject {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
      objectDtoTest.setIdObjet(1);
      objectDtoTest.setIdType(1);
      objectDtoTest.setTitre("Lampe Ikea");
      objectDtoTest.setDescription("Lampe très belle");
      objectDtoTest.setPhoto(null);
      objectDtoTest.setDate(Date.valueOf("2020-03-02"));
      objectDtoTest.setPlageHoraire("Du lundi au mercredi");
      objectDtoTest.setMembre_offreur(1);
      objectDtoTest.setEtat("Offert");
      objectDtoTest.setNbrInteresse(0);
    }

    @DisplayName(value = "update without photo OK")
    @Test
    public void testUpdateObjectOK() throws SQLException {
      objectDto = ucc.updateObject(1, "ordinateur asus", "ordinateur tres bon etat",
          "du lundi a mercredi");
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());

    }

    @DisplayName(value = "update with photo OK")
    @Test
    public void testUpdateObjectWithPhotoOK() throws SQLException {
      objectDto = ucc.updateObjectWithPhoto(1, "ordinateur asus", "ordinateur tres bon etat",
          "du lundi a mercredi", "photo.jpg");
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());

    }

    @DisplayName(value = "manque d'une information ")
    @Test
    public void testUpdate() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.updateObject(1, " ", "tres beau", "du lunid a mardi");
      });

    }

  }


  @Nested
  @DisplayName(value = "mis a jour du nombre d'interesses ")
  public class UpdateNombreInteresse {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
      objectDtoTest.setIdObjet(1);
    }

    @DisplayName(value = "update nombre interesses ok")
    @Test
    public void testUpdateObjectWithPhotoOK() throws SQLException {
      objectDto = ucc.updateNombreInteresses(1);
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());

    }

    @DisplayName(value = "informations incorrectes ")
    @Test
    public void updateNombreInteresses_InfoIncorrecte() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.updateNombreInteresses(-1);
      });
    }
  }

  @Nested
  @DisplayName(value = "annulerOffre ")
  public class AnnulerOffre {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
      objectDtoTest.setIdObjet(1);
      objectDtoTest.setEtat("Offert");

    }

    @DisplayName(value = "annuler offre ok")
    @Test
    public void annulerOffreOK() throws SQLException {
      objectDto = ucc.cancelOffer(1);
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());
    }


    @DisplayName(value = "informations incorrectes ")
    @Test
    public void annulerOffre_Infoincorrectes() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.cancelOffer(0);
      });
    }

    @DisplayName(value = "objet n'existe pas ")
    @Test
    public void annulerOffre_ObjetExistePas() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        objectDto = ucc.cancelOffer(4);
      });
    }

    @DisplayName(value = "etat objet incorrect ")
    @Test
    public void annulerOffre_EtatAnnuler() {
      Assertions.assertThrows(BizException.class, () -> {
        int id = 3;
        objectDto = ucc.getObject(id);
        objectDto = ucc.cancelOffer(id);
        Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());
      });
    }


  }

  @Nested
  @DisplayName(value = " reposter objet interet marqué && reposter objet")
  public class ReposterObjet {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
      objectDtoTest.setIdObjet(1);

    }

    @DisplayName(value = "reposterObjetInteretMarquer OK")
    @Test
    public void reposterObjetInteretMarquerOK() throws SQLException {
      objectDto = ucc.reposterObjetInteretMarquer(1);
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());
    }


    @DisplayName(value = "informations incorrectes pour reposterObjetIntereMarque")
    @Test
    public void reposterObjetInteretMarquer_InfoIncorrecte() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.reposterObjetInteretMarquer(-1);
      });
    }

    @DisplayName(value = "reposterObjet OK")
    @Test
    public void reposterObjetOK() throws SQLException {
      objectDto = ucc.reposterObjet(1);
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());
    }

    @DisplayName(value = "informations incorrectes pour reposterObjet ")
    @Test
    public void reposterObjet_InfoIncorrecte() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.updateNombreInteresses(-1);
      });
    }

    @DisplayName(value = "objet n'exite pas pour reposterObjet ")
    @Test
    public void reposterObjet_ObjetExistePas() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        int id = 5;
        ucc.getObject(id);
        ucc.reposterObjet(id);
      });
    }

    @DisplayName(value = "informations fausses pour reposterObjet ")
    @Test
    public void reposterObjet_InfoFausses() {
      Assertions.assertThrows(BizException.class, () -> {
        int id = 0;
        ucc.getObject(id);
        ucc.reposterObjet(id);
      });
    }


  }

  @Nested
  @DisplayName(value = "test list")
  public class ListObject {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
    }

    @DisplayName(value = "test list get last object")
    @Test
    public void testGetLastObjects() throws SQLException {
      List<ObjectDto> objects = ucc.getLastObjects();
      assertNotNull(objects);
    }

    @DisplayName(value = "test list get all object")
    @Test
    public void testGetAllObjects() throws SQLException {
      List<ObjectDto> objects = ucc.getAllObjects();
      assertNotNull(objects);
    }

    @DisplayName(value = "test list getAllObjectsOffered ")
    @Test
    public void testGetAllObjectsOffered() throws SQLException {
      List<ObjectDto> objects = ucc.getAllObjectsOffered();
      assertNotNull(objects);
    }


    @DisplayName(value = "test list getAllConfirmedObjectsFromUsers")
    @Test
    public void testgetAllConfirmedObjectsFromUsers() throws SQLException {
      int idUser = 1;
      List<ObjectDto> objects = ucc.getAllConfirmedObjectsFromUsers(idUser);
      assertNotNull(objects);
      assertTrue(objects.size() == 9);

    }


    @DisplayName(value = "test list getAllObjectsFromUsers")
    @Test
    public void testGetAllObjectsFromUsers() throws SQLException {
      int idUser = 1;
      List<ObjectDto> objects = ucc.getAllObjectsFromUsers(idUser);
      assertNotNull(objects);
      assertTrue(objects.size() == 9);

    }


    @DisplayName(value = "information manquantes dans getAllObjectsFromUsers")
    @Test
    public void testInfoManquantes_getAllObjectsFromUsers() throws SQLException {
      int idUser = 0;
      Assertions.assertThrows(BizException.class, () -> {
        ucc.getAllObjectsFromUsers(idUser);
      });


    }

  }

  @Nested
  @DisplayName(value = "getObject")
  public class TestGetObject {

    @BeforeEach
    void setUp() {
      objectDtoTest = myBiz.getObjectDto();
      objectDtoTest.setIdObjet(1);
    }

    @DisplayName(value = "getObjet ok")
    @Test
    public void getObjetOk() throws SQLException {
      objectDto = ucc.getObject(1);
      Assertions.assertEquals(objectDto.getIdObjet(), objectDtoTest.getIdObjet());
    }

    @DisplayName(value = "information manquantes dans getAllObjectsFromUsers")
    @Test
    public void testGetObjet_InfoManquante() {
      Assertions.assertThrows(BizException.class, () -> {
        objectDto = ucc.getObject(0);
      });

    }
  }

}



