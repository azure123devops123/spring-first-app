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
3) CHANGE SERVER HOSTNAME
    hostnamectl set-hostname JENKINS-MASTER
    /bin/bash                               // root@JENKINS-MASTER:/home/ubuntu# 
4) INSTALL JAVA RUNTIME
    0  sudo su
    1  apt update
    2  java --version     // java not installed 
    3  apt install openjdk-17-jre
    4  apt upgrade
    5  java --version     // java installed
4) INSTALL JENKINS => https://www.jenkins.io/doc/book/installing/linux/#debianubuntu
jenkins --version         // 2.426.3
which jenkins             // /usr/bin/jenkins

5) CHECK JENKINS SERVICE:
    systemctl status jenkins                // service must be running and also copy the OTP from log
6) CONFIGURE JENKINS MASTER.        // make sure port 8080 is open on server.
7) Set DNS - A record in the AWS Route 53 pointing to a Subdomain => jenkins.master.devopstech24.click
   Check the propagation => https://www.whatsmydns.net
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

# check in browser http://13.211.104.166:8080/
/var/lib/jenkins/secrets/initialAdminPassword

# configure jenkins server and install default plugins.

# setup DNS Server - route 53 A record
jenkins-master.dev.devopstech24.click -> 13.211.104.166

# check dns propagation  - must be fully propagated  
www.whatsmydns.net  // check A record



# Install docker
root@Jenkins-Master:~# apt update
root@Jenkins-Master:~# curl https://get.docker.com/ | sh
root@Jenkins-Master:~# which docker
root@Jenkins-Master:~# docker --version
root@Jenkins-Master:~# systemctl status docker              // service must be enabled and running

# Install docker-compose
apt  install docker-compose
root@Jenkins-Master:~# which docker-compose
root@Jenkins-Master:~# docker-compose --version

# install Nginx Proxy Manager => https://nginxproxymanager.com/setup/
root@Jenkins-Master:~# touch docker-compose.yml
root@Jenkins-Master:~# vim docker-compose.yml 
root@Jenkins-Master:~# docker compose up -d
root@Jenkins-Master:~# docker ps            // 2 containers must be up and running

# configure Nginx Proxy Manager:
http://13.211.104.166:81

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

# Create a pipeline

http://13.211.104.166:8080/github-webhook/
https://jenkins-master.dev.devopstech24.click/github-webhook/







