# Criar base _teste
	$ sudo su
	# su postgres
	$ psql
		CREATE ROLE catrauser SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN PASSWORD 'USER';
		CREATE DATABASE catra;
		\q

#Restaurando o template da base

o arquivo base_create.sql é extraido pela ferramenta dbaever e talvez será necessario algumas alterações

	$ //psql -d catra -1 -f /home/darlan/git/iffar_java/catra/base_create.sql
	$ sudo su
	$ su postgres
	$ psql catraca
	
		\i /opt/catra/base_create.sql
	
	


# 	Para compilar

Compile os projetos classBag e catra, da seguinte forma:

	$ mvn clean install

ou

	Eclipse > run as > maven build
		colocar no campo goals clean install
	

#Para executar o app

o jar e todas as configurações necessarias estao em catra/target/catra. Para executar façao seguinte(linux)

	# mv target/app/ /opt/app
	# chmod -R 777 /opt/app

	$ java -jar catra-1.0-SNAPSHOT.jar 
	
ou

	$ nano /home/ru/.local/share/applications/_catra2.desktop 
	
	[Desktop Entry]
	Type=Application
	Version=1.0
	Name=RU CATRACA
	Comment=RU CATRACA
	Exec=java -Duser.dir=/opt/app -jar /opt/app/catra-1.0-SNAPSHOT.jar
	Terminal=true
	StartupNotify=false
	Path=/opt/app
		
		
usar o netbeans para abrir o form, achei meio chato o do eclipse e a alternativa so roda no java1.8(jfromdesing)

    curl --insecure -L -X POST 'https://localhost:18443/auth/realms/iffar_fw/protocol/openid-connect/auth' \
	-H 'Content-Type: application/x-www-form-urlencoded' \
	--data-urlencode 'client_id=clientid' \
	--data-urlencode 'grant_type=password' \
	--data-urlencode 'client_secret=556a01a1-1873-4992-ac91-a246a24a8f62' \
	--data-urlencode 'scope=openid' \
	--data-urlencode 'username=USER' \
	--data-urlencode 'password=senha'

### rodar server e criar um novo banco e ele nao existir

	java -cp h2*.jar org.h2.tools.Server -ifNotExists


https://sso.staging.acesso.gov.br/login?client_id=pgd-test.iffarroupilha.edu.br&authorization_id=19344951626

https://pgd-test.iffarroupilha.edu.br/login-unico/pgd-test.iffarroupilha.edu.br?code=eyJraWQiOiJjb2RlQ3J5cHRvZ3JhcGh5IiwiYWxnIjoiZGlyIiwiZW5jIjoiQTI1NkdDTSJ9..65BSDQWlf3LfVJ4S.60RNmRIVZQeMwSv88epDhvylcMCisdK-F7Tw2631ywvEBw.ALBPJzFk2GDJK08VGfb3Hg&state=0VmwXKTJ7vgsbsT4dRU2rRbtcUAA0eAnU9er0Qgf

	
curl --insecure -L -X POST 'https://sso.staging.acesso.gov.br/token' \
-H 'Content-Type: application/x-www-form-urlencoded' \
-H 'Authorization: Basic cGdkLXRlc3QuaWZmYXJyb3VwaWxoYS5lZHUuYnI6RVVGWE1DUnZlcldsYjV3Z2EtVHAwNE9jaWphN3QxZnUxTERwdlNxQjAzVWRiekZOMXZRc3FtS3NsUWFleXRIbXZTWTMxTUFrQXFkMVFNaUl4NWVTOUE=' \
-d 'grant_type=authorization_code' \
-d 'code=eyJraWQiOiJjb2RlQ3J5cHRvZ3JhcGh5IiwiYWxnIjoiZGlyIiwiZW5jIjoiQTI1NkdDTSJ9..yRRvNkKcHxbkBttu.icEEU3Om2HhTpcBjsk0TE24IUgvfKO3xhWfpONbx9hpRDA.l5pOJNU91iHzpyLI_cVU2w' \
-d 'redirect_uri=https%3A%2F%2Fpgd-test.iffarroupilha.edu.br%2Flogin-unico%2Fpgd-test.iffarroupilha.edu.br' \
-d 'code_verifier=SHhwtuPCOxZZDmgcOAY-WhvGogFUEd7vR4DQiFmu3UE' \

curl -X POST -d 'grant_type=authorization_code&code=007f89a9-9982-42c7-960b-b09ea2713f38.81c9c808-1509-438d-9649-eea7d8c63c6e.a4685ae1-46fc-413c-b370-84ab6067a9201&redirect_uri=http%3A%2F%2Fappcliente.com.br%2Fphpcliente%2Floginecidadao.Php'&code_verifier='LUnicoAplicacaoCodeVerifierTamanhoComMinimo' https://sso.staging.acesso.gov.br/token



https://sso.staging.acesso.gov.br/authorize?client_id=pgd-test.iffarroupilha.edu.br&redirect_uri=https%3A%2F%2Fpgd-test.iffarroupilha.edu.br%2Flogin-unico%2Fpgd-test.iffarroupilha.edu.br&scope=openid+email+profile+govbr_confiabilidades&response_type=code&state=9n4fvTiJAkcPZGTpAFxvOyGjVWN8j5mxF1hqEFgt&code_challenge=90U3kWptVZpCRjF0cs_BzswfEjW_g6grd403Qi7x6e8&code_challenge_method=S256



code chanllenge
DKdq0DqzcytB_XPJTEmLnGqmiMd3tUlt7kw-r5c2lS8

code verifir
RqGy2wEeJG6P_UQcNBWQPg02PTuLAdujyh0eAbeRdKU