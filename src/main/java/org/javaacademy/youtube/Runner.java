package org.javaacademy.youtube;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.javaacademy.youtube.entity.Comment;
import org.javaacademy.youtube.entity.User;
import org.javaacademy.youtube.entity.Video;

import java.util.List;
import java.util.Properties;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", "jdbc:postgresql://192.168.2.10:5432/youtube");
        properties.put("hibernate.connection.username", "postgres");
        properties.put("hibernate.connection.password", "sa");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        @Cleanup SessionFactory sessionFactory = new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Video.class)
                .addAnnotatedClass(Comment.class)
                .buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        createUser(session);
        session.clear();
        printComment(session);
    }

    private static void createUser(Session session) {
        session.beginTransaction();
        User john= new User("John");
        User rick = new User("Rick");
        Video johnVideo1 = new Video("Мое первое интервью", "Описание - 1", john);
        Video johnVideo2 = new Video("Мое второе интервью", "Описание - 2", john);
        Comment commentRick = new Comment(johnVideo1, rick, "классное интервью");
        session.persist(john);
        session.persist(rick);
        session.persist(johnVideo1);
        session.persist(johnVideo2);
        session.persist(commentRick);
        session.getTransaction().commit();
    }

    private static void printComment(Session session) {
        List<User> userList = session.createQuery("from User where nickName = 'John'", User.class).list();
        User john = userList.stream().filter(user -> user.getNickName().equals("John")).findFirst().orElseThrow();
        StringBuilder builder = new StringBuilder();
        builder.append(john.getNickName()).append(" - ");
        List<Video> videoList = session
                .createQuery("from Video where id = (select id from User where nickName = 'John')"
                        , Video.class)
                .list();
        Video videoResult = videoList
                .stream()
                .filter(video -> video.getNameVideo()
                        .equals("Мое первое интервью"))
                .findFirst().orElseThrow();
        builder.append(videoResult.getNameVideo()).append(" - ");
        List<Comment> comments = session.createNativeQuery("""
                select * 
                from comment 
                where video_id = 
                (select id from video where name_video = 'Мое первое интервью') """,
                Comment.class).list();
        Comment comment = comments.stream().findFirst().orElseThrow();
        builder.append(comment.getText());
        System.out.println(builder.toString());
    }
}
