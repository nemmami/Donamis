package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.business.ucc.AddressUcc;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.presentation.filters.AuthorizeAdmin;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Singleton
@Path("/users")
public class UsersResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final long lifeTime = 5;

  @Inject
  private UserUcc userUcc;

  @Inject
  private AddressUcc addressUcc;

  /**
   * Connexion au site.
   *
   * @param json Le json reçu du frontend
   * @return publicUserJson le user renvoyee au frontend
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) throws SQLException {
    System.out.println("UserResource => Login");
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("motDePasse")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Login or password required")
              .type("text/plain").build());
    }

    String pseudo = json.get("pseudo").asText();
    String motDePasse = json.get("motDePasse").asText();

    // Try to login
    UserDto publicUser = userUcc.seConnecter(pseudo, motDePasse);
    if (publicUser == null) {
      throw new WebApplicationException(
          Response.status(Status.UNAUTHORIZED).entity("Pseudo ou mot de passe incorrect")
              .type("text/plain").build());
    }
    AddressDto publicAdress = addressUcc.findOne(publicUser.getIdAdresse());

    return createToken(publicUser, publicAdress);
  }

  /**
   * Permet de recuperer le user grace a son token.
   *
   * @param json Le json reçu du frontend
   * @return le nouveau token cree
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("token")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getUser(JsonNode json) throws SQLException {
    System.out.println("UserResource => getUser");
    if (!json.hasNonNull("id")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("id required")
              .type("text/plain").build());
    }

    int id = json.get("id").asInt();
    UserDto publicUser = userUcc.getOne(id);
    AddressDto publicAdress = addressUcc.findOne(publicUser.getIdAdresse());
    return createToken(publicUser, publicAdress);
  }

  /**
   * Permet de créer un token.
   *
   * @param publicUser   le membre
   * @param publicAdress l'adresse du membre
   * @return le token crée
   */
  private ObjectNode createToken(UserDto publicUser, AddressDto publicAdress) {
    ObjectNode publicUserJson;
    String token;
    try {
      LocalDateTime ldt = LocalDateTime.now();
      LocalDateTime expired = ldt.plusDays(this.lifeTime);
      ZonedDateTime zdt = expired.atZone(ZoneId.systemDefault());
      Date expirationDate = Date.from(zdt.toInstant());

      token = JWT.create().withIssuer("auth0")
          .withClaim("user", publicUser.getIdUser()).withExpiresAt(expirationDate)
          .withClaim("admin", publicUser.isAdmin()).withExpiresAt(expirationDate)
          .sign(this.jwtAlgorithm);
      publicUserJson = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", publicUser.getIdUser())
          .put("pseudo", publicUser.getPseudo())
          .put("nom", publicUser.getNom())
          .put("prenom", publicUser.getPrenom())
          .put("admin", publicUser.isAdmin())
          .put("telephone", publicUser.getTelephone())
          .put("etat_inscription", publicUser.getEtatInscription())
          .put("id adresse", publicAdress.getIdAddresse())
          .put("rue", publicAdress.getRue())
          .put("numero", publicAdress.getNumero())
          .put("boite", publicAdress.getBoite())
          .put("codePostal", publicAdress.getCodePostal())
          .put("ville", publicAdress.getVille());
    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
    return publicUserJson;
  }

  /**
   * Inscription au site.
   *
   * @param json le json recu du frontend
   * @return publicUserJson le user connecté
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("register")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public ObjectNode register(JsonNode json) throws SQLException {
    System.out.println("UserResource => Register");
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("nom")
        || !json.hasNonNull("prenom") || !json.hasNonNull("motDePasse")
        || !json.hasNonNull("rue") || !json.hasNonNull("codePostal")
        || !json.hasNonNull("ville") || !json.hasNonNull("numero")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certaines informations")
              .type("text/plain").build());
    }

    String pseudo = json.get("pseudo").asText();
    String motDePasse = json.get("motDePasse").asText();
    String nom = json.get("nom").asText();
    String prenom = json.get("prenom").asText();
    String rue = json.get("rue").asText();
    int numero = json.get("numero").asInt();
    int codePostal = json.get("codePostal").asInt();
    String ville = json.get("ville").asText();
    String boite;
    if (json.get("boite").asText().isBlank()) {
      boite = "";
    } else {
      boite = json.get("boite").asText();
    }

    UserDto publicUser = userUcc.inscription(pseudo, prenom, nom, motDePasse, rue, numero, boite,
        codePostal, ville);

    if (publicUser == null) {
      throw new WebApplicationException(
          Response.status(Status.UNAUTHORIZED).entity("Erreur à l'inscription")
              .type("text/plain").build());
    }

    ObjectNode publicUserJson = jsonMapper.createObjectNode()
        .put("pseudo", pseudo)
        .put("nom", nom)
        .put("prenom", prenom)
        .put("rue", rue)
        .put("numero", numero)
        .put("boite", boite)
        .put("codePostal", codePostal)
        .put("ville", ville);

    return publicUserJson;
  }

  /**
   * Permet d'envoyer la liste des users dont l'inscription a été refusé.
   *
   * @return la liste de ces membres
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("declined")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getAllUserDeclined() throws SQLException {
    System.out.println("UserResource => getAllUserDeclined");
    List<UserDto> list = userUcc.getAllUserDeclined();
    return getUserArrayNode(list);
  }

  /**
   * Permet d'envoyer la liste des users dont l'inscription est en attente.
   *
   * @return la liste de ces membres
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("waiting")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getAllUserWaiting() throws SQLException {
    System.out.println("UserResource => getAllUserWaiting");
    List<UserDto> list = userUcc.getAllUserWaiting();
    return getUserArrayNode(list);
  }

  /**
   * Permet d'envoyer la liste des users dont l'inscription est confirmée.
   *
   * @return la liste de ces membres
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("confirmed")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getAllUserConfirmed() throws SQLException {
    System.out.println("UserResource => getAllUserConfirmed");
    List<UserDto> list = userUcc.getAllUserConfirmed();
    return getUserArrayNode(list);
  }

  /**
   * Permet de refusée l'état d'inscription d'un user.
   *
   * @param json le json recu du frontend
   * @return publicUserJson le user renvoyée au frontend
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("setEtat/declined/{pseudo}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ObjectNode setEtatInscriptionRefusee(JsonNode json, @PathParam("pseudo") String pseudo)
      throws SQLException {
    System.out.println("UserResource => setEtatInscriptionRefusee");
    if (!json.hasNonNull("raisonRefus")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certaines informations")
              .type("text/plain").build());
    }

    String raisonRefus = json.get("raisonRefus").asText();

    UserDto publicUser = userUcc.setEtatInscriptionRefusee(raisonRefus, pseudo);
    ObjectNode publicUserJson = jsonMapper.createObjectNode()
        .put("pseudo", publicUser.getPseudo())
        .put("nom", publicUser.getNom())
        .put("prenom", publicUser.getPrenom())
        .put("etatInscription", publicUser.getEtatInscription())
        .put("raisonRefus", publicUser.getRaisonRefus());
    return publicUserJson;
  }

  /**
   * Permet de recuperer la raison d'un refus d'une inscription.
   *
   * @param pseudo le pseudo recu du frontend
   * @return publicUserJson la raison du refus renvoyée au frontend
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("getRaisonRefus/{pseudo}")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode getRaisoRefus(@PathParam("pseudo") String pseudo) throws SQLException {
    System.out.println("UserResource => getRaisoRefus");
    UserDto publicUser = userUcc.getOne(pseudo);
    if (publicUser == null) {
      return null;
    }

    ObjectNode publicUserJson = jsonMapper.createObjectNode()
        .put("raison", publicUser.getRaisonRefus());
    return publicUserJson;
  }

  /**
   * Permet d'annuler la participation d'un user.
   *
   * @param id l'id du membre recu du frontend
   * @return publicUser le user renvoyée au frontend
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("annulerParticipation/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public UserDto annulerParticipation(@PathParam("id") int id)
      throws SQLException {
    System.out.println("UserResource => annulerParticipation");
    UserDto publicUser = userUcc.annulerParticipation(id);
    return publicUser;
  }

  /**
   * Permet de confirmée l'état d'inscription d'un user.
   *
   * @param pseudo le pseudo du membre recu du frontend
   * @return publicUserJson le user renvoyée au frontend
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("setEtat/confirmed/{pseudo}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ObjectNode setEtatInscriptionConfirmee(@PathParam("pseudo") String pseudo)
      throws SQLException {
    System.out.println("UserResource => setEtatInscriptionConfirmee");
    UserDto publicUser = userUcc.setEtatInscriptionConfirmee(pseudo);
    ObjectNode publicUserJson = jsonMapper.createObjectNode()
        .put("pseudo", publicUser.getPseudo())
        .put("nom", publicUser.getNom())
        .put("prenom", publicUser.getPrenom())
        .put("etatInscription", publicUser.getEtatInscription());
    return publicUserJson;
  }

  /**
   * Permet de donner le rôle d'admin à un user.
   *
   * @param pseudo le pseudo recu du frontend
   * @return publicUserJson le user renvoyée au frontend
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("setAdmin/{pseudo}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ObjectNode setAdmin(@PathParam("pseudo") String pseudo) throws SQLException {
    System.out.println("UserResource => setAdmin");
    UserDto publicUser = userUcc.setAdmin(pseudo);
    ObjectNode publicUserJson = jsonMapper.createObjectNode()
        .put("pseudo", publicUser.getPseudo())
        .put("nom", publicUser.getNom())
        .put("prenom", publicUser.getPrenom())
        .put("admin", publicUser.isAdmin());
    return publicUserJson;
  }

  /**
   * Permet d'envoyer la liste de tous les users.
   *
   * @return la liste de ces membres
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("allUsers")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getAllUsers() throws SQLException {
    System.out.println("UserResource => getAllUsers");
    List<UserDto> list = userUcc.getAllUsers();
    return getUserArrayNode(list);
  }

  /**
   * Permet de recuperer un user sur base de son pseudo.
   *
   * @return publicUserJson le user renvoyée au frontend
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("/{pseudo}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getOne(@PathParam("pseudo") String pseudo) throws SQLException {
    System.out.println("UserResource => getOne");
    UserDto publicUser = userUcc.getOne(pseudo);
    ObjectNode publicUserJson = jsonMapper.createObjectNode()
        .put("id", publicUser.getIdUser())
        .put("pseudo", publicUser.getPseudo())
        .put("nom", publicUser.getNom())
        .put("prenom", publicUser.getPrenom())
        .put("admin", publicUser.isAdmin())
        .put("telephone", publicUser.getTelephone())
        .put("etatInscription", publicUser.getEtatInscription())
        .put("raisonRefus", publicUser.getRaisonRefus())
        .put("idAddress", publicUser.getIdAdresse())
        .put("motDePasse", publicUser.getMotDePasse());

    return publicUserJson;
  }

  /**
   * Permet de serialize un tableau de UserDto.
   *
   * @param list une liste de userDto
   * @return un tableau json de cette liste
   */
  private ArrayNode getUserArrayNode(List<UserDto> list) {
    ArrayNode nodeTab = jsonMapper.createArrayNode();
    for (UserDto user : list) {
      ObjectNode groupeNode = nodeTab.addObject();
      groupeNode.put("id", user.getIdUser());
      groupeNode.put("pseudo", user.getPseudo());
      groupeNode.put("nom", user.getNom());
      groupeNode.put("prenom", user.getPrenom());
      groupeNode.put("admin", user.isAdmin());
      groupeNode.put("etatInscription", user.getEtatInscription());
      groupeNode.put("raisonRefus", user.getRaisonRefus());
    }
    return nodeTab;
  }

}



