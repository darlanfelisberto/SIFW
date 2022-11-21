##backup imagens

	cd /opt/app/imagens/
	tar -cvzf /home/darlan/backup/imagens-2022-04-18.tar.gz *
	scp darlan@ruproducao.br:/home/darlan/backup/imagens-2022-04-18.tar.gz /home/darlan/backup/

	cd /opt/app/imagens/
	tar -xvzf /home/darlan/backup/imagens-2022-04-18.tar.gz 

##Compactar imagens

	pngquant --strip --force --ext .png /opt/app/imagens/*

##Copiar base

	pg_dump --format plain --no-owner --file /home/darlan/backup/keycloak17-21-03.backup keycloak17

	pg_dump --format plain --no-owner --file /tmp/base-2022-08-22.backup base --exclude-table-data 'public.log' --exclude-table-data 'log.leituras'

	scp darlan@ruproducao.br:/home/darlan/backup/keycloak17-21-03.backup /home/darlan/backup/

	scp darlan@ruproducao.br:/home/darlan/backup/base-21-03.backup /home/darlan/backup/


##eviar war
	
	scp /home/darlan/git/iffar_java/comendo/target/sifw.war darlan@ruproducao.br:/tmp


# webcam fake

	modprobe v4l2loopback card_label="My Fake Webcam" exclusive_caps=1
	ffmpeg -stream_loop -1 -re -i /home/darlan/acentuacao-grafica-questoes-fcc-parte01-640x360.mp4 -vcodec rawvideo -threads 0 -f v4l2 /dev/video0 
	

#SASS


	/usr/local/bin/sass --update /home/qwerty/git/iffar_java/comendo/src/main/webapp/resources/sass:/home/qwerty/git/iffar_java/comendo/src/main/webapp/resources/css --style=compressed

Build externo eclipse

	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
	<launchConfiguration type="org.eclipse.ui.externaltools.ProgramBuilderLaunchConfigurationType">
	    <booleanAttribute key="org.eclipse.debug.ui.ATTR_LAUNCH_IN_BACKGROUND" value="false"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_LOCATION" value="/usr/local/bin/sass"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_RUN_BUILD_KINDS" value="full,incremental,auto,clean"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_TOOL_ARGUMENTS" value="--update ${workspace_loc:/comendo/src/main/webapp/resources/sass}:${workspace_loc:/comendo/src/main/webapp/resources/css}  --style=compressed"/>
	    <booleanAttribute key="org.eclipse.ui.externaltools.ATTR_TRIGGERS_CONFIGURED" value="true"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_WORKING_DIRECTORY" value="${workspace_loc:/comendo/src/main/webapp/resources}"/>
	</launchConfiguration>

#Compia Bases

	scp darlan@ruproducao.br:/home/darlan/backup/keycloak17.backup /home/darlan/backup/
	scp darlan@ruproducao.br:/home/darlan/backup/base.backup /home/darlan/backup/


# base do cria base:
	
	sudo -u postgres psql
	create database keycloak;
	create database base;
	create user USER with encrypted password 'senha';
	grant all privileges on database keycloak to USER;
	CREATE EXTENSION IF NOT EXISTS dblink;
	CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
	CREATE EXTENSION IF NOT EXISTS unaccent;
	

#POSTGRES RESTORE
	
	su postgres
	
	pg_dump --format tar --file /opt/backup/base.backup base 
	
ou para plain(comente a linha --SET default_table_access_method = heap; caso for postgres < 12)
	
	pg_dump --format plain --no-owner --file /opt/backup/base.backup base
	
	scp /opt/backup/base.backup  darlan@ruproducao.br:/tmp/
	sudo systemctl stop wildfly.service 
	
	sudo su postgres
	psql
		drop database base;
		create database base;
	\q	
	pg_restore --role=USER  -d base /tmp/base.backup
	psql -d base -1 -f /tmp/base.backup 



# para o jasper
	
	sudo apt install fontconfig ttf-dejavu

#intalar jvm	

	$ sudo update-alternatives --install /usr/bin/java java /opt/jdk-16.0.2/bin/java 10

# Inicialize o cada servidor de aplicação de separadamente e execute os comandos
 
	$ {work_dir}/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
	
em outro terminal
	
	$ {work_dir}/bin/add-user.sh
	$ {work_dir}/bin/jboss-cli.sh
		comanndo -> connect
 
Agora em ambos os servidores, no jboss-cli execute o seguinte:

	$ /subsystem=datasources:installed-drivers-list
	$ module add --name=org.postgresql.jdbc --resources=/opt/postgresql-42.2.23.jar  --dependencies=javax.api,javax.transaction.api
	$ /subsystem=datasources/jdbc-driver=postgresql:add(driver-name="postgresql", driver-module-name="org.postgresql.jdbc", driver-class-name="org.postgresql.Driver", driver-xa-datasource-class-name="org.postgresql.xa.PGXADataSource")


ATENÇÃO! somente no keycloak

	$ /subsystem=datasources/data-source=KeycloakDS:remove()
	$ /subsystem=datasources/data-source=KeycloakDS:add(driver-name=postgresql,jndi-name="java:jboss/datasources/KeycloakDS",connection-url="jdbc:postgresql://localhost:5432/keycloak",user-name=USER, password=senha)

ATENÇÃO! somento no wildfly app

	$ /subsystem=datasources/data-source=baseDS:add(driver-name=postgresql,jndi-name="java:jboss/datasources/baseDS",connection-url="jdbc:postgresql://localhost:5432/base",user-name=keyuser, password=12345678)
	

ATENÇÃO! altere as portas do servidor de aplicação do keycloak, apenas.

	$ /socket-binding-group=standard-sockets/socket-binding=http:write-attribute(name=port,value=8088)
	$ /socket-binding-group=standard-sockets/socket-binding=https:write-attribute(name=port,value=8448)

	$ /socket-binding-group=standard-sockets/socket-binding=management-http:write-attribute(name=port,value=9999)
	$ /socket-binding-group=standard-sockets/socket-binding=management-https:write-attribute(name=port,value=9998)

	$ /subsystem=undertow/server=default-server/https-listener=https:write-attribute(name=enable-http2,value=false)
	$ /subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=enable-http2,value=false)


poll de coneçoes

	$ /subsystem=datasources/data-source=baseDS:write-attribute(name=min-pool-size,value=10)
	$ /subsystem=datasources/data-source=baseDS:write-attribute(name=max-pool-size,value=50)



registrando certificado do keycloak na jvm(Apenas se for selfsing)

	chrome - click on site icon left to address in address bar, select "Certificate" -> "Details" -> "Export" and save in format "Der-encoded binary, single certificate".
	
	$ sudo update-alternatives --install /usr/bin/keytool keytool /opt/jdk-16.0.2/bin/keytool 10
	
	$ keytool -import -alias ruproducao.br -keystore "/opt/jdk-16.0.1/lib/security/cacerts" -file /home/darlan/Downloads/ruproducao.br

	$ nao execute isso keytool -delete -alias ruproducao.br -cacerts
 
	para listarlist
 	
	$keytool -list -v -cacerts
 
 
##---------------- proxy reverso-------------------------

#Habilita proxy reverso nos wildfly

executar nos dois servidores de aplicação, pelo jboss-cli

	/socket-binding-group=standard-sockets/socket-binding=proxy-https:add(port=443)
	/subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=proxy-address-forwarding,value=true)
	/subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=redirect-socket,value=https)


#cria virtual hosts
	
No arquivo  /etc/apache2/sites-enabled/ruproducao.br.conf 
	
	<VirtualHost *:80>	
	#	RedirectMatch "^((?!\.well-known).)*$" "https://ruproducao.br/sifw"
		ServerAdmin darlan.felisberto@iffarroupilha.edu.br
		ServerName ruproducao.br
		ServerAlias ruproducao.br
		DocumentRoot /var/www/html
		ErrorLog ${APACHE_LOG_DIR}/error.log
		CustomLog ${APACHE_LOG_DIR}/access.log combined
	
		#certboot coloca isso automaticamente pro ssl
		RewriteEngine on
		RewriteCond %{SERVER_NAME} =ruproducao.br
		RewriteRule ^ https://%{SERVER_NAME}%{REQUEST_URI} [END,NE,R=permanent]
	</VirtualHost>


No arquivo /etc/apache2/sites-enabled/proxy_reverso.conf 

	<IfModule mod_ssl.c>
		<VirtualHost ruproducao.br:443>
			ServerAdmin darlan.felisberto@hostname
			ErrorLog ${APACHE_LOG_DIR}/error.log
			CustomLog ${APACHE_LOG_DIR}/access.log combined
			SSLEngine on
			SSLProxyEngine	On
			RequestHeader unset Request-Range
			ProxyRequests Off
			ProxyPreserveHost On
			RequestHeader add X-Forwarded-Ssl on
			RequestHeader set X-Forwarded-Proto "https"
			RequestHeader set X-Forwarded-Port "443"
			SetEnv force-proxy-request-1.0 1
			SetEnv proxy-nokeepalive 1
			SetEnv proxy-initial-not-pooled 1
	
			<Location / >
			    ProxyPass http://localhost:8080/sifw
			    ProxyPassReverse http://localhost:8080/sifw
			</Location>
		
			<Location /auth >
				ProxyPass http://localhost:8088/auth
				ProxyPassReverse http://localhost:8088/auth
			</Location>
			
			<Location /sifw >
				ProxyPass http://localhost:8080/sifw
				ProxyPassReverse http://localhost:8080/sifw
			</Location>
			
			<Location /sifw/jakarta.faces.resource >
				ProxyPass http://localhost:8080/sifw/jakarta.faces.resource
				ProxyPassReverse http://localhost:8080/sifw/jakarta.faces.resource
			</Location>
			
			<Location /webservice >
				ProxyPass http://localhost:8080/webservice
				ProxyPassReverse http://localhost:8080/webservice
			</Location>
		
			#cert boot coloca isso
			ServerName ruproducao.br
			SSLCertificateFile /etc/letsencrypt/live/ruproducao.br/fullchain.pem
			SSLCertificateKeyFile /etc/letsencrypt/live/ruproducao.br/privkey.pem
			Include /etc/letsencrypt/options-ssl-apache.conf
		</VirtualHost>
	</IfModule>

Habilitar mods

	sudo a2enmod proxy_http
	sudo a2enmod proxy_balancer
	sudo a2enmod headers
	sudo a2enmod ssl
	sudo a2enmod proxy

Habilitar vhosts

	sudo a2ensite ruproducao.br
	sudo a2ensite proxy_reverso

testa 

	apachectl configtest








install  jasper reports
http://community.jaspersoft.com/project/jaspersoft-studio/releases



carregar os cursos de frederico
http://dados.iffarroupilha.edu.br/api/v1/cursos.csv?id_municipio=8883&ativo=1


#Configuração do open id keycloak no servidor do sifw wildfly

Abra o aquivo de configuração do servidor de aplicação que vai rodar o sife e no arquivo 

	/wildfly-26.0.0.Final/standalone/configuration/standalone.xml,

coloque dentro da tag	
	
	<subsystem xmlns="urn:wildfly:elytron-oidc-client:1.0"></subsystem>
	
o seginte:

	<secure-deployment name="sifw.war">
	    <realm>iffar</realm>
	    <auth-server-url>https://debian/auth/</auth-server-url>
	    <ssl-required>EXTERNAL</ssl-required>
	    <resource>sifw</resource>
	    <credential name="secret" secret="3fd70ff4-fe2b-47b6-a8a3-cd1cf281a937"/>
	</secure-deployment>


### Deploy 

	ssh darlan@ruproducao.br -L 127.0.0.1:9990:127.0.0.1:9990
	
abrir navedagor e acessar http://127.0.0.1:9990/console/index.html#deployments


# How to configure WildFly as a systemd service

== Create a wildfly user

    # groupadd -r wildfly
    # useradd -r -g wildfly -d /opt/wildfly -s /sbin/nologin wildfly

== Install WildFly

    # mkdir /opt/wildfly/
    # tar xvzf keycloak-16.1.0.tar.gz -C /opt/wildfly
    # ln -s /opt/wildfly/keycloak-16.1.0/ /opt/wildfly/keycloak
    # chown -R wildfly:wildfly /opt/wildfly

== Configure systemd

    # mkdir /etc/wildfly
    # cp wildfly.conf /etc/wildfly/
    # cp wildfly.service /etc/systemd/system/keycloak.service
    # cp launch.sh /opt/wildfly/keycloak/bin/
    # chmod +x /opt/wildfly/keycloak/bin/launch.sh

== Start and enable

    # systemctl start keycloak.service
    # systemctl enable keycloak.service
