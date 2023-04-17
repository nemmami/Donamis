package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.PhotoDao;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class PhotoUccImpl implements PhotoUcc {

  @Inject
  private PhotoDao photoDao;

  @Inject
  private DalServices dal;

  /**
   * Permet de recuperer une photo.
   *
   * @param nomPhoto l'id de la photo
   * @return la photo qu'on recherche
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idPhoto est nul
   */
  @Override
  public PhotoDto getPhoto(String nomPhoto) throws SQLException {

    if (nomPhoto == null || nomPhoto.isBlank()) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    PhotoDto myPhoto;

    try {
      myPhoto = photoDao.getPhoto(nomPhoto);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getPhoto");
    }

    return myPhoto;
  }

  /**
   * Permet de recuperer une photo.
   *
   * @param idPhoto l'id de la photo
   * @return la photo qu'on recherche
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si idPhoto est nul
   */
  @Override
  public PhotoDto getPhoto(int idPhoto) throws SQLException {

    if (idPhoto <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    PhotoDto myPhoto;

    try {
      myPhoto = photoDao.getPhoto(idPhoto);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getPhoto");
    }

    return myPhoto;
  }

  /**
   * Permet d'inserer une photo dans la DB.
   *
   * @param nomPhoto le nom de la photo
   * @return la photo inserer
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si nomPhoto est vide ou null
   */
  @Override
  public PhotoDto insertPhoto(String nomPhoto) throws SQLException {

    if (nomPhoto == null || nomPhoto.isBlank()) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    PhotoDto myPhoto;

    try {
      photoDao.insertPhoto(nomPhoto);
      myPhoto = photoDao.getPhoto(nomPhoto);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode insertPhoto");
    }

    return myPhoto;
  }

}
