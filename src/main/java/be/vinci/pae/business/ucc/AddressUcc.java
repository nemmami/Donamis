package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.AddressDto;
import java.sql.SQLException;

public interface AddressUcc {

  AddressDto ajouterAdresse(String rue, int numero, String boite, int codePostal, String ville)
      throws SQLException;

  int findAdresse(String rue, int numero, String boite, int codePostal, String ville)
      throws SQLException;

  AddressDto findOne(int id) throws SQLException;
}
