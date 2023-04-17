package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoDaoImpl implements PhotoDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;


  /**
   * Permet de recuperer une photo.
   *
   * @param nomPhoto le nom de la photo
   * @return la photo qu'on recherche
   */
  @Override
  public PhotoDto getPhoto(String nomPhoto) {
    String query = "SELECT p.* FROM projetPAE.photos p WHERE p.nom_photo = ?";

    PhotoDto photoDto = myBizFactory.getPhotoDto();
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, nomPhoto);
      ResultSet rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      photoDto.setNomPhoto(rs.getString(1));
      photoDto.setIdPhoto(rs.getInt(2));

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode getPhoto");
    }
    return photoDto;
  }

  /**
   * Permet de recuperer une photo.
   *
   * @param idPhoto l'id de la photo
   * @return la photo qu'on recherche
   */
  @Override
  public PhotoDto getPhoto(int idPhoto) {
    String query = "SELECT p.* FROM projetPAE.photos p WHERE p.id_photo = ?";

    PhotoDto photoDto = myBizFactory.getPhotoDto();
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, idPhoto);
      ResultSet rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      photoDto.setNomPhoto(rs.getString(1));
      photoDto.setIdPhoto(rs.getInt(2));

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode getPhoto");
    }
    return photoDto;
  }

  /**
   * Permet d'inserer une photo dans la DB.
   *
   * @param nomPhoto le nom de la photo
   * @return la photo inserer
   */
  @Override
  public PhotoDto insertPhoto(String nomPhoto) {
    String query = "INSERT INTO projetPAE.photos(nom_photo, id_photo) VALUES(?, DEFAULT)";

    PhotoDto photoDto = myBizFactory.getPhotoDto();
    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {

      pr.setString(1, nomPhoto);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        photoDto.setIdPhoto(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insert Photo");
    }
    return photoDto;
  }
}
