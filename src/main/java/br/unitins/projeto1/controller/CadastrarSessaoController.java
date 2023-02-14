package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import br.unitins.projeto1.model.Habilitador;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Sala;
import br.unitins.projeto1.model.Sessao;
import br.unitins.projeto1.model.TiposDeSessao;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.FilmeRepository;
import br.unitins.projeto1.repository.IngressoRepository;
import br.unitins.projeto1.repository.SessaoRepository;
import br.unitins.projeto1.repository.ValorVendaRepository;

@Named
@ViewScoped
public class CadastrarSessaoController  extends Controller<Sessao> implements Serializable{

	private static final long serialVersionUID = -1545494102422610762L;

	private Sala sala;
	
	private String tipoDeSessao;
	private List<String> tiposDeSessoes;
	
	private String filme;
	private List<Filmes> listaFilmes;
	private List<String> listaNomeFilmes;	
	
	private Sessao sessaoEscolhida;
	
	private List<Sessao> listaSessao;
	
	private List<Filmes> listaPesquisaSessaoFilmes;
	private String pesquisaNomeFilme;
	
	private Date dataDaPesquisa;
	
	private Usuario usuario;
	
	private boolean abirDialog;
	@Override
	public Sessao getEntity() {
		// TODO Auto-generated method stub
		if(entity == null) {
			entity = new Sessao();
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
	
	public void atualizarValorVenda() {
		setAbirDialog(false);
		if(getTipoDeSessao() != null && !getTipoDeSessao().isBlank() && getFilme() != null &&!getFilme().isBlank()) {
			for (String tiposessao : TiposDeSessao.cadastrados()) {
				if(tiposessao.equals(getTipoDeSessao())) {
					getEntity().setTipoDeSessao(TiposDeSessao.valorString(tiposessao));
				}
			}
			for ( Filmes filmes : getListaFilmes()) {
				if(filmes.getNome().compareTo(getFilme()) == 0) {
					getEntity().setFilme(filmes);
				}
			}
			ValorVendaRepository repo = new ValorVendaRepository();
			try {
				getEntity().setValorVenda(repo.findValoresDoFilme(getEntity().getFilme().getNome())
						.stream().filter((a) -> a.getTipoDeSessao().equals(getEntity().getTipoDeSessao()))
						.collect(Collectors.toList()).get(0));
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
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
			Util.addErrorMessage("O campo do tipo de filme deve ser informado.");
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
		if (getEntity().getDataInicioSessao() == null) {
			Util.addErrorMessage("O campo de inicio da Sessao deve ser informado.");
			return false;
		}
		if (getEntity().getDataTerminoSessao() == null) {
			Util.addErrorMessage("O campo de termino da Sessao deve ser informado.");
			return false;
		}
		if (getEntity().getValorVenda() == null) {
			Util.addErrorMessage("É preciso cadastrar um valor de venda para o tipo de sessao");
			return false;
		}
		getEntity().setSala(getSala());
		getEntity().setHabilitador(Habilitador.HABILITADO);
		System.out.println("Tudo foi cadastrado e chegando");
		return true;
	}
	//
	//
	//
	// MECHER NO CARAI DO HABILITADOR DA SESÃO 
	// FAZER TABELA DE PESQUISA POR HABILITADAS E DESABILITADAS
	// 
	//
	//
	
	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		setAbirDialog(false);
		IngressoRepository repo = new IngressoRepository();
		try {
			if(repo.findAllByIdSessao(getEntity().getId()).isEmpty()) {
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
	
	@PostConstruct
	public void init() {
		setAbirDialog(false);
		Usuario aux3 = new Usuario();
		aux3 = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux3 == null || !aux3.getPerfil().equals(Perfil.ADMINISTRADOR)) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
			setSala(new Sala());
			getSala().setNomeDaSala("abbrobinha");
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
			pesquisarSessao();
		}
	}
	public void pesquisarSessoes() {
		setAbirDialog(true);
		pesquisarSessao();
	}
	public void pesquisarSessao() {
		setListaSessao(new ArrayList<Sessao>());
		SessaoRepository repo = new SessaoRepository();
		SimpleDateFormat parseFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
		if(getDataDaPesquisa() == null) {
			setDataDaPesquisa(new Date());
		}
		try {
			if(getPesquisaNomeFilme() == null || getPesquisaNomeFilme().isBlank()) {
				setListaSessao(repo.findAll().stream().filter(
						(a) -> parseFormat.format(a.getDataInicioSessao()).equals(parseFormat.format(getDataDaPesquisa().getTime()))).collect(Collectors.toList()));
			}else {
				setListaSessao(repo.findAll().stream().filter(
						(a) -> parseFormat.format(a.getDataInicioSessao()).equals(parseFormat.format(getDataDaPesquisa().getTime())) 
						&& (a.getFilme().getNome().toLowerCase().startsWith(getPesquisaNomeFilme())
								|| a.getFilme().getNome().startsWith(getPesquisaNomeFilme()))).collect(Collectors.toList()));
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			Util.addErrorMessage("Problema ao pesquisar.");
			setListaSessao(null);
		}
		
	}
	
	public void sessaoSelecionada(SelectEvent<Sessao> sessao) {
		setAbirDialog(false);
		setEntity(sessao.getObject());
		setTipoDeSessao(getEntity().getTipoDeSessao().getLabel());
		setFilme(getEntity().getFilme().getNome());
	}
	
	public Sala getSala() {
		//setAbirDialog(false);
		if(sala == null) {
			sala = new Sala();
			sala = (Sala) Session.getInstance().getAttribute("salaEscolhida");
			if(sala == null) {
				Util.redirect("/Projeto1/faces/Login.xhtml");
			}
		}
		return sala;
	}
	
	@Override
	public void limpar() {
		// TODO Auto-generated method stub
		super.limpar();
		setEntity(new Sessao());
		setTipoDeSessao(new String());
		setFilme(new String());
		setAbirDialog(false);
	}
	public List<String> getListaNomeFilmes() {
		//setAbirDialog(false);
		if(listaNomeFilmes == null) {
			listaNomeFilmes = new ArrayList<String>();
		}
		return listaNomeFilmes;
	}
	
	
	
	public List<Filmes> getListaPesquisaSessaoFilmes() {
		return listaPesquisaSessaoFilmes;
	}

	public void setListaPesquisaSessaoFilmes(List<Filmes> listaPesquisaSessaoFilmes) {
		this.listaPesquisaSessaoFilmes = listaPesquisaSessaoFilmes;
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

	public Sessao getSessaoEscolhida() {
		return sessaoEscolhida;
	}

	public void setSessaoEscolhida(Sessao sessaoEscolhida) {
		this.sessaoEscolhida = sessaoEscolhida;
	}

	public Date getDataDaPesquisa() {
		return dataDaPesquisa;
	}

	public void setDataDaPesquisa(Date dataDaPesquisa) {
		this.dataDaPesquisa = dataDaPesquisa;
	}

	public List<Sessao> getListaSessao() {
		if(listaSessao == null) {
			listaSessao = new ArrayList<Sessao>();
		}
		return listaSessao;
	}

	public void setListaSessao(List<Sessao> listaSessao) {
		this.listaSessao = listaSessao;
	}

	public void setListaNomeFilmes(List<String> listaNomeFilmes) {
		this.listaNomeFilmes = listaNomeFilmes;
	}

	public List<Filmes> getListaFilmes() {
		return listaFilmes;
	}

	public void setListaFilmes(List<Filmes> listaFilmes) {
		this.listaFilmes = listaFilmes;
	}

	public List<String> getTiposDeSessoes() {
		if(tiposDeSessoes == null) {
			tiposDeSessoes = new ArrayList<String>();
		}
		return tiposDeSessoes;
	}

	public void setTiposDeSessoes(List<String> tiposDeSessoes) {
		this.tiposDeSessoes = tiposDeSessoes;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public String getFilme() {
		return filme;
	}

	public void setFilme(String filme) {
		this.filme = filme;
	}

	public String getTipoDeSessao() {
		return tipoDeSessao;
	}

	public void setTipoDeSessao(String tipoDeSessao) {
		this.tipoDeSessao = tipoDeSessao;
	}
}
