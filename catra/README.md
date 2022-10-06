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


	
