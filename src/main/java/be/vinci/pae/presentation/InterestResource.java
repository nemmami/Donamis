package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.InterestDto;
import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.business.ucc.InterestUcc;
import be.vinci.pae.business.ucc.ObjectUcc;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.presentation.filters.Authorize;
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
@Path("/interest")
public class InterestResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private InterestUcc interestUcc;

  @Inject
  private UserUcc userUcc;

  @Inject
  private ObjectUcc objectUcc;

  /**
   * Permet d'inserer un interet.
   *
   * @param json le json recu du frontend
   * @return publicTypeJson de l'interet
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode insertInterest(JsonNode json) throws SQLException {
    System.out.println("InterestResource => insertInterest");

    if (!json.hasNonNull("id_membre")
        || !json.hasNonNull("id_objet")
        || !json.hasNonNull("date_disponibilite") || !json.hasNonNull("appel")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certains informations")
              .type("text/plain").build());
    }

    int idUser = json.get("id_membre").asInt();
    int idObject = json.get("id_objet").asInt();
    String dateDisponibilite = json.get("date_disponibilite").asText();
    boolean appel = json.get("appel").asBoolean();

    InterestDto interest;
    if (!appel) {
      interest = interestUcc.insertInterest(idUser, idObject, dateDisponibilite, false);
    } else {
      String numero = json.get("numero").asText();
      interest = interestUcc.insertInterest(idUser, idObject, dateDisponibilite, appel, numero);
    }

    ObjectNode publicTypeJson = jsonMapper.createObjectNode()
        .put("id", interest.getIdInterest())
        .put("id_membre", interest.getIdUser())
        .put("id_objet", interest.getIdObject())
        .put("date_disponible", interest.getDateDisponibilite())
        .put("appel", interest.isAppel());

    return publicTypeJson;
  }

  /**
   * Permet de recuperer la liste de tous les interets d'un objet.
   *
   * @param idObject l'id de l'objet recu du frontend
   * @return la liste de ces interets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getObjectInterest(@PathParam("id") int idObject) throws SQLException {
    System.out.println("InterestResource => getObjectInterest");
    List<InterestDto> listTypes = interestUcc.getObjectInterest(idObject);
    return getObjectArrayNode(listTypes);
  }

  /**
   * Permet de recuperer la liste de tous les interets des objets d'un membre.
   *
   * @param idUser l'id du membre recu du frontend
   * @return la liste de ces interets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("allInterestMember/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllInterestForUserObject(@PathParam("id") int idUser) throws SQLException {
    System.out.println("InterestResource => getAllInterestForUserObject");
    List<InterestDto> listTypes = interestUcc.getAllInterestFromUser(idUser);
    return getObjectArrayNode(listTypes);
  }

  /**
   * Permet de recuperer la liste de tous les interets d'un membre dont l'attribut "vue notif est à
   * false".
   *
   * @param idUser l'id du membre recu du frontend
   * @return la liste de ces interets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("/allInterestObjectUserVueNotifFalse/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllInterestForUserObjectVueNotifFalse(@PathParam("id") int idUser)
      throws SQLException {
    System.out.println("InterestResource => getAllInterestForUserObjectVueNotifFalse");
    List<InterestDto> listTypes = interestUcc.getAllInterestForUserObject(idUser);

    return getJsonNodes(listTypes);
  }

  /**
   * Permet de recuperer la liste de tous les interets d'un membre dont l'attribut "vue notif est à
   * true".
   *
   * @param idUser l'id du membre recu du frontend
   * @return la liste de ces interets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("/allInterestObjectUserVueNotifTrue/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllInterestForUserObjectVueNotifTrue(@PathParam("id") int idUser)
      throws SQLException {
    System.out.println("InterestResource => getAllInterestForUserObjectVueNotifTrue");
    List<InterestDto> listTypes = interestUcc.getAllInterestForUserObject(idUser);

    return getJsonNodes(listTypes);
  }

  private ArrayNode getJsonNodes(List<InterestDto> listTypes) throws SQLException {
    ArrayNode nodeTab = jsonMapper.createArrayNode();
    for (InterestDto interest : listTypes) {

      UserDto user = userUcc.getOne(interest.getIdUser());
      ObjectDto objectDto = objectUcc.getObject(interest.getIdObject());

      ObjectNode groupeNode = nodeTab.addObject();
      groupeNode.put("id_interet", interest.getIdInterest());
      groupeNode.put("id_membre", interest.getIdUser());
      groupeNode.put("id_objet", interest.getIdObject());
      groupeNode.put("membre", user.getPseudo());
      groupeNode.put("titre", objectDto.getTitre());
    }

    return nodeTab;
  }

  private ArrayNode getObjectArrayNode(List<InterestDto> list) throws SQLException {
    ArrayNode nodeTab = jsonMapper.createArrayNode();
    for (InterestDto interest : list) {
      UserDto user = userUcc.getOne(interest.getIdUser());

      ObjectNode groupeNode = nodeTab.addObject();
      groupeNode.put("id_interet", interest.getIdInterest());
      groupeNode.put("id_membre", interest.getIdUser());
      groupeNode.put("membre", user.getPseudo());
      groupeNode.put("objet", interest.getIdObject());
      groupeNode.put("date_disponibilite", interest.getDateDisponibilite());
      groupeNode.put("appel", interest.isAppel());
      groupeNode.put("num_telephone", user.getTelephone());
    }

    return nodeTab;
  }


}
