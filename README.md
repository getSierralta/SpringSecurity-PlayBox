# Spring Security for dummies
Code from this course https://www.youtube.com/watch?v=her_7pa0vrg
the code inside is commented please read it, im talking to you especifically, you know who you are 


## Dependencies 
* Spring Security 
* Guava 
* Thymeleaf 


## Users: 
* username most be unique 
* Passwords most be encoded
* they should have Roles 
* with the roles you can define the endpoints of your users using authorities
* higly recommended to use authorities

#### Session id
* As session IDs are often used to identify a user that has logged into a website.
* They are stored in the cookies.
* A session identifier, session ID or session token is a piece of data that is used in network communications (often over HTTP) to identify a session, a series of related message exchanges. Session identifiers become necessary in cases where the communications infrastructure uses a stateless protocol such as HTTP. For example, a buyer who visits a seller's website wants to collect a number of articles in a virtual shopping cart and then finalize the shopping by going to the site's checkout page. This typically involves an ongoing communication where several webpages are requested by the client and sent back to them by the server. In such a situation, it is vital to keep track of the current state of the shopper's cart, and a session ID is one way to achieve that goal.


## Auth:

#### Basic auth
* or HTTP authentication is an authentication method built into browsers. The browser presents a dialog where the user has to provide an ID and password before the page loads.
* Basic auth uses the Authorisation to authenticate. The provided ID and password are encoded with base64. The credentials are not encrypted unless the request is sent over HTTPS.
* basic auth with basic auth you need to specify the username and password inside of the request header as B64 good for external API
* basic auth cant logout because the user and password is sent in everysingle request 
* Basic auth is very limited. It's only possible to provide an ID and password.
* The dialog is opened before the page loads, and there is no way to customise the dialog.
* Each browser determines how long the user is logged in for, and it's not possible to log out users.
* It can be useful to use for internal applications such as a staging site.

#### Form-based auth
* Form-based authentication is not formalized by any RFC. In essence, it is a programmatic method of authentication that developers create to mitigate the downside of basic auth. Most implementations of form-based authentication share the following characteristics:
* They don’t use the formal HTTP authentication techniques (basic or digest).
* They use the standard HTML form fields to pass the username and password values to the server.
* The server validates the credentials and then creates a “session” that is tied to a unique key that is passed between the client and server on each http put and get request.
* When the user clicks “log off” or the server logs the user off (for example after certain idle time), the server will invalidate the session key, which makes any subsequent communication between the client and server require re-validation (resubmission of login credentials via the form) in order to establish a new session key.
* As with basic auth, form-based auth does not protect login credentials when connected over HTTP, therefore it is not more “secure” than basic auth in how it handles user credentials. It is however more secure when it comes to properly logging the user off after a certain period of inactivity or if the user no longer requires use of the system and decides to log out.

### If you dont have any users spring security gives you a default user
To login in the form default:
user /
generated security password



### Authentication (Access Control)

* Roles conceptually represent a named collection of permissions.
* A Permission will be a fine-grained unit of access (“Edit resource X“, “Access the dashboard page“, etc.). A Role will be a collection of 1+ Permissions. 
* A User can have 1+ Roles. All these relationships (Users, Roles, Permissions) are all stored in a database and can be changed on the fly and as needed.
* At the moment of checking, the calling code only needs to know "does user X have permission to perform action Y?".
* The calling code does not care about and should not be aware of relationships between roles and permissions.
* The authorization layer will then check if the user has this permission, typically by checking if the user's role has this permission. This allows you to change authorization logic without updating the calling code.
* If you directly check for role at the call site, you are implicitly forming role ⇄ permission relationships and injecting authorization logic into the calling code, violating separation of concerns.

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

## Java errors

* error java db time zone - Whenever you have the link to your database you can put this link jdbc:mysql://localhost:3306/{nameOfTheSchema}?useTimezone=true&serverTimezone=UTC
* java.lang.IllegalArgumentException: there is no PasswordEncoder mapped for the id "null" Users required to have the password encoded, there's something wrong with your encoder or you didnt implement it 
* Error Encoded password does not look like Bcrypt // you need to inject the PasswordEncoder into your application security configuration
* java.lang.IllegalStateException: UserDetailsService is required. HTTP Status 500 – Internal Server Error if you have the UserDetailService then clean your cookies, if it doesnt work then you have implemented it wrong.

## Java errors WhiteLabel

* whitelabel (type= not found, status=404) is always about the controllers, if this error occur is because you have nothing assignated to were you are trying to go and if you do you probably have a spelling error somewhere
* whitelabel (type=Unauthorized, status=401) you didnt login nor you can access this page without login in 

## Postman errors 

* postman error 401 unautarazed nao fizeste login 
if you are using basic auth you have to sent the user and password in each request 
Authorization -> basic Auth -> put your credentials there 
* postman error 500 Internal Server Error. A part of your server is not on, When you get an error it crashes your server, maybe the db or the program or the maildev
* postman error 400 bad request, check the url, check the type of body you are sending, if you are sending a Json it most say json, check the body, check the spelling, check that what you are putting in your request matches with what you have in your controllers
* postman error 403 Forbidden. this means you are trying to access a url but the user you have logged with doesnt have the permission to access it 
* postman error 403 Forbidden. Or spring is protecting your app from CSRF. In your Application Security Configuration after http put .csrf().disable() 
* postman error 405 Method Not Allowed. check the url and the request type, you are probably using a type that doesnt match and url like DELETE without id 
* postman error 415 Unsupported Media Type. Check the type of media that you are sending, you may be have as default text for example when you are trying to sent a json 

## CSRF 
* https://wanago.io/2020/12/21/csrf-attacks-same-site-cookies/ links 
* Cross-Site Request Forgery attack is an attack that forces the end user to make an unwanted calls to the web application servers where the end user is already authenticated.
* When to use CSRF protection: for any request that could be processed by a browser by normal users. If you are only creating a service that is used by non-browser clients, you will likely want to disable CSRF protection.
* A CSRF token is a unique, secret, unpredictable value that is generated by the server-side application and transmitted to the client in such a way that it is included in a subsequent HTTP request made by the client. When the later request is made, the server-side application validates that the request includes the expected token and rejects the request if the token is missing or invalid.
* CSRF tokens can prevent CSRF attacks by making it impossible for an attacker to construct a fully valid HTTP request suitable for feeding to a victim user. Since the attacker cannot determine or predict the value of a user's CSRF token, they cannot construct a request with all the parameters that are necessary for the application to honor the request.
* You can get a CSRF token with your cookies.
* Depending on the configuration of the cookies, the browser might attach them to the request. It would be a simple Cross-Site Request Forgery attack by forcing the user to perform an unwanted action.
* How to activate the cookies no postman? -> Capture requests and cookies (the bottom looks like an antena) request -> chenge from proxy to interceptor (you need to install a plugin no browser)
* When you do a Get request you can go next to the body and see the cookies, you need to grab the cookie token and before you do a request you go to headers and you put: X-XSRF-TOKEN and in value you put the token.
* If you dont see the token in the cookies section, make sure you have the plugin, you have the explorer on, and that in cookes you have a domain (can be just google.com) 
* https://learning.postman.com/docs/sending-requests/capturing-request-data/interceptor/
* And when that doesnt work give up in life and enable the protection 
* Ok dont give up yet .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) finally let me got those tokens baby

## JWT JSON Web Tokens 
* JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.
#### What is the JSON Web Token structure?
In its compact form, JSON Web Tokens consist of three parts separated by dots (.), which are: Header, Payload, Signature. Therefore, a JWT typically looks like the following:
* xxxxx.yyyyy.zzzzz
* The header typically consists of two parts: the type of the token, which is JWT, and the signing algorithm being used, such as HMAC SHA256 or RSA.
* {
 "alg": "HS256",
 "typ": "JWT"
 }
* payload, which contains the claims. Claims are statements about an entity (typically, the user) and additional data. There are three types of claims: registered, public, and private claims.
* {
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
* The signature is used to verify the message wasn't changed along the way, and, in the case of tokens signed with a private key, it can also verify that the sender of the JWT is who it says it is.
* To create the signature part you have to take the encoded header, the encoded payload, a secret, the algorithm specified in the header, and sign that.
* For example if you want to use the HMAC SHA256 algorithm, the signature will be created in the following way:
* HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
* https://jwt.io/

#### Library 
* https://github.com/jwtk/jjwt

## Glossary 

* .antMatchers() = is kinda like a regex (?)
* application.yml  = YAML is a convenient format for specifying hierarchical configuration data.
* application.properties =  simple key-value storage for configuration properties. 
* @Async = annotating a method of a bean with @Async will make it execute in a separate thread.
* Authorization = Authorization is to check whether user can access the application or not or what user can access and what user can not access. 
* @Autowired = By Default @Autowired will inject the bean byType. byName injection can be forced by using @Qualifier with @Autowired.


* @Bean = is applied on a method to specify that it returns a bean to be managed by Spring context. Spring Bean annotation is usually declared in Configuration classes methods
* BCryptPasswordEncoder = This is the encoder that spring uses internally. Bcrypt uses adaptive hash algorithm to store password.BCrypt internally generates a random salt while encoding passwords and hence it is obvious to get different encoded results for the same string.But one common thing is that everytime it generates a String of length 60.
* @Builder = lets you automatically produce the code required to have your class be instantiable with code such as: Person.builder().name("Adam Savage").city("San Francisco").job("Mythbusters").job("Unchained Reaction").build()
* @Builder.Default = The field annotated with @Default must have an initializing expression; that expression is taken as the default to be used if not explicitly set during building.

* CommandLineRunner = is an interface used to indicate that a bean should run when it is contained within a SpringApplication. A Spring Boot application can have multiple beans implementing CommandLineRunner. These can be ordered with @Order.
* configure(HttpSecurity http) = the mothod where you configure the web security
* @Configuration = indicates that a class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime
* @Controller = @Controller is typically used in combination with a @RequestMapping annotation used on request handling methods.
* csrf = Cross-site request forgery 

* DAO = Data Access Object, It’s a design pattern in which a data access object (DAO) is an object that provides an abstract interface to some type of database or other persistence mechanisms. By mapping application calls to the persistence layer, DAOs provide some specific data operations without exposing details of the database.
* DaoAuthenticationProvider = An AuthenticationProvider implementation that retrieves user details from an UserDetailsService.
* @DeleteMapping = shortcut for @RequestMapping(method =RequestMethod.DELETE).

* @EnableGlobalMethodSecurity(prePostEnabled = true) = EnableGlobalMethodSecurity provides AOP security on methods. Some of the annotations that it provides are PreAuthorize, PostAuthorize. It also has support for JSR-250. Without enabling the annotations mean nothing
* @EnableWebSecurity = is a marker annotation. It allows Spring to find (it's a @Configuration and, therefore, @Component) and automatically apply the class to the global WebSecurity.
* @Entity  =  An entity represents a table stored in a database. Every instance of an entity represents a row in the table.
* @EqualsAndHashCode = Any class definition may be annotated with @EqualsAndHashCode to let lombok generate implementations of the equals(Object other) and hashCode() methods.

* Filtering Requests and Responses = A filter is an object that can transform the header and content (or both) of a request or response. Filters differ from web components in that filters usually do not themselves create a response. Instead, a filter provides functionality that can be “attached” to any kind of web resource. Consequently, a filter should not have any dependencies on a web resource for which it is acting as a filter; this way, it can be composed with more than one type of web resource.

* @GeneratedValue = is to configure the way of increment of the specified column(field).
* @GetMapping = shortcut for @RequestMapping(method = RequestMethod.GET)
* @Getter and @Setter = You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.

* HTTP = is used for communications over the internet
* HttpSecurity = It allows configuring web based security for specific http requests. By default it will be applied to all requests, but can be restricted using requestMatcher(RequestMatcher) or other similar methods.

* @Id = Specifies the primary key of an entity.
* InMemoryUserDetailsManager = Non-persistent implementation of UserDetailsManager which is backed by an in-memory map.

* Jackson = is a simple java based library to serialize java objects to JSON and vice versa.
*  @JoinColumn() = marks a column for as a join column for an entity association or an element collection.
*  JpaRepository<type, id> = JpaRepository is JPA specific extension of Repository. It contains the full API of CrudRepository and PagingAndSortingRepository. So it contains API for basic CRUD operations and also API for pagination and sorting.

* UserRole = An Enum with the roles of your application
* UserDetails = This interface has basic user methods like (.username(), .password(), .role()). You can also extend this class and add your own fields.
* UserDetailsService = is an interface which is used to retrieve the user’s authentication and authorization information.

* @NoArgsConstructor, @RequiredArgsConstructor, @AllArgsConstructor = Constructors made to order: Generates constructors that take no arguments, one argument per final / non-null field, or one argument for every field.
 
*  @OneToOne() = Defines a single-valued association to another entity that has one-to-one multiplicity
*  Optional<E> = Optional is a container type for a value which may be absent.

* PasswordEncoder = is an interface with 3 methods encode, matches and upgradeEncoding.
* @PatchMapping = shortcut for @RequestMapping(method = RequestMethod.PATCH)
* @PathVariable = can be used to handle template variables in the request URI mapping,  and use them as method parameters.
* @PostAuthorize = authorizes on the basis of role or the argument which is passed to the method.
* @PostMapping = shortcut for @RequestMapping(method = RequestMethod.POST)
* @PreAuthorize  = @PreAuthorize can check for authorization before entering into method. hasRole('Role_') hasAnyRole('Role_') hasAuthority('permission') hasAnyAuthority('permission').
* @PutMapping = shortcut for @RequestMapping(method = RequestMethod.PUT).

* @Qualifier = There may be a situation when you create more than one bean of the same type and want to wire only one of them with a property. In such cases, you can use the @Qualifier annotation along with @Autowired to remove the confusion by specifying which exact bean will be wired.

* @Repository = is used in classe with the mechanism for encapsulating storage, retrieval, and search behavior which emulates a collection of objects aka db
* @RequestBody and @ResponseBody =  are used to convert the body of the HTTP request and response with java class objects. Both these annotations will use registered HTTP message converters in the process of converting/mapping HTTP request/response body with java objects.
* @RestController = is a specialized version of the controller. It includes the @Controller and @ResponseBody annotations and as a result, simplifies the controller implementation
* @RequestMapping("url") = The class-level annotation maps a specific request path or pattern onto a controller. You can then apply additional method-level annotations to make mappings more specific to handler methods.

* @Service = is used with classes that provide some business functionalities. Spring context will autodetect these classes when annotation-based configuration and classpath scanning is used.
* SimpleGrantedAuthority = is a basic concrete implementation of a GrantedAuthority must either represent itself as a String or be specifically supported by an AccessDecisionManager.
* Stateless protocols = are the type of network protocols where clients send requests to the server. The server will then respond back in accordance with the current state. There’s no requirement for the server to preserve session information or status concerning each communicating partner for multiple requests. HTTP (Hypertext Transfer Protocol), UDP (User Datagram Protocol), and DNS (Domain Name System) are all examples of a stateless protocol.

* UserDetailsService = The UserDetailsService is a core interface in Spring Security framework, which is used to retrieve the user’s authentication and authorization information.

* WebSecurityConfig = class with the web security configurations
* WebSecurityConfigurerAdapter = The @EnableWebSecurity annotation and WebSecurityConfigurerAdapter work together to provide web based security.


#### If everything fails throw the computer out the window roll yourself like a burrito and cry while listening to the phantom of the wapera

