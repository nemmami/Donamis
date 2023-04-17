package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.InterestDto;
import be.vinci.pae.donnees.dao.InterestDao;
import be.vinci.pae.exceptions.BizException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class MockInterestDao implements InterestDao {

  @Inject
  private BizFactory myBiz;

  @Override
  public InterestDto insertInterest(int idUser, int idObject, String dateDisponibilite,
      boolean appel) {

    if (idUser <= 0 || idObject <= 0 || dateDisponibilite.isBlank() || dateDisponibilite == null) {
      throw new BizException("Certaines informations sont fausses");
    } else {
      InterestDto interestDto = myBiz.getInterestDto();
      interestDto.setIdInterest(1);
      interestDto.setObject(2);
      interestDto.setUser(2);
      interestDto.setDateDisponibilite("Samedi a 18h");
      interestDto.setAppel(true);
      return interestDto;
    }
  }

  @Override
  public InterestDto insertInterest(int idUser, int idObject, boolean appel) {

    if (idUser <= 0 || idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    } else {
      InterestDto interestDto = myBiz.getInterestDto();
      interestDto.setIdInterest(1);
      interestDto.setObject(1);
      interestDto.setUser(1);
      interestDto.setDateDisponibilite("Samedi a 18h");
      interestDto.setAppel(true);
      return interestDto;
    }
  }

  @Override
  public boolean getInterest(int idUser, int idObject) {
    if (idUser == 1 && idObject == 2) {
      return true;
    }
    return false;
  }

  @Override
  public List<InterestDto> getAllInterestForUserObject(int idUser) {

    List<InterestDto> listInterest = new ArrayList<InterestDto>();

    if (idUser == 1) {
      for (int i = 0; i < 3; i++) {
        InterestDto interest = myBiz.getInterestDto();
        listInterest.add(interest);
      }
    }

    return listInterest;
  }

  @Override
  public List<InterestDto> getObjectInterest(int idObject) {

    List<InterestDto> listInterest = new ArrayList<InterestDto>();

    if (idObject == 1) {
      for (int i = 0; i < 3; i++) {
        InterestDto interest = myBiz.getInterestDto();
        listInterest.add(interest);
      }
    }

    return listInterest;
  }

  @Override
  public List<InterestDto> getAllInterestFromUser(int idUser) {
    List<InterestDto> listInterest = new ArrayList<InterestDto>();

    if (idUser == 1) {
      for (int i = 0; i < 3; i++) {
        InterestDto interest = myBiz.getInterestDto();
        listInterest.add(interest);
      }
    }

    return listInterest;
  }


  @Override
  public List<InterestDto> createInterestList(PreparedStatement pr) {
    return null;
  }
}