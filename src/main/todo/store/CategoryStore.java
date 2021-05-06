package main.todo.store;

import main.todo.model.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.function.Function;

public class CategoryStore implements AutoCloseable{
    private SessionFactory sf;

    private CategoryStore() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

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

    private static final class Lazy {
        private static final CategoryStore INSTANCE = new CategoryStore();
    }

    public static CategoryStore instanceOf() {
        return Lazy.INSTANCE;
    }

    public List<Category> findAllCategories() {
        return this.wrapperTransaction(session -> session.createQuery(
                "from main.todo.model.Category", Category.class).list());
    }

    public Category findById(String id) {
        return this.wrapperTransaction(session -> session.get(Category.class, Integer.parseInt(id)));
    }

    @Override
    public void close() {
        sf.close();
    }
}
