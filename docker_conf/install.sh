#!/bin/bash

###
### instale o git bash
### sudo ./install.sh -i /opt/sifw -e darlanfelisberto@gmail.com -s senhaemail -a http://localhost:8080 -o l 
### acresente o -c para limpar o diretorio de instalacao
###

source ../sifw/src/main/resources/config_sifw.properties

_DEBUG="on"
WIN=l;

function DEBUG(){
    echo $@
    [ "$_DEBUG" == "on" ] && $@
}

function usage() { 
    printf "sudo ./install.sh -i /opt -e darlanfelisberto@gmail.com -s senha -a localhost:8080 -o l\n"; 
    return 1; 
}; 

function main() { 
    while [ "$#" -ge 1 ]; do
        case "$1" in 
            -i|--install) 
                shift;
                PATH_INSTALL="${1:?$(usage)}";
                echo " PATH_INSTALL " $1
                ;; 
            -e|--email) 
                shift; 
                EMAIL="${1:?$(usage)}" 
                ;; 
            -s|--senhaEmail) 
                shift; 
                SENHA_EMAIL="${1:?$(usage)}" 
                ;; 
            -a|--authServerUrl) 
                shift; 
                AUTH_SERVER_URL="${1:?$(usage)}" 
                ;; 
            -w|--wildfly) 
                shift; 
                VERSION_WILDFLY="${1:?$(usage)}" 
                ;; 
            -j|--jarPostgres) 
                shift; 
                VERSION_JAR_POSTGRES="${1:?$(usage)}" 
                ;;
            -c|--clear) 
                shift; 
                removeInstalacao;
                exit 0;
                ;;
            -h|--help) 
                shift; 
                usage;
                exit 0;
                ;;
            -o) 
                shift; 
                WIN="${1:?$(usage)}"
                ;; 
            *) 
                usage;
                return 1 
                ;; 
        esac; 
        shift;
    done; 
};

function removeInstalacao(){
    DEBUG rm -R $PATH_INSTALL
    DEBUG rm -R wildfly.*
    DEBUG rm -R launch.sh
#    DEBUG rm $VERSION_JAR_POSTGRES'.jar'
#    DEBUG rm jdk-${VERSION_JDK}_linux-x64_bin.tar.gz
    DEBUG rm /etc/systemd/system/wildfly.service
    DEBUG rm -R /etc/wildfly

    echo "Limpo."
}

function installDev(){
    echo "Instalando"
    DEBUG mkdir -p $PATH_INSTALL;
    DEBUG mkdir -p $PATH_INSTALL"/imagens";
    java;
    wildfly;
}

function java(){
  if [ "$WIN" = w ]; then
    downloadJava jdk-${VERSION_JDK}_windows-x64_bin.zip
	  DEBUG unzip jdk-${VERSION_JDK}_windows-x64_bin.zip -d $PATH_INSTALL
    #windowns problema com links synbolicos, git bash, apeans rename
    DEBUG mv $PATH_INSTALL/jdk-${VERSION_JDK} $PATH_INSTALL/jdk${JDK_LINK_NAME}
    DEBUG setx JAVA_HOME $PATH_INSTALL/jdk${JDK_LINK_NAME}
  else
   	downloadJava jdk-${VERSION_JDK}_linux-x64_bin.tar.gz 
	  DEBUG tar -xvzf jdk-${VERSION_JDK}_linux-x64_bin.tar.gz -C $PATH_INSTALL
    DEBUG mv $PATH_INSTALL/jdk-${VERSION_JDK} $PATH_INSTALL/jdk${JDK_LINK_NAME}
    export JAVA_HOME=$PATH_INSTALL/jdk${JDK_LINK_NAME}
    export PATH=$JAVA_HOME/bin:$PATH
  fi

  sleep 10;
}

function downloadJava(){
  echo $1;
  if [ -e $1 ]; then
    echo 'File:'$1' já existe.'
  else
    DEBUG curl -L -O https://download.oracle.com/java/${JDK_LINK_NAME}/archive/$1
  fi
}

function downloadPostgresJar(){
  if [ -e $1 ]; then
    echo 'File:'$1' já existe.'
  else
    DEBUG curl -L -O https://jdbc.postgresql.org/download/$1
  fi
}

function downloadWildfly(){
  if [ -e $1 ]; then
    echo 'File:'$1' já existe.'
  else
    DEBUG curl -L -O https://github.com/wildfly/wildfly/releases/download/$VERSION_WILDFLY/$1
  fi
}

function wildfly(){
  ROOT_PATH_WILDFLY=$PATH_INSTALL/wildfly-$VERSION_WILDFLY

	downloadPostgresJar $VERSION_JAR_POSTGRES.jar
	downloadWildfly wildfly-$VERSION_WILDFLY.tar.gz

	DEBUG tar -xvzf "wildfly-$VERSION_WILDFLY.tar.gz" -C $PATH_INSTALL

  DEBUG scapeStrings $PATH_INSTALL INS;

	DEBUG $ROOT_PATH_WILDFLY/bin/add-user.sh -u manager -p manager

  DEBUG scapeStrings $PWD PWD_SCA ;


  echo "Configurando Wildfly."
  if [ "$WIN" = l ]; then
    sed -i '1i JBOSS_JAVA_SIZING=" -XX:+UseZGC -XX:+ZGenerational -Xms256m -Xmx8096m -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=512m "'   $ROOT_PATH_WILDFLY/bin/standalone.conf
    $ROOT_PATH_WILDFLY/bin/jboss-cli.sh --file=configure-wildfly.cli --properties=config.properties

    inicializarComSys;

    echo "Pronto tudo instalado."
    echo "Para instalar como servico, utilize os aquivos launch.sh wildfly.conf e wildfly.service"
  else
    echo 'no cmd executes os comandos:'
#    verificar,fazer como no linux se funcionar no windowns
    echo $ROOT_PATH_WILDFLY'/bin/standalone.bat &'
    echo $ROOT_PATH_WILDFLY'/bin/jboss-cli.bat --connect --file='$PWD'/configure-wildfly.cli'
    echo $ROOT_PATH_WILDFLY'/bin/jboss-cli.bat --connect --command=shutdown'
  fi;

}

function inicializarComSys(){
  #Somente linux
  ROOT_PATH_WILDFLY=$PATH_INSTALL/wildfly-$VERSION_WILDFLY

  DEBUG groupadd -r wildfly
	DEBUG useradd -r -g wildfly -d $PATH_INSTALL -s /sbin/nologin wildfly

	DEBUG chown -R wildfly:wildfly $PATH_INSTALL

  # TODO esse é so pra desenvolvimento
   DEBUG chmod -R 765 $PATH_INSTALL

  DEBUG mkdir -p /etc/wildfly

  # escape / for \/ of PATH_INSTALL
  DEBUG scapeStrings $ROOT_PATH_WILDFLY INS

  #TODO - remover debug ou usar cp
  sed 's/<#ROOT_PATH_WILDFLY#>/'$INS'/g' _launch.sh > launch.sh
  sed 's/<#ROOT_PATH_WILDFLY#>/'$INS'/g' _wildfly.conf > wildfly.conf
  sed 's/<#ROOT_PATH_WILDFLY#>/'$INS'/g' _wildfly.service > wildfly.service

  DEBUG cp wildfly.conf /etc/wildfly/
  DEBUG cp wildfly.service /etc/systemd/system/
  DEBUG cp launch.sh $ROOT_PATH_WILDFLY/bin/
  DEBUG chmod +x $ROOT_PATH_WILDFLY/bin/launch.sh
#  DEBUG systemctl enable wildfly.service
#  DEBUG systemctl start wildfly.service
}

function scapeStrings(){
  # -n do declre faze um "passgem por referencia"
  declare -n OUTPUT=$2
  auth=""
  anterior=''
  especial='/"'
  for ((i=1;i<=${#1};i++))
  do
    ca=${1:$i-1:1}
    for ((c=1;c<=${#especial};c++))
    do
      esp=${especial:$c-1:1}
      # echo $esp "<<"
      if [ "$anterior" = '\' ]; then
          break;
      fi
      if [ $ca = $esp ]; then
          # echo 'escape '$ca
          auth=$auth'\'
          break
      fi
    done;
    auth=$auth$ca;
    anterior=${auth: -1}
  done
  OUTPUT=$auth;
};

if command -v curl >&2; then
  echo Curl ok
else
  if [ "$WIN" = l ]; then
    apt -y install curl
  else
    echo instale o comando: curl
    exit 1;
  fi
fi


#diretorio atual, path linux e windowns
PWD=$(pwd)

DEBUG main "$@"

#escape caracter / 
DEBUG scapeStrings $AUTH_SERVER_URL AUTH_SERVER_URL

printf ">%s< >%s< >%s< >%s< >%s< >%s< >%s< >%s< \n" $VERSION_WILDFLY $PATH_INSTALL $EMAIL $SENHA_EMAIL $AUTH_SERVER_URL $VERSION_JAR_POSTGRES $VERSION_JDK;

installDev;


