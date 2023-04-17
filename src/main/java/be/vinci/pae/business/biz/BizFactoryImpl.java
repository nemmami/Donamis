package be.vinci.pae.business.biz;

import be.vinci.pae.business.dto.AddressDto;
import be.vinci.pae.business.dto.ChosenInterestDto;
import be.vinci.pae.business.dto.InterestDto;
import be.vinci.pae.business.dto.NotificationDto;
import be.vinci.pae.business.dto.ObjectDto;
import be.vinci.pae.business.dto.PhotoDto;
import be.vinci.pae.business.dto.TypeDto;
import be.vinci.pae.business.dto.UserDto;

public class BizFactoryImpl implements BizFactory {

  @Override
  public UserDto getUserDTO() {
    return new UserImpl();
  }

  @Override
  public AddressDto getAddresseDTO() {
    return new AddressImpl();
  }

  @Override
  public TypeDto getTypeDto() {
    return new TypeImpl();
  }

  @Override
  public ObjectDto getObjectDto() {
    return new ObjectImpl();
  }

  @Override
  public InterestDto getInterestDto() {
    return new InterestImpl();
  }

  @Override
  public ChosenInterestDto getChosenInterestDto() {
    return new ChosenInterestImpl();
  }

  @Override
  public PhotoDto getPhotoDto() {
    return new PhotoImpl();
  }

  @Override
  public NotificationDto getNotificationDto() {
    return new NotificationImpl();
  }

}
