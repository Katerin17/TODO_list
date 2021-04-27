package main.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.todo.model.User;
import main.todo.store.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserStore userStore = new UserStore();
        User currentUser = userStore.findByEmail(email.toLowerCase()).get(0);
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        ObjectMapper mapper = new ObjectMapper();
        if (currentUser != null && currentUser.getPassword().equals(password)) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", currentUser);
            mapper.writeValue(writer, currentUser);
        } else {
            mapper.writeValue(writer, null);
        }
        writer.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.html").forward(req, resp);
    }
}
