package br.unitins.projeto1.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.ValorVenda;

public class ValorVendaRepository extends Repository<ValorVenda>{

	public ValorVendaRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ValorVendaRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public List<ValorVenda> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM ValorVenda u ORDER BY u.valorDaInteira ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ValorVenda> findValoresDoFilme(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM ValorVenda u WHERE u.filme.nome = :nome");
			query.setParameter("nome", nome);
			
			return query.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ValorVenda> findValoresDosFilmesAtivos() throws RepositoryException{
		try {
			Boolean ativo = true;
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM ValorVenda u WHERE u.filme.estadoFilme = :ativo");
			query.setParameter("ativo", ativo);
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
	}
}
