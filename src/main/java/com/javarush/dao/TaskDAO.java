package com.javarush.dao;

import com.javarush.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Queue;

@Repository
public class TaskDAO {

    private final SessionFactory sessionFactory;

    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Task> getAll(int offSet, int limit) {
        Query<Task> query = getSession().createQuery("select t from Task t", Task.class);
        query.setFirstResult(offSet);
        query.setMaxResults(limit);
        return query.getResultList();
    }
    //ToDo может быть ошибка (если вы это видете, то ошибки все таки не было, я просто забыл убрать todo)
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int getCount() {
        Query<Long> query = getSession().createQuery("select count(t) from Task t", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Task getTaskById(int id) {
        Query<Task> query = getSession().createQuery("select t from Task t where t.id = :id", Task.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createOrUpdateTask(Task task) {
        getSession().persist(task);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteTask(Task task) {
        getSession().remove(task);
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
