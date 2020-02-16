package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    // false if not found
    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    // null if not found, when updated
    @Override
    public User save(User user) {
        if (user == null) return null;
        log.info("save {}", user);
        // new
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // update
        // handle case: update, but not present in storage
        if (repository.containsKey(user.getId())) {
            return repository.computeIfPresent(user.getId(), (id, oldMeal) -> user);
        } else {
            return null;
        }

    }

    // null if not found
    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");

        //first comparison
        Comparator<User> comparator = Comparator.comparing(AbstractNamedEntity::getName);
        //second comparison
        comparator = comparator.thenComparing(User::getEmail);

        return repository
                .values()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // null if not found
    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository
                .values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .reduce(null, (a, b) -> b);
    }
}
