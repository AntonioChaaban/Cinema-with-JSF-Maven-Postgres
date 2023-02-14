package br.unitins.projeto1.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
//import javax.persistence.EntityManager;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

//import br.unitins.projeto1.application.JPAUtil;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.EstadoFilme;
import br.unitins.projeto1.model.Filmes;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.FilmeRepository;
import br.unitins.projeto1.repository.Repository;

@Named
@ViewScoped
public class CadastrarFilmeController extends Controller<Filmes> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Filmes> listaFilmes;
	
	private List<Filmes> listaFilmesEmCartaz;
	private List<Filmes> listaFilmesForaDeCartaz;
	
	private String pesquisaNomeFilme;
	
	private Filmes filmeEscolhido;
	private boolean abirDialog;
	private boolean abirDialog2;
	
	private InputStream imagemPoster = null;
	private InputStream imagemBack = null;
	
	private Usuario usuario;

	@Override
	public Filmes getEntity() {
		// TODO Auto-generated method stub
		if (entity == null)
			entity = new Filmes();
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
		setAbirDialog(false);
		setAbirDialog2(false);
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null || !aux.getPerfil().equals(Perfil.ADMINISTRADOR)) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}
	}
	
	public void uploadPoster(FileUploadEvent event) {
		UploadedFile uploadFile = event.getFile();
		System.out.println("nome arquivo: " + uploadFile.getFileName());
		System.out.println("tipo: " + uploadFile.getContentType());
		System.out.println("tamanho: " + uploadFile.getSize());

		if (uploadFile.getContentType().equals("image/png") || uploadFile.getContentType().equals("image/jpg")) {
			try {
				setImagemPoster(uploadFile.getInputStream());
				System.out.println("inputStream: " + uploadFile.getInputStream().toString());
				if (Util.saveImageFilme(getImagemPoster(), "png", getEntity().getId(),"Poster")) {
					getEntity().setImagemPoster(getEntity().getId() + "Poster");
					salvarSemValidar();
				}else {
					Util.addErrorMessage("Erro ao salvar. Não foi possível salvar a imagem do usuário.");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Util.addInfoMessage("Upload realizado com sucesso.");
		} else {
			Util.addErrorMessage("O tipo da image deve ser png.");
		}

	}
	public void uploadBack(FileUploadEvent event) {
		UploadedFile uploadFile = event.getFile();
		System.out.println("nome arquivo: " + uploadFile.getFileName());
		System.out.println("tipo: " + uploadFile.getContentType());
		System.out.println("tamanho: " + uploadFile.getSize());

		if (uploadFile.getContentType().equals("image/png")) {
			try {
				setImagemBack(uploadFile.getInputStream());
				System.out.println("inputStream: " + uploadFile.getInputStream().toString());
				if (Util.saveImageFilme(getImagemBack(), "png", getEntity().getId(),"Background")) {
					getEntity().setImagemBackground(getEntity().getId() + "Background");
					salvarSemValidar();
				}else {
					Util.addErrorMessage("Erro ao salvar. Não foi possível salvar a imagem do usuário.");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Util.addInfoMessage("Upload realizado com sucesso.");
		} else {
			Util.addErrorMessage("O tipo da image deve ser png.");
		}

	}

	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		FilmeRepository repo = new FilmeRepository();
		if (entity == null) {
			Util.addErrorMessage("Os campos devem ser informado.");
			return false;
		}
		if (getEntity().getNome().isBlank() || getEntity().getNome().equals(null)) {
			Util.addErrorMessage("O campo nome deve ser informado.");
			return false;
		}
		if(getEntity().getId() != null) {
			getEntity().setEstadoFilme(EstadoFilme.EMCARTAZ);
			salvarSemValidar();
			limpar();
			return false;
		}
		Filmes aux = new Filmes();
		try {
			aux = repo.findFilme(getEntity().getNome());
			if(aux.getNome() != null) {
				Util.addErrorMessage("Um Filme ja foi cadastrado com o mesmo nome");
				return false;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getEntity().setEstadoFilme(EstadoFilme.EMCARTAZ);
		System.out.println(getEntity().getNome());
		pesquisarFilmes();
		setAbirDialog(false);
		setAbirDialog2(false);
		return true;
	}
	
	public void uploadDeFoto() {
		setAbirDialog(false);
		setAbirDialog2(true);
	}
	
	public void pesquisar() {
		setAbirDialog(true);
		setAbirDialog2(false);
		pesquisarFilmes();
	}
	public void pesquisarFilmes() {
		//EntityManager em = JPAUtil.getEntityManager();
		setAbirDialog2(false);
		FilmeRepository repo = new FilmeRepository();
		try {
			//setListaFilmes(repo.findAll());
			setListaFilmesEmCartaz(repo.findAllAtivos().stream().filter(a -> (a.getNome().toLowerCase().startsWith(getPesquisaNomeFilme())
					|| a.getNome().startsWith(getPesquisaNomeFilme())))
					   .collect(Collectors.toList()));
			setListaFilmesForaDeCartaz(repo.findAllDesativados().stream().filter(a -> (a.getNome().toLowerCase().startsWith(getPesquisaNomeFilme())
					|| a.getNome().startsWith(getPesquisaNomeFilme())))
					   .collect(Collectors.toList()));
		} catch (RepositoryException e) {
			e.printStackTrace();
			Util.addErrorMessage("Problema ao pesquisar.");
			//setListaFilmes(null);
		}
	}
	
	public void salvarFilmes() {
		setAbirDialog(false);
		setAbirDialog2(false);
		Repository<Filmes> repo = new Repository<Filmes>();
		FilmeRepository repos = new FilmeRepository();
		if(validarDados()) {
			try {
				repo.beginTransaction();
				setEntity(repo.save(getEntity()));
				repo.commitTransaction();
				setListaFilmes(repos.findAll());
				setListaFilmesEmCartaz(repos.findAllAtivos());
				setListaFilmesForaDeCartaz(repos.findAllDesativados());
				Util.addInfoMessage("Operação realizada com sucesso.");
			} catch (RepositoryException e) {
				repo.rollbackTransaction();
				System.out.println("Erro ao salvar.");
				e.printStackTrace();
				Util.addErrorMessage(e.getMessage());
			}
		}
		limpar();
		
	}
	public void desabilitar() {
		setAbirDialog(false);
		setAbirDialog2(false);
		//excluir antes todas as salas que possuem o tipo sala
		FilmeRepository repo = new FilmeRepository();
		try {
			Filmes aux = repo.findFilme(getEntity().getNome());
			aux.setEstadoFilme(EstadoFilme.FORADECARTAZ);
			setEntity(aux);
			salvarSemValidar();
		} catch (Exception e) {
			// TODO: handle exception
			Util.addErrorMessage("Erro ao mudar o estado para desabilitado");
		}
		limpar();
	}
	
	public void filmeSelecionado(SelectEvent<Filmes> filme) {
		setAbirDialog(false);
		setAbirDialog2(false);
		setEntity(filme.getObject());
	}
	
	public void editarFilmes() {
		//EntityManager em = JPAUtil.getEntityManager();
		setAbirDialog(false);
		setAbirDialog2(false);
		FilmeRepository repo = new FilmeRepository();
		if (getEntity().getNome().isBlank()) {
			System.out.println("passo aqui");
			Util.addErrorMessage("O campo nome deve ser informado.");
		}else {
			try {
				if(repo.findFilme(getEntity().getNome()) != null) {
					setEntity(repo.findFilme(getEntity().getNome()));
				}else {
					limpar();
					System.out.println("nao deu met");
				}
			} catch (RepositoryException e) {
				e.printStackTrace();
				Util.addErrorMessage("Problema ao pesquisar.");
				setListaFilmes(null);
			}
		}
	}

	@Override
	public void limpar() {
		// TODO Auto-generated method stub
		super.limpar();
		setEntity(new Filmes());
		setAbirDialog(false);
		setAbirDialog2(false);
	}
	
	
	public InputStream getImagemPoster() {
		return imagemPoster;
	}

	public void setImagemPoster(InputStream imagemPoster) {
		this.imagemPoster = imagemPoster;
	}

	public InputStream getImagemBack() {
		return imagemBack;
	}

	public void setImagemBack(InputStream imagemBack) {
		this.imagemBack = imagemBack;
	}

	
	
	
	public boolean isAbirDialog2() {
		return abirDialog2;
	}

	public void setAbirDialog2(boolean abirDialog2) {
		this.abirDialog2 = abirDialog2;
	}

	public boolean isAbirDialog() {
		return abirDialog;
	}

	public void setAbirDialog(boolean abirDialog) {
		this.abirDialog = abirDialog;
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

	public List<Filmes> getListaFilmes() {
		return listaFilmes;
	}

	public void setListaFilmes(List<Filmes> listaFilmes) {
		this.listaFilmes = listaFilmes;
	}
	
	

	public List<Filmes> getListaFilmesEmCartaz() {
		return listaFilmesEmCartaz;
	}

	public void setListaFilmesEmCartaz(List<Filmes> listaFilmesEmCartaz) {
		this.listaFilmesEmCartaz = listaFilmesEmCartaz;
	}

	public List<Filmes> getListaFilmesForaDeCartaz() {
		return listaFilmesForaDeCartaz;
	}

	public void setListaFilmesForaDeCartaz(List<Filmes> listaFilmesForaDeCartaz) {
		this.listaFilmesForaDeCartaz = listaFilmesForaDeCartaz;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
