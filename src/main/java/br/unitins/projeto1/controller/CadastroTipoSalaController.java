package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Habilitador;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.TipoSala;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.TipoSalaRepository;

@Named
@ViewScoped
public class CadastroTipoSalaController extends Controller<TipoSala> implements Serializable{

	
	//ESSE CONTROLLER ESTÁ PRONTO
	private static final long serialVersionUID = -2734017383767272453L;
	
	private String numeroDeFileiras;
	private String nomeDaSala;
	private List<TipoSala> tipoSala = new ArrayList<TipoSala>();
	private List<TipoSala> paraDataTable;
	private String estado;
	
	private Usuario usuario;

	private boolean abirDialog;
	@Override
	public TipoSala getEntity() {
		// TODO Auto-generated method stub
		if (entity == null) {
			entity = new TipoSala();
		}
		return entity;
	}
	
	public Usuario getUsuario() {
		if(usuario == null) {
			usuario =  new Usuario();
			usuario = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
			if(usuario == null) {
				usuario =  new Usuario();
			}
		}
		return usuario;
	}
	
	@PostConstruct
	public void init() {
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null || !aux.getPerfil().equals(Perfil.ADMINISTRADOR)) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}
		setAbirDialog(false);
	}
	//OK
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		setAbirDialog(false);
		if (getEntity() == null) {
			Util.addErrorMessage("Os campos devem ser informado.");
			return false;
		}
		getEntity().setNomeDoTipo(getNomeDaSala());
		if (getEntity().getNomeDoTipo().isBlank() || getEntity().getNomeDoTipo().equals(null)) {
			Util.addErrorMessage("O campo nome deve ser informado.");
			return false;
		}
		if(getTipoSala().isEmpty()) {
			Util.addErrorMessage("Devem ser informadas as fileiras");
			return false;
		}
		for (TipoSala tipoSala : getTipoSala()) {
			if(tipoSala.getNumeroDePoltronas().isBlank() || tipoSala.getNumeroDePoltronas().isEmpty()) {
				Util.addErrorMessage("Todas as fileiras tem q possuir numeros de cadeiras");
				return false;
			}else {
				tipoSala.setNomeDoTipo(getNomeDaSala());
			}
		}
		if(getEntity().getId() != null) {
			TipoSalaRepository repo = new TipoSalaRepository();
			try {
				List<TipoSala>aux = repo.findAllByName(getEntity().getNomeDoTipo());
				for (TipoSala tipoSala : aux) {
					tipoSala.setHabilitador(Habilitador.HABILITADO);
					setEntity(tipoSala);
					salvarSemValidar();
				}	
			} catch (Exception e) {
				// TODO: handle exception
				Util.addErrorMessage("Erro ao mudar o estado para desabilitado");
			}
			return false;
		}
		TipoSalaRepository repo = new TipoSalaRepository();
		try {
			List<TipoSala> auxSala = repo.findAll();
			for (TipoSala sala : auxSala) {
				if(sala.getNomeDoTipo().equals(getEntity().getNomeDoTipo())) {
					Util.addErrorMessage("Essa Sala ja foi cadastrada.");
					return false;
				}
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			Util.addErrorMessage("problema na pesquisa do tiposala");
			e.printStackTrace();
			return false;
		}
		System.out.println("TA TOMANDO ESSE CAMINHO");
		for (TipoSala tipoSala : getTipoSala()) {
			tipoSala.setHabilitador(Habilitador.HABILITADO);
		}
		setLista(getTipoSala());
		return true;
	}
	//OK
	public void desabilitar() {
		setAbirDialog(false);
		//excluir antes todas as salas que possuem o tipo sala
		TipoSalaRepository repo = new TipoSalaRepository();
		try {
			List<TipoSala>aux = repo.findAllByName(getNomeDaSala());
			for (TipoSala tipoSala : aux) {
				tipoSala.setHabilitador(Habilitador.DESABILITADO);
				System.out.println(Habilitador.DESABILITADO.getId());
				setEntity(tipoSala);
				salvarSemValidar();
			}	
		} catch (Exception e) {
			// TODO: handle exception
			Util.addErrorMessage("Erro ao mudar o estado para desabilitado");
		}
		limpar();
	}
	//OK
	@Override
	public void setLista(List<TipoSala> lista) {
		// TODO Auto-generated method stub
		super.setLista(lista);
	}
	//OK
	public void atualizarTabela() {
		setAbirDialog(false);
		if(!(getNumeroDeFileiras().isBlank() || getNumeroDeFileiras().isEmpty())) {
			if(getTipoSala().isEmpty()) {
				Integer numeroReal = Integer.parseInt(getNumeroDeFileiras());
				System.out.println("aqui1");
				System.out.println(numeroReal);
				for (int i = 0; i < numeroReal; i++) {
					TipoSala aux = new TipoSala();
					char auxchar = (char) ('A' + i);
					aux.setLetraDaFileira(String.valueOf(auxchar));
					getTipoSala().add(aux);
				}
			}else {
				try {
					for (TipoSala t : getTipoSala()) {
						EntityManager em = JPAUtil.getEntityManager();
						em.getTransaction().begin();
						em.flush();
						em.clear();
						Query query = em.createQuery("DELETE FROM TipoSala u WHERE u.id = :id ");
						query.setParameter("id", t.getId());
						query.executeUpdate();
						em.getTransaction().commit();
						//em.close();
						System.out.println("Removeu");
					}
					Util.addInfoMessage("Remoção realizada com sucesso.");
					//repo.commitTransaction();
				} catch (Exception e) {
					System.out.println("Erro ao excluir.");
					e.printStackTrace();
					Util.addErrorMessage("Problema ao excluir.");
				}
				setTipoSala(new ArrayList<TipoSala>());
				Integer numeroReal = Integer.parseInt(getNumeroDeFileiras());
				System.out.println("aqui2");
				System.out.println(numeroReal);
				for (int i = 0; i < numeroReal; i++) {
					TipoSala aux = new TipoSala();
					char auxchar = (char) ('A' + i);
					aux.setLetraDaFileira(String.valueOf(auxchar));
					getTipoSala().add(aux);
				}
			}
		}else {
			setTipoSala(new ArrayList<TipoSala>());
		}
	}
	//OK
	public boolean pesquisarTipoSala() {
		//EntityManager em = JPAUtil.getEntityManager();
		setAbirDialog(true);
		TipoSalaRepository repo = new TipoSalaRepository();
		try {
			setParaDataTable(repo.findAll());
		} catch (RepositoryException e) {
			e.printStackTrace();
			Util.addErrorMessage("Problema ao pesquisar.");
			setParaDataTable(null);
			return false;
		}
		return true;
	}
	//OK
	@SuppressWarnings("null")
	public void editarTipoSala() {
		setAbirDialog(false);
		TipoSalaRepository repo = new TipoSalaRepository();
		if (getNomeDaSala().isBlank() || getNomeDaSala().isEmpty()) {
			Util.addErrorMessage("O campo nome da sala deve ser informado.");
		}else {
			try {
				List<TipoSala> aux = repo.findAllByName(getNomeDaSala());
				if(aux != null  || !(aux.isEmpty())){
					setTipoSala(repo.findAllByName(getNomeDaSala()));
					setEntity(getTipoSala().get(0));
				}else {
					Util.addErrorMessage("Dijite um nome valido");
					limpar();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Util.addErrorMessage("Problema ao pesquisar.");
				setTipoSala(null);
			}
		}
		System.out.println(getTipoSala());
		System.out.println(getEntity().getNomeDoTipo());
	}
	//OK
	@Override
	public void limpar() {
		// TODO Auto-generated method stub
		setAbirDialog(false);
		super.limpar();
		setEntity(null);
		setTipoSala(new ArrayList<TipoSala>());
		setNumeroDeFileiras(new String());
		setNomeDaSala(new String());
	}	 
	
	
	public boolean isAbirDialog() {
		return abirDialog;
	}
	public void setAbirDialog(boolean abirDialog) {
		this.abirDialog = abirDialog;
	}
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNumeroDeFileiras() {
		return numeroDeFileiras;
	}

	public String getNomeDaSala() {
		return nomeDaSala;
	}

	public void setNomeDaSala(String nomeDaSala) {
		setAbirDialog(false);
		this.nomeDaSala = nomeDaSala;
	}

	public List<TipoSala> getParaDataTable() {
		return paraDataTable;
	}

	public void setParaDataTable(List<TipoSala> paraDataTable) {
		this.paraDataTable = paraDataTable;
	}

	public void setNumeroDeFileiras(String numeroDeFileiras) {
		this.numeroDeFileiras = numeroDeFileiras;
	}

	public List<TipoSala> getTipoSala() {
		if(tipoSala == null) {
			tipoSala = new ArrayList<TipoSala>();
		}
		return tipoSala;
	}

	public void setTipoSala(List<TipoSala> tipoSala) {
		this.tipoSala = tipoSala;
	}
	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
