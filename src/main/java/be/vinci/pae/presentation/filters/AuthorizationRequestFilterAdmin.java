package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.dto.UserDto;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.SQLException;

@Singleton
@Provider
@AuthorizeAdmin
public class AuthorizationRequestFilterAdmin implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();

  @Inject
  private UserUcc myUcc;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("A token is needed to access this resource").build());
    } else {
      try {
        checkToken(requestContext, token, this.jwtVerifier, myUcc);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      DecodedJWT decodedToken = this.jwtVerifier.verify(token);

      if (!decodedToken.getClaim("admin").asBoolean()) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("You are forbidden to access this resource").build());
      }
    }
  }

  static void checkToken(ContainerRequestContext requestContext, String token,
      JWTVerifier jwtVerifier, UserUcc myUcc) throws SQLException {
    DecodedJWT decodedToken = null;
    try {
      decodedToken = jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new TokenDecodingException(e);
    }
    UserDto authenticatedUser = myUcc.getOne(decodedToken.getClaim("user").asInt());
    if (authenticatedUser == null) {
      requestContext.abortWith(Response.status(Status.FORBIDDEN)
          .entity("You are forbidden to access this resource").build());
    }
  }

}
