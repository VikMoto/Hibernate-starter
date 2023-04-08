package com.vmdev;

import com.vmdev.entity.BirthDay;
import com.vmdev.entity.Role;
import com.vmdev.entity.User;
import com.vmdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
@Slf4j
public class EntityLifecycleRunner {

//    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        User user = User.builder()
                .username("test3Session2@gmail.com")
//                .firstname("Nolan") //change in user fields personal info
//                .lastname("Session")
                .build();
        log.info("User entity is in transient state, object: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is created, {}", transaction );

                session1.persist(user);
                log.trace("User is in persistent state: {}, session {}", user, session1 );

                session1.getTransaction().commit();
            }
            log.warn("User is detached state: {}, session {}", user, session1 );

//            try (Session session2 = sessionFactory.openSession()) {
//                session2.beginTransaction();
//
//                User freshUser = session2.get(User.class, user.getUsername());
//
//                user.setFirstname(freshUser.getFirstname());
//                user.setLastname(freshUser.getLastname());
//                user.setLastname("Fassion");
//                session2.merge( user);
//
//                session2.getTransaction().commit();
//            }


        }catch (Exception e) {
            log.error("Exception occurred", e);
            throw e;
        }
    }

}
