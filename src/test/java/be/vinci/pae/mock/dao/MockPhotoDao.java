package be.vinci.pae.mock.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.donnees.dao.PhotoDao;
import be.vinci.pae.exceptions.BizException;
import jakarta.inject.Inject;

public class MockPhotoDao implements PhotoDao {


  @Inject
  private BizFactory myBiz;

  @Override
  public PhotoDto getPhoto(String nomPhoto) {
    if (nomPhoto == null || nomPhoto.isBlank()) {
      throw new BizException("Certaines informations sont fausses");
    } else if (nomPhoto.equals("photo1")) {
      PhotoDto photo = myBiz.getPhotoDto();
      photo.setIdPhoto(1);
      return photo;
    }

    return null;
  }

  @Override
  public PhotoDto getPhoto(int idPhoto) {
    if (idPhoto <= 0) {
      throw new BizException("Certaines informations sont fausses");
    } else if (idPhoto == 1) {
      PhotoDto photo = myBiz.getPhotoDto();
      photo.setIdPhoto(1);
      return photo;
    }
    return null;
  }

  @Override
  public PhotoDto insertPhoto(String nomPhoto) {
    if (nomPhoto == null || nomPhoto.isBlank()) {
      throw new BizException("Certaines informations sont fausses");
    } else if (nomPhoto.equals("nomPhoto")) {
      PhotoDto photo = myBiz.getPhotoDto();
      photo.setIdPhoto(1);
      photo.setNomPhoto("nomPhoto");
      return photo;
    }
    return null;
  }
}
