package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User refUser = em.getReference(User.class, userId);
            meal.setUser(refUser);
            em.persist(meal);
            return meal;
        } else {
            if (get(meal.getId(), userId) == null) return null;
            return em.merge(meal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        try {
            Meal refMeal = em.getReference(Meal.class, id);
            if (refMeal == null) return false;
            if (refMeal.getUser().getId() != userId) return false;
        } catch (EntityNotFoundException e) {
            return false;
        }
        Query query = em.createQuery("DELETE FROM Meal m WHERE m.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal findMeal = em.find(Meal.class, id);
        if (findMeal == null) return null;
        if (findMeal.getUser().getId() != userId) return null;
        return findMeal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.BY_GETBETWEEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}