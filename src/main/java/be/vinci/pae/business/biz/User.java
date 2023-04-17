package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.UserDto;

public interface User extends UserDto {

  boolean checkMotDePasse(String motDePasse);

  String hashMotDePasse(String motDePasse);
}
