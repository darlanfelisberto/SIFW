package br.edu.iffar.fw.classBag.interfaces;

import java.util.List;

import br.edu.iffar.fw.classBag.db.model.Unidade;

public interface SwitchTreeNode {

	public List<? extends TreeNodeSearch> getList(Unidade u);

}
