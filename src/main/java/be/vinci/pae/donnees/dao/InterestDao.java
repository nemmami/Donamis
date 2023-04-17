package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.InterestDto;
import java.sql.PreparedStatement;
import java.util.List;

public interface InterestDao {

  InterestDto insertInterest(int idUser, int idObject, String dateDisponibilite,
      boolean appel);

  InterestDto insertInterest(int idUser, int idObject, boolean appel);

  boolean getInterest(int idUser, int idObject);

  List<InterestDto> getAllInterestForUserObject(int idUser);

  //void setNotifVue(int idInterest);

  List<InterestDto> getObjectInterest(int idObject);

  List<InterestDto> getAllInterestFromUser(int idUser);

  List<InterestDto> createInterestList(PreparedStatement pr);

}
