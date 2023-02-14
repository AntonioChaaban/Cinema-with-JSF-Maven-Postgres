package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.event.SelectEvent;

import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Filmes;
import br.unitins.projeto1.model.Sessao;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.SessaoRepository;

@Named
@ViewScoped
public class VisualizarFilmeController extends Controller<Filmes> implements Serializable{


	private static final long serialVersionUID = -3535033219508957753L;
	
	private List<Sessao> listaSessao;
	private Sessao sessaoEscolhida;
	private Date dataDaPesquisa;
	private boolean usuarioLogado;
	
	private Usuario usuario;
	@Override
	public Filmes getEntity() {
		// TODO Auto-generated method stub
		if (entity == null) {
			entity = new Filmes();
			entity = (Filmes) Session.getInstance().getAttribute("filmeEscolhido");
			if (entity == null) {
				entity = new Filmes();
			}
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
		System.out.println("passoaq1");
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null) {
			setUsuarioLogado(false);
		}else {
			setUsuarioLogado(true);
		}
		if(getEntity().getNome() == null) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml"); 
			return;
		}
		pesquisarSessao();
	}
	
	public void sessaoSelecionada(SelectEvent<Sessao> sessao) {
		Session.getInstance().setAttribute("sessaoEscolhida", sessao.getObject());
		Util.redirect("/Projeto1/faces/ComprarIngresso.xhtml");
	}

	public void pesquisarSessao() {
		//EntityManager em = JPAUtil.getEntityManager();
		setListaSessao(new ArrayList<Sessao>());
		SessaoRepository repo = new SessaoRepository();
		SimpleDateFormat parseFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
		try {
			List<Sessao> aux = new ArrayList<Sessao>();
			aux = repo.findAllByFilmeId(getEntity().getId());
			if(aux == null) {
				setDataDaPesquisa(new Date());
			}
			if(getDataDaPesquisa() == null || DateUtils.isSameDay(getDataDaPesquisa(), new Date())) {
				setDataDaPesquisa(new Date());
				for (Sessao sessao : aux) {
					String inicio = parseFormat.format(sessao.getDataInicioSessao());
					String pesquisa = parseFormat.format(getDataDaPesquisa().getTime());
					System.out.println("data Da pesquisa nullas");
					System.out.println(inicio);
					System.out.println(pesquisa);
					if(inicio != null && pesquisa != null &&
							inicio.equals(pesquisa)) {
						SimpleDateFormat parseFormat2 = new SimpleDateFormat(
				                "HH:mm");
						Date auxDataDaPesquisa = new Date();
						
						String horarioDeAgora = parseFormat2.format(auxDataDaPesquisa);
						String horarioDeTerminoDaSessao = parseFormat2.format(sessao.getDataTerminoSessao());
						String horarioDeInicioDaSessao = parseFormat2.format(sessao.getDataInicioSessao());
						
						horarioDeAgora = horarioDeAgora.replace(":","");
						horarioDeTerminoDaSessao = horarioDeTerminoDaSessao.replace(":","");
						horarioDeInicioDaSessao = horarioDeInicioDaSessao.replace(":","");
						
						Integer hA = Integer.parseInt(horarioDeAgora);
						Integer hT = Integer.parseInt(horarioDeTerminoDaSessao);
						Integer hI = Integer.parseInt(horarioDeInicioDaSessao);
						
						if(!(hA > hT) && !(hA > hI + 30)) {
							getListaSessao().add(sessao);
						}
					}
				}
			}else { 
				for (Sessao sessao : aux) {
					String inicio = parseFormat.format(sessao.getDataInicioSessao());
					String pesquisa = parseFormat.format(getDataDaPesquisa());
					
					
					if(getDataDaPesquisa().before(new Date())) {
						Util.addErrorMessage("A data da pesquisa é antes do dia de hoje");
						break;
					}
					if( inicio != null && pesquisa != null &&
							inicio.equals(pesquisa)) {
						getListaSessao().add(sessao);
					}
				}
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			Util.addErrorMessage("Problema ao pesquisar.");
			setListaSessao(null);
		}
		setDataDaPesquisa(null);
	}
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Sessao> getListaSessao() {
		if(listaSessao == null) {
			listaSessao = new ArrayList<Sessao>();
		}
		return listaSessao;
	}

	
	public boolean isUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public void setListaSessao(List<Sessao> listaSessao) {
		this.listaSessao = listaSessao;
	}

	public Date getDataDaPesquisa() {
		return dataDaPesquisa;
	}

	public void setDataDaPesquisa(Date dataDaPesquisa) {
		this.dataDaPesquisa = dataDaPesquisa;
	}

	public Sessao getSessaoEscolhida() {
		return sessaoEscolhida;
	}

	public void setSessaoEscolhida(Sessao sessaoEscolhida) {
		this.sessaoEscolhida = sessaoEscolhida;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
