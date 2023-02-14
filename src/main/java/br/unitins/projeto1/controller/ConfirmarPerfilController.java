package br.unitins.projeto1.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Usuario;

@Named
@ViewScoped
public class ConfirmarPerfilController extends Controller<Usuario> implements Serializable{

	private static final long serialVersionUID = -3110871977029561184L;

	private String senhaVerificacao;
	private Boolean autorizado;
	@Override
	public Usuario getEntity() {
		// TODO Auto-generated method stub
		if (entity == null) {
			Usuario aux = new Usuario();
			aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
			if(aux == null) {
				Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
				Usuario aux2 = new Usuario();
				entity = aux2;
			}else {
				entity = aux;
			}
		}
		return entity;
	}

	@PostConstruct
	public void init() {
		setCaminho("AlterarSenhaLogado.xhtml");
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		setEntity(aux);	
	}
	
	public void next() {
		setSenhaVerificacao(Util.hashSHA256(getSenhaVerificacao()));
		if(getEntity().getSenha().equals(getSenhaVerificacao())) {
			setAutorizado(true);
			Session.getInstance().setAttribute("autorizasao", getAutorizado());
			Util.redirect(getCaminho());
		}else {
			Util.redirect("/Projeto1/faces/EditarUsuario.xhtml");
		}
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

	


	public Boolean getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Boolean autorizado) {
		this.autorizado = autorizado;
	}

	public String getSenhaVerificacao() {
		return senhaVerificacao;
	}

	public void setSenhaVerificacao(String senhaVerificacao) {
		this.senhaVerificacao = senhaVerificacao;
	}
	
	

}
