package br.unitins.projeto1.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.UsuarioRepository;

@Named
@ViewScoped
public class CadastroUsuarioController extends Controller<Usuario> implements Serializable{

 

	private static final long serialVersionUID = 1L;
	

	@Override
	public Usuario getEntity() {
		if (entity == null)
			entity = new Usuario();
		return entity;
	}
	
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		// escrever validação de dados
		if (entity == null) {
			Util.addErrorMessage("Os campos devem ser informado.");
			return false;
		}
		if (getEntity().getNome().isBlank()) {
			Util.addErrorMessage("O campo nome deve ser informado.");
			return false;
		}
		if (getEntity().getSobreNome().isBlank()) {
			Util.addErrorMessage("O campo sobre nome deve ser informado.");
			return false;
		}
		if (getEntity().getEmail().isBlank() || getEntity().getEmail().equals(null)) {
			Util.addErrorMessage("O campo email deve ser informado.");
			return false;
		}
		UsuarioRepository repo = new UsuarioRepository();
		Usuario aux = new Usuario();
		try {
			aux = repo.findUsuarioByEmail(getEntity().getEmail());
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(aux != null) {
			System.out.println("entrou aqui ");
			Util.addErrorMessage("Esse email já foi cadastrado.");
			return false;
		}
		
		if (getEntity().getSenha().isBlank()) {
			Util.addErrorMessage("O campo senha deve ser informado.");
			return false;
		}
		if (getEntity().getDicaSenha().isBlank()) {
			Util.addErrorMessage("O campo dica para senha deve ser informado.");
			return false;
		}

		String senha = Util.hashSHA256(getEntity().getSenha());
		getEntity().setSenha(senha);
		if(Session.getInstance().getAttribute("usuarioLogado") == null) {
			getEntity().setPerfil(Perfil.CLIENTE);
			setCaminho("Login.xhtml");
		}else {
			getEntity().setPerfil(Perfil.ADMINISTRADOR);
			setCaminho("MenuPrincipalAdm.xhtml");
		}
		System.out.println(getEntity().getSenha());
		return true;
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	
}
