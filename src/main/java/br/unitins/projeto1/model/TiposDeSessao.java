package br.unitins.projeto1.model;

import java.util.ArrayList;
import java.util.List;

public enum TiposDeSessao {

	DUBLADO2D(1, "Dublado 2D"), 
	DUBLADO3D(2, "Dublado 3D"), 
	LEGENDADO2D(3, "Legendado 2D"),
	LEGENDADO3D(4,"Legendado 3D");
	
	private int id;
	private String label;
	
	private TiposDeSessao(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static TiposDeSessao valueOf(TiposDeSessao tipoDeSessao) {
		if (tipoDeSessao == null)
			return null;
		
		for (TiposDeSessao dep : TiposDeSessao.values()) {
			if (dep.getId() == tipoDeSessao.getId())
				return dep;
		}
		return null;
	}
	public static String valorString(Integer id) {
		if (id == null)
			return null;
		
		for (TiposDeSessao dep : TiposDeSessao.values()) {
			if (dep.getId() == id)
				return dep.getLabel();
		}
		return null;
	}
	
	public static TiposDeSessao valorString(String nome) {
		if (nome == null)
			return null;
		
		for (TiposDeSessao dep : TiposDeSessao.values()) {
			if (dep.getLabel() == nome)
				return dep;
		}
		return null;
	}
	
	public static List<String> cadastrados(){
		List<String> aux = new ArrayList<String>();
		for (TiposDeSessao dep : TiposDeSessao.values()) {
			aux.add(dep.getLabel());
		}
		return aux;
	}
	
}
