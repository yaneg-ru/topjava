package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.slf4j.LoggerFactory.getLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("TOPJAVA_ROOT: " + System.getenv("TOPJAVA_ROOT"));
        log.debug("MealServlet. doGet. Forward to /meals.jsp");
        req.setAttribute("listMeals", MealsUtil.generateList());
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
