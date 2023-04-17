package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.business.ucc.PhotoUcc;
import be.vinci.pae.presentation.filters.Authorize;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.sql.SQLException;

@Singleton
@Path("/photo")
public class PhotoResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private PhotoUcc photoUcc;

  /**
   * Permet de recuperer le nom d'une image.
   *
   * @param idPhoto l'id de la photo
   * @return publicPhotoJson la photo
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getPhoto(@PathParam("id") int idPhoto) throws SQLException {
    System.out.println("PhotoResource => getPhoto");
    PhotoDto photoDto = photoUcc.getPhoto(idPhoto);
    ObjectNode publicPhotoJson = jsonMapper.createObjectNode().put("id", photoDto.getIdPhoto())
        .put("name", photoDto.getNomPhoto());

    return publicPhotoJson;
  }
}
