package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dao.TypeDao;
import be.vinci.pae.exceptions.BizConflictException;
import be.vinci.pae.exceptions.BizException;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class TypeUccImpl implements TypeUcc {

  @Inject
  private TypeDao typeDao;

  @Inject
  private DalServices dal;

  /**
   * Permet de recuperer un type.
   *
   * @param id l'id du type qu'on veut recuperer
   * @return le type qu'on recherche
   * @throws SQLException si qqchose se passe mal
   * @throws BizException si id est nul
   */
  @Override
  public TypeDto getOne(int id) throws SQLException {

    if (id <= 0) {
      throw new BizException("Certaines informations sont fausse");
    }

    dal.startTransaction();
    TypeDto typeDto;

    try {
      typeDto = typeDao.getOne(id);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getOne d'un type");
    }

    return typeDto;
  }

  /**
   * Permet de recuperer la liste de tous les types.
   *
   * @return la liste de tous les types
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public List<TypeDto> getAllTypes() throws SQLException {
    dal.startTransaction();
    List<TypeDto> list;

    try {
      list = typeDao.getAllTypes();
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la méthode getAllTypes");
    }

    return list;
  }


  /**
   * Permet d'inserer un type en db.
   *
   * @param libelle le libelle du type.
   * @return le type inseré
   * @throws SQLException         si qqchose se passe mal
   * @throws BizException         si l'id est vide ou null
   * @throws BizConflictException s'il existe un type qui possède le même libellé
   */
  @Override
  public TypeDto insert(String libelle) throws SQLException {

    if (libelle == null || libelle.isBlank()) {
      throw new BizException("Il manque une ou plusieurs informations");
    }

    dal.startTransaction();
    TypeDto type;

    try {
      TypeDto typeDto = typeDao.getOne(libelle);
      if (typeDto != null) {
        dal.commitTransaction();
        throw new BizConflictException("ce libelle existe déjà");
      }

      type = typeDao.insert(libelle);
      dal.commitTransaction();
    } catch (SQLException e) {
      dal.rollbackTransaction();
      throw new FatalException("Erreur lors de la methode insert d'un type");
    }

    return type;
  }


}
