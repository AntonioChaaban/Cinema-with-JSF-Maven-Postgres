package br.unitins.projeto1.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.Repository;
import br.unitins.projeto1.repository.UsuarioRepository;

@Named
@ViewScoped
public class AlterarSenhaLogadoController extends Controller<Usuario> implements Serializable{

	private static final long serialVersionUID = -538179950326149639L;

	private String senha;
	private String confirmarSenha;
	
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
		setCaminho("MenuPrincipal.xhtml");
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		UsuarioRepository repo = new UsuarioRepository();
		aux = repo.findById(aux.getId());
		Session.getInstance().setAttribute("usuarioLogado", aux);
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		Boolean autorizado;
		autorizado = (Boolean) Session.getInstance().getAttribute("autorizasao");
		if(aux == null || autorizado == null || autorizado == false) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
			Usuario aux2 = new Usuario();
			setEntity(aux2);
		}else {
			setEntity(aux);
		}
		System.out.println(getEntity().getNome());
		System.out.println(getEntity().getEmail());
		System.out.println(getEntity().getSobreNome());
	}
	
	public void alterarUsuario() {
		if(validarDados()) {
			UsuarioRepository repo = new UsuarioRepository();
			Usuario usu = new Usuario();
			Repository<Usuario> repoUsu = new Repository<Usuario>();
			
			try {
				usu = repo.findById(getEntity().getId());
				usu.setSenha(Util.hashSHA256(getSenha()));
				repoUsu.beginTransaction();
				repoUsu.save(usu);
				repoUsu.commitTransaction();
				usu = repo.findById(getEntity().getId());
				Session.getInstance().setAttribute("usuarioLogado", usu );
				Boolean autorizado = false;
				Session.getInstance().setAttribute("autorizasao",autorizado);
				Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		System.out.println("passoaq2");
		if(getSenha() == null || getConfirmarSenha() == null) {
			Util.addErrorMessage("Informe a senha");
			return false;
		}
		if(getSenha().isBlank() || getSenha().equals(null)) {
			Util.addErrorMessage("Informe a senha");
			return false;
		}
		if(getConfirmarSenha().isBlank() || getConfirmarSenha().equals(null)) {
			Util.addErrorMessage("Confirme a senha");
			return false;
		}
		if(!getSenha().equals(getConfirmarSenha())) {
			Util.addErrorMessage("As duas Senhas estão diferentes");
			return false;
		}
		return true;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
}
