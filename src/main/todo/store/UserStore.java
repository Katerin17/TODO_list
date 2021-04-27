package main.todo.store;

import main.todo.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Function;

public class UserStore implements AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private <T> T wrapperTransaction(final Function<Session, T> command) {
        final Session session = sf.openSession();
        try (session) {
            final Transaction transaction = session.beginTransaction();
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    public User add(User user) {
        this.wrapperTransaction(session -> session.save(user));
        return user;
    }

    public List<User> findByEmail(String email) {
        return this.wrapperTransaction(session -> {
            final Query<User> query = session.createQuery("from main.todo.model.User where email = :email", User.class);
            query.setParameter("email", email);
            return query.list();
        });
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
