package com.hibernate.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.hibernate.model.Adopcion;
import com.hibernate.model.Animal;
import com.hibernate.model.Cliente;
import com.hibernate.util.HibernateUtil;

public class AdopcionDAO {

	public void insertAdopcion(Adopcion ad) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();
	        // Usamos getReference para evitar cargar las medicinas EAGER
	        Animal animal = session.getReference(Animal.class, ad.getAnimal().getId());
	        Cliente cliente = session.getReference(Cliente.class, ad.getCliente().getId());
	        Adopcion nueva = new Adopcion();
	        nueva.setFecha(ad.getFecha());
	        nueva.setObservaciones(ad.getObservaciones());
	        nueva.setAnimal(animal);
	        nueva.setCliente(cliente);
	        session.persist(nueva);
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) {
	            try { transaction.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
	        }
	        e.printStackTrace();
	    }
	}

	public void updateAdopcion(Adopcion ad) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();
	        Adopcion existente = session.get(Adopcion.class, ad.getId());
	        Animal animal = session.get(Animal.class, ad.getAnimal().getId());
	        Cliente cliente = session.get(Cliente.class, ad.getCliente().getId());
	        existente.setFecha(ad.getFecha());
	        existente.setObservaciones(ad.getObservaciones());
	        existente.setAnimal(animal);
	        existente.setCliente(cliente);
	        session.merge(existente);
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        e.printStackTrace();
	    }
	}

    public void deleteAdopcion(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Adopcion ad = session.find(Adopcion.class, id);
            if (ad != null) {
                session.remove(ad);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Adopcion selectAdopcionById(int id) {
        Transaction transaction = null;
        Adopcion ad = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ad = session.createQuery(
                "SELECT a FROM Adopcion a JOIN FETCH a.cliente JOIN FETCH a.animal WHERE a.id = :id",
                Adopcion.class)
                .setParameter("id", id)
                .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return ad;
    }

    public List<Adopcion> selectAllAdopcion() {
        Transaction transaction = null;
        List<Adopcion> lista = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            lista = session.createQuery(
                "SELECT a FROM Adopcion a JOIN FETCH a.cliente JOIN FETCH a.animal",
                Adopcion.class
            ).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean animalYaAdoptado(int animalId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long count = session.createQuery(
                    "SELECT COUNT(a) FROM Adopcion a WHERE a.animal.id = :id",
                    Long.class)
                    .setParameter("id", animalId)
                    .uniqueResult();
            transaction.commit();
            return count != null && count > 0;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}