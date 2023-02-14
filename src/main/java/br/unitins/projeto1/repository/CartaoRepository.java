package br.unitins.projeto1.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.Cartao;
import br.unitins.projeto1.model.Usuario;

public class CartaoRepository extends Repository<Cartao>{

	public CartaoRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartaoRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}
	
/*	@SuppressWarnings("unchecked")
	public List<Cartao> findAll() throws RepositoryException{ 
		
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Cartao u ORDER BY u.nome ");
			
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			throw new RepositoryException("Erro ao realizar uma consulta ao banco.");
		}
		
}*/	
	
	public Cartao findCartaoByEmailUsuario(String email) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
			query.setParameter("email", email);
			Usuario usu = (Usuario) query.getSingleResult();
			if(usu == null) {
				Cartao f = new Cartao();
				return f;
			}else {
				return usu.getCartao();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			Cartao f = new Cartao();
			return f;
		}
	}

}
