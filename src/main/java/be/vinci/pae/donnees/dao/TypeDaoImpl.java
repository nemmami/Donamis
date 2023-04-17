package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeDaoImpl implements TypeDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;


  /**
   * Permet de recuperer un type.
   *
   * @param id l'id du type qu'on veut recuperer
   * @return le type qu'on recherche
   */
  @Override
  public TypeDto getOne(int id) {
    String query = "SELECT t.* FROM projetPAE.types t WHERE t.id_type = ?";

    TypeDto type = myBizFactory.getTypeDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, id);
      ResultSet rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      type.setIdType(rs.getInt(1));
      type.setLibelle(rs.getString(2));
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode getOne");
    }

    return type;
  }

  /**
   * Permet de recuperer le libelle d'un type.
   *
   * @param libelle le libelle du type qu'on veut recuperer
   * @return le type qu'on recherche
   */
  @Override
  public TypeDto getOne(String libelle) {
    String query = "SELECT t.* FROM projetPAE.types t WHERE t.libelle = ?";

    TypeDto type = myBizFactory.getTypeDto();

    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, libelle);
      ResultSet rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      type.setIdType(rs.getInt(1));
      type.setLibelle(rs.getString(2));
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode getOne");
    }

    return type;
  }

  /**
   * Permet d'inserer un type.
   *
   * @param libelle le libelle du type qu'on veut inserer
   * @return type
   */
  @Override
  public TypeDto insert(String libelle) {
    String query = "INSERT INTO projetPAE.types(id_type, libelle) VALUES(DEFAULT, ?)";

    TypeDto type = myBizFactory.getTypeDto();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, libelle);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        type.setIdType(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Probleme avec la methode insert");
    }
    return type;
  }

  /**
   * Permet de recuperer la liste de tous les types.
   *
   * @return la liste de tous ses types
   */
  @Override
  public List<TypeDto> getAllTypes() {
    String query = "SELECT t.* FROM projetPAE.types t";
    List<TypeDto> listTypes;

    try (PreparedStatement pr = dal.createStatement(query)) {
      listTypes = createTypeList(pr);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Probleme avec la méthode getALlTypes");
    }

    return listTypes;
  }

  /**
   * Permet de creer une liste de types.
   *
   * @param pr le preparedstatement
   * @return la liste de type
   */
  @Override
  public List<TypeDto> createTypeList(PreparedStatement pr) {
    List<TypeDto> types = new ArrayList<>();

    try {

      ResultSet rs = pr.executeQuery();
      while (rs.next()) {
        TypeDto type = myBizFactory.getTypeDto();
        fillType(rs, type);
        types.add(type);
      }

      rs.close();
      return types;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("problème dans la methode createObjectListTypes");
    }

  }

  /**
   * Permet de remplir le typeDTO de tous ses attributs.
   *
   * @param rs      le resultset
   * @param typeDto le typeDto a remplir
   * @throws SQLException si qqchose se passe mal
   */
  private void fillType(ResultSet rs, TypeDto typeDto) throws SQLException {
    typeDto.setIdType(rs.getInt(1));
    typeDto.setLibelle(rs.getString(2));
  }


}
