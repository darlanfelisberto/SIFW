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
    DEBUG rm -R /usr/lib/jvm/jdk${JDK_LINK_NAME}
    DEBUG rm -R /usr/lib/jvm/jdk-${VERSION_JDK}
    DEBUG rm -R wildfly-$VERSION_WILDFLY.tar.gz
    DEBUG rm -R wildfly.*
    DEBUG rm -R launch.sh 
    DEBUG rm $VERSION_JAR_POSTGRES'.jar'
    DEBUG rm jdk-${VERSION_JDK}_linux-x64_bin.tar.gz
    DEBUG rm configure-wildfly.cli
    DEBUG rm /etc/systemd/system/wildfly.service 
    DEBUG rm -R /etc/wildfly

    echo "Limpo."
}

function installDevL(){
    DEBUG mkdir -p $PATH_INSTALL;
    DEBUG mkdir -p $PATH_INSTALL"/imagens";
    fileContantesL;
    javaL;
    wildflyL;
}

function fileContantesL(){
    # escape / for \/ of path
    DEBUG scapeStrings $PATH_INSTALL INS;	

    line='\"'$INS'\";\/\/<#PATH#>'

	DEBUG sed -i "s/\"[a-z\/]*\";\/\/<#PATH#>/$line/g" ../../classShared/src/main/java/br/edu/iffar/fw/classShared/constantes/InitConstantes.java

    DEBUG sed 's/<#PATH#>/'$INS'/g' _configuration_linux.properties > configuration_linux.properties
    DEBUG sed -i 's/<#AUTH_SERVER_URL#>/'$AUTH_SERVER_URL'/g' configuration_linux.properties
	DEBUG cp configuration_linux.properties  $PATH_INSTALL/configuration_linux.properties
}

function javaL(){
	DEBUG mkdir -p /usr/lib/jvm
	DEBUG wget -cv https://download.oracle.com/java/${JDK_LINK_NAME}/archive/jdk-${VERSION_JDK}_linux-x64_bin.tar.gz
	DEBUG tar -xvzf jdk-${VERSION_JDK}_linux-x64_bin.tar.gz -C /usr/lib/jvm
	DEBUG ln -s /usr/lib/jvm/jdk-${VERSION_JDK} /usr/lib/jvm/jdk${JDK_LINK_NAME}

	# DEBUG update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk${JDK_LINK_NAME}/bin/java 10
}

function javaW(){
	DEBUG mkdir -p /usr/lib/jvm
	DEBUG wget -cv https://download.oracle.com/java/${JDK_LINK_NAME}/archive/jdk-${VERSION_JDK}_windows-x64_bin.zip
	DEBUG unzip jdk-${VERSION_JDK}_windows-x64_bin.zip -d test
    DEBUG ln -s /usr/lib/jvm/jdk-${VERSION_JDK} /usr/lib/jvm/jdk${JDK_LINK_NAME}

	# DEBUG update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk${JDK_LINK_NAME}/bin/java 10
}

function wildflyL(){
	DEBUG wget -cv https://jdbc.postgresql.org/download/$VERSION_JAR_POSTGRES.jar
	DEBUG wget -cv https://github.com/wildfly/wildfly/releases/download/$VERSION_WILDFLY/wildfly-$VERSION_WILDFLY.tar.gz;

	DEBUG tar -xvzf "wildfly-$VERSION_WILDFLY.tar.gz" -C $PATH_INSTALL

    
	DEBUG ln -s "$PATH_INSTALL/wildfly-$VERSION_WILDFLY" "$PATH_INSTALL/wildfly"

    DEBUG sed -i    's/#JAVA_HOME.*/JAVA_HOME=\/usr\/lib\/jvm\/jdk'${JDK_LINK_NAME}'/g' $PATH_INSTALL/wildfly/bin/standalone.conf

	DEBUG $PATH_INSTALL/wildfly/bin/add-user.sh -u manager -p manager
    DEBUG echo "Sleep de 10 segundos..."
    DEBUG sleep 1
	DEBUG $PATH_INSTALL/wildfly/bin/standalone.sh &
	DEBUG echo "aguardando 10 segundos..."
	DEBUG sleep 10
	
    
    DEBUG sed       's/<#JAR_POSTGRES#>/'$VERSION_JAR_POSTGRES'/g'          _configure-wildfly.cli > configure-wildfly.cli
	DEBUG sed -i    's/<#EMAIL#>/'$EMAIL'/g'                        configure-wildfly.cli
	DEBUG sed -i    's/<#SENHA_EMAIL#>/'$SENHA_EMAIL'/g'            configure-wildfly.cli
    DEBUG sed -i    's/<#AUTH_SERVER_URL#>/'$AUTH_SERVER_URL'/g'    configure-wildfly.cli

    DEBUG $PATH_INSTALL/wildfly/bin/jboss-cli.sh --connect --file=configure-wildfly.cli
	DEBUG $PATH_INSTALL/wildfly/bin/jboss-cli.sh --connect --command=shutdown
	
	DEBUG groupadd -r wildfly
	DEBUG useradd -r -g wildfly -d $PATH_INSTALL -s /sbin/nologin wildfly

	DEBUG chown -R wildfly:wildfly $PATH_INSTALL
    # TODO esse é so pra desenvolvimento
    DEBUG chmod -R 777 $PATH_INSTALL
	
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

function prepareWindowns(){
    WILDFLY="30.0.1.Final";
    PATH_INSTALL="/opt/dev";
    EMAIL="";
    SENHA_EMAIL="";
    AUTH_SERVER_URL="http://localhost:8080";
    JAR_POSTGRES="postgresql-42.6.0";
    JDK="21.0.2";
}


PATH_INSTALL="/opt/sifw";
EMAIL="";
SENHA_EMAIL="";
AUTH_SERVER_URL="http://localhost:8080";
VERSION_JAR_POSTGRES="postgresql-42.6.0";
VERSION_JDK="21.0.2";
JDK_LINK_NAME="21";
VERSION_WILDFLY="30.0.1.Final";

DEBUG scapeStrings $AUTH_SERVER_URL AUTH_SERVER_URL

printf ">%s< >%s< >%s< >%s< >%s< >%s< >%s< >%s< \n" $VERSION_WILDFLY $PATH_INSTALL $EMAIL $SENHA_EMAIL $AUTH_SERVER $VERSION_JAR_POSTGRES $VERSION_JDK;


DEBUG main "$@"

installDevL;

#-i /opt -e darlan@gmail.com -s senha -a localhostr:8080 -w wildfly -j jar

