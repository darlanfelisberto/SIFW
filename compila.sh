#!/bin/bash


function print(){
echo "";
echo -e "\E[01;33m--------------------------------------------------------------------
-------------- $1: $2
--------------------------------------------------------------------";tput sgr0
echo "";
}

cd classBag/ &&
print 'COMPILANDO' "classbag" &&
mvn clean install &&

cd ../sifw/ &&
print 'COMPILANDO' "comendo(SIFW)" &&
mvn clean install &&
print 'COPIANDO' "comendo(SIFW) para backup" &&
cp target/sifw.war /home/darlan/backup/war/sifw.war-`date +"%F"` &&
print 'COPIANDO' "comendo(SIFW) para SERVIDOR" &&
scp target/sifw.war  root@ru.edu.br:/tmp &&
cd /home/darlan/backup/war &&
ls -la

