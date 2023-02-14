package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import br.unitins.projeto1.application.Email;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Cartao;
import br.unitins.projeto1.model.Ingresso;
import br.unitins.projeto1.model.Sessao;
import br.unitins.projeto1.model.Ticket;
import br.unitins.projeto1.model.TiposDeSessao;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.Repository;
import br.unitins.projeto1.repository.UsuarioRepository;

@Named
@ViewScoped
public class FinalizarCompraController extends Controller<Ingresso> implements Serializable{

	private static final long serialVersionUID = -23586967698692995L;
	
	private List<Ingresso> listaIngressos;
	
	private Usuario usuario;
	private Cartao cartao;
	private String step;
	
	private boolean irParaLogin;
	private boolean irParaCadastroCartao;
	private boolean irParaEscolherCartao;
	
	private String nomeDoFilme;
	private String horario;
	private String sala;
	private String tipoDeSessao;
	
	private boolean abirDialog;
	
	private Integer maxInteira;
	private Integer maxMeia;
	
	private Integer valorInteira;
	private Integer valorMeia;
	
	private Integer valorMaximo;
	
	private boolean usuarioLogado;
	private Usuario usuarioMenu;
	
	public static final String STEP_LAST = "last";

	private boolean temTicket;
	private String codigoTicket;
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		Usuario aux3 = new Usuario();
		aux3 = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux3 == null) {
			setUsuarioLogado(false);
		}else {
			setUsuarioLogado(true);
		}
		getListaIngressos();
		List<Ingresso> aux = (List<Ingresso>) Session.getInstance().getAttribute("IngressosEscolhidos");
		if(aux == null || aux.isEmpty()) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}else {
			setListaIngressos(aux);
			Integer maximo = getListaIngressos().size();
			setMaxInteira(maximo);
			setMaxMeia(maximo);
			setValorMaximo(maximo);
			
			Sessao aux2 = new Sessao();
			aux2 = (Sessao) Session.getInstance().getAttribute("sessaoEscolhida");
			
			
			setNomeDoFilme(aux2.getFilme().getNome());
			
			setSala(aux2.getSala().getNomeDaSala());
			
			setTipoDeSessao(TiposDeSessao.valorString(aux2.getTipoDeSessao().getId()));
			
			DateFormat df = new SimpleDateFormat("hh:mm a");
			String hour = df.format(aux2.getDataInicioSessao());
			setHorario(hour);
		}
	}
	
	public void atualizarNumeros() {
		setAbirDialog(false);
		setTemTicket(false);
		setMaxMeia(getValorMaximo() - getValorInteira());
		setMaxMeia(getValorMaximo() - getValorInteira());
	}
	
	public void abrirDialog() {
		Usuario auxUsuario = new Usuario();
		auxUsuario = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		Integer resultado = getValorInteira() + getValorMeia();
		if(resultado != getValorMaximo()) {
			Util.addErrorMessage("preencha a quantidade de ingressos selecionados");
			setAbirDialog(false);
			setTemTicket(false);
		}else {
			if(auxUsuario == null) {
				setStep("login");
				setIrParaLogin(false);
				setIrParaCadastroCartao(true);
				System.out.println("Entrou aqui 1111");
			}else {
				if(auxUsuario.getCartao() == null || auxUsuario.getCartao().getNomeNoCartao() == null) {
					setStep("cartao");
					setIrParaCadastroCartao(false);
					setIrParaEscolherCartao(false);
					System.out.println("Entrou aqui 2222");
				}
				else {
					System.out.println("Entrou aqui 3333");
					setStep("comprar");
					setIrParaEscolherCartao(false);
				}
			}
			setAbirDialog(true);
			setTemTicket(false);
		}
	}
	@SuppressWarnings("unchecked")
	public String flowDoCadastro(FlowEvent event) {
		if(isIrParaLogin()) {
			System.out.println("Entrou aqui 1");
			setIrParaLogin(false);
			setIrParaCadastroCartao(true);
			setIrParaEscolherCartao(false);
			return "login";	
		}
    	if(isIrParaCadastroCartao()) {
    		UsuarioRepository repo = new UsuarioRepository();
    		if(getUsuario() == null) {
    			Util.addErrorMessage("Necessário Informar Login e senha");
    			System.out.println("Entrou aqui 22");
    			return "login";
    		}else {
    			try {
    				System.out.println(Util.hashSHA256(getUsuario().getSenha()));
    				Usuario usuarioLogado = 
    						repo.findUsuario(getUsuario().getEmail(), 
    								Util.hashSHA256(getUsuario().getSenha()));
    				if (usuarioLogado.getEmail() == null || usuarioLogado.getSenha() == null) {
    					//setStep("login");
    					System.out.println("Entrou aqui 555");
    					return "login";
    				}else {
    					Session.getInstance().setAttribute("usuarioLogado", usuarioLogado);
    				}
    				if(usuarioLogado.getCartao() != null) {
    					//setStep("comprar");
    					setIrParaLogin(false);
    					setIrParaCadastroCartao(false);
    					return "comprar";
    				}else {
    					setIrParaLogin(false);
    					setIrParaCadastroCartao(false);
    					setIrParaEscolherCartao(false);
    					abrirDialog();
    					return "cartao";
    				}
    						
    			} catch (Exception e) {
    				e.printStackTrace();
    				Util.addErrorMessage("Problema ao verificar o Login. Entre em contato pelo email: HyperFilmes@gmail.com.br");
    			}
    		}
    		System.out.println("Entrou aqui 8");
    		return null;
		}else {
			setIrParaLogin(false);
			setIrParaCadastroCartao(false);
			if(validarDados()) {
				Usuario aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
				UsuarioRepository repo = new UsuarioRepository();
				aux.setCartao(getCartao());
				try {
					repo.beginTransaction();
					repo.save(aux);
					repo.commitTransaction();
					aux = repo.findById(aux.getId());
					Session.getInstance().setAttribute("usuarioLogado", aux);
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					System.out.println("Não ta Salvando");
					e.printStackTrace();
				}
				abrirDialog();
				return getStep();
			}
			return "cartao";
		}
	}
	
	public void finalizarCompra() {
		Sessao aux2 = new Sessao();
		aux2 = (Sessao) Session.getInstance().getAttribute("sessaoEscolhida");
		aux2.getValorVenda().getValorDaInteira();
		Usuario usu = new Usuario();
		usu = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		List<Ingresso> ingressos = new ArrayList<Ingresso>();
		ingressos = (List<Ingresso>) Session.getInstance().getAttribute("IngressosEscolhidos");
		setMaxInteira(Integer.parseInt(aux2.getValorVenda().getValorDaInteira()) * getValorInteira());
		setMaxMeia(Integer.parseInt(aux2.getValorVenda().getValorDaMedia()) * getValorMeia());
		
		String valorTotal = String.valueOf(getMaxInteira() + getMaxMeia());
		//"\r\n" usar para formatar uma String para enviar por Email os ingressos comprados, isso pula a linha
		String email = "Ingresso" + "\r\n";
		for (Ingresso ing : ingressos) {
			ing.setCodFiscal("sdfvndlsfkvnçkldsf");
			ing.setUsuario(usu);
			ing.setValor(valorTotal);
			setEntity(ing);
			salvarSemValidar();
			
			email = email +"\r\n" + "Codigo fiscal:" + getEntity().getCodFiscal() + "\r\n" 
					+ "Filme:" + getEntity().getSessao().getFilme().getNome() + "\r\n"
					+ "Inicio Da Sessão:" + getEntity().getSessao().getDataInicioSessao() + "\r\n"
					+ "Sala:" + getEntity().getSessao().getSala().getNomeDaSala() + "\r\n"
					+ "Letra da Fileira:" + getEntity().getLetraDaFileiraIngresso() + "\r\n"
					+ "Numero da Poltrona:" + getEntity().getNumeroDePoltronasIngresso() + "\r\n"
					+ "Usuario:" + getEntity().getUsuario().getNome() + "\r\n";
		}
		email = email + "\r\n" + "Valor Total:" + valorTotal + "\r\n";
		Email emailsend = new Email(usu.getEmail(),"Ingressos",email);
		if (!emailsend.enviar()) {
			Util.addErrorMessage("Problema ao enviar o email.");
		} else
			Util.addInfoMessage("Os Ingressos foram enviados para seu email.");
		setMaxInteira(Integer.parseInt(aux2.getValorVenda().getValorDaInteira()) * getValorInteira());
		setMaxMeia(Integer.parseInt(aux2.getValorVenda().getValorDaMedia()) * getValorMeia());
		
		Random random = new Random();
		int r = random.nextInt(2);
		System.out.println(r);
		if(r == 1) {
			
			Ticket ticket = new Ticket();
			ticket.setUsuario(usu);
			int aleatorio = usu.getEmail().length();
			ticket.setCodigoTicket(String.valueOf(random.nextInt(10000000) + aleatorio));
			setCodigoTicket(ticket.getCodigoTicket());
			setTemTicket(true);
			Repository<Ticket> repo = new Repository<Ticket>();
			try {
				repo.beginTransaction();
				setEntity(repo.save(ticket));
				repo.commitTransaction();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				System.out.println("Erro ao salvar.");
				e.printStackTrace();
			}
		}
		Util.addInfoMessage("Compra Finalizada com Sucesso!!!");
		System.out.println("Entrou aqui 9");
		setIrParaEscolherCartao(false);
	}
	
	@Override
	public Ingresso getEntity() {
		// TODO Auto-generated method stub
		if(entity == null) {
			entity = new Ingresso();
		}
		return entity;
	}

	public Usuario getUsuarioMenu() {
		if(usuarioMenu == null) {
			usuarioMenu =  new Usuario();
			usuarioMenu = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
			if(usuarioMenu == null) {
				usuarioMenu =  new Usuario();
			}
		}
		return usuarioMenu;
	}
	
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		if (getCartao() == null) {
			Util.addErrorMessage("Os campos devem ser informado.");
			return false;
		}
		if (getCartao().getNumeroNoCartao() == null || getCartao().getNumeroNoCartao().isBlank()) {
			Util.addErrorMessage("O campo nome deve ser informado.");
			return false;
		}
		if (getCartao().getCvc() == null || getCartao().getCvc().isBlank()) {
			Util.addErrorMessage("O campo sobre nome deve ser informado.");
			return false;
		}
		if (getCartao().getDataDeValidade() == null || getCartao().getDataDeValidade().isBlank()) {
			Util.addErrorMessage("O campo email deve ser informado.");
			return false;
		}
		if (getCartao().getNomeNoCartao() == null || getCartao().getNomeNoCartao().isBlank()) {
			Util.addErrorMessage("O campo senha deve ser informado.");
			return false;
		}	
		return true;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
		
		
		
	}

	public void logar() {
		
		UsuarioRepository repo = new UsuarioRepository();
		if(getUsuario() == null) {
			Util.addErrorMessage("Necessário Informar Login e senha");
		}else {
			try {
				System.out.println(Util.hashSHA256(getUsuario().getSenha()));
				Usuario usuarioLogado = 
						repo.findUsuario(getUsuario().getEmail(), 
								Util.hashSHA256(getUsuario().getSenha()));
				System.out.println(usuarioLogado.getEmail());
				if (usuarioLogado.getEmail() == null || usuarioLogado.getSenha() == null) {
					setStep("login");
					setIrParaLogin(true);
					abrirDialog();
				}else {
					// Usuario existe com as credenciais
					Session.getInstance().setAttribute("usuarioLogado", usuarioLogado);
					if(usuarioLogado.getCartao() == null) {
						setStep("cartao");
						setIrParaCadastroCartao(true);
						abrirDialog();
					}else {
						setStep("comprar");
						setIrParaEscolherCartao(false);
						abrirDialog();
					}
				}		
			} catch (Exception e) {
				e.printStackTrace();
				Util.addErrorMessage("Problema ao verificar o Login. Entre em contato pelo email: HyperFilmes@gmail.com.br");
			}
		}
	}
	
	
	
	public String getCodigoTicket() {
		return codigoTicket;
	}

	public void setCodigoTicket(String codigoTicket) {
		this.codigoTicket = codigoTicket;
	}

	public boolean isTemTicket() {
		return temTicket;
	}

	public void setTemTicket(boolean temTicket) {
		this.temTicket = temTicket;
	}

	public boolean isUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(boolean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public boolean isIrParaLogin() {
		return irParaLogin;
	}

	public void setIrParaLogin(boolean irParaLogin) {
		this.irParaLogin = irParaLogin;
	}

	public boolean isIrParaCadastroCartao() {
		return irParaCadastroCartao;
	}

	public void setIrParaCadastroCartao(boolean irParaCadastroCartao) {
		this.irParaCadastroCartao = irParaCadastroCartao;
	}

	public boolean isIrParaEscolherCartao() {
		return irParaEscolherCartao;
	}

	public void setIrParaEscolherCartao(boolean irParaEscolherCartao) {
		this.irParaEscolherCartao = irParaEscolherCartao;
	}

	public boolean isAbirDialog() {
		return abirDialog;
	}

	public void setAbirDialog(boolean abirDialog) {
		this.abirDialog = abirDialog;
	}

	public Usuario getUsuario() {
		if(usuario == null) {
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Cartao getCartao() {
		if(cartao == null) {
			cartao = new Cartao();
		}
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
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
	public List<Ingresso> getListaIngressos() {
		if(listaIngressos == null) {
			listaIngressos = new ArrayList<Ingresso>();
		}
		return listaIngressos;
	}

	public void setListaIngressos(List<Ingresso> listaIngressos) {
		this.listaIngressos = listaIngressos;
	}
	public Integer getMaxInteira() {
		if(maxInteira == null) {
			maxInteira = 0;
		}
		return maxInteira;
	}
	public void setMaxInteira(Integer maxInteira) {
		this.maxInteira = maxInteira;
	}
	public Integer getMaxMeia() {
		if(maxMeia == null) {
			maxMeia = 0;
		}
		return maxMeia;
	}
	public void setMaxMeia(Integer maxMeia) {
		this.maxMeia = maxMeia;
	}
	public Integer getValorInteira() {
		if(valorInteira == null) {
			valorInteira = 0;
		}
		return valorInteira;
	}
	public void setValorInteira(Integer valorInteira) {
		this.valorInteira = valorInteira;
	}
	public Integer getValorMeia() {
		if(valorMeia == null) {
			valorMeia = 0;
		}
		return valorMeia;
	}
	public void setValorMeia(Integer valorMeia) {
		this.valorMeia = valorMeia;
	}
	public Integer getValorMaximo() {
		if(valorMaximo == null) {
			valorMaximo = 0;
		}
		return valorMaximo;
	}
	public void setValorMaximo(Integer valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	
	
}
