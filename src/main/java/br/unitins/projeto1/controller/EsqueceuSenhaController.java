package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.projeto1.application.Email;
import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.EsqueceuSenha;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.UsuarioRepository;

@Named
@ViewScoped
public class EsqueceuSenhaController extends Controller<EsqueceuSenha> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8745807031388142584L;
	private String email;

	public void enviarEmail() {
		UsuarioRepository repo = new UsuarioRepository();
		Usuario usuario = new Usuario();
		try {
			usuario = repo.findUsuarioByEmail(getEmail());
			if(usuario == null) {
				Util.addErrorMessage("E-mail não encontrado.");
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Util.addErrorMessage("Problema ao encotar o email.");
			return;
		}
		Random random = new Random();
		random.nextInt(1000000);
		String codigo = new DecimalFormat("T-000000").format(random.nextInt(100000));
		
		EsqueceuSenha esqueceu = new EsqueceuSenha();
		esqueceu.setUsuario(usuario);
		esqueceu.setUtilizado(false);
		esqueceu.setCodigo(codigo);
		esqueceu.setDataHoraLimite(LocalDateTime.now().plusDays(1));
		
		//implementar o salvar no banco
		setEntity(esqueceu);
		salvarSemValidar();
		
		//enviar email
		Email email = new Email(usuario.getEmail(), 
				"Esqueceu a senha", 
				"Segue o código de recuperar a senha: " +codigo);
		if (!email.enviar()) {
			Util.addErrorMessage("Problema ao enviar o email.");
		} else
			Util.addInfoMessage("Código enviado para seu email.");
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public EsqueceuSenha getEntity() {
		// TODO Auto-generated method stub
		if(entity == null) {
			entity = new EsqueceuSenha();
		} 
		return entity;
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
	
	
}
