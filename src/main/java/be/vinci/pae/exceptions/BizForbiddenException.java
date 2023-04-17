package be.vinci.pae.exceptions;

import be.vinci.pae.utils.Logging;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class BizForbiddenException extends RuntimeException implements
    ExceptionMapper<BizForbiddenException> {

  private static final long serialVersionUID = 1L;

  @Inject
  Logging logger;

  /**
   * Constructeur par défaut permettant de créer une exception (Qui sera appelée dans la couche
   * biz).
   */
  public BizForbiddenException() {
    super();
  }

  /**
   * Constructeur permettant de créer une exception en passant en paramètre un message.
   *
   * @param message : message à afficher
   */
  public BizForbiddenException(String message) {
    super(message);
  }

  @Override
  public Response toResponse(BizForbiddenException exception) {
    logger.log(Level.WARNING, exception.getMessage());
    return Response.status(Status.FORBIDDEN).entity(exception.getMessage()).type("text/plain")
        .build();
  }
}
