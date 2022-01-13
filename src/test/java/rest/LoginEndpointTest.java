package rest;

import dtos.DinnerEventDTO;
import entities.*;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;

import org.junit.jupiter.api.*;
import utils.EMF_Creator;

//Disabled
public class LoginEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    User user;
    DinnerEvent dinnerEvent1;
    DinnerEvent dinnerEvent2;
    DinnerEvent dinnerEvent3;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Transaction").executeUpdate();
            em.createQuery("delete from Assignment ").executeUpdate();
            em.createQuery("delete from DinnerEvent ").executeUpdate();

            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();



            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User testUser = new User("testUser","test");
            testUser.addRole(userRole);
             user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.persist(testUser);
            //System.out.println("Saved test data to database");
            em.getTransaction().commit();

            Date date;
            try {
                String date_string = "13-01-2020";
                //Instantiating the SimpleDateFormat class
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                //Parsing the given String to Date object

                date = formatter.parse(date_string);
            } catch (NumberFormatException | ParseException e){
                throw new NumberFormatException("format fejl");
            }

            //Creating Dinner Events
            dinnerEvent1 = new DinnerEvent("Fiktivvej 13, kbh Ø","Kylling i karry",200,date);
            dinnerEvent2 = new DinnerEvent("Fiktivvej 15, Søborg","Burger",150,date);
            dinnerEvent3 = new DinnerEvent("Fiktivvej 100, Emdrup","Pizza",300,date);

            //Not marked Cascade persist. So have to be persisted first
            em.getTransaction().begin();
            em.persist(dinnerEvent1);
            em.persist(dinnerEvent2);
            em.persist(dinnerEvent3);
            em.getTransaction().commit();
          /*  dinnerEvent1.setId(1);
            dinnerEvent2.setId(2);
            dinnerEvent3.setId(3);*/

            //Creating assignments;
            Assignment assignment1 = new Assignment(dinnerEvent1,"Poulsen","112233");
            Assignment assignment2 = new Assignment(dinnerEvent2,"Jensen","2222222");
            Assignment assignment3 = new Assignment(dinnerEvent3,"Poulsen","112233");

            //Creating transactions:
            Transaction transaction1 = new Transaction(dinnerEvent1.getPricePerPerson());
            Transaction transaction2 = new Transaction(dinnerEvent2.getPricePerPerson());
            Transaction transaction3 = new Transaction(dinnerEvent3.getPricePerPerson());

            user.setBalance(1000);

            user.addAssignment(assignment1); //Assignment contains Dinner Event
            user.addTransaction(transaction1);

            user.addAssignment(assignment3);
            user.addTransaction(transaction3);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }


    //US1
    @Test
    @DisplayName("US1")
    public void getAllEvents(){
        login("user","test");
        given().log().all().when().get("/all/getAllEvents").then().log().body();
        List<DinnerEventDTO> dinnerEventDTOS;
        dinnerEventDTOS = given()
                .contentType("application/json")
                .when()
                .get("/all/getAllEvents")
                .then()
                .extract().body().jsonPath().getList(".", DinnerEventDTO.class);
        DinnerEventDTO dinnerEventDTO1 = new DinnerEventDTO(dinnerEvent1);
        DinnerEventDTO dinnerEventDTO2 = new DinnerEventDTO(dinnerEvent2);
        DinnerEventDTO dinnerEventDTO3 = new DinnerEventDTO(dinnerEvent3);

        //Requires equals method in DinnerEventDTO
        assertThat(dinnerEventDTOS, containsInAnyOrder(dinnerEventDTO1,dinnerEventDTO2,dinnerEventDTO3));

    }

    //US2 --> Check account balance
    @Test
    @DisplayName("US2 --> Check account balance")
    public void getAccountBalance(){
        login("user","test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/all/getAccountBalance/{username}",user.getUserName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("accountBalance", equalTo("500.0"));
    }

    //US 2 --> check all previous transactions
    @Test
    @DisplayName("US 2 --> check all previous transactions")
    public void getAllTransactionsById(){
        login("user","test");
        given().log().all().when().get("/all/getAllTransactionsById/{username}",user.getUserName()).then().log().body();
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/all/getAllTransactionsById/{username}",user.getUserName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2));

    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }

    @Test
    public void testRestNoAuthenticationRequired() {
        given()
                .contentType("application/json")
                .when()
                .get("/info/").then()
                .statusCode(200)
                .body("msg", equalTo("Hello anonymous"));
    }

    @Test
    public void testRestForAdmin() {
        login("admin", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/info/admin").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to (admin) User: admin"));
    }

    @Test
    public void testRestForUser() {
        login("user", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/user").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to User: user"));
    }

    @Test
    public void testAutorizedUserCannotAccesAdminPage() {
        login("user", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/admin").then() //Call Admin endpoint as user
                .statusCode(401);
    }

    @Test
    public void testAutorizedAdminCannotAccesUserPage() {
        login("admin", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/user").then() //Call User endpoint as Admin
                .statusCode(401);
    }

    @Test
    public void testRestForMultiRole1() {
        login("user_admin", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/info/admin").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to (admin) User: user_admin"));
    }

    @Test
    public void testRestForMultiRole2() {
        login("user_admin", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/user").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to User: user_admin"));
    }

    @Test
    public void userNotAuthenticated() {
        logOut();
        given()
                .contentType("application/json")
                .when()
                .get("/info/user").then()
                .statusCode(403)
                .body("code", equalTo(403))
                .body("message", equalTo("Not authenticated - do login"));
    }

    @Test
    public void adminNotAuthenticated() {
        logOut();
        given()
                .contentType("application/json")
                .when()
                .get("/info/user").then()
                .statusCode(403)
                .body("code", equalTo(403))
                .body("message", equalTo("Not authenticated - do login"));
    }

}