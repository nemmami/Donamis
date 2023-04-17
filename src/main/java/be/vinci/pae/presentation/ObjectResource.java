package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.business.ucc.ObjectUcc;
import be.vinci.pae.business.ucc.PhotoUcc;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Singleton
@Path("/objects")
public class ObjectResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private ObjectUcc objectUcc;

  @Inject
  private TypeUcc typeUcc;

  @Inject
  private UserUcc userUcc;

  @Inject
  private PhotoUcc photoUcc;

  /**
   * Permet de poster un objet.
   *
   * @param json le json recu du frontend
   * @return la reponse de l'opération
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("insert")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public Response insertObject(JsonNode json) throws SQLException {
    System.out.println("ObjectResource => insertObject");
    if (!json.hasNonNull("titre") || !json.hasNonNull("description")
        || !json.hasNonNull("plage_horaire")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certaines informations")
              .type("text/plain").build());
    }

    int idType = json.get("id_type").asInt();
    String titre = json.get("titre").asText();
    String description = json.get("description").asText();
    String photo = json.get("photo").asText();
    String plageHoraire = json.get("plage_horaire").asText();
    int membreOffreur = json.get("membre_offreur").asInt();

    if (!photo.equals("")) {
      objectUcc.insertObject(idType, titre, description, photo, plageHoraire,
          membreOffreur);
    } else {
      objectUcc.insertObjectWithoutPhoto(idType, titre, description, plageHoraire,
          membreOffreur);
    }

    return Response.ok(MediaType.APPLICATION_JSON).build();
  }

  /**
   * Permet d'upload des photos.
   *
   * @param file            le file
   * @param fileDisposition le fileDisposition
   * @return publicPhotoJson la photo ajouté
   * @throws IOException  si qqchose se passe mal
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Path("upload")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Authorize
  public ObjectNode uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition)
      throws IOException, SQLException {
    System.out.println("ObjectRessource => uploadFile");

    String extension = ".";
    extension += FilenameUtils.getExtension(fileDisposition.getFileName());
    extension = extension.toLowerCase();
    String fileName = UUID.randomUUID().toString();
    fileName += extension;

    PhotoDto photoDto = photoUcc.insertPhoto(fileName);
    Files.copy(file, Paths.get("photo/" + fileName));

    ObjectNode publicPhotoJson = jsonMapper.createObjectNode()
        .put("id", photoDto.getIdPhoto())
        .put("name", photoDto.getNomPhoto());

    return publicPhotoJson;
  }

  /**
   * Permet de recuperer un objet apd de son id.
   *
   * @param idObject l'id de l'objet recu du frontend
   * @return publicObjectJson l'objet recherche
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getObject(@PathParam("id") int idObject) throws SQLException {
    System.out.println("ObjectResource => getObject");

    ObjectDto objectDto = objectUcc.getObject(idObject);
    TypeDto typeDto = typeUcc.getOne(objectDto.getIdType());
    UserDto userDto = userUcc.getOne(objectDto.getMembre_offreur());

    ObjectNode publicObjectJson = jsonMapper.createObjectNode()
        .put("id", objectDto.getIdObjet())
        .put("id_type", typeDto.getLibelle())
        .put("titre", objectDto.getTitre())
        .put("description", objectDto.getDescription())
        .put("photo", objectDto.getPhoto())
        .put("plage_horaire", objectDto.getPlageHoraire())
        .put("id_membre_offreur", objectDto.getMembre_offreur())
        .put("membre_offreur", userDto.getPseudo())
        .put("date", objectDto.getDate().toString())
        .put("etat", objectDto.getEtat())
        .put("nbr_interesse", objectDto.getNbrInteresse());

    return publicObjectJson;
  }

  /**
   * Permet d'envoyer la liste des derniers objets offert.
   *
   * @return la liste de tout ses objets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("lastObjects")
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getLastObjects() throws SQLException {
    System.out.println("ObjectResource => getLastObjects");
    List<ObjectDto> objectList = objectUcc.getLastObjects();
    return getObjectArrayNode(objectList);
  }

  /**
   * Permet d'envoyer la liste de tout les objets offert.
   *
   * @return la liste de ces objets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("allObjectsOffered")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllObjectsOffered() throws SQLException {
    System.out.println("ObjectResource => getAllObjectsOffered");
    List<ObjectDto> objectList = objectUcc.getAllObjectsOffered();
    return getObjectArrayNode(objectList);
  }

  /**
   * Permet d'envoyer la liste de tout les objets.
   *
   * @return la liste de ces objets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("allObjects")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ArrayNode getAllObjects() throws SQLException {
    System.out.println("ObjectResource => getAllObjects");
    List<ObjectDto> objectList = objectUcc.getAllObjects();
    return getObjectArrayNode(objectList);
  }

  /**
   * Permet d'envoyer tous les objets d'un membre.
   *
   * @param idUser l'id du membre recu du frontend
   * @return la liste de ces objets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("object/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllObjectFromUser(@PathParam("id") int idUser) throws SQLException {
    System.out.println("ObjectResource => getAllObjectFromUser");
    List<ObjectDto> objectList = objectUcc.getAllObjectsFromUsers(idUser);
    return getObjectArrayNode(objectList);
  }

  /**
   * Permet d'envoyer tous les objets d'un membre.
   *
   * @param idUser l'id du membre recu du frontend
   * @return la liste de ces objets
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("objectConfirmedUser/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllConfirmedObjectsFromUsers(@PathParam("id") int idUser)
      throws SQLException {
    System.out.println("ObjectResource => getAllConfirmedObjectsFromUsers");
    List<ObjectDto> objectList = objectUcc.getAllConfirmedObjectsFromUsers(idUser);
    return getObjectArrayNode(objectList);
  }

  /**
   * Permet d'annuler une offre.
   *
   * @param idObject l'id de l'objet recu du frontend
   * @return l'objet mis a jour
   * @throws SQLException si qqchose se passe mal
   */
  @PUT
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectDto cancelOffer(@PathParam("id") int idObject) throws SQLException {
    System.out.println("ObjectResource => cancelOffer");
    ObjectDto objetUpdated = objectUcc.cancelOffer(idObject);
    return objetUpdated;
  }

  /**
   * Permet de reposter un objet.
   *
   * @param idObject l'id de l'objet recu du frontend
   * @return l'objet mis a jour
   * @throws SQLException si qqchose se passe mal
   */
  @PUT
  @Path("reposter/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectDto reposterObject(@PathParam("id") int idObject) throws SQLException {
    System.out.println("ObjectResource => reposterObject");
    ObjectNode object = this.getObject(idObject);
    ObjectDto objetUpdated;
    if (object.get("nbr_interesse").asInt() == 0) {
      objetUpdated = objectUcc.reposterObjet(idObject);
    } else {
      objetUpdated = objectUcc.reposterObjetInteretMarquer(idObject);
    }

    return objetUpdated;
  }

  /**
   * Permet de reposter un objet annuler.
   *
   * @param idObject l'id de l'objet recu du frontend
   * @return l'objet mis a jour
   * @throws SQLException si qqchose se passe mal
   */
  @PUT
  @Path("reposterInteretMarquer/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectDto reposterObjetInteretMarquer(@PathParam("id") int idObject) throws SQLException {
    System.out.println("ObjectResource => reposterObjetInteretMarquer");
    ObjectDto objetUpdated = objectUcc.reposterObjetInteretMarquer(idObject);
    return objetUpdated;
  }

  /**
   * Permet de mettre a jour les info d'un objet.
   *
   * @param json le json recu du frontend
   * @return la reponse de l'operation
   */
  @PUT
  @Path("UpdateObject/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public Response updateObject(@PathParam("id") int idObject, JsonNode json) throws SQLException {
    System.out.println("ObjectResource => updateObject");

    if (!json.hasNonNull("titre") || !json.hasNonNull("description")
        || !json.hasNonNull("plage_horaire")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certaines informations")
              .type("text/plain").build());
    }

    ObjectDto objectBase = objectUcc.getObject(idObject);

    String titre = json.get("titre").asText();
    String description = json.get("description").asText();
    String plageHoraire = json.get("plage_horaire").asText();
    final String photo = json.get("photo").asText();

    if (titre.equals("")) {
      titre = objectBase.getTitre();
    }
    if (description.equals("")) {
      description = objectBase.getDescription();
    }
    if (plageHoraire.equals("")) {
      plageHoraire = objectBase.getPlageHoraire();
    }

    if (photo.equals("")) {
      objectUcc.updateObject(idObject, titre, description,
          plageHoraire);
    } else {
      objectUcc.updateObjectWithPhoto(idObject, titre, description,
          plageHoraire, photo);
    }

    return Response.ok(MediaType.APPLICATION_JSON).build();
  }


  /**
   * Permet de serialize un tableau de ObjectDto.
   *
   * @param list une liste de ObjectDto
   * @return un tableau json de cette liste
   */
  private ArrayNode getObjectArrayNode(List<ObjectDto> list) throws SQLException {
    ArrayNode nodeTab = jsonMapper.createArrayNode();
    for (ObjectDto object : list) {
      TypeDto type = typeUcc.getOne(object.getIdType());
      UserDto user = userUcc.getOne(object.getMembre_offreur());
      ObjectNode groupeNode = nodeTab.addObject();
      groupeNode.put("id", object.getIdObjet());
      groupeNode.put("id_type", type.getLibelle());
      groupeNode.put("titre", object.getTitre());
      groupeNode.put("description", object.getDescription());
      groupeNode.put("photo", object.getPhoto());
      groupeNode.put("plage_horaire", object.getPlageHoraire());
      groupeNode.put("date", object.getDate().toString());
      groupeNode.put("membre_offreur", user.getPseudo());
      groupeNode.put("etat", object.getEtat());
      groupeNode.put("nbr_interesse", object.getNbrInteresse());
    }
    return nodeTab;
  }
}
