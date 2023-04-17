package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDto;
import java.sql.SQLException;

public interface PhotoUcc {

  PhotoDto getPhoto(String nomPhoto) throws SQLException;

  PhotoDto getPhoto(int idPhoto) throws SQLException;
  
  PhotoDto insertPhoto(String nomPhoto) throws SQLException;


}
