package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.InterestDto;
import java.sql.SQLException;
import java.util.List;

public interface InterestUcc {

  InterestDto insertInterest(int idUser, int idObject, String dateDisponibilite,
      boolean appel, String numero) throws SQLException;

  InterestDto insertInterest(int idUser, int idObject, String dateDisponibilite, boolean appel)
      throws SQLException;

  List<InterestDto> getObjectInterest(int idObject) throws SQLException;

  List<InterestDto> getAllInterestFromUser(int idUser) throws SQLException;

  //void setNotifVue(int idInterest) throws SQLException;

  List<InterestDto> getAllInterestForUserObject(int idUser)
      throws SQLException;

}
