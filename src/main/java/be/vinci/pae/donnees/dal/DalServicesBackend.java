package be.vinci.pae.donnees.dal;

import java.sql.PreparedStatement;

public interface DalServicesBackend {

  PreparedStatement createStatement(String query);
}
