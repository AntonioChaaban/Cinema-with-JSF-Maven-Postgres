package br.unitins.projeto1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;

@Entity
public class Usuario extends DefaultEntity<Usuario>{

	@Column(length = 100)
	private String nome;
	
	@Column(length = 100)
	private String sobreNome;

	@Column(length = 60,unique = true)
	@Email
	private String email;
	
	@Column(length = 100)
	private String senha;
	
	@Column(length = 100)
	private String dicaSenha;
	
	@Column(length = 3)
	private Perfil perfil;

	@OneToOne
	private Cartao cartao;
	
	

	

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobreNome() {
		return sobreNome;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getDicaSenha() {
		return dicaSenha;
	}

	public void setDicaSenha(String dicaSenha) {
		this.dicaSenha = dicaSenha;
	}
	
	
}
