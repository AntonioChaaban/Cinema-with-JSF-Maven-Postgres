package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.unitins.projeto1.application.VersionException;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.DefaultEntity;
import br.unitins.projeto1.repository.Repository;

public abstract class Controller <T extends DefaultEntity<? super T>> implements Serializable {

	private static final long serialVersionUID = -2022582742025295921L;
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projeto1");
	EntityManager em = emf.createEntityManager();
	
	protected T entity = null;
	protected List<T> lista = null;
	protected String caminho = null; 
	
	public Controller() {

	}
	public abstract T getEntity();
	
	public abstract boolean validarDados();
	
	public abstract boolean validarDadosParaExcluir();
	
	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	public List<T> getLista() {
		return lista;
	}

	public void setLista(List<T> lista) {
		this.lista = lista;
	}
	public void sair() {
		Session.getInstance().invalidateSession();
		Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
	}
	
	public void salvar() throws VersionException{
		Repository<T> repo = new Repository<T>();
		if(validarDados()) {
			try {
				repo.beginTransaction();
				setEntity(repo.save(getEntity()));
				repo.commitTransaction();
				Util.addInfoMessage("Operação realizada com sucesso.");
			} catch (RepositoryException e) {
				repo.rollbackTransaction();
				System.out.println("Erro ao salvar.");
				e.printStackTrace();
				Util.addErrorMessage(e.getMessage());
			}
		}
		setEntity(null);
		limpar();
	}
	
	public void salvarEmLote() throws VersionException{
		Repository<T> repo = new Repository<T>();
		if(validarDados()) {
			try {
				repo.beginTransaction();
				for (T t : getLista()) {
					setEntity(repo.save(t));
				}
				repo.commitTransaction();
				Util.addInfoMessage("Operação realizada com sucesso.");
			} catch (RepositoryException e) {
				repo.rollbackTransaction();
				System.out.println("Erro ao salvar.");
				e.printStackTrace();
				Util.addErrorMessage(e.getMessage());
			}
		}
		setEntity(null);
		limpar();
	}
	
	public void salvarComTransisao() throws VersionException {
		Repository<T> repo = new Repository<T>();
		if(validarDados()) {
			try {
				repo.beginTransaction();
				setEntity(repo.save(getEntity()));
				repo.commitTransaction();
				System.out.println(getCaminho());
				Util.redirect(getCaminho());
			} catch (RepositoryException e) {
				repo.rollbackTransaction();
				System.out.println("Erro ao salvar.");
				e.printStackTrace();
				Util.addErrorMessage(e.getMessage());
			}
		}
		limpar();
	}
	public void salvarSemValidar(){
		Repository<T> repo = new Repository<T>();
		try {
			repo.beginTransaction();
			setEntity(repo.save(getEntity()));
			repo.commitTransaction();
			Util.addInfoMessage("Operação realizada com sucesso.");
		} catch (RepositoryException e) {
			repo.rollbackTransaction();
			System.out.println("Erro ao salvar.");
			e.printStackTrace();
			Util.addErrorMessage(e.getMessage());
		}
	}
	
	public void alterarSemValidar() throws VersionException{
		Repository<T> repo = new Repository<T>();
		try {
			repo.beginTransaction();
			setEntity(repo.save(getEntity()));
			repo.commitTransaction();
			Util.addInfoMessage("Operação realizada com sucesso.");
		} catch (RepositoryException e) {
			repo.rollbackTransaction();
			System.out.println("Erro ao salvar.");
			e.printStackTrace();
			Util.addErrorMessage(e.getMessage());
		}
	}

	
	public void alterar() {
		limpar();
		if (validarDados()) {		
			try {
				em.getTransaction().begin();
				em.merge(getEntity());
				em.getTransaction().commit();
				System.out.println("deu bom");
				Util.addInfoMessage("Inclusão realizada com sucesso.");
				Util.addErrorMessage("Inclusão realizada com sucesso.");
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("deu merda");
				Util.addInfoMessage("Erro ao incluir no banco de dados.");
				Util.addErrorMessage("Erro ao incluir no banco de dados.");
			}
		}
	}

	public void excluir() {
		Repository<T> repo = new Repository<T>();
		try {
			repo.beginTransaction();
			repo.remove(getEntity());
			Util.addInfoMessage("Remoção realizada com sucesso.");
			//repo.commitTransaction();
		} catch (RepositoryException e) {
			repo.rollbackTransaction();
			System.out.println("Erro ao excluir.");
			e.printStackTrace();
			Util.addErrorMessage("Problema ao excluir.");
		}
		setEntity(null);
		limpar();
	}
	
	public void excluirComValidasao() {
		Repository<T> repo = new Repository<T>();
		if (validarDadosParaExcluir()) {
			try {
				repo.beginTransaction();
				repo.remove(getEntity());
				Util.addInfoMessage("Remoção realizada com sucesso.");
				//repo.commitTransaction();
			} catch (RepositoryException e) {
				repo.rollbackTransaction();
				System.out.println("Erro ao excluir.");
				e.printStackTrace();
				Util.addErrorMessage("Problema ao excluir.");
			}
		}
		setEntity(null);
		limpar();
	}
	public void editar(T entity) {
		setEntity(entity);
	}
	
	public void limpar() {
		entity = null;
	}
	

}