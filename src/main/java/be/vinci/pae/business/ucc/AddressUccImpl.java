package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.AddressDao;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class AddressUccImpl implements AddressUcc {

  @Inject
  private AddressDao adressDao;

  @Inject
  private DalServices dal;


  /**
   * Permet d'ajouter une adresse en DB.
   *
   * @param rue        la rue
   * @param numero     le numero
   * @param boite      la boite
   * @param codePostal le code postal
   * @param ville      la ville
   * @return adresseDao l'adresse a introduire
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si un des parametres est vide ou null
   */
  @Override
  public AddressDto ajouterAdresse(String rue, int numero, String boite, int codePostal,
      String ville) throws SQLException {

    if (rue == null || ville == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (codePostal <= 0 || numero <= 0 || rue.isBlank() || ville.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    AddressDto adresse;

    try {
      adresse = adressDao.insert(rue, numero, boite, codePostal, ville);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode ajouterAdresse");
    }

    return adresse;
  }

  /**
   * Permet d'avoir l'id apd de ses informations.
   *
   * @param rue        la rue
   * @param numero     le numero
   * @param boite      la boite
   * @param codePostal le code postal
   * @param ville      la ville
   * @return l'id de l'adresse
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si une des informations est vide ou null
   */
  @Override
  public int findAdresse(String rue, int numero, String boite, int codePostal, String ville)
      throws SQLException {

    if (rue == null || boite == null || ville == null) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    if (codePostal <= 0 || numero <= 0 || rue.isBlank() || boite.isBlank() || ville.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    int id;

    try {
      id = adressDao.findOne(rue, numero, boite, codePostal, ville);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode findAddress");
    }

    return id;
  }

  /**
   * Permet d'avoir l'adresse selon l'id.
   *
   * @param id l'id de l'adresse
   * @return l'adresse rechercher
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si l'id est nul
   */
  @Override
  public AddressDto findOne(int id) throws SQLException {

    if (id <= 0) {
      throw new BizException("Certaines informations sont fausses");
    }

    dal.startTransaction();
    AddressDto adresse;

    try {
      adresse = adressDao.findOne(id);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode findOne");
    }

    return adresse;
  }

}
