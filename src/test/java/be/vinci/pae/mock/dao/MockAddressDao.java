package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.donnees.dao.AddressDao;
import be.vinci.pae.exceptions.BizException;
import jakarta.inject.Inject;

public class MockAddressDao implements AddressDao {

  @Inject
  private BizFactory myBiz;

  @Override
  public AddressDto insert(String rue, int numero, String boite, int codePostal, String ville) {
    if (rue == null || rue.equals("")) {
      throw new BizException("La rue est vide !");
    } else if (numero <= 0) {
      throw new BizException("Le numero ne peut pas etre nul");
    } else if (codePostal <= 0) {
      throw new BizException("Le code postal ne peut pas etre nul");
    } else if (ville == null || ville.equals("")) {
      throw new BizException("La ville est vide !");
    } else if (rue.equals("Rue du test")) {
      AddressDto addressDto = myBiz.getAddresseDTO();
      addressDto.setIdAddresse(1);
      addressDto.setRue("Rue du test");
      addressDto.setNumero(1);
      addressDto.setBoite("");
      addressDto.setCodePostal(1080);
      addressDto.setVille("Ville du test");
      return addressDto;
    }

    return null;
  }

  @Override
  public int findOne(String rue, int numero, String boite, int codePostal, String ville) {

    if (rue == null || rue == " ") {
      throw new BizException("Il manque une ou plusieurs informations");

    } else if (numero < 0) {
      throw new BizException("Il manque une ou plusieurs informations");

    } else if (boite == null || boite == "") {
      throw new BizException("Il manque une ou plusieurs informations");

    } else if (codePostal < 0) {
      throw new BizException("Il manque une ou plusieurs informations");
    } else if (ville == null || ville == "") {
      throw new BizException("Il manque une ou plusieurs informations");
    } else {
      AddressDto addressDto = myBiz.getAddresseDTO();
      addressDto.setIdAddresse(1);
      addressDto.setRue("Rue du test");
      addressDto.setNumero(1);
      addressDto.setBoite("");
      addressDto.setCodePostal(1080);
      addressDto.setVille("Ville du test");
      return addressDto.getIdAddresse();
    }
  }

  @Override
  public AddressDto findOne(int id) {

    if (id < 0) {
      throw new BizException("Informations incorrects");
    } else {
      AddressDto addressDto = myBiz.getAddresseDTO();
      addressDto.setIdAddresse(1);
      return addressDto;
    }
  }
}
