package br.unitins.projeto1.controller;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Usuario;

@Named
@ViewScoped
public class TemplateController implements Serializable {

	private static final long serialVersionUID = -7934765070404036100L;
	
	private Usuario usuarioLogado;
	
	public void encerrarSessao() {
		Session.getInstance().invalidateSession();
		Util.redirect("Login.xhtml");
	}

	public Usuario getUsuarioLogado() {
		// obtendo o usuario da sessao
		if (usuarioLogado == null) {
			usuarioLogado = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		}
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
	
}
