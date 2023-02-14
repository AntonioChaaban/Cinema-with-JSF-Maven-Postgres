package br.unitins.projeto1.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.Usuario;

public class UsuarioRepository  extends Repository<Usuario> {

	public UsuarioRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UsuarioRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Usuario u ORDER BY u.nome ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.1");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
	}
	
	public Usuario findUsuario(String nome) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.nome = :nome");
			query.setParameter("nome", nome);
			if((Usuario) query.getSingleResult() == null) {
				Usuario u = new Usuario();
				return u;
			}else {
				return (Usuario) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.2");
			e.printStackTrace();
			Usuario us = new Usuario();
			return us;
		}
	}
	
	public Usuario findUsuarioByEmail(String email) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
			query.setParameter("email", email);
			if((Usuario) query.getSingleResult() == null) {
				Usuario u = new Usuario();
				return u;
			}else {
				return (Usuario) query.getSingleResult();
			}
		}catch (NoResultException e) {
			System.out.println("Erro sem resultado");
			return null;
			// TODO: handle exception
		} 
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.3");
			e.printStackTrace();
			Usuario us = new Usuario();
			return us;
		}
	}
	
	public Usuario findUsuario(String email,String senha) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			StringBuffer jpql = new StringBuffer();
			jpql.append("SELECT ");
			jpql.append("  u ");
			jpql.append("FROM ");
			jpql.append("  Usuario u ");
			jpql.append("WHERE ");
			jpql.append("  u.email = :email ");
			jpql.append("  AND u.senha = :senha ");
			
			Query query = em.createQuery(jpql.toString());
			query.setParameter("email", email);
			query.setParameter("senha", senha);
			
			if((Usuario) query.getSingleResult() == null) {
				Usuario u = new Usuario();
				return u;
			}else {
				return (Usuario) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.4");
			e.printStackTrace();
			Usuario us = new Usuario();
			return us;
		}
	}
}
