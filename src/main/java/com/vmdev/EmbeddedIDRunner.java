package com.vmdev;

import com.vmdev.entity.BirthDay;
import com.vmdev.entity.PersonalInfo;
import com.vmdev.entity.User;
import com.vmdev.entity.UserEmbeddedId;
import com.vmdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.internal.log.SubSystemLogging;

import java.time.LocalDate;
@Slf4j
public class EmbeddedIDRunner {
    public static void main(String[] args) {
        UserEmbeddedId user = UserEmbeddedId.builder()
                .username("nikId1@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Nik")
                        .lastname("EmbeddedId")
                        .birthDate(new BirthDay(LocalDate.of(1995, 05, 10)))
                        .build())
                .build();
        log.info("User entity is in transient state, object: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactoryEmbeddedId()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is created, {}", transaction );

                session1.persist(user);
                log.trace("User is in persistent state: {}, session {}", user, session1 );

                session1.getTransaction().commit();
            }
            log.warn("User is detached state: {}, session {}", user, session1 );

            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                PersonalInfo key = PersonalInfo.builder()
                        .firstname("Nik")
                        .lastname("EmbeddedId")
                        .birthDate(new BirthDay(LocalDate.of(1995, 05, 10)))
                        .build();

                UserEmbeddedId freshUser = session2.get(UserEmbeddedId.class, key);
                System.out.println();
                session2.getTransaction().commit();
            }


        }catch (Exception e) {
            log.error("Exception occurred", e);
            throw e;
        }
    }

}
