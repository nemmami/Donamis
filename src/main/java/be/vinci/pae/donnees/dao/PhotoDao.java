package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.PhotoDto;

public interface PhotoDao {

  PhotoDto getPhoto(String nomPhoto);

  PhotoDto getPhoto(int idPhoto);

  PhotoDto insertPhoto(String nomPhoto);

}
