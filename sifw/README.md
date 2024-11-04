### Backup imagens

	cd /opt/app/imagens/
	tar -cvzf /home/darlan/backup/imagens-2022-04-18.tar.gz *
	scp darlan@ruproducao.br:/home/darlan/backup/imagens-2022-04-18.tar.gz /home/darlan/backup/

	cd /opt/app/imagens/
	tar -xvzf /home/darlan/backup/imagens-2022-04-18.tar.gz 

### Compactar imagens

	pngquant --strip --force --ext .png /opt/app/imagens/*

### Copiar base

	pg_dump --format plain --no-owner --file /home/darlan/backup/keycloak17-21-03.backup keycloak17

	pg_dump --format plain --no-owner --file /tmp/base-2023-03-08.backup base --exclude-table-data 'public.log' --exclude-table-data 'log.leituras'

	scp darlan@ruproducao.br:/home/darlan/backup/keycloak17-21-03.backup /home/darlan/backup/

	scp darlan@ruproducao.br:/home/darlan/backup/base-21-03.backup /home/darlan/backup/


# webcam fake

	modprobe v4l2loopback card_label="My Fake Webcam" exclusive_caps=1
	ffmpeg -stream_loop -1 -re -i /home/darlan/acentuacao-grafica-questoes-fcc-parte01-640x360.mp4 -vcodec rawvideo -threads 0 -f v4l2 /dev/video0 
	

### SASS


	/usr/local/bin/sass --update /home/qwerty/git/iffar_java/comendo/src/main/webapp/resources/sass:/home/qwerty/git/iffar_java/comendo/src/main/webapp/resources/css --style=compressed

#### Build externo para o SASS no eclipse

	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
	<launchConfiguration type="org.eclipse.ui.externaltools.ProgramBuilderLaunchConfigurationType">
	    <booleanAttribute key="org.eclipse.debug.ui.ATTR_LAUNCH_IN_BACKGROUND" value="false"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_LOCATION" value="/usr/local/bin/sass"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_RUN_BUILD_KINDS" value="full,incremental,auto,clean"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_TOOL_ARGUMENTS" value="--update ${workspace_loc:/comendo/src/main/webapp/resources/sass}:${workspace_loc:/comendo/src/main/webapp/resources/css}  --style=compressed"/>
	    <booleanAttribute key="org.eclipse.ui.externaltools.ATTR_TRIGGERS_CONFIGURED" value="true"/>
	    <stringAttribute key="org.eclipse.ui.externaltools.ATTR_WORKING_DIRECTORY" value="${workspace_loc:/comendo/src/main/webapp/resources}"/>
	</launchConfiguration>


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



### Para os relatórios em PDF
	
	sudo apt install fontconfig ttf-dejavu

			
### EMAIL
	
Jakarta mail tem suporte nativo ao Xoauth do google

	/subsystem=mail/mail-session=default/server=smtp/:write-attribute(name=username,value=email@iffarroupilha.edu.br)
	/subsystem=mail/mail-session=default/server=smtp/:write-attribute(name=password,value=senha)
	/subsystem=mail/mail-session=default/server=smtp/:write-attribute(name=tls,value=true)
	/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=mail-smtp/:write-attribute(name=host,value=smtp.gmail.com)
	/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=mail-smtp/:write-attribute(name=port,value=587)
 
Agora em ambos os servidores, no jboss-cli execute o seguinte:

	$ /subsystem=datasources:installed-drivers-list
	$ module add --name=org.postgresql.jdbc --resources=/opt/darlan/postgresql-42.6.0.jar  --dependencies=javax.api,javax.transaction.api
	$ /subsystem=datasources/jdbc-driver=postgresql:add(driver-name="postgresql", driver-module-name="org.postgresql.jdbc", driver-class-name="org.postgresql.Driver", driver-xa-datasource-class-name="org.postgresql.xa.PGXADataSource")


ATENÇÃO! somente no keycloak

	$ /subsystem=datasources/data-source=KeycloakDS:remove()
	$ /subsystem=datasources/data-source=KeycloakDS:add(driver-name=postgresql,jndi-name="java:jboss/datasources/KeycloakDS",connection-url="jdbc:postgresql://localhost:5432/keycloak",user-name=USER, password=senha)

ATENÇÃO! somente no wildfly app

	$ /subsystem=datasources/data-source=baseDS:add(driver-name=postgresql,jndi-name="java:jboss/datasources/baseDS",connection-url="jdbc:postgresql://localhost:5432/base",user-name=keyuser, password=12345678)

### Poll de conexões

	$ /subsystem=datasources/data-source=baseDS:write-attribute(name=min-pool-size,value=10)
	$ /subsystem=datasources/data-source=baseDS:write-attribute(name=max-pool-size,value=50)



### ---------------- proxy reverso-------------------------

### Habilita proxy reverso nos wildfly

executar nos dois servidores de aplicação, pelo jboss-cli

	/subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=proxy-address-forwarding,value=true)
	/subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=redirect-socket,value=https)


#### cria virtual hosts
	
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

### Configurando o OIDC:

Utilize no terminal execute os camandos abaixo 

	wildfly_intall/bin/standalone.sh 

espere o AS  subri e execute em outro terminal

	wildfly_intall/bin/jboss-cli.sh --connect 

	/subsystem=elytron-oidc-client/secure-deployment="sifw.war"/:add( \
		realm="iffar", \
		resource="sifw", \
		auth-server-url=<#AUTH_SERVER_URL#>/auth/, \
		ssl-required=EXTERNAL)

	/subsystem=elytron-oidc-client/secure-deployment=sifw.war/credential=secret:add(secret=3fd70ff4-fe2b-47b6-a8a3-cd1cf281a937)


### Deploy pela interface web

No AS com acesso externo apenas liberado por proxy pela para a porta 80/8080, temos que fazerum tunel ssh: 

	ssh user@ip -L 127.0.0.1:9990:127.0.0.1:9990
	
Após, abrir no navedago http://127.0.0.1:9990/console/index.html#deployments

