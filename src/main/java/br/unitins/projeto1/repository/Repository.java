package br.unitins.projeto1.repository;


import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.DefaultEntity;

public class Repository <T extends DefaultEntity<? super T>>{

	private EntityManager entityManager;
	
	public Repository() {
		this(JPAUtil.getEntityManager());
	}
	
	public Repository(EntityManager em) {
		setEntityManager(em);
	}
	
	public void beginTransaction() {
		try {
			getEntityManager().getTransaction().begin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public T save(T entity) throws RepositoryException {
		try { 
			return getEntityManager().merge(entity);
		
		} catch (OptimisticLockException e) {
			System.out.println("Problema de concorrencia (@version).");
			e.printStackTrace();
			throw new RepositoryException("Problema ao salvar. Atualize a página e tente novamente.");
		} catch (Exception e) {
			System.out.println("Erro ao salvarq 1.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao Salvar1 11122.");
		} 
	
	}
	
	public void remove(T entity) throws RepositoryException {
		
		try { 
			T t = getEntityManager().merge(entity);
			getEntityManager().remove(t);
			getEntityManager().getTransaction().commit();
			System.out.println("Removeu");
		} catch (Exception e) {
			System.out.println("Erro ao Remover.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao Remover. ");
		} 
	
	}

	public void commitTransaction() throws RepositoryException {
		try {
			getEntityManager().getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar um commit.");
		}
	}
	
	public void rollbackTransaction() {
		try {
			getEntityManager().getTransaction().rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public T findById(Integer id) {
		// obtendo o tipo da classe de forma generica (a classe deve ser publica)
		final ParameterizedType type = 
				(ParameterizedType) getClass().getGenericSuperclass();
		@SuppressWarnings("unchecked")
		Class<T> tClass = (Class<T>) (type).getActualTypeArguments()[0];
		
		T t = (T) getEntityManager().find(tClass, id);
		
		return t;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	private void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
