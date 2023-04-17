package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.donnees.dao.ObjectDao;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.BizNotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class MockObjectDao implements ObjectDao {

  @Inject
  private BizFactory myBiz;

  @Override
  public List<ObjectDto> getAllConfirmedObjectsFromUsers(int idUser) {

    List<ObjectDto> listObject = new ArrayList<ObjectDto>();

    if (idUser == 1) {
      for (int i = 0; i < 9; i++) {
        ObjectDto object = myBiz.getObjectDto();
        listObject.add(object);
      }
    }

    return listObject;

  }

  /**
   * Insert un objet.
   *
   * @param idType        le type
   * @param titre         le titre
   * @param description   la description
   * @param photo         la photo
   * @param plageHoraire  la plage horaire
   * @param membreOffreur le membre offreur
   * @return l'object
   */
  public ObjectDto insertObject(int idType, String titre, String description, String photo,
      String plageHoraire, int membreOffreur) {
    if (description == null || description.equals("")) {
      throw new BizException("La description est vide !");
    } else if (titre == null || titre.equals("")) {
      throw new BizException("Le titre est vide !");
    } else if (plageHoraire == null || plageHoraire.equals("")) {
      throw new BizException("La plage horaire est vide !");
    } else if (photo == null || photo.equals("")) {
      throw new BizException("La photo est vide !");
    } else if (titre.equals("Lampe Ikea")) {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      objectDto.setIdType(1);
      objectDto.setTitre("Lampe Ikea");
      objectDto.setDescription("Lampe très belle");
      objectDto.setPhoto("photo.png");
      objectDto.setPlageHoraire("Du lundi au mercredi");
      objectDto.setMembre_offreur(1);
      objectDto.setEtat("Offert");
      objectDto.setNbrInteresse(0);
      return objectDto;
    }
    return null;
  }

  @Override
  public ObjectDto insertObject(int idType, String titre, String description, String plageHoraire,
      int membreOffreur) {
    if (description == null || description.equals("")) {
      throw new BizException("La description est vide !");
    } else if (titre == null || titre.equals("")) {
      throw new BizException("Le titre est vide !");
    } else if (plageHoraire == null || plageHoraire.equals("")) {
      throw new BizException("La plage horaire est vide !");
    } else if (titre.equals("Lampe Ikea")) {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      objectDto.setIdType(1);
      objectDto.setTitre("Lampe Ikea");
      objectDto.setDescription("Lampe très belle");
      objectDto.setPhoto(null);
      objectDto.setPlageHoraire("Du lundi au mercredi");
      objectDto.setMembre_offreur(1);
      objectDto.setEtat("Offert");
      objectDto.setNbrInteresse(0);
      return objectDto;
    }
    return null;
  }

  @Override
  public ObjectDto updateObject(int idObject, String titre, String description,
      String plageHoraire) {
    if (titre == null || titre.equals("")) {
      throw new BizException("Le titre est vide !");
    } else if (description == null || description.equals("")) {
      throw new BizException("La description est vide !");
    } else if (plageHoraire == null || plageHoraire.equals("")) {
      throw new BizException("La plage horaire est vide !");
    } else if (titre.equals("ordinateur asus")) {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      objectDto.setTitre("ordinateur asus");
      objectDto.setDescription("ordinateur tres bon etat");
      objectDto.setPhoto(null);
      objectDto.setPlageHoraire("Du lundi au mercredi");
      return objectDto;
    }

    return null;
  }

  @Override
  public ObjectDto updateObjectWithPhoto(int idObject, String titre, String description,
      String plageHoraire, String photo) {

    if (titre == null || titre.equals("")) {
      throw new BizException("Le titre est vide !");
    } else if (description == null || description.equals("")) {
      throw new BizException("La description est vide !");
    } else if (plageHoraire == null || plageHoraire.equals("")) {
      throw new BizException("La plage horaire est vide !");
    } else if (photo == null || photo.equals("")) {
      throw new BizException("La photo est vide !");
    } else if (titre.equals("ordinateur asus")) {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      objectDto.setTitre("ordinateur asus");
      objectDto.setDescription("ordinateur tres bon etat");
      objectDto.setPhoto("photo.png");
      objectDto.setPlageHoraire("Du lundi au mercredi");
      return objectDto;
    }

    return null;
  }

  /**
   * get an object.
   *
   * @param idObject l'id de l'object
   * @return l'objet
   */
  public ObjectDto getObject(int idObject) {

    if (idObject <= 0) {
      throw new BizException("Informations incorrects");
    }

    if (idObject == 1) {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      objectDto.setIdType(1);
      objectDto.setTitre("Lampe Ikea");
      objectDto.setDescription("Lampe très belle");
      objectDto.setPhoto(null);
      objectDto.setPlageHoraire("Du lundi au mercredi");
      objectDto.setMembre_offreur(1);
      objectDto.setEtat("Offert");
      objectDto.setNbrInteresse(0);
      return objectDto;
    } else if (idObject == 2) {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(2);
      objectDto.setIdType(1);
      objectDto.setTitre("Table Ikea");
      objectDto.setDescription("table très belle");
      objectDto.setPhoto(null);
      objectDto.setPlageHoraire("Du lundi au mercredi");
      objectDto.setMembre_offreur(1);
      objectDto.setEtat("Offert");
      objectDto.setNbrInteresse(0);
      return objectDto;
    } else if (idObject == 3) {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(3);
      objectDto.setEtat("Annulé");
      return objectDto;

    }
    return null;
  }

  @Override
  public List<ObjectDto> createObjectsList(PreparedStatement pr) {
    return null;
  }

  @Override
  public List<ObjectDto> getLastObjects() {
    List<ObjectDto> listObject = new ArrayList<ObjectDto>();

    for (int i = 0; i < 9; i++) {
      ObjectDto object = myBiz.getObjectDto();
      listObject.add(object);
    }

    return listObject;

  }

  @Override
  public List<ObjectDto> getAllObjects() {
    List<ObjectDto> listObject = new ArrayList<ObjectDto>();

    for (int i = 0; i < 9; i++) {
      ObjectDto object = myBiz.getObjectDto();
      listObject.add(object);
    }

    return listObject;

  }

  @Override
  public List<ObjectDto> getAllObjectsOffered() {
    List<ObjectDto> listObject = new ArrayList<ObjectDto>();

    for (int i = 0; i < 9; i++) {
      ObjectDto object = myBiz.getObjectDto();
      object.setEtat("Offert");
      listObject.add(object);
    }

    return listObject;
  }

  @Override
  public List<ObjectDto> getAllObjectsFromUsers(int idUser) {
    List<ObjectDto> listObject = new ArrayList<ObjectDto>();

    if (idUser <= 0) {
      throw new BizException("Informations incorrects");
    }

    if (idUser == 1) {
      for (int i = 0; i < 9; i++) {
        ObjectDto object = myBiz.getObjectDto();
        listObject.add(object);
      }
    }

    return listObject;
  }


  @Override
  public ObjectDto updateNombreInteresses(int idObject) {

    if (idObject < 0) {
      throw new BizException("Informations incorrects");
    } else {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      return objectDto;
    }

  }

  @Override
  public ObjectDto annulerOffre(int idObject) {

    if (idObject <= 0) {
      throw new BizException("Informations incorrects");
    } else {
      ObjectDto objectDto = myBiz.getObjectDto();

      objectDto.setIdObjet(1);
      objectDto.setEtat(" ");

      if (objectDto.getIdObjet() != idObject) {
        throw new BizNotFoundException(" l'objet n'existe pas");
      }

      /*if (!objectDto.getEtat().equals("Offert") && !objectDto.getEtat().equals("Receveur Choisi")
          && !objectDto.getEtat().equals("Interet marqué")) {

        throw new BizException(
            "L'objet doit obligatoirement etre offert ou que le receveur ait été choisi "
                + " pour etre annulé");
      }

       */
      return objectDto;
    }


  }

  @Override
  public ObjectDto setEtatReceveurChoisi(int idObject) {
    return null;
  }

  @Override
  public ObjectDto setEtatDonne(int idObject) {
    return null;
  }

  @Override
  public ObjectDto reposterObjet(int idObject) {
    if (idObject < 0) {
      throw new BizException("Informations incorrects");
    } else {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      return objectDto;
    }
  }

  @Override
  public ObjectDto reposterObjetInteretMarquer(int idObject) {
    if (idObject <= 0) {
      throw new BizException("Informations incorrects");
    } else {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      return objectDto;
    }
  }

  @Override
  public ObjectDto setEtatOffert(int idObject) {
    return null;
  }

  @Override
  public ObjectDto setEtatInteretMarquer(int idObject) {
    if (idObject <= 0) {
      throw new BizException("Informations incorrects");
    } else {
      ObjectDto objectDto = myBiz.getObjectDto();
      objectDto.setIdObjet(1);
      return objectDto;
    }
  }

}
