package com.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.model.Adopcion;

import com.hibernate.util.HibernateUtil;

public class AdopcionDAO {
	
		public void insertAdopcion(Adopcion ad) {
			Transaction transaction = null;
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				transaction = session.beginTransaction();
				session.merge(ad);
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
			}
		}

		public void updateAdopcion(Adopcion ad) {
			Transaction transaction = null;
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				transaction = session.beginTransaction();
				session.merge(ad);
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
			}
		}

		public void deleteAdopcion(int id) {
			Transaction transaction = null;
			Adopcion ad = null;
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				transaction = session.beginTransaction();
				ad = session.find(Adopcion.class, id);
				session.remove(ad);
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
			}
		}

		public Adopcion selectAdopcionById(int id) {
			Transaction transaction = null;
			Adopcion ad = null;
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				transaction = session.beginTransaction();
				ad = session.find(Adopcion.class, id);
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
			}
			return ad;
		}

		public List<Adopcion> selectAllAdopcion() {
			Transaction transaction = null;
			List<Adopcion> adopciones = null;
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				transaction = session.beginTransaction();
				adopciones = session.createQuery("from Adopcion", Adopcion.class).getResultList();
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
			}
			return adopciones;
		}
}
