package main.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.todo.model.Category;
import main.todo.model.Task;
import main.todo.model.User;
import main.todo.store.CategoryStore;
import main.todo.store.TaskStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class TaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        TaskStore taskStore = new TaskStore();
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        new ObjectMapper().writeValue(writer, taskStore.findAll());
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        TaskStore taskStore = new TaskStore();
        HttpSession session = req.getSession();
        Task task = Task.of(req.getParameter("description"), (User) session.getAttribute("user"));
        String[] arr = req.getParameter("categories").split(",");
        for (String s: arr) {
            task.addCategories(CategoryStore.instanceOf().findById(s));
        }
        taskStore.add(task);
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        new ObjectMapper().writeValue(writer, task);
        writer.flush();
    }
}
