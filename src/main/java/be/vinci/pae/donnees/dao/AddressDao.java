package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.AddressDto;

public interface AddressDao {

  AddressDto insert(String rue, int numero, String boite, int codePostal, String ville);

  int findOne(String rue, int numero, String boite, int codePostal, String ville);

  AddressDto findOne(int id);

}
