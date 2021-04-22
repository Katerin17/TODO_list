package main.todo.store;

import main.todo.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class TaskStore implements AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public Task add(Task task) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        }
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
        Task result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.get(Task.class, id);
            session.getTransaction().commit();
        }
        return result;
    }

    public List<Task> findAll() {
        List<Task> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from main.todo.model.Task", Task.class).list();
            session.getTransaction().commit();
        }
        return result;
    }

    public List<Task> findNoDone() {
        List<Task> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from main.todo.model.Task where done = false", Task.class).list();
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
