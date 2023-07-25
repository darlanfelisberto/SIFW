#!/bin/bash

function usage() { 
    printf "%s -i /opt -e darlan@gmail.com -s senha -a localhostr:8080 -w wildfly -j jar\n" "$0"; 
    return 1; 
}; 

function main() { 
  #  req="${1:?$(usage)}";
   # shift; 
    while [ "$#" -ge 1 ]; do
    	echo $1
        case "$1" in 
            -i|--install) 
                shift;
                INSTALL="${1:?$(usage)}";
                echo " install " $1
                ;; 
            -e|--email) 
                shift; 
                EMAIL="${1:?$(usage)}" 
                ;; 
            -s|--senhaEmail) 
                shift; 
                SENHA_EMAIL="${1:?$(usage)}" 
                ;; 
            -a|--authServer) 
                shift; 
                AUTH_SERVER="${1:?$(usage)}" 
                ;; 
            -w|--wildfly) 
                shift; 
                WILDFLY="${1:?$(usage)}" 
                ;; 
            -j|--jarPostgres) 
                shift; 
                JAR_POSTGRES="${1:?$(usage)}" 
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
    printf "req = %s\ns = %s\np = %s\n" "$req" "$s" "$p"; 
};

WILDFLY="29.0.0.Final";
INSTALL="/opt/sifw";
EMAIL="";
SENHA_EMAIL="";
AUTH_SERVER="";
JAR_POSTGRES="postgresql-42.6.0"

printf ">%s< >%s< >%s< >%s< >%s< >%s< >%s< \n" $WILDFLY $INSTALL $EMAIL $SENHA_EMAIL $AUTH_SERVER $JAR_POSTGRES;


WILDFLY_URL="https://github.com/wildfly/wildfly/releases/download/"$WILDFLY"/wildfly-"$WILDFLY".tar.gz";

echo $WILDFLY_URL
main "$@"


mkdir -p $INSTALL;
echo $INSTALL
mkdir -p $INSTALL"/imagens";
#replaceInFileRegex("../../classBag/src/main/java/br/edu/iffar/fw/classBag/init/InitConstantes.java")
#executeOS("cp _configuration_linux.properties " + pastaInstall + "/configuration_linux.properties");


printf ">%s< >%s< >%s< >%s< >%s< >%s< \n" $WILDFLY $INSTALL $EMAIL $SENHA_EMAIL $AUTH_SERVER $JAR_POSTGRES
#-i /opt -e darlan@gmail.com -s senha -a localhostr:8080 -w wildfly -j jar

