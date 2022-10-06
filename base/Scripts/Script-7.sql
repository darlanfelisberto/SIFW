CREATE TABLE PUBLIC."usuarios" (
	"usuario_id" uuid NOT NULL,
	"cpf" VARCHAR(255),
	"dt_nasc" DATE,
	"email" VARCHAR(255),
	"nome" VARCHAR(255),
	"token_ru" VARCHAR(255),
	"username" VARCHAR(255),
	CONSTRAINT CONSTRAINT_A PRIMARY KEY ("usuario_id")
);
CREATE UNIQUE INDEX PRIMARY_KEY_A ON PUBLIC."usuarios" ("usuario_id");


CREATE TABLE PUBLIC."saldo_usuario" (
	"saldo_usuario_id" VARCHAR(255) NOT NULL,
	"saldo" DOUBLE,
	"usuario_id" uuid NOT NULL,
	CONSTRAINT CONSTRAINT_1A PRIMARY KEY ("saldo_usuario_id"),
	CONSTRAINT "FK569usc7ge4q6989vykyrn1qrm" FOREIGN KEY ("usuario_id") REFERENCES PUBLIC."usuarios"("usuario_id") ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX "FK569usc7ge4q6989vykyrn1qrm_INDEX_1" ON PUBLIC."saldo_usuario" ("usuario_id");
CREATE UNIQUE INDEX PRIMARY_KEY_1A ON PUBLIC."saldo_usuario" ("saldo_usuario_id");
