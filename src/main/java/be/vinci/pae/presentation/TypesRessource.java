package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.business.ucc.TypeUcc;
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
@Path("/types")
public class TypesRessource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private TypeUcc typeUcc;

  /**
   * Permet d'envoyer le type correspondant.
   *
   * @param id l'id du type recu du frontend
   * @return publicTypeJson du type
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getOneType(@PathParam("id") int id) throws SQLException {
    System.out.println("TypesResource => getOneType");
    TypeDto publicType = typeUcc.getOne(id);
    ObjectNode publicTypeJson = jsonMapper.createObjectNode()
        .put("id", publicType.getIdType())
        .put("libelle", publicType.getLibelle());
    return publicTypeJson;
  }

  /**
   * Permet d'inserer un type.
   *
   * @param json le json recu du frontend
   * @return publicTypeJson du type
   * @throws SQLException si qqchose se passe mal
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public ObjectNode insertType(JsonNode json) throws SQLException {
    System.out.println("TypesResource => insertType");
    if (!json.hasNonNull("libelle")) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Il manque certaines informations")
              .type("text/plain").build());
    }

    String libelle = json.get("libelle").asText();
    TypeDto publicType = typeUcc.insert(libelle);
    ObjectNode publicTypeJson = jsonMapper.createObjectNode()
        .put("id", publicType.getIdType())
        .put("libelle", publicType.getLibelle());
    return publicTypeJson;
  }


  /**
   * Permet de serialize un tableau de TypeDto.
   *
   * @return un tableau json de cette liste
   */
  @GET
  @Path("allTypes")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ArrayNode getAllTypes() throws SQLException {
    System.out.println("ObjectResource => getAllTypes");
    List<TypeDto> listTypes = typeUcc.getAllTypes();
    return getObjectArrayNode(listTypes);
  }

  private ArrayNode getObjectArrayNode(List<TypeDto> list) {
    ArrayNode nodeTab = jsonMapper.createArrayNode();
    for (TypeDto type : list) {
      ObjectNode groupeNode = nodeTab.addObject();
      groupeNode.put("id", type.getIdType());
      groupeNode.put("libelle", type.getLibelle());

    }
    return nodeTab;
  }

}
