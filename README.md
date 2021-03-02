# Spring Security for dummies
Code from this course https://www.youtube.com/watch?v=her_7pa0vrg
the code inside is commented please read it, im talking to you especifically, you know who you are 


## Dependencies 
* Spring Security 
* Guava 

#### If you dont have any users spring security gives you a default user
To login in the form default:
user /
generated security password

### Users: 
* username most be unique 
* Passwords most be encoded
* they should have Roles 
* with the roles you can define the endpoints of your users using authorities
* higly recommended to use authorities

basic auth with basic auth you need to specify the username and password inside of the request header as B64 good for external API
basic auth cant logout because the user and password is sent in everysingle request 

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

## CSRF 
* https://wanago.io/2020/12/21/csrf-attacks-same-site-cookies/ links 
* Cross-Site Request Forgery attack is an attack that forces the end user to make an unwanted calls to the web application servers where the end user is already authenticated.
* When to use CSRF protection: for any request that could be processed by a browser by normal users. If you are only creating a service that is used by non-browser clients, you will likely want to disable CSRF protection.
* Depending on the configuration of the cookies, the browser might attach them to the request. It would be a simple Cross-Site Request Forgery attack by forcing the user to perform an unwanted action.
* How to activate the cookies no postman? -> Capture requests and cookies (the bottom looks like an antena) request -> chenge from proxy to interceptor (you need to install a plugin no browser)
* When you do a Get request you can go next to the body and see the cookies, you need to grab the cookie token and before you do a request you go to headers and you put: X-XSRF-TOKEN and in value you put the token.


## Java errors

* error java db time zone - Whenever you have the link to your database you can put this link jdbc:mysql://localhost:3306/{nameOfTheSchema}?useTimezone=true&serverTimezone=UTC
* java.lang.IllegalArgumentException: there is no PasswordEncoder mapped for the id "null" Users required to have the password encoded, there's something wrong with your encoder or you didnt implement it 
* Error Encoded password does not look like Bcrypt // you need to inject the PasswordEncoder into your application security configuration

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

* @DeleteMapping = shortcut for @RequestMapping(method =RequestMethod.DELETE).

* @EnableGlobalMethodSecurity(prePostEnabled = true) = EnableGlobalMethodSecurity provides AOP security on methods. Some of the annotations that it provides are PreAuthorize, PostAuthorize. It also has support for JSR-250. Without enabling the annotations mean nothing
* @EnableWebSecurity = is a marker annotation. It allows Spring to find (it's a @Configuration and, therefore, @Component) and automatically apply the class to the global WebSecurity.
* @Entity  =  An entity represents a table stored in a database. Every instance of an entity represents a row in the table.
* @EqualsAndHashCode = Any class definition may be annotated with @EqualsAndHashCode to let lombok generate implementations of the equals(Object other) and hashCode() methods.

* @GeneratedValue = is to configure the way of increment of the specified column(field).
* @GetMapping = shortcut for @RequestMapping(method = RequestMethod.GET)
* @Getter and @Setter = You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.

* HTTP = is used for communications over the internet
* HttpSecurity = It allows configuring web based security for specific http requests. By default it will be applied to all requests, but can be restricted using requestMatcher(RequestMatcher) or other similar methods.

* @Id = Specifies the primary key of an entity.
* InMemoryUserDetailsManager = Non-persistent implementation of UserDetailsManager which is backed by an in-memory map.

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
* @PutMapping = shortcut for @RequestMapping(method = RequestMethod.PUT)

* @Repository = is used in classe with the mechanism for encapsulating storage, retrieval, and search behavior which emulates a collection of objects aka db
* @RequestBody and @ResponseBody =  are used to convert the body of the HTTP request and response with java class objects. Both these annotations will use registered HTTP message converters in the process of converting/mapping HTTP request/response body with java objects.
* @RestController = is a specialized version of the controller. It includes the @Controller and @ResponseBody annotations and as a result, simplifies the controller implementation
* @RequestMapping("url") = The class-level annotation maps a specific request path or pattern onto a controller. You can then apply additional method-level annotations to make mappings more specific to handler methods.

* @Service = is used with classes that provide some business functionalities. Spring context will autodetect these classes when annotation-based configuration and classpath scanning is used.
* SimpleGrantedAuthority = is a basic concrete implementation of a GrantedAuthority must either represent itself as a String or be specifically supported by an AccessDecisionManager.

* UserDetailsService = The UserDetailsService is a core interface in Spring Security framework, which is used to retrieve the user’s authentication and authorization information.

* WebSecurityConfig = class with the web security configurations
* WebSecurityConfigurerAdapter = The @EnableWebSecurity annotation and WebSecurityConfigurerAdapter work together to provide web based security.


#### If everything fails throw the computer out the window roll yourself like a burrito and cry while listening to the phantom of the wapera

