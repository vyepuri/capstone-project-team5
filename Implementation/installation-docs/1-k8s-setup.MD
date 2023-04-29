# Installation of _Kubernetes 1.26_  on _Ubuntu 20.04 LTS_ with _containerd_ as runtime


We need following servers for installation of k8s

- One Master Node - 4 vCPU, 8 GB RAM
- Two Slave nodes -  4 vCPU, 8 GB RAM


<br/><br/>

## Run the follwing steps in all the nodes



#### Step 1 : Add Kubernetes repositories and Install k8s tools and disable updates

```sh
>> curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
>> echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list
>> sudo apt update -y
>> sudo apt -y install vim git curl wget kubelet=1.26.0-00 kubeadm=1.26.0-00 kubectl=1.26.0-00
>> sudo apt-mark hold kubelet kubeadm kubectl

```

#### Step 2 : Execute the following commands for Kernel level settings
```sh
>> sudo modprobe overlay
>> sudo modprobe br_netfilter
```
```console
>> sudo tee /etc/sysctl.d/kubernetes.conf<<EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF
```
#### Step 3 : Reload the system to take effect
```sh
>> sudo sysctl --system
```

#### Step 4 : Install containerd
```sh
>> cat <<EOF | sudo tee /etc/modules-load.d/containerd.conf
overlay
br_netfilter
EOF

>> sudo modprobe overlay
>> sudo modprobe br_netfilter

# Setup required sysctl params, these persist across reboots.
>> cat <<EOF | sudo tee /etc/sysctl.d/99-kubernetes-cri.conf
net.bridge.bridge-nf-call-iptables  = 1
net.ipv4.ip_forward                 = 1
net.bridge.bridge-nf-call-ip6tables = 1
EOF

# Apply sysctl params without reboot
>> sudo sysctl --system


#Install and configure containerd 
>> curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
>> sudo add-apt-repository "deb [arch="$(dpkg --print-architecture)"] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
>> sudo apt update -y
>> sudo apt install -y containerd.io
>> sudo mkdir -p /etc/containerd
>> containerd config default | sudo tee /etc/containerd/config.toml

#Start containerd
>> sudo systemctl restart containerd
>> sudo systemctl enable containerd

```

#### Step 5 : Pull the images, pulls the images for Kubernetes 1.26 version
```sh
>> sudo kubeadm config images pull --image-repository=registry.k8s.io --cri-socket unix:///run/containerd/containerd.sock --kubernetes-version v1.26.0
```


<br/><br/>
## Run the commands only on master node

#### Step 6 : Initialize cluster 
```sh
>> sudo kubeadm init --pod-network-cidr=10.244.0.0/16 --upload-certs --kubernetes-version=v1.26.0 --control-plane-endpoint=$(hostname -i) --cri-socket unix:///run/containerd/containerd.sock
```

#### Step 7 : After successful installation and join command, kube config is generated. Move kube config file generated to kubectl's default path.
```sh
>> sudo mkdir -p $HOME/.kube
>> sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
>> sudo chown $(id -u):$(id -g) $HOME/.kube/config
>> export KUBECONFIG=/etc/kubernetes/admin.conf
```

#### Step 8 : Copy the join command generated from the master node. If the join command is missing, create a new one using the following command
```sh
>> kubeadm token create --print-join-command
```

<br/><br/>

## Run the commands on all the slave nodes

#### Step 9: Run the copied join command from the master node in the slave node  
```
>> kubeadm join <master-hostname>:6443 --token <token> --discovery-token-ca-cert-hash <hash>

```
#### Step 10: You should see a message saying, this node successfully joined the cluster.



<br/><br/>
## Run the commands on master node
#### Step 11: Check all the nodes in the cluster
```sh
>> kubectl get nodes -o wide
```

#### Step 12: All the nodes will be in NotReady state. Go with flannel network. 
```sh
>> kubectl apply -f https://github.com/coreos/flannel/raw/master/Documentation/kube-flannel.yml
```

#### Step 13: Check the status of all the nodes. All of them will be in the ready state. Our cluster is ready now.
```sh
>> kubectl get nodes -o wide
```
<br/><br/>


### Reference:
https://blog.kubesimplify.com/kubernetes-126

<br/><br/>
