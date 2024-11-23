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
		
	
