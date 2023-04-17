package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.ChosenInterestDto;
import be.vinci.pae.donnees.dao.ChosenInterestDao;
import be.vinci.pae.exceptions.BizConflictException;
import be.vinci.pae.exceptions.BizException;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MockChosenInterestDao implements ChosenInterestDao {

  @Inject
  private BizFactory myBiz;


  @Override
  public ChosenInterestDto insertChosenInterest(int idMembre, int idObject) {

    if (idMembre <= 0 || idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");
    } else {
      List<ChosenInterestDto> chosen;

      ChosenInterestDto chosenInterestDto = myBiz.getChosenInterestDto();
      chosen = getChosenInterest(idObject);
      for (ChosenInterestDto c : chosen) {
        if (c.getIdMember() == idMembre && c.getEtatTransaction().equals("en attente")) {
          throw new BizConflictException("Vous avez déja essayer de choisir cette personne");
        }
      }
      chosenInterestDto.setIdChosenInterest(1);
      chosenInterestDto.setIdMember(1);
      chosenInterestDto.setIdObject(1);
      return chosenInterestDto;
    }

  }

  @Override
  public ChosenInterestDto setEtatTransactionVenu(int idObject) {
    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");

    } else {
      ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
      choseInterest.setIdChosenInterest(1);
      choseInterest.setIdObject(1);
      return choseInterest;
    }
  }

  @Override
  public ChosenInterestDto setEtatTransactionPasVenu(int idObject) {
    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");

    } else {
      ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
      choseInterest.setIdChosenInterest(1);
      choseInterest.setIdObject(1);
      return choseInterest;
    }
  }

  @Override
  public List<ChosenInterestDto> getGivenObjects(int idUser) {
    if (idUser <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    List<ChosenInterestDto> listChosenInterest = new ArrayList<ChosenInterestDto>();

    if (idUser == 1) {
      for (int i = 0; i < 3; i++) {
        ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
        listChosenInterest.add(choseInterest);
      }
    }

    return listChosenInterest;
  }


  @Override
  public List<ChosenInterestDto> getReceivedObjects(int idMember) {
    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    List<ChosenInterestDto> listChosenInterest = new ArrayList<ChosenInterestDto>();

    if (idMember == 1) {
      for (int i = 0; i < 3; i++) {
        ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
        listChosenInterest.add(choseInterest);
      }
    }

    return listChosenInterest;
  }


  @Override
  public List<ChosenInterestDto> getNeverCameObjects(int idMember) {

    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }
    List<ChosenInterestDto> listChosenInterest = new ArrayList<ChosenInterestDto>();

    if (idMember == 1) {
      for (int i = 0; i < 3; i++) {
        ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
        listChosenInterest.add(choseInterest);
      }
    }

    return listChosenInterest;
  }

  @Override
  public List<ChosenInterestDto> getAllChosenInterestMember(int idMember) {

    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }
    List<ChosenInterestDto> listChosenInterest = new ArrayList<ChosenInterestDto>();

    if (idMember == 1) {
      for (int i = 0; i < 3; i++) {
        ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
        listChosenInterest.add(choseInterest);
      }
    }

    return listChosenInterest;
  }

  @Override
  public List<ChosenInterestDto> getChosenInterestByMember(int idUser) {

    if (idUser <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }
    List<ChosenInterestDto> listChosenInterest = new ArrayList<ChosenInterestDto>();

    if (idUser == 1) {
      for (int i = 0; i < 3; i++) {
        ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
        listChosenInterest.add(choseInterest);
      }
    }

    return listChosenInterest;
  }

  @Override
  public List<ChosenInterestDto> getChosenInterest(int idMember) {
    if (idMember <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    List<ChosenInterestDto> listChosenInterest = new ArrayList<ChosenInterestDto>();

    if (idMember == 1) {
      for (int i = 0; i < 3; i++) {
        ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
        listChosenInterest.add(choseInterest);
      }
    }

    return listChosenInterest;
  }

  @Override
  public ChosenInterestDto getChosenInterestByObject(int idObject) {

    if (idObject <= 0) {
      throw new BizException("Certaines informations sont fausses");

    } else {
      ChosenInterestDto choseInterest = myBiz.getChosenInterestDto();
      choseInterest.setIdChosenInterest(1);
      choseInterest.setIdObject(1);
      return choseInterest;
    }

  }
}
