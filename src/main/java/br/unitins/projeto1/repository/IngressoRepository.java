package br.unitins.projeto1.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.Ingresso;

public class IngressoRepository extends Repository<Ingresso>{

	public IngressoRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IngressoRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	
	@SuppressWarnings("unchecked")
	public List<Ingresso> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Ingresso u ORDER BY u.id ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	@SuppressWarnings("unchecked")
	public List<Ingresso> findAllByIdSessao(Integer id) throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Ingresso u WHERE u.sessao.id = :id ");
			query.setParameter("id", id);
			
			if( query.getResultList() == null) {
				return new ArrayList<Ingresso>();
			}else {
				return query.getResultList();
			}
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			return new ArrayList<Ingresso>();
		}
		
	}
	
	public Ingresso findFilme(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Ingresso u WHERE u.nome = :nome");
			query.setParameter("nome", nome);
			if((Ingresso) query.getSingleResult() == null) {
				Ingresso f = new Ingresso();
				return f;
			}else {
				return (Ingresso) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			Ingresso i = new Ingresso();
			return i;
		}
	}
}
