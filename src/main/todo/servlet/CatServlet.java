package main.todo.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.todo.store.CategoryStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        new ObjectMapper().writeValue(writer, CategoryStore.instanceOf().findAllCategories());
        writer.flush();
    }
}
