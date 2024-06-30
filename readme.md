# Spring Azure 
The goal to project is the app container in Azure with Storage 

## Create AZURE Credentials 
````psh
az ad sp create-for-rbac --name "Gildas Tema GitHub" --role contributor --scopes subscriptions/<subscription-id>
````