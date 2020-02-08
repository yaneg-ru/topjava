package ru.javawebinar.topjava.web;


import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImplInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.slf4j.LoggerFactory.getLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class MealServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
    private static final String ADD_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private final MealDao dao = new MealDaoImplInMemory();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String forward = LIST_MEALS;
        String action = req.getParameter("action");

       if (action!=null && action.equalsIgnoreCase("delete")) {
            Long mealId = Long.parseLong(req.getParameter("mealId"));
            log.debug("MealServlet. doGet. Delete mealID = " + mealId);
            dao.deleteMeal(mealId);
        } else if (action!=null && action.equalsIgnoreCase("edit")) {
            if (req.getParameter("mealId") != null) {
                Long mealId = Long.parseLong(req.getParameter("mealId"));
                Meal meal = dao.getMealById(mealId);
                req.setAttribute("meal", meal);
                forward = ADD_OR_EDIT;
            }

        } else if (action!=null && action.equalsIgnoreCase("add")) {
            forward = ADD_OR_EDIT;
        }

        if (forward.equals(LIST_MEALS)) {
            req.setAttribute("listMealsBase", MealsUtil.filteredByStreams(MealsUtil.generateList(), LocalTime.MIN, LocalTime.MAX, 2000));
            req.setAttribute("dateTimeFormatter", dateTimeFormatter);
            req.setAttribute("listMealsDynamic", MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000));
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getParameter("idFromAddEditForm") != null) {
            long id;
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));
            try {
                id = Long.parseLong(req.getParameter("idFromAddEditForm"));
                dao.updateMeal(new Meal(id, dateTime, description, calories));
            } catch (Exception e) {
                dao.addMeal(dateTime, description, calories);
            }
        }
        resp.sendRedirect("meals");
    }
}
