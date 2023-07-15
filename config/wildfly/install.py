#!/usr/bin/env python
# Desenvolvimento Aberto
# shell.py

import argparse
import re 
import os
import subprocess
import shlex
import time
 
parser = argparse.ArgumentParser(description="Just an example")

parser.add_argument("-i", "--install", help="versão de instalação", default="wildfly-28.0.1.Final")
parser.add_argument("-d", "--destino", help="Pasta de instalação", default="/opt/sifw")
args = parser.parse_args()
config = vars(args)
print(config)
print(args.install)
print(args.destino)

match = re.match(r"(wildfly)-([0-9\.]+Final)",args.install)

if match :
    print("Instalando a versao: "+ args.install)
else :
    print("verifique a versao, o padrao deve ser: wildfly-28.0.1.Final")
    exit()


def execute(command):
    print(subprocess.list2cmdline(command))
    os.system(subprocess.list2cmdline(command))
    # saida = subprocess.run(command, capture_output=True, text=True)    
    # return saida

def executeOS(command):
    print(subprocess.list2cmdline(command))
    os.system(command)

pastaInstall = args.destino;
wildflyName = args.install;
wildflyVersao = match.group(2);

print("fim")

urlDowloadWildfly = "https://github.com/wildfly/wildfly/releases/download/" + wildflyVersao + "/" + wildflyName +".tar.gz"

execute(["cd", "/tmp"])
execute(["mkdir", "-p", pastaInstall]);

execute(["wget", "-cv", urlDowloadWildfly])
execute(["tar", "-xvzf", wildflyName+".tar.gz", "-C", pastaInstall])

list_files = execute(["ln", "-s", pastaInstall +"/"+ wildflyName, pastaInstall + "/wildfly"])

executeOS("/opt/sifw/wildfly/bin/add-user.sh -u manager -p manager")
executeOS("/opt/sifw/wildfly/bin/standalone.sh &")


print("Sleep(10 segundos): aguarda wildfly subir para continuar.")
time.sleep(5)
executeOS(pastaInstall + "/wildfly/bin/jboss-cli.sh --connect --file=configure-wildfly.cli")
executeOS(pastaInstall + "/wildfly/bin/jboss-cli.sh --connect  --command=shutdown")



