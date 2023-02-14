package br.unitins.projeto1.model;

import java.util.ArrayList;
import java.util.List;

public enum Perfil {

	ADMINISTRADOR(1, "Administrador"), 
	CLIENTE(2, "Cliente");
	
	private int id;
	private String label;
	private List<String> paginasComPermissao = null;
	
	Perfil(int id, String label) {
		this.id = id;
		this.label = label;
		paginasComPermissao = new ArrayList<String>();
		if (id == 1) {
			paginasComPermissao.add("pages/CadastroFilmes.xhtml");
			paginasComPermissao.add("pages/CadastroSala.xhtml");
			paginasComPermissao.add("pages/CadastroTipoSala.xhtml");
			paginasComPermissao.add("pages/TransasaoCadastroSessao.xhtml");
			paginasComPermissao.add("pages/CadastroCartao.xhtml");
			paginasComPermissao.add("pages/FinalizarCompra.xhtml");
		} else if (id == 2) {
			paginasComPermissao.add("pages/CadastroCartao.xhtml");
			paginasComPermissao.add("pages/FinalizarCompra.xhtml");
		} 
	}
	
	public int getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public List<String> getPaginasComPermissao() {
		return paginasComPermissao;
	}
	
	public static Perfil valueOf(Integer id) {
		if (id == null)
			return null;
		
		for (Perfil dep : Perfil.values()) {
			if (dep.getId() == id)
				return dep;
		}
		return null;
	}	
}
