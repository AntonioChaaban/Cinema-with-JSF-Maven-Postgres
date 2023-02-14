package br.unitins.projeto1.controller;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.faces.view.ViewScoped;
import javax.inject.Named;


import br.unitins.projeto1.application.RepositoryException;
import br.unitins.projeto1.application.Util;
import br.unitins.projeto1.model.EsqueceuSenha;
import br.unitins.projeto1.model.Usuario;
import br.unitins.projeto1.repository.EsqueceuSenhaRepository;
import br.unitins.projeto1.repository.Repository;
import br.unitins.projeto1.repository.UsuarioRepository;

@Named
@ViewScoped
public class AlterarSenhaController extends Controller<EsqueceuSenha> implements Serializable{

	private static final long serialVersionUID = 2024928279384276673L;
	private String senha;
	private String confirmarSenha;

	@Override
	public EsqueceuSenha getEntity() {
		// TODO Auto-generated method stub
		if(entity == null) {
			entity = new EsqueceuSenha();
		} 
		return entity;
	}

	public void alterar() {
		if(validarDados()) {
			UsuarioRepository repo = new UsuarioRepository();
			Usuario usu = new Usuario();
			Repository<Usuario> repoUsu = new Repository<Usuario>();
			
			try {
				usu = repo.findById(getEntity().getUsuario().getId());
				usu.setSenha(Util.hashSHA256(getSenha()));
				repoUsu.beginTransaction();
				repoUsu.save(usu);
				repoUsu.commitTransaction();
				Util.redirect("/Projeto1/faces/Login.xhtml");
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			Util.addErrorMessage("Codigo invalido");
		}
	}
	@Override
	public boolean validarDados() {
		// TODO Auto-generated method stub
		EsqueceuSenhaRepository repo = new EsqueceuSenhaRepository();
		EsqueceuSenha esq = new EsqueceuSenha();
		System.out.println("passoaq1");
		try {
			esq = repo.findCodigo(getEntity().getCodigo());
			if(esq == null || esq.getCodigo() == null) {
				Util.addErrorMessage("Codigo invalido");
				return false;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			Util.addErrorMessage("problema na consulta");
			e.printStackTrace();
		}
		LocalDateTime now = LocalDateTime.now();
		if(!now.isBefore(esq.getDataHoraLimite())) {
			Util.addErrorMessage("Ultrapassou o tempo de duração do codigo");
			return false;
		}
		if(!getSenha().equals(getConfirmarSenha())) {
			Util.addErrorMessage("As duas Senhas estão diferentes");
			return false;
		}
		setEntity(esq);
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
