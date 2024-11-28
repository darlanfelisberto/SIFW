package br.edu.iffar.fw.classBag.interfaces;

import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.Unidade;

public interface TreeNodeSearch {

	public String label();

	public boolean search(String termo);

	public UUID getMMId();

	public Unidade getUnidade();

}
