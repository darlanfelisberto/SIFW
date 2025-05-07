#!/bin/bash

###
### instale o git bash
### sudo ./build_deploy.sh -i /opt/sifw -e darlanfelisberto@gmail.com -s senhaemail -a http://localhost:8080 -o l
### acresente o -c para limpar o diretorio de instalacao
###
source ../sifw/src/main/resources/config_sifw.properties

echo "instalando"

apt -y install git

curl -L -O  https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
tar -xvzf apache-maven-3.9.9-bin.tar.gz -C /tmp/sifw

export JAVA_HOME=$PATH_INSTALL/jdk${JDK_LINK_NAME}

#$JAVA_HOME/bin/java --version

cd /tmp/sifw/git/
git clone https://github.com/feliva/authService.git
git clone https://github.com/feliva/sharedClass.git


cd /tmp/sifw/git/sharedClass
/tmp/sifw/apache-maven-3.9.9/bin/mvn clean install                                                                                                       #compila jar e instala


cd /tmp/sifw/git/authService
/tmp/sifw/apache-maven-3.9.9/bin/mvn clean install
/tmp/sifw/apache-maven-3.9.9/bin/mvn flyway:baseline -DdbIP="${DB_IP}" -DdbUser="${DB_USER}" -DdbPassword="${DB_PASSWD}"
/tmp/sifw/apache-maven-3.9.9/bin/mvn flyway:migrate -DdbIP="${DB_IP}" -DdbUser="${DB_USER}" -DdbPassword="${DB_PASSWD}"
cp ./auth/target/auth.war $PATH_INSTALL/wildfly-$VERSION_WILDFLY/standalone/deployments                                    #copia o war para o diretorio de execução do AS


#é copiado pelo build do docker
cd /tmp/sifw/git/SIFW
/tmp/sifw/apache-maven-3.9.9/bin/mvn clean install
/tmp/sifw/apache-maven-3.9.9/bin/mvn flyway:baseline -DdbIP="${DB_IP}" -DdbUser="${DB_USER}" -DdbPassword="${DB_PASSWD}"
/tmp/sifw/apache-maven-3.9.9/bin/mvn flyway:migrate -DdbIP="${DB_IP}" -DdbUser="${DB_USER}" -DdbPassword="${DB_PASSWD}"
cp ./sifw/target/sifw.war $PATH_INSTALL/wildfly-$VERSION_WILDFLY/standalone/deployments                                    #copia o war para o diretorio de execução do AS


#remoção de lixo
#rm -R /opt/git
#rm -R /root/.m2/
#rm -R /home/wildfly/.m2/



