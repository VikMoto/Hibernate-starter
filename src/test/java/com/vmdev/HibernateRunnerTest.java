package com.vmdev;

import com.vmdev.entity.*;
import com.vmdev.util.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;

class HibernateRunnerTest {


    @Test
    void checkLocaleInfo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 3L);

//            company.getLocales().add(LocaleInfo.of("ua", "Опис українскою"));
//            company.getLocales().add(LocaleInfo.of("en", "English description"));

            System.out.println("company.getLocales() = " + company.getLocales());

//            session.persist(company);


            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToMany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 10L);
            Chat chat = session.get(Chat.class, 1L);

            UserChat userChat = UserChat.builder()
                    .createdAt(Instant.now())
                    .createdBy(user.getUsername())
                    .build();

            userChat.setChat(chat);
            userChat.setUser(user);

            session.persist(userChat);
//            Chat chat = Chat.builder()
//                    .name("Ginger")
//                    .build();
//
//            user.addChat(chat);
//            session.persist(chat);

            System.out.println();

            session.getTransaction().commit();
        }
    }

//    @Test
//    void checkManyToMany() {
//        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
//             Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//
//            User user = session.get(User.class, 10L);
//            user.getChats().clear();
//
////            Chat chat = Chat.builder()
////                    .name("Ginger")
////                    .build();
////
////            user.addChat(chat);
////            session.persist(chat);
//
//            System.out.println();
//
//            session.getTransaction().commit();
//        }
//    }
    @Test
    void checkOneToOne() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = session.get(User.class, 13L);

            User user1 = User.builder()
                    .username("test4@gmail.com")
                    .build();

            Profile profile = Profile.builder()
                    .language("UA")
                    .street("Franko 14")
                    .build();
            profile.setUser(user1);

            session.persist(user1);
//            session.persist(profile);

            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrhanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.getReference(Company.class, 3L);
            company.getUsers().removeIf(user -> user.getId().equals(12L));

            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialisation() {
        Company company = null;

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.get(Company.class, 3L);

            System.out.println();

            session.getTransaction().commit();
        }

        Set<User> users = company.getUsers();
        for (User user : users) {
            System.out.println("user = " + user);
        }
    }


    @Test
    void deleteCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        Company company = session.get(Company.class, 8L);

       session.remove(company);

        System.out.println();

        session.getTransaction().commit();
    }


    @Test
    void addUserToNewCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        Company company = Company.builder()
                .name("Google")
                .build();

        User user = User.builder()
                .username("gora@gmail.com")
                .build();
//       user.setCompany(company);
//       company.getUsers().add(user);

        company.addUser(user);

        session.persist(company);

        System.out.println();

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        Company company = session.get(Company.class, 3);
        System.out.println();

        session.getTransaction().commit();

    }

    @Test
    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement  preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("firstname");
        resultSet.getString("lastname");

        Class<User> aClass = User.class;
        Constructor<User> constructor = aClass.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = aClass.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("ivan@gmail.com")
//                .firstname("Ivan")
//                .lastname("Ivanko")
//                .birthDate(new BirthDay(LocalDate.of(2000, 11, 18)))
                .build();
        String sql = """
                insert
                into
                %s
                (%s)
                values
                (%s)
                """;
        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnotation -> tableAnotation.schema() + "." + tableAnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnName = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.println(sql.formatted(tableName, columnName, columnValues));

        Connection connection = null;
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql.formatted(tableName, columnName, columnValues));

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            preparedStatement.setObject(1,declaredField.get(user));
        }



    }

}