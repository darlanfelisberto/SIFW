package br.edu.iffar.fw.classBag.db.model.api;

import java.util.UUID;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-05-27T18:13:45.018-0300")
@StaticMetamodel(APIImagen.class)
public class APIImagen_ {
	public static volatile SingularAttribute<APIImagen, UUID> imagenId;
	public static volatile SingularAttribute<APIImagen, String> fileName;
	public static volatile SingularAttribute<APIImagen, APIUsuario> usuario;
}
