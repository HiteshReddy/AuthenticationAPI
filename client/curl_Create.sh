#!/bin/bash
echo Enter Account Number
read ACCOUNT_NUMBER
echo Enter UserName
read USER_NAME
echo Enter password
read PASSWORD
curl --key ./client.key --cert ./client.pem:changeit --cacert ca.pem -H "content-type:Application/Json" --data '{"accountNumber":"'$ACCOUNT_NUMBER'","userName":"'$USER_NAME'","password":"'$PASSWORD'"}' -v https://localhost:8084/create
