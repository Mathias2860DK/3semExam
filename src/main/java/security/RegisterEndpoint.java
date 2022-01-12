package security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import entities.Role;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.GenericExceptionMapper;
import facades.UserFacade;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("register")
public class RegisterEndpoint {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String registerUser(String userJSON) throws API_Exception {
        EntityManager em = EMF.createEntityManager();
        User userFromDB;
        String username;
        String password;
        try {
            JsonObject json = JsonParser.parseString(userJSON).getAsJsonObject();
            username = json.get("newUsername").getAsString();
            password = json.get("newPassword").getAsString();

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied", 400, e);
        }
        userFromDB = em.find(User.class, username);
        if (userFromDB == null) {
            User user = new User(username, password);
            em.getTransaction().begin();
            Role userRole = new Role("user");//ikke markeret CASCADE PERSIST
            user.addRole(userRole);
            Role checkIfRoleExists = em.find(Role.class,userRole.getRoleName());
            if (checkIfRoleExists == null){
                em.persist(userRole);
            }
            em.persist(user);

            em.getTransaction().commit();
            JsonObject responseJson = new JsonObject();
            int statusCode = Response.ok().build().getStatus();
            responseJson.addProperty("statusCode", statusCode);
            return responseJson.toString();
        } else {
            throw new WebApplicationException("Username: '" + username + "' is already taken", 404);
        }
    }
}
