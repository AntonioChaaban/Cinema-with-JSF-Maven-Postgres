package br.unitins.projeto1.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.TipoSala;

public class TipoSalaRepository extends Repository<TipoSala> {

	public TipoSalaRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoSalaRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	
	@SuppressWarnings("unchecked")
	public List<TipoSala> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM TipoSala u ORDER BY u.nomeDoTipo ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoSala> findAllByName(String nome) throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM TipoSala u WHERE u.nomeDoTipo = :nomeDoTipo");
			query.setParameter("nomeDoTipo", nome);
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	
	public TipoSala findTipoSala(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM TipoSala u WHERE u.nomeDoTipo = :nomeDoTipo");
			query.setParameter("nomeDoTipo", nome);
			if((TipoSala) query.getSingleResult() == null) {
				TipoSala f = new TipoSala();
				return f;
			}else {
				return (TipoSala) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			TipoSala t = new TipoSala();
			return t;
		}
	}
}
