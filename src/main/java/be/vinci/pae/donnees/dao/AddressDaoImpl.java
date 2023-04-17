package be.vinci.pae.donnees.dao;


import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDaoImpl implements AddressDao {

  @Inject
  private BizFactory myBizFactory;

  @Inject
  private DalServicesBackend dal;


  /**
   * Permet d'inserer une adresse dans la DB.
   *
   * @param rue        la rue
   * @param numero     le numero
   * @param boite      la boite
   * @param codePostal le code postal
   * @param ville      la ville
   * @return adresse l'adresse qui a été introduite
   */
  @Override
  public AddressDto insert(String rue, int numero, String boite, int codePostal, String ville) {
    String query =
        "INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville) "
            + "VALUES (DEFAULT, ?, ?, ?, ?, ?)";

    AddressDto adresse = myBizFactory.getAddresseDTO();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, rue);
      pr.setInt(2, numero);
      pr.setString(3, boite);
      pr.setInt(4, codePostal);
      pr.setString(5, ville);
      pr.executeUpdate();

      rs = pr.getGeneratedKeys();
      if (rs.next()) {
        adresse.setIdAddresse(rs.getInt(1));
      }

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode insert");
    }

    return adresse;
  }

  /**
   * Permet de trouver l'id d'une adresse apd de ces informations.
   *
   * @param rue        la rue
   * @param numero     le numero
   * @param boite      la boite
   * @param codePostal le code postal
   * @param ville      la ville
   * @return l'id de l'adresse ou -1 s'il n'existe pas
   */
  @Override
  public int findOne(String rue, int numero, String boite, int codePostal, String ville) {
    String query =
        "SELECT * FROM projetPAE.adresses a WHERE a.rue = ? AND a.numero = ? AND a.boite = ?"
            + " AND a.code_postal = ? AND a.ville = ?";

    int id = -1;
    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setString(1, rue);
      pr.setInt(2, numero);
      pr.setString(3, boite);
      pr.setInt(4, codePostal);
      pr.setString(5, ville);

      rs = pr.executeQuery();

      if (!rs.next()) {
        return id;
      }

      id = rs.getInt(1);

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode findOne");
    }

    return id;
  }

  /**
   * Permet de trouver une adresse sur base l'id.
   *
   * @param id l'id
   * @return l'adresse rechercher
   */
  @Override
  public AddressDto findOne(int id) {
    String query =
        "SELECT a.* FROM projetPAE.adresses a WHERE a.id_adresse = ?";

    AddressDto adresse = myBizFactory.getAddresseDTO();

    ResultSet rs;
    try (PreparedStatement pr = dal.createStatement(query)) {
      pr.setInt(1, id);

      rs = pr.executeQuery();

      if (!rs.next()) {
        return null;
      }

      adresse.setIdAddresse(rs.getInt(1));
      adresse.setRue(rs.getString(2));
      adresse.setNumero(rs.getInt(3));
      adresse.setBoite(rs.getString(4));
      adresse.setCodePostal(rs.getInt(5));
      adresse.setVille(rs.getString(6));

      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Problème de la méthode findOne");
    }

    return adresse;
  }
}
