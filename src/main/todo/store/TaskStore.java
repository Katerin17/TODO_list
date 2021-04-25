package main.todo.store;

import main.todo.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.function.Function;

public class TaskStore implements AutoCloseable {

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

    public Task add(Task task) {
        this.wrapperTransaction(session -> session.save(task));
        return task;
    }

    public void update(Task task) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        }
    }

    public Task findById(int id) {
        return this.wrapperTransaction(session -> session.get(Task.class, id));
    }

    public List<Task> findAll() {
        return this.wrapperTransaction(session -> session.createQuery("from main.todo.model.Task", Task.class).list());
    }

    public List<Task> findNoDone() {
        return this.wrapperTransaction(session -> session.createQuery("from main.todo.model.Task where done = false", Task.class).list());
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
