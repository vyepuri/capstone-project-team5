
Run the following steps in k8s master node. All the commands are executed on *default* namespace.
<br/>

### Step 1 : Apply RBAC for nfs provisioner to create pv and pvcs

```sh
>> kubectl apply -f 1nfs-rbac.yaml
```
#### 1nfs-rbac.yaml
```
kind: ServiceAccount
apiVersion: v1
metadata:
  name: nfs-pod-provisioner-sa
---
kind: ClusterRole # Role of kubernetes
apiVersion: rbac.authorization.k8s.io/v1 # auth API
metadata:
  name: nfs-provisioner-clusterRole
rules:
  - apiGroups: [""] # rules on persistentvolumes
    resources: ["persistentvolumes"]
    verbs: ["get", "list", "watch", "create", "delete"]
  - apiGroups: [""]
    resources: ["persistentvolumeclaims"]
    verbs: ["get", "list", "watch", "update"]
  - apiGroups: ["storage.k8s.io"]
    resources: ["storageclasses"]
    verbs: ["get", "list", "watch"]
  - apiGroups: [""]
    resources: ["events"]
    verbs: ["create", "update", "patch"]
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-provisioner-rolebinding
subjects:
  - kind: ServiceAccount
    name: nfs-pod-provisioner-sa # defined on top of file
    namespace: default
roleRef: # binding cluster role to service account
  kind: ClusterRole
  name: nfs-provisioner-clusterRole # name defined in clusterRole
  apiGroup: rbac.authorization.k8s.io
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-pod-provisioner-otherRoles
rules:
  - apiGroups: [""]
    resources: ["endpoints"]
    verbs: ["get", "list", "watch", "create", "update", "patch"]
---
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-pod-provisioner-otherRoles
subjects:
  - kind: ServiceAccount
    name: nfs-pod-provisioner-sa # same as top of the file
    # replace with namespace where provisioner is deployed
    namespace: default
roleRef:
  kind: Role
  name: nfs-pod-provisioner-otherRoles
  apiGroup: rbac.authorization.k8s.io
```



<br/><br/>

### Step 2 : Create NFS Storage class

```sh
>> kubectl apply -f 2nfs-storageclass.yaml
```
#### 2nfs-storageclass.yaml
```
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: nfs-storageclass # IMPORTANT pvc needs to mention this name
provisioner: nfs-pod  # name can be anything
parameters:
  archiveOnDelete: "false"
```


<br/><br/>

### Step 3 : Create NFS Client provisioner
Replace NFS server and path details as per your configuration

```sh
>> kubectl apply -f 3nfs-client-provisioner.yaml
```
#### 3nfs-client-provisioner.yaml
```
kind: Deployment
apiVersion: apps/v1
metadata:
  name: nfs-client-provisioner
spec:
  selector:
    matchLabels:
      app: nfs-client-provisioner
  replicas: 1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: nfs-client-provisioner
    spec:
      serviceAccountName: nfs-pod-provisioner-sa
      containers:
        - name: nfs-client-provisioner
          image: gcr.io/k8s-staging-sig-storage/nfs-subdir-external-provisioner:v4.0.0
          volumeMounts:
            - name: nfs-client-root
              mountPath: /persistentvolumes
          env:
            - name: PROVISIONER_NAME
              value: nfs-pod
            - name: NFS_SERVER
              value: 10.202.0.3    # Replace this with your NFS server ip
            - name: NFS_PATH
              value: /nfsshare     # Replace this with your NFS path
      volumes:
        - name: nfs-client-root
          nfs:
            server: 10.202.0.3    # Replace this with your NFS server ip
            path: /nfsshare       # Replace this with your NFS path
```

<br/><br/>
### Step 4 : Make this "nfs-storageclass" storage class as default storage class.

If your PVC yaml file contains the filed "*storageClass: someSCname*", then skip this step.
  
```sh
>> kubectl patch storageclass nfs-storageclass -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
```

<br/><br/>
### Step 5 : Now, for testing create pvc and it should automatically create pv. The PV should be created in NFS server.

 
<br/><br/>

### References:
https://dev.to/amritanshupandey/setup-nfs-storage-class-on-kubernetes-267c

<br/>
