package br.unitins.projeto1.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Usuario;

@Named
@ViewScoped
public class MenuPrincipalAdmController implements Serializable{

	private static final long serialVersionUID = -4566364291634256182L;
	
	private Usuario usuario;
	
	@PostConstruct
	public void init() {
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if(aux == null || (!aux.getPerfil().equals(Perfil.ADMINISTRADOR)) ) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}
		Util.addInfoMessage("Bem vindo!!" + getUsuario().getNome());
	}
	public void sair() {
		Session.getInstance().invalidateSession();
		Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
	}
	public void encerrarSessao() {
		Util.redirect("/Projeto1/faces/Login.xhtml");
		Session.getInstance().invalidateSession();
	}
	
	public void cadastroFilmes() {
		// TODO Auto-generated method stub
		Util.redirect("/Projeto1/faces/CadastroFilmes.xhtml");
	}
	public void cadastroSala() {
		// TODO Auto-generated method stub
		Util.redirect("/Projeto1/faces/CadastroSala.xhtml");
	}
	public void CadastroValorVenda() {
		// TODO Auto-generated method stub
		
		Util.redirect("/Projeto1/faces/CadastroValorVenda.xhtml");
	}
	public void transasaoCadastroSessao() {
		// TODO Auto-generated method stub
		Util.redirect("/Projeto1/faces/TransasaoCadastroSessao.xhtml");
	}
	public void cadastroCartao() {
		// TODO Auto-generated method stub
		Util.redirect("/Projeto1/faces/CadastroCartao.xhtml");
	}
	public void cadastroAdm() {
		// TODO Auto-generated method stub
		Util.redirect("/Projeto1/faces/CadastroUsuario.xhtml");
	}
	public void cadastroTipoSala() {
		// TODO Auto-generated method stub
		Util.redirect("/Projeto1/faces/CadastroTipoSala.xhtml");
	}
	public void menuPrincipal() {
		// TODO Auto-generated method stub
		Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
