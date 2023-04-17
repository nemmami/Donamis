package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.UserDto;
import java.sql.SQLException;
import java.util.List;

public interface UserUcc {

  UserDto seConnecter(String pseudo, String motDePasse) throws SQLException;

  UserDto inscription(String pseudo, String prenom, String nom, String motDePasse, String rue,
      int numero, String boite, int codePostal, String ville) throws SQLException;

  UserDto getOne(String pseudo) throws SQLException;

  UserDto getOne(int id) throws SQLException;

  List<UserDto> getAllUserDeclined() throws SQLException;

  List<UserDto> getAllUserWaiting() throws SQLException;

  List<UserDto> getAllUserConfirmed() throws SQLException;

  UserDto setEtatInscriptionRefusee(String raisonRefus, String pseudo) throws SQLException;

  UserDto setEtatInscriptionConfirmee(String pseudo) throws SQLException;

  UserDto setAdmin(String pseudo) throws SQLException;

  UserDto annulerParticipation(int id) throws SQLException;

  UserDto setNumeroTelephone(int id, String numero) throws SQLException;

  List<UserDto> getAllUsers() throws SQLException;
}

