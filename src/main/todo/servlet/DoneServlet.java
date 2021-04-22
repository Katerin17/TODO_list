package main.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.todo.model.Task;
import main.todo.store.TaskStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DoneServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        TaskStore taskStore = new TaskStore();
        int taskId = Integer.parseInt(req.getParameter("id"));
        Task doneTask = taskStore.findById(taskId);
        doneTask.setDone(Boolean.parseBoolean(req.getParameter("check")));
        taskStore.update(doneTask);
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        new ObjectMapper().writeValue(writer, doneTask);
        writer.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        TaskStore taskStore = new TaskStore();
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        new ObjectMapper().writeValue(writer, taskStore.findNoDone());
        writer.flush();
    }
}
