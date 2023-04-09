package com.vmdev;

import com.vmdev.entity.*;
import com.vmdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;

@Slf4j
public class ManyToOneRunner {
    public static void main(String[] args) {
//        Company company = Company.builder()
//                .name("Sa2")
//                .build();


        User user = User.builder()
                .username("nikId7@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Rick")
                        .lastname("EmbeddedId")
                        .birthDate(new BirthDay(LocalDate.of(1995, 05, 10)))
                        .build())
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();

                Company company2 = session1.get(Company.class, 3);
                company2.addUser(user);

//                session1.persist(company);
                session1.persist(company2);
//                Object object = Hibernate.unproxy(company1);

                session1.getTransaction().commit();
             }



        }catch (Exception e) {
            log.error("Exception occurred", e);
            throw e;
        }
    }
}
