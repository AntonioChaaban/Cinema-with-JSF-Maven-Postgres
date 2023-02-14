package br.unitins.projeto1.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.application.VersionException;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.UsuarioRepository;

@Named
@ViewScoped
public class EditarUsuarioController extends Controller<Usuario> implements Serializable{

	private static final long serialVersionUID = 4062437947212787327L;


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
		if(aux == null) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}else {
			UsuarioRepository repo = new UsuarioRepository();
			aux = repo.findById(aux.getId());
			Session.getInstance().setAttribute("usuarioLogado", aux);
			aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
			if(aux == null) {
				Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
				Usuario aux2 = new Usuario();
				setEntity(aux2);
			}else {
				setEntity(aux);
			}
		}
	}
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void salvarUsuario() {
		UsuarioRepository repo = new UsuarioRepository();
		Usuario aux = new Usuario();
		aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		try {
			salvarComTransisao();
			aux = repo.findById(aux.getId());
			Session.getInstance().setAttribute("usuarioLogado", aux);
		} catch (VersionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}

}
