
# Installation of _NFS Server_  on _Ubuntu 20.04 LTS_ 


We need atleast one serverfor installation of NFS Server

- One Ubuntu NFS Server Node - 1 vCPU, 2 GB RAM, 20 GB disk
- One Ubuntu NFS Client Node - 1 vCPU, 1 GB RAM


<br/><br/>

## Run the follwing commands on NFS Server

#### Step 1 : Update packages
```sh
>> sudo apt update
```

#### Step 2 : Install NFS Server

```sh
>> sudo apt install nfs-kernel-server
```

#### Step 3 : Enable NFS server service
```sh
>> sudo systemctl status nfs-kernel-server.service
>> sudo systemctl enable nfs-kernel-server.service
```

#### Step 4 : Make a directory in the server for sharing
```sh
>> sudo mkdir -p /nfsshare
>> sudo chmod 777 /nfsshare
```

#### Step 5 : Change the configuration of NFS exports
```sh
>> vi /etc/exports
     /nfsshare  client_ip_addr(rw,sync,no_subtree_check,no_root_squash,insecure)
     
Note: Replace client_ip_addr here. If you are setting up for k8s, then use *.   Eg: /nfsshare  *(rw,sync,no_subtree_check,no_root_squash,insecure)     
```

#### Step 6 : Apply configuration changes
```sh
>>  sudo exportfs -a
```

#### Step 7 : View all active exports
```sh
>>  sudo exportfs -rv
```

#### Step 8 : Restart NFS server
```sh
>>  sudo systemctl restart nfs-kernel-server
```

<br/><br/>



## Run the commands on NFS Client (Not required for k8s persistent storage setup, but install *nfs-common* package in all k8s nodes)

#### Step 1 : Update the packages
```sh
>> sudo apt update
>> sudo apt install nfs-common
```

#### Step 2 :  Create a new directory on the client to serve as mountpoint

```sh
>> sudo mkdir -p /nfsshare
```

#### Step 3 : Mount the NFS directory. This mounts only until next reboot. (Note: Replace the server ip address here)
```sh
>>  sudo mount -t nfs  server_ip_addr:/nfsshare  /nfsshare
```

#### Step 4 : View the mounted directory
```sh
>> df -h
```

#### Step 5 : To mount permanently configure in /etc/fstab file
```sh
>>  sudo vi /etc/fstab
       server_ip_addr:/nfsshare  /nfsshare nfs timeo=900,intr,actimeo=1800 0 0  
```
#### Step 6 : Verify : Try creating a file in */nfsshare* directory on nfs client node and it should be available in */nfssahre* in NFS server node.

<br/><br/><br/>

#### References:
https://www.linode.com/docs/guides/using-an-nfs-server-on-ubuntu2004/
<br/>
https://medium.com/@myte/kubernetes-nfs-and-dynamic-nfs-provisioning-97e2afb8b4a9

