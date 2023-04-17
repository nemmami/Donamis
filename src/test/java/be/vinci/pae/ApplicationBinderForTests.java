package be.vinci.pae;

import be.vinci.pae.business.biz.BizFactory;
import be.vinci.pae.business.biz.BizFactoryImpl;
import be.vinci.pae.business.ucc.AddressUcc;
import be.vinci.pae.business.ucc.AddressUccImpl;
import be.vinci.pae.business.ucc.ChosenInterestUcc;
import be.vinci.pae.business.ucc.ChosenInterestUccImpl;
import be.vinci.pae.business.ucc.InterestUcc;
import be.vinci.pae.business.ucc.InterestUccImpl;
import be.vinci.pae.business.ucc.NotificationUcc;
import be.vinci.pae.business.ucc.NotificationUccImpl;
import be.vinci.pae.business.ucc.ObjectUcc;
import be.vinci.pae.business.ucc.ObjectUccImpl;
import be.vinci.pae.business.ucc.PhotoUcc;
import be.vinci.pae.business.ucc.PhotoUccImpl;
import be.vinci.pae.business.ucc.TypeUcc;
import be.vinci.pae.business.ucc.TypeUccImpl;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.business.ucc.UserUccImpl;
import be.vinci.pae.donnees.dal.DalServices;
import be.vinci.pae.donnees.dal.DalServicesBackend;
import be.vinci.pae.donnees.dao.AddressDao;
import be.vinci.pae.donnees.dao.ChosenInterestDao;
import be.vinci.pae.donnees.dao.InterestDao;
import be.vinci.pae.donnees.dao.NotificationDao;
import be.vinci.pae.donnees.dao.ObjectDao;
import be.vinci.pae.donnees.dao.PhotoDao;
import be.vinci.pae.donnees.dao.TypeDao;
import be.vinci.pae.donnees.dao.UserDao;
import be.vinci.pae.mock.dao.MockAddressDao;
import be.vinci.pae.mock.dao.MockChosenInterestDao;
import be.vinci.pae.mock.dao.MockDalServiceDao;
import be.vinci.pae.mock.dao.MockInterestDao;
import be.vinci.pae.mock.dao.MockNotificationDao;
import be.vinci.pae.mock.dao.MockObjectDao;
import be.vinci.pae.mock.dao.MockPhotoDao;
import be.vinci.pae.mock.dao.MockTypesDao;
import be.vinci.pae.mock.dao.MockUserDao;
import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinderForTests extends AbstractBinder {

  /**
   * Configure the binder.
   */
  public void configure() {
    bind(BizFactoryImpl.class).to(BizFactory.class).in(Singleton.class);

    bind(MockUserDao.class).to(UserDao.class).in(Singleton.class);
    bind(UserUccImpl.class).to(UserUcc.class).in(Singleton.class);

    bind(MockObjectDao.class).to(ObjectDao.class).in(Singleton.class);
    bind(ObjectUccImpl.class).to(ObjectUcc.class).in(Singleton.class);

    bind(MockAddressDao.class).to(AddressDao.class).in(Singleton.class);
    bind(AddressUccImpl.class).to(AddressUcc.class).in(Singleton.class);

    bind(MockTypesDao.class).to(TypeDao.class).in(Singleton.class);
    bind(TypeUccImpl.class).to(TypeUcc.class).in(Singleton.class);

    bind(MockInterestDao.class).to(InterestDao.class).in(Singleton.class);
    bind(InterestUccImpl.class).to(InterestUcc.class).in(Singleton.class);

    bind(MockNotificationDao.class).to(NotificationDao.class).in(Singleton.class);
    bind(NotificationUccImpl.class).to(NotificationUcc.class).in(Singleton.class);

    bind(MockChosenInterestDao.class).to(ChosenInterestDao.class).in(Singleton.class);
    bind(ChosenInterestUccImpl.class).to(ChosenInterestUcc.class).in(Singleton.class);

    bind(MockPhotoDao.class).to(PhotoDao.class).in(Singleton.class);
    bind(PhotoUccImpl.class).to(PhotoUcc.class).in(Singleton.class);

    bind(MockDalServiceDao.class).to(DalServices.class).to(DalServicesBackend.class)
        .in(Singleton.class);


  }
}
