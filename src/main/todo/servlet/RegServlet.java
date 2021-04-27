package main.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.todo.model.User;
import main.todo.store.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userName = req.getParameter("name");
        String userEmail = req.getParameter("email").toLowerCase();
        String userPassword = req.getParameter("password");
        UserStore userStore = new UserStore();
        User newUser = userStore.add(User.of(userName, userEmail, userPassword));
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        new ObjectMapper().writeValue(writer, newUser);
        writer.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("registration.html").forward(req, resp);
    }
}
