package com.hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.hibernate.model.Animal;
import com.hibernate.model.Medicina;
import com.hibernate.util.HibernateUtil;

public class AnimalDAO {

	public void insertAnimal(Animal a) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();
	        
	        if (a.getMedicinas() != null && !a.getMedicinas().isEmpty()) {
	            List<Medicina> medicinasGestionadas = new ArrayList<>();
	            for (Medicina m : a.getMedicinas()) {
	                System.out.println("Medicina id: " + m.getId() + " nombre: " + m.getNombre());
	                Medicina gestionada = session.get(Medicina.class, m.getId());
	                System.out.println("Gestionada: " + gestionada);
	                if (gestionada != null) medicinasGestionadas.add(gestionada);
	            }
	            a.setMedicinas(medicinasGestionadas);
	        }
	        
	        session.persist(a);
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        e.printStackTrace();
	    }
	}

    public void updateAnimal(Animal a) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(a);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteAnimal(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Animal a = session.find(Animal.class, id);
            if (a != null) {
                session.remove(a);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Animal selectAnimalById(int id) {
        Transaction transaction = null;
        Animal a = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            a = session.find(Animal.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return a;
    }

    public List<Animal> selectAllAnimal() {
        Transaction transaction = null;
        List<Animal> animales = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            animales = session.createQuery("from Animal", Animal.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return animales;
    }
    
    public Animal selectAnimalByIdConMedicinas(int id) {
        Transaction transaction = null;
        Animal a = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            a = session.createQuery(
                    "SELECT a FROM Animal a LEFT JOIN FETCH a.medicinas WHERE a.id = :id",
                    Animal.class)
                    .setParameter("id", id)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return a;
    }

    public List<Animal> selectAllAnimalConMedicinas() {
        Transaction transaction = null;
        List<Animal> animales = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            animales = session.createQuery(
                    "SELECT DISTINCT a FROM Animal a LEFT JOIN FETCH a.medicinas",
                    Animal.class)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return animales;
    }
}