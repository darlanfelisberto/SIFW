#!/usr/bin/env python

import argparse
import re 
import os
import subprocess
import shlex
import time
 
parser = argparse.ArgumentParser(description="Just an example")

parser.add_argument("-i", "--install", help="versão de instalação", default="wildfly-28.0.1.Final")
parser.add_argument("-e", "--email", help="E-mail para envio de mensagens", default="email@dom.com")
parser.add_argument("-se", "--senhaEmail", help="Senha do 0mail", default="senha")
parser.add_argument("--urlOidc", help="url do auth-server", default="http://localhost:8080")
parser.add_argument("-d", "--destino", help="Pasta de instalação", default="/opt/sifw")
parser.add_argument("-j", "--jarDrivePostgres", help="vesao drive jar postgres jdbc", default="postgresql-42.6.0")
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


pastaInstall = args.destino;
wildflyName = args.install;
wildflyVersao = match.group(2);

def execute(command):
    print(" ".join(command))
    os.system(" ".join(command))
    # saida = subprocess.run(command, capture_output=True, text=True)    
    # return saida

def executeOS(command):
    print(command)
    os.system(command)

def replaceInFile(fileName, source,target):
    with open(fileName) as r:
        text = r.read().replace(target, source)
    with open(fileName, "w") as w:
        w.write(text)
        
def replaceInFileRegex(fileName):
    with open(fileName) as r:
        text = r.read()
    re.sub("<#PATH_RESOURCES#>([\W\w\D\t\n\r])+<#PATH_RESOURCES#>", 	"//<#PATH_RESOURCES#>\n"+"\tstatic public final String PATH_RESOURCES = \"" + pastaInstall+ "\";\n"+"\t//<#PATH_RESOURCES#>\n", text)
    with open(fileName, "w") as w:
        w.write(text)



urlDowloadWildfly = "https://github.com/wildfly/wildfly/releases/download/" + wildflyVersao + "/" + wildflyName +".tar.gz"


#sifw
execute(["mkdir", "-p", pastaInstall]);
execute(["mkdir", "-p", pastaInstall + "/imagens"]);
replaceInFileRegex("../../classBag/src/main/java/br/edu/iffar/fw/classBag/init/InitConstantes.java")
executeOS("cp configuration_linux.properties " + pastaInstall);
#fim sifw


#java
execute(["mkdir", "-p", "/usr/lib/jvm"]);
execute(["wget", "-cv", "https://download.oracle.com/java/17/archive/jdk-17.0.7_linux-x64_bin.tar.gz"])
execute(["tar", "-xvzf", "jdk-17.0.7_linux-x64_bin.tar.gz", "-C", "/usr/lib/jvm"])
execute(["ln", "-s", pastaInstall +"/jdk-17.0.7", "/usr/lib/jvm/jdk17"])
executeOS("update-alternatives --install \"/usr/bin/java\" \"java\" \"/usr/lib/jvm/jdk17cp /bin/java\" 10")
#fim java



#wildfly
execute(["wget", "-cv", "https://jdbc.postgresql.org/download/" + args.jarDrivePostgres + ".jar"])
execute(["wget", "-cv", urlDowloadWildfly])
execute(["tar", "-xvzf", wildflyName+".tar.gz", "-C", pastaInstall])
execute(["ln", "-s", pastaInstall +"/"+ wildflyName, pastaInstall + "/wildfly"])
executeOS(pastaInstall + "/wildfly/bin/add-user.sh -u manager -p manager")
executeOS(pastaInstall + "/wildfly/bin/standalone.sh &")

print("Sleep(10 segundos): aguarda wildfly subir para continuar.")
time.sleep(10)

executeOS(pastaInstall + "/wildfly/bin/jboss-cli.sh --connect --file=configure-wildfly.cli")
executeOS(pastaInstall + "/wildfly/bin/jboss-cli.sh --connect  --command=shutdown")


executeOS("groupadd -r wildfly")
executeOS("useradd -r -g wildfly -d " +pastaInstall + " -s /sbin/nologin wildfly")

executeOS("chown -R wildfly:wildfly " + pastaInstall)
executeOS("mkdir /etc/wildfly")


replaceInFile("configure-wildfly.cli",args.jarDrivePostgres,"<#jarDrivePostgres#>")
replaceInFile("configure-wildfly.cli",args.urlOidc,"<#urlOidc#>")
replaceInFile("configure-wildfly.cli",args.email,"<#email#>")
replaceInFile("configure-wildfly.cli",args.senhaEmail,"<#senhaEmail#>")

replaceInFile("launch.sh",pastaInstall,"<#pastaInstall#>")
replaceInFile("wildfly.conf",pastaInstall,"<#pastaInstall#>")
replaceInFile("wildfly.service",pastaInstall,"<#pastaInstall#>")

executeOS("cp wildfly.conf /etc/wildfly/")
executeOS("cp wildfly.service /etc/systemd/system/")
executeOS("cp launch.sh " + pastaInstall + "/wildfly/bin/")
executeOS("chmod +x " + pastaInstall + "/wildfly/bin/launch.sh")
#executeOS("systemctl start wildfly.service")
#executeOS("systemctl enable wildfly.service")
#fim wildfly
