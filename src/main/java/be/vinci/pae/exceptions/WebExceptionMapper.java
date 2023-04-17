package be.vinci.pae.exceptions;

import be.vinci.pae.utils.Logging;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Inject
  Logging logger;

  @Override
  public Response toResponse(Throwable exception) {
    logger.log(Level.WARNING, exception.getMessage());
    exception.printStackTrace();
    if (exception instanceof WebApplicationException) {
      return ((WebApplicationException) exception).getResponse();
      // the response is already prepared
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }
}
