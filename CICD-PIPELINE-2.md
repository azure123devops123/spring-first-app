# AWS Server Configuration:
Ram: 8GB or 4GB in case of master/slave setup
Processor - Instance type: t2.large or t2.medium in case of master/slave setup
Harddisk: 25GB or 20gb in case of master/slave setup
Vpc: devopstech24
Subnet ID: devopstech24-alb-public-subnet-2a
Key pair: jabir-practice

===============================================================================
1) SERVER LAUNCH
2) ELASTIC IP
3) Create a non root user with sudo privileges:

sudo su         // root user
lsb_release -a                       // Ubuntu 22.04.3 LTS

adduser linuxadmin                   // create a user    (PASSWORD: pass1234)
usermod -aG sudo linuxadmin          // add this user to sudo group

exit             // exit from root user
su - linuxadmin  // login as a not root user who got sudo privileges.

# Update and Upgrade System:
sudo apt update    // system update
sudo apt upgrade   // system upgrade

# CHANGE SERVER HOSTNAME
linuxadmin@ip-10-10-54-240:~$ sudo hostnamectl set-hostname Jenkins-Master
linuxadmin@ip-10-10-54-240:~$ /bin/bash
linuxadmin@Jenkins-Master:~$

4) INSTALL JAVA RUNTIME Required for Jenkins:
    1  java --version     // java not installed 
    2  sudo apt install openjdk-17-jre
    3  sudo apt upgrade
    4  java --version     // java installed and java runtime versions

4) INSTALL JENKINS => https://www.jenkins.io/doc/book/installing/linux/#debianubuntu
jenkins --version         // 2.426.3
which jenkins             // /usr/bin/jenkins

5) CHECK JENKINS SERVICE:
    systemctl status jenkins                // service must be running and also copy the OTP from log       //  c65d871f2af249ed8e7a6624beea8651
6) CONFIGURE JENKINS MASTER.        // make sure port 8080 is open on server.
7) Set DNS - A record in the AWS Route 53 pointing to a Subdomain => jenkins.dev.devopstech24.click
   Check the propagation => https://www.whatsmydns.net

---- > stop here  <-------
8) Install nginx using mainline nginx packages
=> https://docs.nginx.com/nginx/admin-guide/installing-nginx/installing-nginx-open-source/#installing-prebuilt-ubuntu-packages
Go to section: Installing a Prebuilt Ubuntu Package from the Official NGINX Repository

9) Verify Installation: 
    nginx -v                        // nginx/1.25.3
    which nginx                     // /usr/sbin/nginx

9) Check the nginx service:
        systemctl status nginx          // inactive
        systemctl start nginx
=========================================jenkins master================================================
ubuntu@ip-10-10-62-53:~$ sudo -i
root@ip-10-10-62-53:~# hostnamectl set-hostname Jenkins-Master
root@ip-10-10-62-53:~# /bin/bash
root@Jenkins-Master:~# apt update && apt upgrade -y

# attach elastic ip address

# just check root login access must be disabled -> https://askubuntu.com/questions/435615/disable-password-authentication-in-ssh
root@Jenkins-Master:~# cd /etc/ssh/ssh
root@Jenkins-Master:/etc/ssh# vim sshd_config                   
root@Jenkins-Master:/etc/ssh# cd ~

# install java
root@Jenkins-Master:~# which java
root@Jenkins-Master:~# java -version

# install jenkins https://www.jenkins.io/doc/book/installing/linux/#debianubuntu
root@Jenkins-Master:~# which jenkins
root@Jenkins-Master:~# jenkins --version
root@Jenkins-Master:~# systemctl status jenkins  // service must be enabled and running and get the initial password

---- > start again  <-------
# check in browser http://13.211.104.166:8080/
/var/lib/jenkins/secrets/initialAdminPassword

# configure jenkins server and install default plugins.

# setup DNS Server - route 53 A record
jenkins.dev.devopstech24.click -> 13.211.104.166

# check dns propagation  - must be fully propagated  
www.whatsmydns.net  // check A record

# Install docker
linuxadmin@Jenkins-Master:~# docker --version
linuxadmin@Jenkins-Master:~# sudo apt  install docker.io
linuxadmin@Jenkins-Master:~# which docker
linuxadmin@Jenkins-Master:~# docker --version
linuxadmin@Jenkins-Master:~# systemctl status docker              // service must be enabled and running

# Install docker-compose
linuxadmin@Jenkins-Master:~# which docker-compose
linuxadmin@Jenkins-Master:~# apt install docker-compose
linuxadmin@Jenkins-Master:~# which docker-compose
linuxadmin@Jenkins-Master:~# docker-compose --version

# Make Sure linuxadmin must be part of docker group so it can run docker commands:
sudo usermod -aG docker linuxadmin
exit                // re-login
su linuxadmin
groups              // linuxadmin is part of three groups => linuxadmin sudo docker

# install Nginx Proxy Manager => https://nginxproxymanager.com/setup/
linuxadmin@Jenkins-Master:~# mkdir NginxProxyManager
linuxadmin@Jenkins-Master:~# cd NginxProxyManager
linuxadmin@Jenkins-Master:~# touch docker-compose.yml
linuxadmin@Jenkins-Master:~# vim docker-compose.yml 
linuxadmin@Jenkins-Master:~# docker compose up -d
linuxadmin@Jenkins-Master:~# docker ps            // 2 containers must be up and running

# configure Nginx Proxy Manager:
http://54.66.81.189:81/

// Default Administrator User
Email:    admin@example.com
Password: changeme

// new email and password:
Email:    azure123.devops123@gmail.com
Password: pass1234

# Create a ssl certificate and then add proxy server and attach the created certificate.

NOW YOUR SERVER IS SECURE AND THE CERTIFICATE FROM LETS ENCRYPT IS VALID FOR 3 MONTHS. IT WILL BE AUTOMATICALLY RENEWED. BUT YOU CAN ALSO CHECK BY DRY RUN.

======================================================= SET UP JENKINS-SLAVE SERVER =======================================================================
ubuntu@ip-10-10-62-53:~$ sudo -i
root@ip-10-10-62-53:~# hostnamectl set-hostname Jenkins-Slave
root@ip-10-10-62-53:~# /bin/bash
root@Jenkins-Slave:~# apt update && apt upgrade -y

# just check root login access must be disabled -> https://askubuntu.com/questions/435615/disable-password-authentication-in-ssh
root@Jenkins-Slave:~# cd /etc/ssh/ssh
root@Jenkins-Slave:/etc/ssh# vim sshd_config                   
root@Jenkins-Slave:/etc/ssh# cd ~

# install java
root@Jenkins-Slave:~# which java
root@Jenkins-Slave:~# java -version

# attach elastic ip address

# Create a directory with 755 permissions
root@Jenkins-Slave:/home/ubuntu# mkdir jenkins
root@Jenkins-Slave:/home/ubuntu# chmod 755 jenkins
root@Jenkins-Slave:/home/ubuntu# cd jenkins/
root@Jenkins-Slave:/home/ubuntu/jenkins# pwd
/home/ubuntu/jenkins

# generate keys - as a root user this root user must be used in connection.
ubuntu@Jenkins-Slave:~/jenkins$ sudo su
root@Jenkins-Slave:/home/ubuntu/jenkins# pwd
/home/ubuntu/jenkins
root@Jenkins-Slave:/home/ubuntu/jenkins# ssh-keygen -t ed25519               // https://www.ssh.com/academy/ssh/keygen

# copy public key into paste it into the authorized keys
root@Jenkins-Slave:~/.ssh# pwd
/root/.ssh
root@Jenkins-Slave:~/.ssh# ls
authorized_keys                     // use inside vim ESC and then $ key to the end of line.

root@Jenkins-Slave:~# systemctl restart sshd
root@Jenkins-Slave:~# systemctl status sshd             // service must be running

# after a successful connection you will see all the processes came like remoting.jar 
root@Jenkins-Slave:/home/ubuntu/jenkins# ps -ef | grep jenkins
root        5244    5119  1 16:00 ?        00:00:07 java -jar remoting.jar -workDir /home/ubuntu/jenkins -jar-cache /home/ubuntu/jenkins/remoting/jarCache
root        5286    5094  0 16:09 pts/1    00:00:00 grep --color=auto jenkins

======================================================= MASTER SLAVE SETUP =======================================================================

# copy private key for jenkins authentication credentials. user: root and password: PRIVATE KEY

# Create a pipeline and set the webhook:
https://jenkins.dev.devopstech24.click/github-webhook/

======================================================= SONARQUBE SERVER =======================================================================
# We are using sonarqube:lts-community in our Docker Compose .yml file
https://docs.sonarsource.com/sonarqube/latest/setup-and-upgrade/install-the-server/installing-sonarqube-from-docker/

# Install 'SonarQube Scanner' Plugin

# Required Always: Kernel tuning for sonarqube:
root@Jenkins-Master:~/sonarqube# sudo sysctl -w vm.max_map_count=262144        // you will notice the output:   vm.max_map_count = 262144

# Always run these two sonarqube and postgres containers after restart:
root@Jenkins-Master:~/sonarqube# docker compose up -d

# Port 5432 for (postgres) and Port 9000 for (sonarqube) must be open on EC2
# Set DNS - A record in the AWS Route 53 pointing to a Subdomain => sonarqube.dev.devopstech24.click
Check the propagation => https://www.whatsmydns.net
sonarqube.dev.devopstech24.click -> 13.211.104.166

# check in browser: default username and password is admin, admin
https://sonarqube.dev.devopstech24.click


Generate Token on Sonarqube for Sonarqube Authentication from JENKINS PIPELINE: 

Name: sonarqube-token
Token: sqa_01504165a3af9a0ebf65db3b2f21ae7f96c6508f

# In System section set the sonarqube server. This server name we can pass on in our pipeline.

======================================================= OWASP (Open Worldwide Application Security Project)  =========================================================
# install plugin: 'OWASP Dependency-Check'
# Configure using option Install automatically by 'Install from github.com' and version '6.5.1' otherwise we will get error.

======================================================= Docker  =========================================================

# docker credentials  to connect with docker hub

# install following docker related plugins:
Docker
Docker Pipeline
docker-build-step

# Configure Docker Tool using option Install automatically by 'Download from docker.com' and latest version.

# make sure you install docker and docker-compose on the slave node. otherwise you will get an error.

# Write the groovy script to build docker image

======================================================= Trivy Scan  =========================================================
# Install Trivy using Install Script - > https://aquasecurity.github.io/trivy/v0.18.3/installation/#install-script

# scan the image and get the output into an xml formate.

======================================================= Docker Push Image / Docker Scout Scan  =========================================================
# After trivy scan push the docker image to docker hub.

# After pushing the image you can also scan using docker scout -> https://github.com/docker/scout-cli?tab=readme-ov-file#ci-integration

=======================================================  Account Token / trigger pipeline =====================================================

# Create account token:
Muhammad Jabir (Account) => Configure => API Token => JENKINS_API_TOKEN => 1163ab5aef147103ea675c81c298546f3a

# Create Credentials:
Manage Jenkins => Credentials => JENKINS_API_TOKEN   

=======================================================  SLACK INTEGRATION =====================================================
# Create an account in Slack => https://slack.com/
# Add Jenkins CI APP for Slack: (WE HAVE TO REPEAT THIS PROCESS EVERY TIME WE CREATE A NEW CHANNEL)
Inside your Account go into => Settings & administrator => Manage apps => Search for 'Jenkins CI' => CHOOSE YOUR CHANNEL => Add Jenkins CI Integration
it will spit the details which you can use for credentials and into System section.
like for example:
Team subdomain: devopsleadworkspace
Integration token credential ID: Create a secret text credential using 2XH4jnfrv4OYNRPYkxT62ST8 as the value.
# Slack Credentials using Token.

# Install Slack plugins called 'slack notification' in Jenkins

# Now configure and test in System Section of Jenkins Master.

# write the configration in jenkins file.   

# HELP FROM:
https://www.youtube.com/watch?v=9ZUy3oHNgh8
https://mrcloudbook.hashnode.dev/a-guide-to-integrating-slack-with-jenkins

=============================================================
jenkins-master.dev.devopstech24.click
sonarqube.dev.devopstech24.click

https://medium.com/@madhan97.sms/a-step-by-step-guide-to-configuring-smtp-in-jenkins-with-gmail-ea79eb3aaefc

=======================================================  Minikube Installation ou your laptop =====================================================

INSTALL MINIKUBE (all commands): => https://minikube.sigs.k8s.io/docs/start/
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64
sudo install minikube-darwin-amd64 /usr/local/bin/minikube

START YOU DOCKER DESKTOP
minikube status
minikube start
minikube status
minikube          // we must have following output.
            type: Control Plane
            host: Running
            kubelet: Running
            apiserver: Running
            kubeconfig: Configured

kubectl get po -A  
OR   
kubectl get pods -n kube-system          

=======================================================  Argocd on minikube on macos apple =====================================================

Install ArgoCD: => https://apexlemons.com/devops/argocd-on-minikube-on-macos-apple-silicon-version-m1-m2/

# Installation
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl get pods -n argocd

# Argocd UI
kubectl get SERVICES -n argocd                                      // NOTICE argocd-server running on HTTP and HTTPS PORTS
kubectl port-forward -n argocd service/argocd-server 8080:443       // port forwarding so we can access it

BROWSER: Check in browser:   127.0.0.1:8080        // username: admin    and password is auto generated and saves inside 'argocd-initial-admin-secret'

kubectl get secret argocd-initial-admin-secret -o yaml -n argocd        // copy the password: d1llQlNVUzN2dnNRbDFPMg==
echo ZldocEJVaHZIRllrYkpENw== | base64 --decode             //    fWhpBUhvHFYkbJD7          NOTE:- DO NOT COPY % at the end

# Change the password from GUI
New Password: pass1234
username: admin 

# Argocd Configuration:


//
kubectl create namespace argocd
kubectl delete namespace argocd


# Monitoring => https://github.com/brandonleegit/OpenSourceMonitoring/blob/main/README.md
#### ========== FREE AND OPER SOURCE MONITORING OF LINUX SERVERS AND CONTAINERS ==========

lsb_release -a                       // Ubuntu 22.04.3 LTS
apt update
docker --version                     // Docker version 25.0.2, build 29cf629
docker-compose --version             // Docker Compose version v2.20.3
adduser linuxadmin                   // create a user    (PASSWORD: pass1234)
usermod -aG sudo linuxadmin          // add this user to sudo group
su - linuxadmin                      // Testing sudo Access
sudo apt update                      // it should work
linuxadmin@Jenkins-Slave:~$ id -u    // check the id of the user. it must be 1001 because we will use it in our script
linuxadmin@Jenkins-Slave:~$ docker ps
# make sure linuxadmin can run the docker ps command. if not exit the user and log back in.
linuxadmin@Jenkins-Slave:~$ docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES

# script - # DO NOT WORRY ABOUT : command not found
mkdir -p promgrafnode/prometheus && \ 
mkdir -p promgrafnode/grafana/provisioning && \
touch promgrafnode/docker-compose.yml && \
touch promgrafnode/prometheus/prometheus.yml

# 4 directories, 2 files
$ tree
.
└── promgrafnode
    ├── docker-compose.yml
    ├── grafana
    │   └── provisioning
    └── prometheus
        └── prometheus.yml

4 directories, 2 files

=====

linuxadmin@Jenkins-Slave:~/promgrafnode$ ls
docker-compose.yml  grafana  prometheus

# Paste the configuration into the docker-compose.yml inside promgrafnode directory:
# Then run the containers
linuxadmin@Jenkins-Slave:~/promgrafnode$ docker compose up -d

# 5 containers are working:  
linuxadmin@Jenkins-Slave:~/promgrafnode$ docker ps

