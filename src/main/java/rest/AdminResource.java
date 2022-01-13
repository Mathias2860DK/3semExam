package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.DinnerEventDTO;
import facades.AdminFacade;
import facades.AllFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
@Path("admin")
public class AdminResource {
    
    private EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade userFacade = UserFacade.getUserFacade(EMF);
    private final AllFacade allFacade = AllFacade.getAllFacade(EMF);
    private final AdminFacade adminFacade = AdminFacade.getAdminFacade(EMF);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

   @POST
   @Path("addDinnerEvent")
   @Produces(MediaType.APPLICATION_JSON)
   //@RolesAllowed()
    public String addDinnerEvent(String jsonEvent){
       DinnerEventDTO dinnerEventDTO = gson.fromJson(jsonEvent,DinnerEventDTO.class);
       DinnerEventDTO newDinnerEventDTO = adminFacade.addDinnerEvent(dinnerEventDTO);
       return gson.toJson(newDinnerEventDTO);
   }
}