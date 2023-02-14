package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Habilitador;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Sala;
import br.unitins.projeto1.model.TipoSala;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.SalaRepository;
import br.unitins.projeto1.repository.TipoSalaRepository;


@Named
@ViewScoped
public class CadastrarSalaController extends Controller<Sala> implements Serializable{

	//ESSE CONTROLLER ESTÁ PRONTO
	private static final long serialVersionUID = -5828832967007120685L;
	
	private String tipoDeSala;
	private List<TipoSala> listaTipoSala;
	private List<String> nomeDosTiposDeSala;
	private List<Sala> listaSalas;
	
	private Usuario usuario;
	
	private Sala salaEscolhida;

	private boolean abirDialog;
	
	@PostConstruct
    public void init() {
		
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null || !aux.getPerfil().equals(Perfil.ADMINISTRADOR)) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}else {
			TipoSalaRepository repo = new TipoSalaRepository();
			setTipoDeSala(new String());
			try {
				setListaTipoSala(repo.findAll());
			} catch (RepositoryException e) {
				e.printStackTrace();
				Util.addErrorMessage("Problema ao pesquisar.");
				setListaTipoSala(null);
			}
			setNomeDosTiposDeSala(new ArrayList<String>());
			List<String> auxNome = new ArrayList<String>();
			for (TipoSala sala : getListaTipoSala()) {
				if(auxNome.isEmpty() && sala.getHabilitador().equals(Habilitador.HABILITADO)) {
					auxNome.add(sala.getNomeDoTipo());
				}
				if(!(auxNome.contains(sala.getNomeDoTipo())) && sala.getHabilitador().equals(Habilitador.HABILITADO)) {
					auxNome.add(sala.getNomeDoTipo());
				}
			}
			setListaTipoSala(new ArrayList<TipoSala>());
			setNomeDosTiposDeSala(auxNome);
			pesquisarSalas();
			setAbirDialog(false);
			
		}
	}
	@Override
	public Sala getEntity() {
		// TODO Auto-generated method stub
		//setAbirDialog(false);
		if(entity == null) {
			entity = new Sala();
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
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		TipoSalaRepository repo = new TipoSalaRepository();
		SalaRepository repo2 = new SalaRepository();
		List<TipoSala> aux = new ArrayList<TipoSala>();
		
		
		if (getEntity() == null) {
			Util.addErrorMessage("Os campos devem ser informado.");
			return false;
		}
		if (getEntity().getNomeDaSala().isBlank() || getEntity().getNomeDaSala().equals(null)) {
			Util.addErrorMessage("O campo nome da Sala deve ser informado.");
			return false;
		}
		if(getTipoDeSala().isBlank() || getTipoDeSala().equals(null)) {
			Util.addErrorMessage("O campo Tipo de Sala deve ser informado.");
			return false;
		}
		System.out.println(getTipoDeSala());
		
		//Para verificar se está atualizando, se estiver atualiza como habilitado
		if(getEntity().getId() != null) {
			getEntity().setHabilitador(Habilitador.HABILITADO);
			try {
				aux = repo.findAllByName(getTipoDeSala());
				getEntity().setTipoSala(aux.get(0));
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			salvarSemValidar();
			limpar();
			return false;
		}
		try {
			if(repo2.findAll().stream().filter((a) -> a.getNomeDaSala().equals(getEntity().getNomeDaSala())).collect(Collectors.toList()).size() != 0) {
				Util.addErrorMessage("Essa Sala ja foi cadastrada.");
				return false;
			}
			aux = repo.findAllByName(getTipoDeSala());
			System.out.println(aux);
			getEntity().setTipoSala(aux.get(0));
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			Util.addErrorMessage("problema na pesquisa do tiposala");
			e.printStackTrace();
			return false;
		}
		getEntity().setHabilitador(Habilitador.HABILITADO);
		init();
		setListaTipoSala(new ArrayList<TipoSala>());
		setAbirDialog(false);
		return true;
	}
	
	public void desabilitar() {
		//excluir antes todas as salas que possuem o tipo sala
		setAbirDialog(false);
		SalaRepository repo = new SalaRepository();
		try {
			Sala aux = repo.findSala(getEntity().getNomeDaSala());
			aux.setHabilitador(Habilitador.DESABILITADO);
			setEntity(aux);
			salvarSemValidar();
		} catch (Exception e) {
			// TODO: handle exception
			Util.addErrorMessage("Erro ao mudar o estado para desabilitado");
		}
		limpar();
	}
	public void atualizarTabela() {
		setAbirDialog(false);
		TipoSalaRepository repo = new TipoSalaRepository();
		try {
			setListaTipoSala(repo.findAllByName(getTipoDeSala()));
		} catch (RepositoryException e) {
			e.printStackTrace();
			Util.addErrorMessage("Problema ao pesquisar.");
			setListaTipoSala(null);
		}
	}
	
	public void salaSelecionada(SelectEvent<Sala> sala) {
		setAbirDialog(false);
		setEntity(sala.getObject());
		setTipoDeSala(getEntity().getTipoSala().getNomeDoTipo());
		atualizarTabela();
	}
	
	public void pesquisarSalas() {
		//EntityManager em = JPAUtil.getEntityManager();
		setAbirDialog(true);
		SalaRepository repo = new SalaRepository();
		try {
			setListaSalas(repo.findAll());
		} catch (RepositoryException e) {
			e.printStackTrace();
			Util.addErrorMessage("Problema ao pesquisar.");
			setListaSalas(null);
		}
	}
	
	@Override
	public void limpar() {
		// TODO Auto-generated method stub
		super.limpar();
		setAbirDialog(false);
		setEntity(new Sala());
		setTipoDeSala(new String());
		setListaTipoSala(new ArrayList<TipoSala>());
	}
	
	
	public Sala getSalaEscolhida() {
		return salaEscolhida;
	}
	public void setSalaEscolhida(Sala salaEscolhida) {
		this.salaEscolhida = salaEscolhida;
	}
	public boolean isAbirDialog() {
		return abirDialog;
	}
	public void setAbirDialog(boolean abirDialog) {
		this.abirDialog = abirDialog;
	}
	public List<Sala> getListaSalas() {
		return listaSalas;
	}
	public void setListaSalas(List<Sala> listaSalas) {
		this.listaSalas = listaSalas;
	}
	public void cadastrarTipoSala() {
		Util.redirect("CadastroTipoSala.xhtml");
	}
	
	public void cadastrarSessoes() {
		Util.redirect("CadastroSessao.xhtml");
	}

	public String getTipoDeSala() {
		return tipoDeSala;
	}
	
	public List<String> getNomeDosTiposDeSala() {
		return nomeDosTiposDeSala;
	}

	public void setNomeDosTiposDeSala(List<String> nomeDosTiposDeSala) {
		this.nomeDosTiposDeSala = nomeDosTiposDeSala;
	}

	public void setTipoDeSala(String tipoDeSala) {
		this.tipoDeSala = tipoDeSala;
	}

	public List<TipoSala> getListaTipoSala() {
		return listaTipoSala;
	}

	public void setListaTipoSala(List<TipoSala> listaTipoSala) {
		this.listaTipoSala = listaTipoSala;
	}
	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}

}
