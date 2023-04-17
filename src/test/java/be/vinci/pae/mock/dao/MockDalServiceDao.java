package be.vinci.pae.mock.dao;

import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import java.sql.PreparedStatement;

public class MockDalServiceDao implements DalServicesBackend, DalServices {

  @Override
  public void startTransaction() {

  }

  @Override
  public void commitTransaction() {

  }

  @Override
  public void rollbackTransaction() {

  }

  @Override
  public PreparedStatement createStatement(String query) {
    return null;
  }
}
