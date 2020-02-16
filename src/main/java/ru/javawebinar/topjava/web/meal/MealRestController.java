package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }


    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");

        if (request.getMethod().equals("POST")) {
            Meal meal = new Meal(
                    SecurityUtil.authUserId(),
                    id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories"))
            );
            log.info(meal.isNew() ? "Create {}" : "Update {}", service.save(meal, SecurityUtil.authUserId()), SecurityUtil.authUserId());
            response.sendRedirect("meals");
        } else if (request.getMethod().equals("GET")) {
            String action = request.getParameter("action");
            final Meal meal = "create".equals(action) ?
                    new Meal(SecurityUtil.authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                    service.get(getId(request), SecurityUtil.authUserId());
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
        }
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getId(request);
        log.info("delete {} with userId={}", id, SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
        response.sendRedirect("meals");
    }

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("getAll with userId={}", SecurityUtil.authUserId());

        String startDateString = request.getParameter("startDate");
        LocalDate startDate = (startDateString == null ? LocalDate.MIN : LocalDate.parse(startDateString));

        String endDateString = request.getParameter("endDate");
        LocalDate endDate = (endDateString == null ? LocalDate.MAX : LocalDate.parse(endDateString));

        String startTimeString = request.getParameter("startTime");
        LocalTime startTime = (startTimeString == null ? LocalTime.MIN : LocalTime.parse(startTimeString));

        String endTimeString = request.getParameter("endTime");
        LocalTime endTime = (endTimeString == null ? LocalTime.MAX : LocalTime.parse(endTimeString));

        request.setAttribute("meals",
                MealsUtil.getTos(service.getAll(SecurityUtil.authUserId(), startDate, endDate, startTime, endTime), MealsUtil.DEFAULT_CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);

    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}