package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.donnees.dao.TypeDao;
import be.vinci.pae.exceptions.BizException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class MockTypesDao implements TypeDao {

  @Inject
  private BizFactory myBiz;

  @Override
  public TypeDto getOne(int id) {
    if (id == 1) {
      TypeDto typeDto = myBiz.getTypeDto();
      typeDto.setIdType(1);
      typeDto.setLibelle("testLibelle");
      return typeDto;
    }
    return null;
  }

  @Override
  public TypeDto getOne(String libelle) {
    if (libelle.equals("type1")) {
      TypeDto typeDto = myBiz.getTypeDto();
      typeDto.setIdType(2);
      typeDto.setLibelle("type1");
      return typeDto;
    }
    return null;
  }

  @Override
  public TypeDto insert(String libelle) {
    if (libelle.equals("")) {
      throw new BizException("Le Libelle est vide !");
    } else if (libelle.equals("testLibelle")) {
      TypeDto typeDto = myBiz.getTypeDto();
      typeDto.setIdType(1);
      typeDto.setLibelle(libelle);
      return typeDto;
    }
    return null;
  }

  @Override
  public List<TypeDto> createTypeList(PreparedStatement pr) {

    return null;
  }

  @Override
  public List<TypeDto> getAllTypes() {

    List<TypeDto> listType = new ArrayList<TypeDto>();

    for (int i = 0; i < 5; i++) {
      TypeDto type = myBiz.getTypeDto();
      listType.add(type);
    }
    return listType;
  }
}