package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjectDto;
import java.sql.SQLException;
import java.util.List;

public interface ObjectUcc {

  ObjectDto insertObject(int idType, String titre, String description, String photo,
      String plageHoraire, int membreOffreur) throws SQLException;

  ObjectDto insertObjectWithoutPhoto(int idType, String titre, String description,
      String plageHoraire, int membreOffreur) throws SQLException;

  ObjectDto updateObject(int idObject, String titre, String description,
      String plageHoraire) throws SQLException;

  ObjectDto updateObjectWithPhoto(int idObject, String titre, String description,
      String plageHoraire, String photo) throws SQLException;

  ObjectDto getObject(int idObject) throws SQLException;

  List<ObjectDto> getLastObjects() throws SQLException;

  List<ObjectDto> getAllObjects() throws SQLException;

  List<ObjectDto> getAllObjectsFromUsers(int idUSer) throws SQLException;

  List<ObjectDto> getAllObjectsOffered() throws SQLException;

  ObjectDto updateNombreInteresses(int idObject) throws SQLException;

  ObjectDto cancelOffer(int idObject) throws SQLException;

  ObjectDto reposterObjetInteretMarquer(int idObject) throws SQLException;

  ObjectDto reposterObjet(int idObject) throws SQLException;

  List<ObjectDto> getAllConfirmedObjectsFromUsers(int idUser) throws SQLException;
}
