# AuthenticationApi
Authentication API provides the following capabilities
1. create/register user login credentials 
2. return JWT Token on successful Authentication

## End-points

- /create 

````
sample request body:
{
    "accountNumber":"77853449",
    "userName":"amsterdam",
    "password":"1234567"
}
````

- /authenticate
````
sample request body
{    
    "userName":"amsterdam",
    "password":"1234567"
}
````

### How to start the App

#### pre-requisite:
- The Accounts API should be running and available at port `8444`
- set the MAVEN_HOME env variable
- set the JAVA_HOME env variable


#### Invoke 

`./startApp.sh`

This will download the mvn dependencies and start the SpringBoot application at port `8084`

URL to access the application
`https://localhost:8084/api/create`

The application is enabled with mutual TLS.

### How to test the App

Navigate to the client directory

> /create end-point

Invoke the `curl_Create.sh`, 
it will ask the following details:
- Account Number
- UserName
- Password

The response will have the success/error message and the Http status code

> /authenticate end-point

Invoke the `curl_Authenticate.sh`, 
it will ask the following details:
- UserName
- Password

The response will have the JWT Token if the authentication is successful,
error message and status code in case of authentication failure


##### These curl commands can be used directly to invoke the API's

> /create

`curl --key ./client.key --cert ./client.pem:changeit --cacert ca.pem -H "content-type:Application/Json" --data '{"accountNumber":"77853449","userName":"nathanlouis","password":"1234567"}' -v https://localhost:8084/create`

> /authenticate

`curl --key ./client.key --cert ./client.pem:changeit --cacert ca.pem -H "content-type:Application/Json" --data '{"userName":"nathanlouis","password":"1234567"}' -v https://localhost:8084/authenticate` 
