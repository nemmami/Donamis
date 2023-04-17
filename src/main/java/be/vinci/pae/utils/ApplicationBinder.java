package be.vinci.pae.utils;

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
import be.vinci.pae.donnees.dal.DalServicesImpl;
import be.vinci.pae.donnees.dao.AddressDao;
import be.vinci.pae.donnees.dao.AddressDaoImpl;
import be.vinci.pae.donnees.dao.ChosenInterestDao;
import be.vinci.pae.donnees.dao.ChosenInterestDaoImpl;
import be.vinci.pae.donnees.dao.InterestDao;
import be.vinci.pae.donnees.dao.InterestDaoImpl;
import be.vinci.pae.donnees.dao.NotificationDao;
import be.vinci.pae.donnees.dao.NotificationDaoImpl;
import be.vinci.pae.donnees.dao.ObjectDao;
import be.vinci.pae.donnees.dao.ObjectDaoImpl;
import be.vinci.pae.donnees.dao.PhotoDao;
import be.vinci.pae.donnees.dao.PhotoDaoImpl;
import be.vinci.pae.donnees.dao.TypeDao;
import be.vinci.pae.donnees.dao.TypeDaoImpl;
import be.vinci.pae.donnees.dao.UserDao;
import be.vinci.pae.donnees.dao.UserDaoImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(BizFactoryImpl.class).to(BizFactory.class).in(Singleton.class);

    bind(UserDaoImpl.class).to(UserDao.class).in(Singleton.class);
    bind(UserUccImpl.class).to(UserUcc.class).in(Singleton.class);

    bind(AddressDaoImpl.class).to(AddressDao.class).in(Singleton.class);
    bind(AddressUccImpl.class).to(AddressUcc.class).in(Singleton.class);

    bind(DalServicesImpl.class).to(DalServicesBackend.class).to(DalServices.class)
        .in(Singleton.class);
    bind(LoggingImpl.class).to(Logging.class).in(Singleton.class);

    bind(TypeDaoImpl.class).to(TypeDao.class).in(Singleton.class);
    bind(TypeUccImpl.class).to(TypeUcc.class).in(Singleton.class);

    bind(ObjectDaoImpl.class).to(ObjectDao.class).in(Singleton.class);
    bind(ObjectUccImpl.class).to(ObjectUcc.class).in(Singleton.class);

    bind(InterestDaoImpl.class).to(InterestDao.class).in(Singleton.class);
    bind(InterestUccImpl.class).to(InterestUcc.class).in(Singleton.class);

    bind(ChosenInterestDaoImpl.class).to(ChosenInterestDao.class).in(Singleton.class);
    bind(ChosenInterestUccImpl.class).to(ChosenInterestUcc.class).in(Singleton.class);

    bind(NotificationDaoImpl.class).to(NotificationDao.class).in(Singleton.class);
    bind(NotificationUccImpl.class).to(NotificationUcc.class).in(Singleton.class);

    bind(PhotoDaoImpl.class).to(PhotoDao.class).in(Singleton.class);
    bind(PhotoUccImpl.class).to(PhotoUcc.class).in(Singleton.class);
  }
}

