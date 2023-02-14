package br.unitins.projeto1.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.model.EsqueceuSenha;

public class EsqueceuSenhaRepository extends Repository<EsqueceuSenha>{

	public EsqueceuSenhaRepository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EsqueceuSenhaRepository(EntityManager em) {
		super(em);
		// TODO Auto-generated constructor stub
	}

	public EsqueceuSenha findCodigo(String codigo) throws RepositoryException{
		try {
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery("SELECT u FROM EsqueceuSenha u WHERE u.codigo = :codigo");
			query.setParameter("codigo", codigo);
			if((EsqueceuSenha) query.getSingleResult() == null) {
				EsqueceuSenha f = new EsqueceuSenha();
				return f;
			}else {
				return (EsqueceuSenha) query.getSingleResult();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Erro ao realizar uma consulta ao banco.");
			e.printStackTrace();
			EsqueceuSenha f = new EsqueceuSenha();
			return f;
		}	
	}
}
