Brief explaination of solution:
First I'd like to say that I've reused code where I found it appropriate from the PG6100 Github repository.
The solution is pretty straightforward, but things I'd like to mention are that I decided to set the CountryController ApplicationScoped mainly due to the fact that it's loading the countries and it saves a lot of loading considering the constant use of countries in the project.
Event is using a join table for its' participants.
Throughout the project, I've attempted to structure in packages and folders neatly, especially under "webapp".
--------------------------------------

Controllers, EJBs, Entities and rest web is first of all placed under the package "com.westerdals.hauaug13".
Here you will find the following packages with their respective files:
- controller
* CountryController
* EventController
* UserController
- EJB
* CountryEJB
* EventEJB
* UserEJB
- entity
* Event
* User
- restweb
* ApplicationConfig
* EventDTO
* EventList
* EventRestService

--------------------------------------

Webapp structure:
- event
- layouts
- shared
- user
- home.xhtml
- index.html (redirects to home.faces)

--------------------------------------

Testing:
-RestIT
-WebPageIT
* WebPageIT has 1 Wiremock test, and in order to test the tests in this class properly, it needs to be commented out while running the tests. Otherwise please run the tests one by one.
* To run the wiremock test please comment out all the other tests and run "mvn verify"

* Both test files needs a refresh of the database, which means that the wildfly server has to be restarted. It automatically creates and drops the database.
* This means that after running one of the test classes, the wildfly server needs to be restarted. This is mainly due to inconsistent test data.

* I'd also like to warn that I've experienced some inconsistencies with the results of the tests I've implemented, especially when running "mvn verify", and I believe it's mainly due to the order of the tests. And the fact that the database needs a refresh after each test class.
* If a test should fail while running, please run it one by one.

--------------------------------------

Thanks for a great semester and a lot of valuable education! Hope you have a good holiday :)