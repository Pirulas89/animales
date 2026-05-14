package com.hibernate.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.hibernate.model.Medicina;
import com.hibernate.util.HibernateUtil;

public class MedicinaDAO {

    public void insertMedicina(Medicina m) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(m);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void updateMedicina(Medicina m) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(m);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteMedicina(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Medicina m = session.find(Medicina.class, id);
            if (m != null) {
                session.remove(m);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Medicina selectMedicinaById(int id) {
        Transaction transaction = null;
        Medicina m = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            m = session.find(Medicina.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return m;
    }

    public List<Medicina> selectAllMedicina() {
        Transaction transaction = null;
        List<Medicina> medicinas = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            medicinas = session.createQuery("from Medicina", Medicina.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return medicinas;
    }

    public Medicina selectMedicinaByNombreTipo(String nombre, String tipo) {
        if (nombre == null || tipo == null) return null;
        for (Medicina m : selectAllMedicina()) {
            if (m.getNombre().equals(nombre) && m.getTipoMed().equals(tipo)) {
                return m;
            }
        }
        return null;
    }

    public boolean tieneAnimalesAsignados(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long count = session.createQuery(
                "SELECT COUNT(a) FROM Animal a JOIN a.medicinas m WHERE m.id = :id",
                Long.class
            ).setParameter("id", id).uniqueResult();
            transaction.commit();
            return count != null && count > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return true;
        }
    }
}