package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.DinnerEventDTO;
import entities.User;
import facades.AllFacade;
import facades.UserFacade;
import utils.EMF_Creator;
import utils.SetupTestUsers;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
@Path("all")
public class AllResource {
    
    private EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade userFacade = UserFacade.getUserFacade(EMF);
    private final AllFacade allFacade = AllFacade.getAllFacade(EMF);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    //US 1
   @GET
   @Path("getAllEvents")
   @Produces(MediaType.APPLICATION_JSON)
    public String getAllEvents(){
       List<DinnerEventDTO> dinnerEventDTOS = allFacade.getAllEvents();
       return gson.toJson(dinnerEventDTOS);
   }

   //US 3
   @POST
    @Path("addMembersToEvent")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
    public String addMembersToEvent(){


       return "";

   }

}