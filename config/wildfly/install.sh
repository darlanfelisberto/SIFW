#!/bin/bash

_DEBUG="on"

function DEBUG(){
    [ "$_DEBUG" == "on" ] && $@
}

function usage() { 
    printf "%s -i /opt -e darlan@gmail.com -s senha -a localhostr:8080 -w wildfly -j jar\n" "$0"; 
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
                WILDFLY="${1:?$(usage)}" 
                ;; 
            -j|--jarPostgres) 
                shift; 
                JAR_POSTGRES="${1:?$(usage)}" 
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
    DEBUG rm -R /usr/lib/jvm/jdk17
    DEBUG rm -R /usr/lib/jvm/jdk-${JDK}
    DEBUG rm -R wildfly-$WILDFLY.tar.gz
    DEBUG rm -R wildfly.*
    DEBUG rm -R launch.sh 
    DEBUG rm $JAR_POSTGRES'.jar'
    DEBUG rm jdk-${JDK}_linux-x64_bin.tar.gz
    DEBUG rm configure-wildfly.cli
    DEBUG rm /etc/systemd/system/wildfly.service 
    DEBUG rm -R /etc/wildfly

    echo "Limpo."
}

function installSifw(){
    DEBUG mkdir -p $PATH_INSTALL;
    DEBUG mkdir -p $PATH_INSTALL"/imagens";
    fileContantes;
    java;
    wildfly;
}

function fileContantes(){
    # escape / for \/ of path
    DEBUG scapeStrings $PATH_INSTALL INS;	

    line='\"'$INS'\";\/\/<#PATH#>'

	DEBUG sed -i "s/\"[a-z\/]*\";\/\/<#PATH#>/$line/g" ../../classShared/src/main/java/br/edu/iffar/fw/classShared/constantes/InitConstantes.java

    DEBUG sed 's/<#PATH#>/'$INS'/g' _configuration_linux.properties > configuration_linux.properties
    DEBUG sed -i 's/<#AUTH_SERVER_URL#>/'$AUTH_SERVER_URL'/g' configuration_linux.properties
	DEBUG cp configuration_linux.properties  $PATH_INSTALL/configuration_linux.properties
}

function java(){
	DEBUG mkdir -p /usr/lib/jvm
	DEBUG wget -cv https://download.oracle.com/java/17/archive/jdk-${JDK}_linux-x64_bin.tar.gz
	DEBUG tar -xvzf jdk-${JDK}_linux-x64_bin.tar.gz -C /usr/lib/jvm
	DEBUG ln -s /usr/lib/jvm/jdk-${JDK} /usr/lib/jvm/jdk17

	# DEBUG update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk17/bin/java 10
}

function wildfly(){
	DEBUG wget -cv https://jdbc.postgresql.org/download/$JAR_POSTGRES.jar
	DEBUG wget -cv https://github.com/wildfly/wildfly/releases/download/$WILDFLY/wildfly-$WILDFLY.tar.gz;

	DEBUG tar -xvzf "wildfly-$WILDFLY.tar.gz" -C $PATH_INSTALL

    
	DEBUG ln -s "$PATH_INSTALL/wildfly-$WILDFLY" "$PATH_INSTALL/wildfly"
	DEBUG $PATH_INSTALL/wildfly/bin/add-user.sh -u manager -p manager
    DEBUG echo "Sleep de 10 segundos..."
    DEBUG sleep 1
	DEBUG $PATH_INSTALL/wildfly/bin/standalone.sh &
	DEBUG echo "aguardando 10 segundos..."
	DEBUG sleep 10
	
    DEBUG sed -i    's/#JAVA_HOME.*/JAVA_HOME=\/usr\/lib\/jvm\/jdk17/g' $PATH_INSTALL/wildfly/bin/standalone.conf
    DEBUG sed       's/<#JAR_POSTGRES#>/'$JAR_POSTGRES'/g'          _configure-wildfly.cli > configure-wildfly.cli
	DEBUG sed -i    's/<#EMAIL#>/'$EMAIL'/g'                        configure-wildfly.cli
	DEBUG sed -i    's/<#SENHA_EMAIL#>/'$SENHA_EMAIL'/g'            configure-wildfly.cli
    DEBUG sed -i    's/<#AUTH_SERVER_URL#>/'$AUTH_SERVER_URL'/g'    configure-wildfly.cli

    DEBUG $PATH_INSTALL/wildfly/bin/jboss-cli.sh --connect --file=configure-wildfly.cli
	DEBUG $PATH_INSTALL/wildfly/bin/jboss-cli.sh --connect --command=shutdown
	
	DEBUG groupadd -r wildfly
	DEBUG useradd -r -g wildfly -d $PATH_INSTALL -s /sbin/nologin wildfly

	DEBUG chown -R wildfly:wildfly $PATH_INSTALL
	
    DEBUG mkdir -p /etc/wildfly

    # escape / for \/ of PATH_INSTALL
    DEBUG scapeString $PATH_INSTALL INS;

    DEBUG sed 's/<#PATH#>/'$INS'/g' _launch.sh > launch.sh
    DEBUG sed 's/<#PATH#>/'$INS'/g' _wildfly.conf > wildfly.conf
    DEBUG sed 's/<#PATH#>/'$INS'/g' _wildfly.service > wildfly.service
    
    DEBUG cp wildfly.conf /etc/wildfly/
    DEBUG cp wildfly.service /etc/systemd/system/
    DEBUG cp launch.sh $PATH_INSTALL/wildfly/bin/
    DEBUG chmod +x $PATH_INSTALL/wildfly/bin/launch.sh
#   DEBUG systemctl start wildfly.service
#   DEBUG systemctl enable wildfly.service
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

function scapeString(){
    # -n do declre faze um "passgem por referencia"
    declare -n OUTPUT=$2
    auth=""
    for ((i=1;i<=${#1};i++))
    do
        ca=${1:$i-1:1}
        if [ $ca = "/" ]; then
            auth=$auth"\\"$ca
        else
            auth=$auth$ca
        fi
    done
    OUTPUT=$auth;
};

WILDFLY="29.0.0.Final";
PATH_INSTALL="/opt/sifw";
EMAIL="";
SENHA_EMAIL="";
AUTH_SERVER_URL="http://localhost:8080";
JAR_POSTGRES="postgresql-42.6.0";
JDK="17.0.7";

DEBUG scapeStrings $AUTH_SERVER_URL AUTH_SERVER_URL

printf ">%s< >%s< >%s< >%s< >%s< >%s< >%s< >%s< \n" $WILDFLY $PATH_INSTALL $EMAIL $SENHA_EMAIL $AUTH_SERVER $JAR_POSTGRES $JDK;


DEBUG main "$@"

installSifw;

#-i /opt -e darlan@gmail.com -s senha -a localhostr:8080 -w wildfly -j jar

