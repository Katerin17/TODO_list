package main.todo.store;

import main.todo.model.User;
import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.function.Function;

public class UserStore implements AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private <T> T wrapperTransaction(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    public User add(User user) {
        try {
            this.wrapperTransaction(session -> session.save(user));
            return user;
        } catch (ConstraintViolationException e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        return this.wrapperTransaction(session -> {
            final Query<User> query = session.createQuery(
                    "from main.todo.model.User where email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        });
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
