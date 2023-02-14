package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.ResponsiveOption;

import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Filmes;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.FilmeRepository;

@Named
@ViewScoped
public class MenuPrincipalUsuarioController  extends Controller<Filmes> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1756584593269737027L;

	private List<Filmes> listaFilmes;
	private String pesquisaNomeFilme;
	private Filmes filmeEscolhido;
	private boolean usuarioLogado;
	private List<ResponsiveOption> responsiveOptions;
	
	private Usuario usuario;
	
    @PostConstruct
    public void init() {
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("560px", 1, 1));
        Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null) {
			setUsuarioLogado(false);
		}else {
			setUsuarioLogado(true);
		}
    }

    public void alterarUsuario() {
    	Util.redirect("/Projeto1/faces/EditarUsuario.xhtml");
    }
	public List<ResponsiveOption> getResponsiveOptions() {
		return responsiveOptions;
	}

	public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
		this.responsiveOptions = responsiveOptions;
	}

	@Override
	public Filmes getEntity() {
		// TODO Auto-generated method stub
		if (entity == null)
			entity = new Filmes();
		return entity;
	}

	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void login() {
		Util.redirect("/Projeto1/faces/Login.xhtml");
	}
	
	public void filmeSelecionado(SelectEvent<Filmes> filme) {
		System.out.println(filme.getObject().getNome());
		Session.getInstance().setAttribute("filmeEscolhido", filme.getObject());
		Util.redirect("/Projeto1/faces/VisualizarFilme.xhtml");
	}
	public void filmeSelecionado(Filmes filme) {
		System.out.println(filme.getNome());
		Session.getInstance().setAttribute("filmeEscolhido", filme);
		Util.redirect("/Projeto1/faces/VisualizarFilme.xhtml");
	}

	public void pesquisarFilmes() {
		//EntityManager em = JPAUtil.getEntityManager();
		FilmeRepository repo = new FilmeRepository();
		getPesquisaNomeFilme();
			try {
				List<Filmes> aux = new ArrayList<Filmes>();
				List<Filmes> aux3 = new ArrayList<Filmes>();
				aux = repo.findAllAtivos();
				aux3 = aux.stream().filter(a -> (a.getNome().toLowerCase().startsWith(getPesquisaNomeFilme())
						|| a.getNome().startsWith(getPesquisaNomeFilme())))
								   .collect(Collectors.toList());
				setListaFilmes(aux3);
			} catch (RepositoryException e) {
				e.printStackTrace();
				Util.addErrorMessage("Problema ao pesquisar.");
				setListaFilmes(null);
			}
		}

	public List<Filmes> getListaFilmes() {
		FilmeRepository repo = new FilmeRepository();
		if(listaFilmes == null) {
			try {
				List<Filmes> aux = repo.findAllAtivos();
				setListaFilmes(aux);
			} catch (RepositoryException e) {
				e.printStackTrace();
				Util.addErrorMessage("Problema ao pesquisar.");
				setListaFilmes(null);
			}
		}
		return listaFilmes;
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

	
	public boolean isUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public void setListaFilmes(List<Filmes> listaFilmes) {
		this.listaFilmes = listaFilmes;
	}
	
	public String getPesquisaNomeFilme() {
		if (pesquisaNomeFilme == null)
			pesquisaNomeFilme = new String();
		return pesquisaNomeFilme;
	}

	public void setPesquisaNomeFilme(String pesquisaNomeFilme) {
		this.pesquisaNomeFilme = pesquisaNomeFilme;
	}

	public Filmes getFilmeEscolhido() {
		return filmeEscolhido;
	}

	public void setFilmeEscolhido(Filmes filmeEscolhido) {
		this.filmeEscolhido = filmeEscolhido;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
