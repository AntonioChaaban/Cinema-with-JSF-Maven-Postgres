package br.unitins.projeto1.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Session;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.Cartao;
import br.unitins.projeto1.model.Perfil;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.UsuarioRepository;

@Named
@ViewScoped
public class CadastrarCartaoController extends Controller<Cartao> implements Serializable{

	private static final long serialVersionUID = 6142531441083450917L;

	
	@Override
	public Cartao getEntity() {
		// TODO Auto-generated method stub
		if (entity == null)
			entity = new Cartao();
		return entity;
	}
	
	@PostConstruct
	public void init() {
		if(Session.getInstance().getAttribute("usuarioLogado") == null) {
			Util.redirect("/Projeto1/faces/MenuPrincipal.xhtml");
		}
	}

	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		if (entity == null) {
			Util.addErrorMessage("Os campos devem ser informado.");
			return false;
		}
		if (getEntity().getNumeroNoCartao().isBlank()) {
			Util.addErrorMessage("O campo nome deve ser informado.");
			return false;
		}
		if (getEntity().getCvc().isBlank()) {
			Util.addErrorMessage("O campo sobre nome deve ser informado.");
			return false;
		}
		if (getEntity().getDataDeValidade().isBlank() || getEntity().getDataDeValidade().equals(null)) {
			Util.addErrorMessage("O campo email deve ser informado.");
			return false;
		}
		if (getEntity().getNomeNoCartao().isBlank()) {
			Util.addErrorMessage("O campo senha deve ser informado.");
			return false;
		}	
		salvarSemValidar();
		Usuario aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		UsuarioRepository repo = new UsuarioRepository();
		aux.setCartao(getEntity());
		try {
			repo.save(aux);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public void salvarCartao() {
		if(validarDados()) {
			Usuario aux = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
			UsuarioRepository repo = new UsuarioRepository();
			try {
				repo.beginTransaction();
				repo.save(aux);
				repo.commitTransaction();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				Util.addErrorMessage("Não salvou no usuario deu erro");
				e.printStackTrace();
			}
			if(aux.getPerfil().equals(Perfil.CLIENTE)) {
				Util.redirect("MenuPrincipal.xhtml");
				
			}else {
				Util.redirect("MenuPrincipalAdm.xhtml");
				
			}
		}
	}

	@Override
	public boolean validarDadosParaExcluir() {
		// TODO Auto-generated method stub
		return false;
	}

}
