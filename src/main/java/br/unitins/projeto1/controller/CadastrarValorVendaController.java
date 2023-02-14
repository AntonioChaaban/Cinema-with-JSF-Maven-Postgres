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
import br.unitins.projeto1.model.EstadoFilme;
import br.unitins.projeto1.model.Filmes;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.TiposDeSessao;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.model.ValorVenda;
import br.unitins.projeto1.repository.FilmeRepository;
import br.unitins.projeto1.repository.IngressoRepository;
import br.unitins.projeto1.repository.ValorVendaRepository;

@Named
@ViewScoped
public class CadastrarValorVendaController extends Controller<ValorVenda> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2555254678734874907L;

	private String tipoDeSessao;
	private List<String> tiposDeSessoes;
	
	private String filme;
	private List<Filmes> listaFilmes;
	private List<String> listaNomeFilmes;	
	
	private boolean abirDialog;
	
	private String pesquisaNomeFilme;
	
	private Usuario usuario;
	
	private ValorVenda valorVendaEscolhido;
	@PostConstruct
	public void init() {
		setAbirDialog(false);
		Usuario aux3 = new Usuario();
		aux3 = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux3 == null || !aux3.getPerfil().equals(Perfil.ADMINISTRADOR)) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}else {
			setTiposDeSessoes(TiposDeSessao.cadastrados());
			FilmeRepository repo = new FilmeRepository();
			try {
				setListaFilmes(repo.findAll().stream().filter((a) -> a.getEstadoFilme().equals(EstadoFilme.EMCARTAZ)).collect(Collectors.toList()));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Util.addErrorMessage("Problema ao pesquisar.");
			}
			for (int i = 0; i < getListaFilmes().size(); i++) {
				getListaNomeFilmes().add(getListaFilmes().get(i).getNome());
			}
		}
	}
    public List<String> completeText(String query) {
    	ValorVendaRepository repo = new ValorVendaRepository();
        String queryLowerCase = query.toLowerCase();
        List<String> nomeFilmes = new ArrayList<String>();
        List<ValorVenda> aux = new ArrayList<ValorVenda>();
		try {
			aux = repo.findAll();
			 for (ValorVenda filme : aux) {
		        	nomeFilmes.add(filme.getFilme().getNome());
		        }
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        return nomeFilmes.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }
	public void pesquisar() {
		setAbirDialog(true);
		pesquisarValorVendas();
	}
	public void pesquisarValorVendas() {
		ValorVendaRepository repo = new ValorVendaRepository();
		try {
			//Arrumar essa pesquisa
			List<ValorVenda> aux = new ArrayList<ValorVenda>();
			List<ValorVenda> aux3 = new ArrayList<ValorVenda>();
			aux = repo.findAll();
			setLista(aux);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void vendaSelecionada(SelectEvent<ValorVenda> valorVenda) {
		setAbirDialog(false);
		setEntity(valorVenda.getObject());
		setTipoDeSessao(getEntity().getTipoDeSessao().getLabel());
		setFilme(getEntity().getFilme().getNome());
	}
	
	@Override
	public ValorVenda getEntity() {
		// TODO Auto-generated method stub
		if(entity == null) {
			entity = new ValorVenda();
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
		setAbirDialog(false);
		for (String tiposessao : TiposDeSessao.cadastrados()) {
			if(tiposessao.equals(getTipoDeSessao())) {
				getEntity().setTipoDeSessao(TiposDeSessao.valorString(tiposessao));
			}
		}
		if(getEntity().getTipoDeSessao() == null) {
			Util.addErrorMessage("O campo do tipo de Sessao deve ser informado.");
			return false;
		}
		for ( Filmes filmes : getListaFilmes()) {
			if(filmes.getNome().compareTo(getFilme()) == 0) {
				getEntity().setFilme(filmes);
			}
		}
		if(getEntity().getFilme() == null || getEntity().getFilme().equals(null)) {	
			Util.addErrorMessage("\"O campo de Filme deve ser informado.");
			return false;
		}
		if (getEntity().getValorDaInteira() == null) {
			Util.addErrorMessage("O campo do valor da Inteira deve ser informado.");
			return false;
		}
		if (getEntity().getValorDaMedia() == null) {
			Util.addErrorMessage("O campo do valor da Meia deve ser informado.");
			return false;
		}
		ValorVendaRepository repo = new ValorVendaRepository();
		List<ValorVenda> aux = new ArrayList<ValorVenda>();
		try {
			aux = repo.findValoresDoFilme(getEntity().getFilme().getNome())
			.stream().filter((a) -> a.getTipoDeSessao().equals(getEntity().getTipoDeSessao()))
			.collect(Collectors.toList());
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(aux.size() > 0) {
			Util.addErrorMessage("O valor ja foi cadastrado");
			return false;
		}
		return true;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		setAbirDialog(false);
		IngressoRepository repo = new IngressoRepository();
		//alterar a validação aqui no sentido que só poderá excluir se não tiver nenhum ingresso vendido 
		//da sessao em que o valorVenda está vinculado
		try {
			if(repo.findAllByIdSessao(1).isEmpty()) {
				System.out.println("A lista ta vazia entao rola excluir");
				return true;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("A lista n ta vazia entao n rola excluir");
		return false;
	}
	
	@Override
	public void limpar() {
		// TODO Auto-generated method stub
		super.limpar();
		setEntity(new ValorVenda());
		setTipoDeSessao(new String());
		setFilme(new String());
		setAbirDialog(false);
	}
	public ValorVenda getValorVendaEscolhido() {
		return valorVendaEscolhido;
	}
	public void setValorVendaEscolhido(ValorVenda valorVendaEscolhido) {
		this.valorVendaEscolhido = valorVendaEscolhido;
	}
	public String getPesquisaNomeFilme() {
		return pesquisaNomeFilme;
	}
	public void setPesquisaNomeFilme(String pesquisaNomeFilme) {
		this.pesquisaNomeFilme = pesquisaNomeFilme;
	}
	public boolean isAbirDialog() {
		return abirDialog;
	}
	public void setAbirDialog(boolean abirDialog) {
		this.abirDialog = abirDialog;
	}
	public String getTipoDeSessao() {
		return tipoDeSessao;
	}

	public void setTipoDeSessao(String tipoDeSessao) {
		this.tipoDeSessao = tipoDeSessao;
	}

	public String getFilme() {
		return filme;
	}

	public void setFilme(String filme) {
		this.filme = filme;
	}

	public List<String> getTiposDeSessoes() {
		return tiposDeSessoes;
	}

	public void setTiposDeSessoes(List<String> tiposDeSessoes) {
		this.tiposDeSessoes = tiposDeSessoes;
	}

	public List<Filmes> getListaFilmes() {
		return listaFilmes;
	}

	public void setListaFilmes(List<Filmes> listaFilmes) {
		this.listaFilmes = listaFilmes;
	}

	public List<String> getListaNomeFilmes() {
		if(listaNomeFilmes == null) {
			listaNomeFilmes = new ArrayList<String>();
		}
		return listaNomeFilmes;
	}

	public void setListaNomeFilmes(List<String> listaNomeFilmes) {
		this.listaNomeFilmes = listaNomeFilmes;
	}

	
	
}
