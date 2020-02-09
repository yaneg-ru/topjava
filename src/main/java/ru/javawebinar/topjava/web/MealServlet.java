package ru.javawebinar.topjava.web;


import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImplInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class MealServlet extends HttpServlet {

    private static final String ADD_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");;
    private MealDao dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = new MealDaoImplInMemory();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action != null) {
            switch (action) {
                case "delete":
                    Long idDel = Long.parseLong(req.getParameter("Id"));
                    dao.delete(idDel);
                    resp.sendRedirect("meals");
                    break;
                case "edit":
                    Long idAdd = Long.parseLong(req.getParameter("Id"));
                    Meal meal = dao.getById(idAdd);
                    req.setAttribute("meal", meal);
                    req.getRequestDispatcher(ADD_OR_EDIT).forward(req, resp);
                    break;
                case "add":
                    req.getRequestDispatcher(ADD_OR_EDIT).forward(req, resp);
            }
        } else {
            req.setAttribute("dateTimeFormatter", dateTimeFormatter);
            req.setAttribute("listMealsDynamic", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
            req.getRequestDispatcher(LIST_MEALS).forward(req, resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        String idFromParam = req.getParameter("idFromAddEditForm");
        if (idFromParam.isEmpty()) {
            dao.add(new Meal(0L, dateTime, description, calories));
        } else {
            long id = Long.parseLong(idFromParam);
            dao.update(new Meal(id, dateTime, description, calories));
        }

        resp.sendRedirect("meals");
    }
}
