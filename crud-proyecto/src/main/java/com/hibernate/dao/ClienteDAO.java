package com.hibernate.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.hibernate.model.Cliente;
import com.hibernate.util.HibernateUtil;

public class ClienteDAO {

    public void insertCliente(Cliente c) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(c);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateCliente(Cliente c) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(c);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteCliente(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Cliente c = session.find(Cliente.class, id);
            if (c != null) {
                session.remove(c);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Cliente selectClienteById(int id) {
        Transaction transaction = null;
        Cliente c = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            c = session.find(Cliente.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return c;
    }

    public List<Cliente> selectAllCliente() {
        Transaction transaction = null;
        List<Cliente> clientes = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            clientes = session.createQuery("from Cliente", Cliente.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return clientes;
    }
}