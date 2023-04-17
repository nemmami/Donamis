package be.vinci.pae.donnees.dal;

import be.vinci.pae.exceptions.CustomException;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class DalServicesImpl implements DalServicesBackend, DalServices {

  static {
    Config.load("prod.properties");
  }

  private final String dbFilePath = Config.getProperty("UrlConnection");
  private final String dbName = Config.getProperty("UserConnection");
  private final String dbPassword = Config.getProperty("PasswordConnection");
  private BasicDataSource dataSource;
  private ThreadLocal<Connection> threadLocalConnections;

  /**
   * Crée la connexion à la DB.
   */
  public DalServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new FatalException("Driver PostgreSQL manquant !");
    }

    threadLocalConnections = new ThreadLocal<>();

    dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUsername(dbName);
    dataSource.setUrl(dbFilePath);
    dataSource.setPassword(dbPassword);
  }

  /**
   * Methode qui se connect a la DB pour un query.
   *
   * @param query query reçu
   * @return
   */
  @Override
  public PreparedStatement createStatement(String query) {
    PreparedStatement pr;
    try {
      pr = threadLocalConnections.get().prepareStatement(query);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new CustomException();
    }
    return pr;
  }

  /**
   * Pemret de start un transaction et d'ouvrir une connexion à la db.
   */
  @Override
  public void startTransaction() {
    try {
      if (threadLocalConnections.get() != null) {
        throw new FatalException("Probleme startTransaction");
      }
      threadLocalConnections.set(dataSource.getConnection());
      threadLocalConnections.get().setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Probleme startTransaction");
    }
  }

  /**
   * Permet de commit toutes les requetes effectuées et permet de fermer la connexion à la db.
   *
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public void commitTransaction() throws SQLException {
    try {
      if (threadLocalConnections.get() != null) {
        threadLocalConnections.get().commit();
      }
    } catch (SQLException e) {
      throw new FatalException("Probleme commitTransaction");
    } finally {
      if (threadLocalConnections.get() != null) {

        try {
          threadLocalConnections.get().close();
        } catch (SQLException e) {
          throw new FatalException("Probleme commitTranscation");
        } finally {
          if (threadLocalConnections.get() != null) {
            threadLocalConnections.set(null);
          }
        }

      }
    }
  }

  /**
   * Permet d'annulé toutes les requetes effectuées et de fermer une conenxion à la db.
   *
   * @throws SQLException si qqchose se passe mal
   */
  @Override
  public void rollbackTransaction() throws SQLException {
    try {
      if (threadLocalConnections.get() != null) {
        threadLocalConnections.get().rollback();
      }
    } catch (SQLException e) {
      throw new FatalException("Problème rollbackTransaction");
    } finally {

      if (threadLocalConnections.get() != null) {
        try {
          threadLocalConnections.get().close();
        } catch (SQLException e) {
          throw new FatalException("Probleme rollbackTransaction");
        } finally {
          if (threadLocalConnections.get() != null) {
            threadLocalConnections.set(null);
          }
        }
      }

    }
  }

}
