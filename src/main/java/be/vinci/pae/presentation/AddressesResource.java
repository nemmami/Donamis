package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.business.ucc.AddressUcc;
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
@Path("/address")
public class AddressesResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private AddressUcc addressUcc;

  /**
   * Permet de recuperer toutes les informations d'une adresse.
   *
   * @param id l'id recu du frontend
   * @return publicAddressJson l'adresse
   * @throws SQLException si qqchose se passe mal
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode findOne(@PathParam("id") int id) throws SQLException {
    System.out.println("Address => findOne");
    AddressDto publicAddress = addressUcc.findOne(id);
    ObjectNode publicAddressJson = jsonMapper.createObjectNode()
        .put("idAddress", publicAddress.getIdAddresse())
        .put("rue", publicAddress.getRue())
        .put("numero", publicAddress.getNumero())
        .put("boite", publicAddress.getBoite())
        .put("codePostal", publicAddress.getCodePostal())
        .put("ville", publicAddress.getVille());

    return publicAddressJson;
  }

}
