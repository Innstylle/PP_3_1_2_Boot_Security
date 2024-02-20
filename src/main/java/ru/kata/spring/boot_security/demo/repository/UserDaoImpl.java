package ru.kata.spring.boot_security.demo.repository;


import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUser() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(entityManager.contains(user) ? user : entityManager.merge(user));
        entityManager.flush();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }


    @Override
    public void deleteUser(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }
}

