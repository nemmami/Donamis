package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.TypeDto;
import java.sql.SQLException;
import java.util.List;

public interface TypeUcc {

  TypeDto getOne(int id) throws SQLException;

  TypeDto insert(String libelle) throws SQLException;

  List<TypeDto> getAllTypes() throws SQLException;

}
