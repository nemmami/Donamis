package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.ObjectDto;
import java.sql.PreparedStatement;
import java.util.List;

public interface ObjectDao {

  List<ObjectDto> getAllConfirmedObjectsFromUsers(int idUser);

  ObjectDto insertObject(int idType, String titre, String description, String photo,
      String plageHoraire, int membreOffreur);

  ObjectDto insertObject(int idType, String titre, String description,
      String plageHoraire, int membreOffreur);

  ObjectDto updateObject(int idObject, String titre, String description,
      String plageHoraire);

  ObjectDto updateObjectWithPhoto(int idObject, String titre, String description,
      String plageHoraire, String photo);

  ObjectDto getObject(int idObject);

  List<ObjectDto> createObjectsList(PreparedStatement pr);

  List<ObjectDto> getLastObjects();

  List<ObjectDto> getAllObjects();

  List<ObjectDto> getAllObjectsOffered();

  List<ObjectDto> getAllObjectsFromUsers(int idUser);

  ObjectDto annulerOffre(int idObject);

  ObjectDto setEtatReceveurChoisi(int idObject);

  ObjectDto setEtatDonne(int idObject);

  ObjectDto reposterObjet(int idObject);

  ObjectDto reposterObjetInteretMarquer(int idObject);

  ObjectDto setEtatOffert(int idObject);

  ObjectDto setEtatInteretMarquer(int idObject);

  ObjectDto updateNombreInteresses(int idObject);
}
