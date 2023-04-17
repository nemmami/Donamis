package be.vinci.pae.donnees.dao;

import be.vinci.pae.business.dto.UserDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

  UserDto getOne(String pseudo);

  UserDto getOne(int id);

  UserDto inscription(String pseudo, String prenom, String nom, String motDePasse,
      int idAdresse) throws SQLException;

  boolean pseudoExiste(String pseudo) throws SQLException;

  List<UserDto> findAllUsers();

  List<UserDto> createListUser(PreparedStatement pr);

  List<UserDto> findAllUserWaiting();

  List<UserDto> findAllUserDeclined();

  void setEtatInscriptionRefusee(String raisonRefus, String pseudo);

  void setEtatInscriptionConfirmee(String pseudo);

  void setAdmin(String pseudo);


  void annulerParticipation(int id);

  void setNumeroTelephone(int id, String numero);

  List<UserDto> findAllUserConfirmed();
}
