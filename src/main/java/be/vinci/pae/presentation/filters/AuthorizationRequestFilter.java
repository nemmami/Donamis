package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.sql.SQLException;

@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgo = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwt = JWT.require(this.jwtAlgo).withIssuer("auth0")
      .build();

  @Inject
  private UserUcc myUcc;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("A token is needed to access this resource, retry again").build());
    } else {
      try {
        AuthorizationRequestFilterAdmin.checkToken(requestContext, token, this.jwt, myUcc);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
