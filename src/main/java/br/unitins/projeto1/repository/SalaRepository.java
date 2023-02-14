package br.unitins.projeto1.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.Sala;

public class SalaRepository extends Repository<Sala> {

	public SalaRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SalaRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	
	@SuppressWarnings("unchecked")
	public List<Sala> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sala u ORDER BY u.nomeDaSala ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	@SuppressWarnings("unchecked")
	public List<Sala> findAllByIdTipoSala(Integer id) throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sala u WHERE u.tipoSala = :tiposala_id");
			query.setParameter("tiposala_id", id);
			if( query.getResultList() == null) {
				return new ArrayList<Sala>();
			}else {
				return query.getResultList();
			}
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			return new ArrayList<Sala>();
		}
		
	}
	
	public Sala findByIdTipoSala(Integer idTipoSala,Integer idSala) throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sala u WHERE u.id = :id AND u.tipoSala.id = :tiposala_id");
			query.setParameter("id", idSala);
			query.setParameter("tiposala_id", idTipoSala);
			if( query.getResultList() == null) {
				return new Sala();
			}else {
				return  (Sala) query.getSingleResult();
			}
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			return new Sala();
		}
		
	}

	public Sala findSala(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sala u WHERE u.nomeDaSala = :nome");
			query.setParameter("nome", nome);
			if((Sala) query.getSingleResult() == null) {
				Sala s = new Sala();
				return s;
			}else {
				return (Sala) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			Sala f = new Sala();
			return f;
		}
	}
	public Sala findSalaById(Integer id) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sala u WHERE u.id = :id");
			query.setParameter("id", id);
			if((Sala) query.getSingleResult() == null) {
				Sala s = new Sala();
				return s;
			}else {
				return (Sala) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			Sala f = new Sala();
			return f;
		}
	}
}
