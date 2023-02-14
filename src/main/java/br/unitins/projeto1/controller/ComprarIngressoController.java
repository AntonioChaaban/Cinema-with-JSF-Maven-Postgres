package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.AjudaSalaView;
import br.unitins.projeto1.model.Ingresso;
import br.unitins.projeto1.model.Sessao;
import br.unitins.projeto1.model.TipoSala;
import br.unitins.projeto1.model.TiposDeSessao;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.IngressoRepository;
import br.unitins.projeto1.repository.TipoSalaRepository;

@Named
@ViewScoped
public class ComprarIngressoController extends Controller<Ingresso> implements Serializable{

	private static final long serialVersionUID = 5724942378140790590L;
	
	private List<String> aux;
	
	private List<AjudaSalaView> vi;
	
	private List<Ingresso> listaIngressos;
	
	private String nomeDoFilme;
	private String horario;
	private String sala;
	private String tipoDeSessao;
	
	private Integer totalCadeiras;
	private Integer totalColunas;
	
	private String colunas;
	
	private boolean usuarioLogado;

	private Usuario usuario;
	@PostConstruct
	public void init() {
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null) {
			setUsuarioLogado(false);
		}else {
			setUsuarioLogado(true);
		}
		Sessao aux2 = new Sessao();
		aux2 = (Sessao) Session.getInstance().getAttribute("sessaoEscolhida");
		if(aux2 == null || aux2.equals(null)) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}else {
			setNomeDoFilme(aux2.getFilme().getNome());
			
			setSala(aux2.getSala().getNomeDaSala());
			
			setTipoDeSessao(TiposDeSessao.valorString(aux2.getTipoDeSessao().getId()));
			
			DateFormat df = new SimpleDateFormat("hh:mm a");
			String hour = df.format(aux2.getDataInicioSessao());
			setHorario(hour);
			
			TipoSalaRepository repo = new TipoSalaRepository();
			IngressoRepository repo2 = new IngressoRepository();
			List<TipoSala> aux3 = new ArrayList<TipoSala>();
			List<Ingresso> auxIngresso = new ArrayList<Ingresso>();
			try {
				aux3 = repo.findAllByName(aux2.getSala().getTipoSala().getNomeDoTipo());
				auxIngresso = repo2.findAllByIdSessao(aux2.getId());
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Integer i = 0;
			Integer b = 0;
			for (TipoSala tipoSala : aux3) {
				String u = tipoSala.getNumeroDePoltronas();
				Integer z = Integer.parseInt(u);
				i = i+z;
				b++;
			}
			setTotalCadeiras(i);
			System.out.println(b);
			System.out.println(getTotalCadeiras());
			Integer g = Integer.parseInt(aux3.get(1).getNumeroDePoltronas());
			setTotalColunas(g);
			
			setColunas(aux3.get(1).getNumeroDePoltronas());
			
			System.out.println("dividir");
			
			System.out.println(getTotalColunas());
			//bom tenho o maximo que é 40 e as colunas 10
			for (int j = 0; j < b; j++) {
				for (int j2 = 1; j2 <= getTotalColunas(); j2++) {
					AjudaSalaView help = new AjudaSalaView();
					char auxchar = (char) ('A' + j);
					help.setLetraFileira(String.valueOf(auxchar));
					help.setNumeroDaPoltrona(String.valueOf(j2));
					if(!auxIngresso.isEmpty()) {
						for (Ingresso ingresso : auxIngresso) {
							if(help.getLetraFileira().equals(ingresso.getLetraDaFileiraIngresso()) &&
									help.getNumeroDaPoltrona().equals(ingresso.getNumeroDePoltronasIngresso())) {
								help.setOcupado(true);
								help.setIcon("ui-button-danger");
								break;
							}else {
								help.setOcupado(false);
								help.setIcon("ui-button-info");
							}
						}
					}else {
						help.setOcupado(false);
						help.setIcon("ui-button-info");
					}
					getVi().add(help);
				}
			}
			getListaIngressos();
			System.out.println("Chegou no final do processamento");
		}
	}
	
	public void atualizarTabela(AjudaSalaView salaView) {
		//preencher ingresso para ser comprado na proxima pagina
		
		Sessao aux2 = new Sessao();
		aux2 = (Sessao) Session.getInstance().getAttribute("sessaoEscolhida");
		
		Ingresso auxIngresso = new Ingresso();
		
		auxIngresso.setLetraDaFileiraIngresso(salaView.getLetraFileira());
		auxIngresso.setNumeroDePoltronasIngresso(salaView.getNumeroDaPoltrona());
		auxIngresso.setSessao(aux2);
		
		int int_random = ThreadLocalRandom.current().nextInt();
		String convert = String.valueOf(int_random);
		
		auxIngresso.setCodFiscal(convert);
		
		String letra = salaView.getLetraFileira();
		String poltrona = salaView.getNumeroDaPoltrona();
		System.out.println(poltrona);
		
		List<Ingresso> aux = new ArrayList<Ingresso>();
		
		if(salaView.getIcon().equals("ui-button-raised ui-button-help")) {
			for (Ingresso ingresso : new ArrayList<Ingresso>(getListaIngressos())) {
				
				if(ingresso.getLetraDaFileiraIngresso().compareTo(letra) == 0 &&
						ingresso.getNumeroDePoltronasIngresso().compareTo(poltrona) == 0) {
					salaView.setIcon("ui-button-info");
				}else {
					aux.add(ingresso);
				}
				
			}
			setListaIngressos(new ArrayList<Ingresso>());
			setListaIngressos(aux);
		}else {
			if(getListaIngressos().size() < 6) {
				salaView.setIcon("ui-button-raised ui-button-help");
				getListaIngressos().add(auxIngresso);
			}else {
				Util.addInfoMessage("O numero maximo de poltronas para comprar é 6");
			}
		}
	}
	
	public void comprar() {
		if(getListaIngressos().isEmpty()) {
			Util.addInfoMessage("É preciso escolher alguma poltrona para comprar");
		}else {
			Session.getInstance().setAttribute("IngressosEscolhidos", getListaIngressos());
			Util.redirect("/Projeto1/faces/FinalizarCompra.xhtml");
		}
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
	public Ingresso getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<String> getAux() {
		if(aux == null) {
			aux = new ArrayList<String>();
			for (int i = 0; i < 50; i++) {
				char auxchar = (char) ('A' + i);
				aux.add(String.valueOf(auxchar));
			}
		}
		return aux;
	}

	
	public List<Ingresso> getListaIngressos() {
		if(listaIngressos == null) {
			listaIngressos = new ArrayList<Ingresso>();
		}
		return listaIngressos;
	}

	
	public boolean isUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public void setListaIngressos(List<Ingresso> listaIngressos) {
		this.listaIngressos = listaIngressos;
	}

	public String getSala() {
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getTipoDeSessao() {
		return tipoDeSessao;
	}

	public void setTipoDeSessao(String tipoDeSessao) {
		this.tipoDeSessao = tipoDeSessao;
	}

	public List<AjudaSalaView> getVi() {
		if(vi == null) {
			vi = new ArrayList<AjudaSalaView>();
		}
		return vi;
	}

	public void setVi(List<AjudaSalaView> vi) {
		this.vi = vi;
	}

	public void setAux(List<String> aux) {
		this.aux = aux;
	}

	public String getNomeDoFilme() {
		return nomeDoFilme;
	}

	public void setNomeDoFilme(String nomeDoFilme) {
		this.nomeDoFilme = nomeDoFilme;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public Integer getTotalCadeiras() {
		return totalCadeiras;
	}

	public void setTotalCadeiras(Integer totalCadeiras) {
		this.totalCadeiras = totalCadeiras;
	}

	public Integer getTotalColunas() {
		return totalColunas;
	}

	public void setTotalColunas(Integer totalColunas) {
		this.totalColunas = totalColunas;
	}

	public String getColunas() {
		return colunas;
	}

	public void setColunas(String colunas) {
		this.colunas = colunas;
	}

	

	
	
}
