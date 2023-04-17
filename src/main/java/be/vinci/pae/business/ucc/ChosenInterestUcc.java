package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ChosenInterestDto;
import java.sql.SQLException;
import java.util.List;

public interface ChosenInterestUcc {

  ChosenInterestDto insertChosenInterest(int idMembre, int idObject) throws SQLException;

  ChosenInterestDto setEtatTransactionPasVenu(int idObject) throws SQLException;

  ChosenInterestDto setEtatTransactionVenu(int idObject) throws SQLException;

  List<ChosenInterestDto> getChosenInterest(int idObject) throws SQLException;

  //ChosenInterestDto setNotifVue(int idChosenObject) throws SQLException;

  List<ChosenInterestDto> getChosenInterestByMember(int idUser)
      throws SQLException;

  ChosenInterestDto getChosenInterestByObject(int idObject) throws SQLException;

  List<ChosenInterestDto> getAllChosenInterestMember(int idMember)
      throws SQLException;

  List<ChosenInterestDto> getGivenObjects(int idMember)
      throws SQLException;

  List<ChosenInterestDto> getReceivedObjects(int idMember)
      throws SQLException;

  List<ChosenInterestDto> getNeverCameObjects(int idMember) throws SQLException;
}
