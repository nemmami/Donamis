package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.business.dto.ChosenInterestDto;
import be.vinci.pae.business.dto.InterestDto;
import be.vinci.pae.business.dto.NotificationDto;
import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.business.dto.UserDto;

public interface BizFactory {

  UserDto getUserDTO();

  AddressDto getAddresseDTO();

  TypeDto getTypeDto();

  ObjectDto getObjectDto();

  InterestDto getInterestDto();

  ChosenInterestDto getChosenInterestDto();

  PhotoDto getPhotoDto();

  NotificationDto getNotificationDto();
}
