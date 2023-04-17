package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.TypeDto;
import java.sql.PreparedStatement;
import java.util.List;

public interface TypeDao {

  TypeDto getOne(int id);

  TypeDto getOne(String libelle);

  TypeDto insert(String libelle);

  List<TypeDto> createTypeList(PreparedStatement pr);

  List<TypeDto> getAllTypes();
}
