package br.unitins.projeto1.model;

public enum Habilitador {
	
	HABILITADO(1, "Habilitado"), 
	DESABILITADO(2, "Desabilitado");
	
	private Integer id;
	private String label;
	
	private Habilitador(Integer id, String label) {
		this.id = id;
		this.label = label;
	}

	public Integer getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static TiposDeSessao valueOf(Integer id) {
		if (id == null)
			return null;
		
		for (TiposDeSessao dep : TiposDeSessao.values()) {
			if (dep.getId() == id)
				return dep;
		}
		return null;
	}
	
}
