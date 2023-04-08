package com.vmdev;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vmdev.convertor.BirthDateConvertor;
import com.vmdev.entity.BirthDay;
import com.vmdev.entity.Role;
import com.vmdev.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.SqlTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;
import java.sql.SQLType;
import java.time.LocalDate;


public class HibernateRunner {
    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {


//        BlockingDeque<Connection> pool = null;
//        Connection connection = pool.take();
//        SessionFactory

//        Connection connection = DriverManager.getConnection("db.url",
//                "db.username", "db.password");
//        Session
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(SqlTypes.class);
        configuration.addAttributeConverter(new BirthDateConvertor());
//        configuration.registerTypeOverride(SqlTypes.class);
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy())

        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            User user = User.builder()
                    .username("Dads232n@gmail.com")
//                 .firstname("Nolan") //change in user fields personal info
//                 .lastname("Session")
//                    .birthDate(new BirthDay(LocalDate.of(2000, 11, 18)))
                    .info("""
                            {
                            "name": "Fix",
                            "id": 95
                            }
                            """)
                    .role(Role.ADMIN)
                    .build();

            log.info("User entity is in transient state, object: {}", user);

            session.persist(user);

            User user1 = session.get(User.class, "iva2n@gmail.com");
            User user2 = session.get(User.class, "iva2n@gmail.com");

//            user1.setLastname("Petrenko32");
            session.flush();
//            session.evict(user1); // delete entity from persistenceContext (first Level Cash)
//            session.clear(); //clear all cash
//            session.close(); //also clear all cash after close of session
//            session.delete(user);
            session.getTransaction().commit();



        }
    }
}
