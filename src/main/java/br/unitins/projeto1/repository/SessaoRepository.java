package br.unitins.projeto1.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.Sessao;

public class SessaoRepository extends Repository<Sessao> {

	public SessaoRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessaoRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Sessao> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sessao u ORDER BY u.tipoDeSessao ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	@SuppressWarnings("unchecked")
	public List<Sessao> findAllByFilmeId(Integer id) throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sessao u WHERE u.filme.id = :id ");
			query.setParameter("id", id);
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	
	public Sessao findFilme(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Sessao u WHERE u.nome = :nome");
			query.setParameter("nome", nome);
			if((Sessao) query.getSingleResult() == null) {
				Sessao f = new Sessao();
				return f;
			}else {
				return (Sessao) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			Sessao s = new Sessao();
			return s;
		}
	}

}
