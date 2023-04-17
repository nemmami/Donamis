package be.vinci.pae.exceptions;

import be.vinci.pae.utils.Logging;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class CustomException extends RuntimeException implements ExceptionMapper<CustomException> {

  private static final long serialVersionUID = 1L;

  @Inject
  private Logging logger;

  public CustomException() {
    super("Erreur CustomException");
  }

  public CustomException(String s) {
    super(s);
  }

  @Override
  public Response toResponse(CustomException exception) {
    logger.log(Level.WARNING, exception.getMessage());
    return Response.status(Status.UNAUTHORIZED).entity(exception.getMessage()).type("text/plain")
        .build();
  }
}
