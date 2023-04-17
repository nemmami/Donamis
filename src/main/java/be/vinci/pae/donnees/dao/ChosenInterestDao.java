package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.ChosenInterestDto;
import java.util.List;

public interface ChosenInterestDao {

  ChosenInterestDto insertChosenInterest(int idMembre, int idObject);

  ChosenInterestDto setEtatTransactionVenu(int idObject);

  ChosenInterestDto setEtatTransactionPasVenu(int idObject);

  List<ChosenInterestDto> getGivenObjects(int idUser);

  List<ChosenInterestDto> getReceivedObjects(int idMember);

  List<ChosenInterestDto> getNeverCameObjects(int idMember);

  //ChosenInterestDto setNotifVue(int idChosenObject);

  List<ChosenInterestDto> getAllChosenInterestMember(int idMember);

  List<ChosenInterestDto> getChosenInterestByMember(int idUser);

  List<ChosenInterestDto> getChosenInterest(int idMember);

  ChosenInterestDto getChosenInterestByObject(int idObject);

}
