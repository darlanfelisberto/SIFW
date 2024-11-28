package br.edu.iffar.fw.classBag.impl;

import br.edu.iffar.fw.classBag.interfaces.Configs;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConfigsCSVImpl implements Configs {

    private List<GrupoProcessamento> listGrupoProcessamentos = new ArrayList<>();

}
