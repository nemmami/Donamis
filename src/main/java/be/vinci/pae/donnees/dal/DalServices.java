package be.vinci.pae.donnees.dal;

import java.sql.SQLException;

public interface DalServices {

  void startTransaction();

  void commitTransaction() throws SQLException;

  void rollbackTransaction() throws SQLException;

}
