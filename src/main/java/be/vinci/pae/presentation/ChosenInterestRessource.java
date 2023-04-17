package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.ChosenInterestDto;
import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.business.ucc.ChosenInterestUcc;
import be.vinci.pae.business.ucc.ObjectUcc;
import be.vinci.pae.business.ucc.TypeUcc;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.presentation.filters.AuthorizeAdmin;
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
import java.util.List;

@Singleton
@Path("/chosenInterest")
public class ChosenInterestRessource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private ChosenInterestUcc chosenInterestUcc;

  @Inject
  private UserUcc userUcc;

  @Inject
  private ObjectUcc objectUcc;

  @Inject
  private TypeUcc typeUcc;

  /**
   * Permet d'inserer un interet choisi.
   *
   * @param json le json recu du frontend
   * @return publicChosenInterestJson de l'interet choisi
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode insertChosenInterest(JsonNode json) throws SQLException {
    System.out.println("ChosenInterestResource => insertChosenInterest");

    if (!json.hasNonNull("id_membre") || !json.hasNonNull("id_objet")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certains informations")
              .type("text/plain").build());
    }

    int idMembre = json.get("id_membre").asInt();
    int idObject = json.get("id_objet").asInt();

    ChosenInterestDto chosenInterestDto;
    chosenInterestDto = chosenInterestUcc.insertChosenInterest(idMembre, idObject);

    ObjectNode publicChosenInterestJson = jsonMapper.createObjectNode()
        .put("id", chosenInterestDto.getIdChosenInterest())
        .put("id_membre", chosenInterestDto.getIdMember())
        .put("id_objet", chosenInterestDto.getIdObject())
        .put("etat_transaction", chosenInterestDto.getEtatTransaction());

    return publicChosenInterestJson;
  }

  /**
   * Permet de changer l'etat de transaction quand le receveur est venu.
   *
   * @param json le json recu du frontend
   * @return publicChosenInterestJson de l'interet choisi
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("venu")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode updateEtatTransactionVenuChosenInterest(JsonNode json) throws SQLException {
    System.out.println("ChosenInterestResource => updateEtatTransactionVenuChosenInterest");

    if (!json.hasNonNull("id_objet")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certains informations")
              .type("text/plain").build());
    }

    int idObject = json.get("id_objet").asInt();

    ChosenInterestDto chosenInterestDto;
    chosenInterestDto = chosenInterestUcc.setEtatTransactionVenu(idObject);

    ObjectNode publicChosenInterestJson = jsonMapper.createObjectNode()
        .put("id", chosenInterestDto.getIdChosenInterest())
        .put("id_membre", chosenInterestDto.getIdMember())
        .put("id_objet", chosenInterestDto.getIdObject())
        .put("etat_transaction", chosenInterestDto.getEtatTransaction());

    return publicChosenInterestJson;
  }


  /**
   * Permet de changer l'etat de transaction quand le receveur n'est pas venu.
   *
   * @param json le json recu du frontend
   * @return publicChosenInterestJson de l'interet choisi
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("pasVenu")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize

  public ObjectNode updateEtatTransactionPasVenuChosenInterest(JsonNode json) throws SQLException {
    System.out.println("ChosenInterestResource => updateEtatTransactionPasVenuChosenInterest");

    if (!json.hasNonNull("id_objet")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certains informations")
              .type("text/plain").build());
    }

    int idObject = json.get("id_objet").asInt();

    ChosenInterestDto chosenInterestDto = chosenInterestUcc.getChosenInterestByObject(idObject);
    chosenInterestUcc.setEtatTransactionPasVenu(idObject);

    ObjectNode publicChosenInterestJson = jsonMapper.createObjectNode()
        .put("id", chosenInterestDto.getIdChosenInterest())
        .put("id_membre", chosenInterestDto.getIdMember())
        .put("id_objet", chosenInterestDto.getIdObject())
        .put("etat_transaction", chosenInterestDto.getEtatTransaction());

    return publicChosenInterestJson;
  }

  /**
   * Permet de recuperer un interet choisi.
   *
   * @param idObject id recu du frontend
   * @return publicChosenInterestJson de l'interet choisi
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getChosenInterest(@PathParam("id") int idObject) throws SQLException {
    System.out.println("ChosenInterestResource => getChosenInterest");
    List<ChosenInterestDto> listTypes = chosenInterestUcc.getChosenInterest(idObject);
    return getJsonNodes(listTypes);
  }

  /**
   * Permet de recuperer tout les interets choisi pour un objet.
   *
   * @param idObject id recu du frontend
   * @return publicChosenInterestJson de l'interet choisi
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("getChosenInterestByObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getChosenInterestByObject(@PathParam("id") int idObject) throws SQLException {
    System.out.println("ChosenInterestResource => getChosenInterestByObject");
    ChosenInterestDto chosenInterestDto = chosenInterestUcc.getChosenInterestByObject(idObject);

    ObjectNode publicChosenInterestJson = jsonMapper.createObjectNode()
        .put("id", chosenInterestDto.getIdChosenInterest())
        .put("id_membre", chosenInterestDto.getIdMember())
        .put("id_objet", chosenInterestDto.getIdObject())
        .put("etat_transaction", chosenInterestDto.getEtatTransaction());

    return publicChosenInterestJson;
  }

  /**
   * Permet de recuperer tout les interets choisis d'un membre.
   *
   * @param idMember id recu du frontend
   * @return publicTypeJson de l'interet
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("getAllChosenInterestMember/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllChosenInterestMember(@PathParam("id") int idMember) throws SQLException {
    System.out.println("ChosenInterestResource => getAllChosenInterestMember");
    List<ChosenInterestDto> listTypes = chosenInterestUcc.getAllChosenInterestMember(idMember);
    return getJsonNodes(listTypes);
  }


  /**
   * Permet de recuperer un interet choisi.
   *
   * @param idUser id recu du frontend
   * @return la liste de tous ses interets choisis
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("getAllChosenInterestByMemberNotifVueTrue/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getChosenInterestByMemberNotifVueTrue(@PathParam("id") int idUser)
      throws SQLException {
    System.out.println("ChosenInterestResource => getChosenInterestByMemberNotifVueTrue");
    List<ChosenInterestDto> listTypes = chosenInterestUcc.getChosenInterestByMember(idUser);
    return getJsonNodes(listTypes);
  }

  /**
   * Permet de recuperer touts les interets choisi d'un membre offreur dont l'etat est "donné".
   *
   * @param idMember id recu du frontend
   * @return la liste de tous ses interets choisis
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("getGivenObjects/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getGivenObjects(@PathParam("id") int idMember)
      throws SQLException {
    System.out.println("ChosenInterestResource => getGivenObjects");
    List<ChosenInterestDto> listTypes = chosenInterestUcc.getGivenObjects(idMember);
    return getJsonNodes(listTypes);
  }

  /**
   * Permet de recuperer touts les interets choisi d'un membre receveur dont l'etat est "donné".
   *
   * @param idMember id recu du frontend
   * @return la liste de tous ses interets choisis
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("getReceivedObjects/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getReceivedObjects(@PathParam("id") int idMember)
      throws SQLException {
    System.out.println("ChosenInterestResource => getReceivedObjects");
    List<ChosenInterestDto> listTypes = chosenInterestUcc.getReceivedObjects(idMember);
    return getJsonNodes(listTypes);
  }

  /**
   * Permet de recuperer touts les interets choisis d'un membre receveur dont l'etat est "pas
   * venu".
   *
   * @param idMember id recu du frontend
   * @return la liste de tous ses interets choisis
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("getNeverCameObjects/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getNeverCameObjects(@PathParam("id") int idMember)
      throws SQLException {
    System.out.println("ChosenInterestResource => getNeverCameObjects");
    List<ChosenInterestDto> listTypes = chosenInterestUcc.getNeverCameObjects(idMember);
    return getJsonNodes(listTypes);
  }


  /**
   * Permet de créer une liste d'interets choisis.
   *
   * @param listTypes la liste de ChosenInterestDTO
   * @return l'arrayNode de cette liste
   * @throws SQLException si qqchose se passe mal
   */
  private ArrayNode getJsonNodes(List<ChosenInterestDto> listTypes) throws SQLException {
    ArrayNode nodeTab = jsonMapper.createArrayNode();

    for (ChosenInterestDto chosen : listTypes) {
      UserDto user = userUcc.getOne(chosen.getIdMember());
      ObjectDto objet = objectUcc.getObject(chosen.getIdObject());
      UserDto user2 = userUcc.getOne(objet.getMembre_offreur());
      TypeDto type = typeUcc.getOne(objet.getIdType());

      ObjectNode groupeNode = nodeTab.addObject();
      groupeNode.put("id_chosen_interet", chosen.getIdChosenInterest());
      groupeNode.put("id_membre", chosen.getIdMember());
      groupeNode.put("id_objet", chosen.getIdObject());
      groupeNode.put("membre", user.getPseudo());
      groupeNode.put("titre", objet.getTitre());
      groupeNode.put("membre_offreur", user2.getPseudo());
      groupeNode.put("date", objet.getDate().toString());
      groupeNode.put("etat_transaction", chosen.getEtatTransaction());
      groupeNode.put("photo", objet.getPhoto());
      groupeNode.put("description", objet.getDescription());
      groupeNode.put("id_type", type.getLibelle());
      groupeNode.put("nbr_interesse", objet.getNbrInteresse());
    }

    return nodeTab;
  }
}