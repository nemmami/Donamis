package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.NotificationDto;
import be.vinci.pae.business.ucc.NotificationUcc;
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
@Path("/notification")
public class NotificationRessource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private NotificationUcc notificationUcc;


  /**
   * Permet d'inserer une notification.
   *
   * @param json le json recu du frontend
   * @return publicNotificationJson de la notification
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode insertNotification(JsonNode json) throws SQLException {
    System.out.println("NotificationResource => insertNotification");

    if (!json.hasNonNull("id_membre")
        || !json.hasNonNull("libelle")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certains informations")
              .type("text/plain").build());
    }

    int idUser = json.get("id_membre").asInt();
    String libelle = json.get("libelle").asText();
    String type = json.get("type").asText();
    NotificationDto notificationDto = notificationUcc.insertNotification(libelle, idUser, type);

    ObjectNode publicNotificationJson = jsonMapper.createObjectNode()
        .put("id", notificationDto.getIdNotif())
        .put("libelle", notificationDto.getLibelle())
        .put("membre", notificationDto.getIdMembre());

    return publicNotificationJson;
  }

  /**
   * Permet de savoir si il y a de nouvelles notifications ou non.
   *
   * @param idUser l'id du user recu du frontend
   * @return true si oui, false sinon
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("/newNotif/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public boolean isNewNotif(@PathParam("id") int idUser) throws SQLException {
    System.out.println("NotificationResource => isNewNotif");
    return notificationUcc.isNewNotification(idUser);
  }

  /**
   * Permet de recuperer la liste de toutes les notifications d'un membre.
   *
   * @param idUser l'id du membre recu du frontend
   * @return la list de toutes ses notifications
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("/allNotificationFromUser/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllNotificationForUser(@PathParam("id") int idUser) throws SQLException {
    System.out.println("NotificationResource => getAllNotificationForUser");
    List<NotificationDto> listNotifs = notificationUcc.getAllNotificationFromUser(idUser);
    return getJsonNodes(listNotifs);
  }

  /**
   * Permet de changer l'etat de l'attribut "vue notif" a true d'une notification.
   *
   * @param idNotification l'id de la notification recu du frontend
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("/setNotifVueTrue/{idNotification}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public void setNotifVueTrue(@PathParam("idNotification") int idNotification) throws SQLException {
    System.out.println("NotificationResource => setNotifVueTrue");
    notificationUcc.setNotifVue(idNotification);
  }

  private ArrayNode getJsonNodes(List<NotificationDto> listNotifs) throws SQLException {
    ArrayNode nodeTab = jsonMapper.createArrayNode();
    for (NotificationDto notif : listNotifs) {

      ObjectNode groupeNode = nodeTab.addObject();
      groupeNode.put("id_notif", notif.getIdNotif());
      groupeNode.put("id_membre", notif.getIdMembre());
      groupeNode.put("libelle", notif.getLibelle());
      groupeNode.put("date", notif.getDateNotif().toString());
      groupeNode.put("notifVue", notif.isNotifVue());
      groupeNode.put("type", notif.getType());
    }

    return nodeTab;
  }

}
