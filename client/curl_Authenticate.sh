#!/bin/bash
echo Enter UserName
read USER_NAME
echo Enter password
read PASSWORD
curl --key ./client.key --cert ./client.pem:changeit --cacert ca.pem -H "content-type:Application/Json" --data '{"userName":"'$USER_NAME'","password":"'$PASSWORD'"}' -v https://localhost:8084/api/authenticate
