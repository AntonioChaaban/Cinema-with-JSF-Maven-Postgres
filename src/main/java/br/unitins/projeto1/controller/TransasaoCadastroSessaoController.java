package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Habilitador;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Sala;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.SalaRepository;

@Named
@ViewScoped
public class TransasaoCadastroSessaoController extends Controller<Sala> implements Serializable{

	private static final long serialVersionUID = -662249105754341133L;
	
	private List<Sala> listaSalaAtivo;
	private List<Sala> listaSalaDesativado;
	private String pesquisaNomeSala;
	private Sala salaEscolhida;

	private Usuario usuario;
	
	@Override
	public Sala getEntity() {
		// TODO Auto-generated method stub
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
	@PostConstruct
	public void init() {
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null || !aux.getPerfil().equals(Perfil.ADMINISTRADOR)) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}else {
			getListaSalaDesativado();
			getListaSalaAtivo();
		}
	}

	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void SalaSelecionada(SelectEvent<Sala> sala) {
		System.out.println(sala.getObject().getNomeDaSala());
		Session.getInstance().setAttribute("salaEscolhida", sala.getObject());
		Util.redirect("/Projeto1/faces/CadastroSessao.xhtml");
	}
	

	public List<Sala> getListaSalaDesativado() {
		SalaRepository repo = new SalaRepository();
		if(listaSalaAtivo == null) {
			try {
				List<Sala> aux2 = new ArrayList<Sala>();
				List<Sala> aux = repo.findAll();
				for (Sala sala : aux) {
					System.out.println(sala.getHabilitador());
					
					if(sala.getHabilitador().equals(Habilitador.DESABILITADO) || 
							sala.getTipoSala().getHabilitador().equals(Habilitador.DESABILITADO)) {
						aux2.add(sala);
					}
				}
				setListaSalaDesativado(aux2);
			} catch (Exception e) {
				// TODO: handle exception
				Util.addErrorMessage("Problema ao pesquisar.");
				setListaSalaDesativado(null);
			}
		}
		return listaSalaDesativado;
	}

	public void setListaSalaDesativado(List<Sala> listaSalaDesativado) {
		this.listaSalaDesativado = listaSalaDesativado;
	}

	public List<Sala> getListaSalaAtivo() {
		SalaRepository repo = new SalaRepository();
		if(listaSalaAtivo == null) {
			try {
				List<Sala> aux2 = new ArrayList<Sala>();
				List<Sala> aux = repo.findAll();
				for (Sala sala : aux) {
					if(sala.getHabilitador().equals(Habilitador.HABILITADO) &&
							sala.getTipoSala().getHabilitador().equals(Habilitador.HABILITADO) ) {
						aux2.add(sala);
					}
				}
				setListaSalaAtivo(aux2);
			} catch (Exception e) {
				// TODO: handle exception
				Util.addErrorMessage("Problema ao pesquisar.");
				setListaSalaAtivo(null);
			}
		}
		return listaSalaAtivo;
	}

	public void setListaSalaAtivo(List<Sala> listaSalaAtivo) {
		this.listaSalaAtivo = listaSalaAtivo;
	}

	public String getPesquisaNomeSala() {
		if(pesquisaNomeSala == null) {
			pesquisaNomeSala =  new String();
		}
		return pesquisaNomeSala;
	}

	public void setPesquisaNomeSala(String pesquisaNomeSala) {
		this.pesquisaNomeSala = pesquisaNomeSala;
	}

	public Sala getSalaEscolhida() {
		return salaEscolhida;
	}

	public void setSalaEscolhida(Sala salaEscolhida) {
		this.salaEscolhida = salaEscolhida;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
