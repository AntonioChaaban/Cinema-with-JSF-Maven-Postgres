package br.unitins.projeto1.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.EstadoFilme;
import br.unitins.projeto1.model.Filmes;

public class FilmeRepository extends Repository<Filmes> {

	public FilmeRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FilmeRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public List<Filmes> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Filmes u ORDER BY u.nome ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Filmes> findAllAtivos() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Filmes u WHERE u.estadoFilme = :estadoFilme ORDER BY u.nome ");
			query.setParameter("estadoFilme", EstadoFilme.EMCARTAZ);
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	@SuppressWarnings("unchecked")
	public List<Filmes> findAllDesativados() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Filmes u WHERE u.estadoFilme = :estadoFilme ORDER BY u.nome ");
			query.setParameter("estadoFilme", EstadoFilme.FORADECARTAZ);
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	
	public Filmes findFilme(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Filmes u WHERE u.nome = :nome");
			query.setParameter("nome", nome);
			if((Filmes) query.getSingleResult() == null) {
				Filmes f = new Filmes();
				return f;
			}else {
				return (Filmes) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			Filmes f = new Filmes();
			return f;
		}
	}
	
	public Filmes findFilmeLike(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Filmes u WHERE u.nome = :nome");
			query.setParameter("nome", nome);
			if((Filmes) query.getSingleResult() == null) {
				Filmes f = new Filmes();
				return f;
			}else {
				return (Filmes) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			Filmes f = new Filmes();
			return f;
		}
	}

}
