# Spring Security for dummies

# spring security

## Dependencies 
* Spring Security 
* Guava 

#### If you dont have any users spring security gives you a default user
To login in the form default:
user 
generated security password

### Users: 
* username most be unique 
* Passwords most be encoded
* they should have Roles 
* with the roles you can define the endpoints of your users using authorities
* higly recommended to use authorities

basic auth with basic auth you need to specify the username and password inside of the request header as B64 good for external API
basic auth cant logout because the user and password is sent in everysingle request 

### Ant matches 
The mapping matches URLs using the following rules:

* ? matches one character
*  "*" matches zero or more characters
*  "**" matches zero or more 'directories' in a path
* {spring:[a-z]+} matches the regexp [a-z]+ as a path variable named "spring"

#### Examples: 
* com/t?st.jsp - matches com/test.jsp but also com/tast.jsp or com/txst.jsp
* com/*.jsp - matches all .jsp files in the com directory
* com/**/test.jsp - matches all test.jsp files underneath the com path
* org/springframework/**/*.jsp - matches all .jsp files underneath the org/springframework path
* org/**/servlet/bla.jsp - matches org/springframework/servlet/bla.jsp but also org/springframework/testing/servlet/bla.jsp and org/servlet/bla.jsp
* com/{filename:\\w+}.jsp will match com/test.jsp and assign the value test to the filename variable


## Java errors WhiteLabel

* whitelabel (type= not found, status=404) is always about the controllers, if this error occur is because you have nothing assignated to were you are trying to go and if you do you probably have a spelling error somewhere
* whitelabel (type=Unauthorized, status=401) you didnt login nor do you can access this page withput login in 

## Postman errors 

* postman error 401 unautarazed nao fizeste login 
if you are using basic auth you have to sent the user and password in each request 
Authorization -> basic Auth -> put your credentials there 
* postman error 500 Internal Server Error. A part of your server is not on, When you get an error it crashes your server, maybe the db or the program or the maildev
* postman error 400 bad request, check the url, check the type of body you are sending, if you are sending a Json it most say json, check the body, check the spelling, check that what you are putting in your request matches with what you have in your controllers
* postman error 403 Forbidden. this means you are trying to access a url but the user you have logged with doesnt have the permission to access it 
* postman error 403 Forbidden. Or spring is protecting your app from CSRF. In your Application Security Configuration after http put .csrf().disable() 
/*CSRF- Cross-Site Request Forgery attack is an attack that forces the end user to make an unwanted calls to the web application servers where the end user is already authenticated.*/
* postman error 405 Method Not Allowed. check the url and the request type, you are probably using a type that doesnt match and url like DELETE without id 

## Java errors

* error java db time zone - Whenever you have the link to your database you can put this link jdbc:mysql://localhost:3306/{nameOfTheSchema}?useTimezone=true&serverTimezone=UTC
* java.lang.IllegalArgumentException: there is no PasswordEncoder mapped for the id "null" Users required to have the password encoded, there's something wrong with your encoder or you didnt implement it 
* Error Encoded password does not look like Bcrypt // you need to inject the PasswordEncoder into your application security configuration
