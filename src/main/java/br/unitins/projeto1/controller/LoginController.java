package br.unitins.projeto1.controller;

import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.UsuarioRepository;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.Session;

@Named
@ViewScoped
public class LoginController  implements Serializable{
	
	private static final long serialVersionUID = -7453283222587113508L;
	private Usuario usuario;

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
				if (usuarioLogado.getEmail() == null || usuarioLogado.getSenha() == null)
					Util.addErrorMessage("Usuário ou senha inválido.");
				else {
					// Usuario existe com as credenciais
					Session.getInstance().setAttribute("usuarioLogado", usuarioLogado);
					if(usuarioLogado.getPerfil().equals(Perfil.ADMINISTRADOR)) {
						Util.redirect("/Projeto1/faces/MenuPrincipalAdm.xhtml");
					}else {
						Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
					}
					
				}
					
			} catch (Exception e) {
				e.printStackTrace();
				Util.addErrorMessage("Problema ao verificar o Login. Entre em contato pelo email: HyperFilmes@gmail.com.br");
			}
		}
	}
	
	public void cadastrar() {
		Util.redirect("/Projeto1/faces/CadastroUsuario.xhtml");
		
		
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
	
	
}
