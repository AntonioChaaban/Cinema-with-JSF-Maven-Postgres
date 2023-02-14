package br.unitins.projeto1.model;

public enum EstadoFilme {

	EMCARTAZ(1, "Em cartaz"), 
	FORADECARTAZ(2, "Fora de cartaz");
	
	private int id;
	private String label;
	
	private EstadoFilme(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
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
