package com.vmdev.util;

import com.vmdev.convertor.BirthDateConvertor;
import com.vmdev.entity.Company;
import com.vmdev.entity.User;
import com.vmdev.entity.UserEmbeddedId;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.SqlTypes;
@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(SqlTypes.class);
        configuration.addAttributeConverter(new BirthDateConvertor());
        configuration.configure();
        return configuration.buildSessionFactory();
    }
    public static SessionFactory buildSessionFactoryEmbeddedId() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserEmbeddedId.class);
        configuration.addAnnotatedClass(SqlTypes.class);
        configuration.addAttributeConverter(new BirthDateConvertor());
        configuration.configure();
        return configuration.buildSessionFactory();
    }
}
