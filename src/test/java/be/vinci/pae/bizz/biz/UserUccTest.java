package be.vinci.pae.bizz.biz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.vinci.pae.ApplicationBinderForTests;
import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.exceptions.BizConflictException;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.BizForbiddenException;
import be.vinci.pae.exceptions.BizNotFoundException;
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
class UserUccTest {

  private UserUcc ucc;
  private UserDto userDto;
  private UserDto userDtoTest;
  private BizFactory myBiz;

  @BeforeAll
  void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderForTests());
    this.ucc = locator.getService(UserUcc.class);
    this.myBiz = locator.getService(BizFactory.class);
  }

  @Test
  public void demoTest() {
    assertNotNull(this.ucc);
  }

  @Nested
  @DisplayName(value = "Login")
  public class LoginTest {

    @BeforeEach
    void setUp() {
      userDtoTest = myBiz.getUserDTO();
      userDtoTest.setIdUser(0);
      userDtoTest.setPseudo("test1");
      userDtoTest.setNom("dupond");
      userDtoTest.setPrenom("julien");
      userDtoTest.setEtatInscription("Confirmé");
      userDtoTest.setMotDePasse("1234");
    }

    @DisplayName(value = "Mot de passe du user vide")
    @Test
    public void loginMDPVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.seConnecter("test2", "");
      });
    }

    @DisplayName(value = "Pseudo du user vide")
    @Test
    public void loginPseudoVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.seConnecter("", "1234");
      });
    }

    @DisplayName(value = "Mot de passe du user null")
    @Test
    public void loginMDPNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.seConnecter("test2", null);
      });
    }

    @DisplayName(value = "Pseudo du user null")
    @Test
    public void loginPseudoNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.seConnecter(null, "1234");
      });
    }

    @DisplayName(value = "Pseudo et mot de passe du user null")
    @Test
    public void loginPseudoMDPNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.seConnecter(null, null);
      });
    }

    @DisplayName(value = "Pseudo non trouve")
    @Test
    public void loginPseudoNonTrouve() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        userDto = ucc.seConnecter("tes", "1234");
      });
    }

    @DisplayName(value = "Pseudo trouve mais mot de passse incorrect")
    @Test
    public void loginPseudoTrouveMauvaisMDP() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        userDto = ucc.seConnecter("test1", "12341");
      });
    }

    @DisplayName(value = "identifiants corrects mais inscription en attente de confirmation")
    @Test
    public void loginPseudoTrouveBonMDPEnAttente() {
      Assertions.assertThrows(BizForbiddenException.class, () -> {
        userDto = ucc.seConnecter("test2", "1234");
      });
    }

    @DisplayName(value = "identifiants corrects mais inscription refusée")
    @Test
    public void loginPseudoTrouveBonMDPRefusee() {
      Assertions.assertThrows(BizForbiddenException.class, () -> {
        userDto = ucc.seConnecter("test3", "1234");
      });
    }

    @DisplayName(value = "identifiants corrects et inscription accepté")
    @Test
    public void loginPseudoTrouveBonMDPConfirmee() throws SQLException {
      userDto = ucc.seConnecter("test1", "1234");
      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }
  }

  @Nested
  @DisplayName(value = "register")
  public class RegisterTest {

    @BeforeEach
    void setUp() {
      userDto = null;
      userDtoTest = myBiz.getUserDTO();
      userDtoTest.setIdUser(1);
      userDtoTest.setPseudo("test1");
      userDtoTest.setNom("test1");
      userDtoTest.setPrenom("test1");
      userDtoTest.setMotDePasse("1234");
      userDtoTest.setIdAdresse(2);
    }

    @DisplayName(value = "Pseudo déjà utlisé")
    @Test
    public void testRegisterPseudoDejaPresent() {
      Assertions.assertThrows(BizConflictException.class, () -> {
        userDto = ucc.inscription("test2", "test2", "test2 ", "1234", "Rue de Haute Folie",
            6,
            "A103", 4800, "Verviers");
      });
    }

    @DisplayName(value = "Pseudo vide")
    @Test
    public void testRegisterPseudoVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.inscription("", "test1", "test1", "1234", "Rue de Haute Folie", 6,
            "A103", 4800, "Verviers");
      });
    }


    @DisplayName(value = "Prenom vide")
    @Test
    public void testRegisterPrenomVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.inscription("test1", "", "test1", "1234", "Rue de Haute Folie", 6,
            "A103", 4800, "Verviers");
      });
    }

    @DisplayName(value = "Nom vide")
    @Test
    public void testRegisterNomVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.inscription("test1", "test1", "", "1234", "Rue de Haute Folie", 6,
            "A103", 4800, "Verviers");
      });
    }

    @DisplayName(value = "Nom null")
    @Test
    public void testRegisterNomNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.inscription("test1", "test1", null, "1234", "Rue de Haute Folie", 6,
            "A103", 4800, "Verviers");
      });
    }

    @DisplayName(value = "Nom null")
    @Test
    public void testRegisterPrenomNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.inscription("test1", null, "test1", "1234", "Rue de Haute Folie", 6,
            "A103", 4800, "Verviers");
      });
    }

    @DisplayName(value = "Pseudo null")
    @Test
    public void testRegisterPseudoNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.inscription(null, "test1", "test1", "1234", "Rue de Haute Folie", 6,
            "A103", 4800, "Verviers");
      });
    }

    @DisplayName(value = "Register Fonctionne")
    @Test
    public void registerOK() throws SQLException {
      userDto = ucc.inscription("test1", "test1", "test1", "1234", "Rue du test1", 6,
          "A103", 4800, "ville du test1");
      System.out.println(userDto.getIdUser());

      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

  }

  @Nested
  @DisplayName(value = "list")
  public class ListTest {

    @BeforeEach
    void setUp() {
      userDto = null;
      userDtoTest = myBiz.getUserDTO();

    }

    @DisplayName(value = "test list users refuse")
    @Test
    public void testListerUsersRefuse() throws SQLException {
      assertNotNull(ucc.getAllUserDeclined());
    }

    @DisplayName(value = "test list users confirme")
    @Test
    public void testListerUsersConfirme() throws SQLException {
      assertNotNull(ucc.getAllUserConfirmed());
    }

    @DisplayName(value = "test list users en attente")
    @Test
    public void testListerUsersEnAttente() throws SQLException {
      assertNotNull(ucc.getAllUserWaiting());
    }

    @DisplayName(value = "test list tout les users")
    @Test
    public void testGetAllUsers() throws SQLException {
      assertNotNull(ucc.getAllUsers());
    }

  }

  @Nested
  @DisplayName(value = "getOne de l'id && getOne du pseudo")
  public class TestGetOne {

    @BeforeEach
    void setUp() {
      userDtoTest = myBiz.getUserDTO();
      userDtoTest.setIdUser(1);
      userDtoTest.setPseudo("test2");
    }

    @DisplayName(value = "getOne Fonctionne")
    @Test
    public void getOneOK() throws SQLException {
      userDto = ucc.getOne(1);

      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

    @DisplayName(value = "getOne : certaines info sont fausses")
    @Test
    public void testGetOne_InfoFausses() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.getOne(0);
      });
    }

    @DisplayName(value = "getOne Fonctionne")
    @Test
    public void getOnePseudoOK() throws SQLException {
      String pseudo = "test2";
      userDto = ucc.getOne(pseudo);
      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

    @DisplayName(value = "getOne du pseudo : certaines info sont fausses")
    @Test
    public void testGetOne_PseudoVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.getOne("");
      });
    }

    @DisplayName(value = "getOne du pseudo : certaines info sont fausses")
    @Test
    public void testGetOne_PseudoNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.getOne(null);
      });
    }

  }

  @Nested
  @DisplayName(value = "annuler la participation d'un membre ")
  public class TestAnnulerParticipation {

    @BeforeEach
    void setUp() {
      userDtoTest = myBiz.getUserDTO();
      userDtoTest.setIdUser(1);
      userDtoTest.setEtatInscription("Annulé");
    }

    @DisplayName(value = "annuler Fonctionne")
    @Test
    public void testAnnulerOK() throws SQLException {
      userDto = ucc.annulerParticipation(1);
      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

    @DisplayName(value = "PseudoIncorrect du membre")
    @Test
    public void testAnnulerParticipation_PseudoIncorrect() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        userDto = ucc.annulerParticipation(2);
      });
    }

    @DisplayName(value = "manque des information")
    @Test
    public void testAnnulerParticipation_ManqueInfo() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.annulerParticipation(0);
      });
    }

  }

  @Nested
  @DisplayName(value = "test de setters ")
  public class TestSetters {

    @BeforeEach
    void setUp() {
      userDtoTest = myBiz.getUserDTO();
      userDtoTest.setIdUser(1);
      userDtoTest.setPseudo("test1");
    }

    @DisplayName(value = "setAdminOK ")
    @Test
    public void testSetAdminOK() throws SQLException {
      String pseudo = "test2";
      userDto = ucc.getOne(pseudo);
      ucc.setAdmin(pseudo);
      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

    @DisplayName(value = "setAdmin membre deja admin ")
    @Test
    public void testSetAdminDEjaAdmin() {
      String pseudo = "test4";
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.getOne(pseudo);
        ucc.setAdmin(pseudo);
      });
    }


    @DisplayName(value = "pseudo est null ")
    @Test
    public void testSetAdmin_PseudoNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setAdmin(null);
      });
    }

    @DisplayName(value = "pseudo est vide ")
    @Test
    public void testSetAdmin_PseudoVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setAdmin("");
      });
    }

    @DisplayName(value = "pseudo incorrect ")
    @Test
    public void testSetAdmin_PseudoIncorrect() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        userDto = ucc.setAdmin("pseudo");
      });
    }

    @DisplayName(value = "setEtatInscriptionConfirmee Fonctionne")
    @Test
    public void setEtatInscriptionConfirmeeOK() throws SQLException {
      String pseudo = "test2";
      userDto = ucc.getOne(pseudo);
      ucc.setEtatInscriptionConfirmee(pseudo);
      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

    @DisplayName(value = "setEtatInscriptionConfirmee , inscription deja confirmeé")
    @Test
    public void setEtatInscriptionConfirmee_DejaConfirme() throws SQLException {
      String pseudo = "test1";
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.getOne(pseudo);
        ucc.setEtatInscriptionConfirmee(pseudo);
      });
    }

    @DisplayName(value = "pseudo est null dans setEtatInscriptionConfirmee ")
    @Test
    public void testsetEtatInscriptionConfirmee_PseudoNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setEtatInscriptionConfirmee(null);
      });
    }

    @DisplayName(value = "pseudo est vide dans setEtatInscriptionConfirmee ")
    @Test
    public void testsetEtatInscriptionConfirmee_PseudoVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setEtatInscriptionConfirmee("");
      });
    }


    @DisplayName(value = "setEtatInscriptionConfirmee Fonctionne")
    @Test
    public void setEtatInscriptionRefuseeOK() throws SQLException {
      String pseudo = "test2";
      userDto = ucc.getOne(pseudo);
      ucc.setEtatInscriptionRefusee("raison", pseudo);
      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

    @DisplayName(value = "setEtatInscriptionRefusee l'user n'est pas inscrit ")
    @Test
    public void testsetEtatInscriptionRefuseeEtatNonInscrit() {
      Assertions.assertThrows(BizException.class, () -> {
        String pseudo = "test1";
        userDto = ucc.getOne(pseudo);
        userDto = ucc.setEtatInscriptionRefusee("raisonRefus", pseudo);
      });
    }

    @DisplayName(value = "pseudo incorrect ")
    @Test
    public void setEtatInscriptionRefusee_Pseudoincorrect() {
      Assertions.assertThrows(BizNotFoundException.class, () -> {
        userDto = ucc.setEtatInscriptionRefusee("raisonREfus", "pseudo");
      });
    }


    @DisplayName(value = "pseudo est vide dans setEtatInscriptionRefusee ")
    @Test
    public void testsetEtatInscriptionRefuseePseudoVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setEtatInscriptionRefusee("raison", "");
      });
    }

    @DisplayName(value = "pseudo est null dans setEtatInscriptionRefusee ")
    @Test
    public void testSetEtatInscriptionRefuseePseudonull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setEtatInscriptionRefusee("raison", null);
      });
    }


    @DisplayName(value = "raison refus est vide dans setEtatInscriptionConfirmee ")
    @Test
    public void testsetEtatInscriptionRefuseeRaisonRefusVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setEtatInscriptionRefusee("", "pseudo");
      });
    }

    @DisplayName(value = "raison refus est null dans setEtatInscriptionConfirmee ")
    @Test
    public void testsetEtatInscriptionRefuseeRaisonRefusNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setEtatInscriptionRefusee(null, "pseudo");
      });
    }


    @DisplayName(value = "setNumeroTelephone Fonctionne ")
    @Test
    public void setNumeroTelephoneOk() throws SQLException {
      int id = 1;
      userDto = ucc.getOne(id);
      ucc.setNumeroTelephone(id, "0949494949");
      assertEquals(userDto.getIdUser(), userDtoTest.getIdUser());
    }

    @DisplayName(value = "setNumeroTelephone manque des info ")
    @Test
    public void setNumeroTelephoneId0() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setNumeroTelephone(0, "0498983989");
      });
    }

    @DisplayName(value = "setNumeroTelephone numero null ")
    @Test
    public void setNumeroTelephoneNumeroNull() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setNumeroTelephone(2, null);
      });
    }

    @DisplayName(value = "setNumeroTelephone numero vide ")
    @Test
    public void setNumeroTelephoneNumeroVide() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setNumeroTelephone(2, "");
      });
    }

    @DisplayName(value = "setNumeroTelephone mauvais format de numero ")
    @Test
    public void setNumeroTelephoneNumeroMauvaisFormat() {
      Assertions.assertThrows(BizException.class, () -> {
        userDto = ucc.setNumeroTelephone(2, "00303");
      });
    }

  }


}



