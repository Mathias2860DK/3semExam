package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.nimbusds.jose.shaded.json.JSONArray;
import dtos.DinnerEventDTO;
import dtos.TransactionDTO;
import dtos.UserDTO;
import entities.DinnerEvent;
import entities.Transaction;
import entities.User;
import facades.AllFacade;
import facades.UserFacade;
import utils.EMF_Creator;
import utils.MemberId;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
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
//US2
    @GET
    @Path("getAllEventsByUser/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEventsByUser(@PathParam("username") String userName){
        List<DinnerEventDTO> dinnerEventDTOS = allFacade.getAllEventsByUser(userName);
        return gson.toJson(dinnerEventDTOS);
    }

    //US 2
    @GET
    @Path("getAllTransactionsById/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTransactionsById(@PathParam("username") String userName){
        List<TransactionDTO> transactions = allFacade.getAllTransactionsById(userName);
        return gson.toJson(transactions);
    }

    //US 2 Current account status
    @GET
    @Path("getAccountBalance/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccountBalance(@PathParam("username") String userName){
       double accountBalance = allFacade.getAccountBalance(userName);
        return "{\"accountBalance\": \"" + accountBalance + "\"}";
    }

   //US 3
   @PUT
    @Path("addMemberToEvent/{eventId}")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
    public String addMemberToEvent(@PathParam("eventId") String eventId, String memberJson){

      UserDTO userDTO = gson.fromJson(memberJson,UserDTO.class);
      UserDTO userDTO1 = allFacade.addMemberToEvent(userDTO,eventId);

       return gson.toJson(userDTO1);

   }

   //US3
    @PUT
    @Path("addMembersToEvent/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addNewMembersToEvent(@PathParam("eventId") String eventId, String membersJson){

        //[ {id: "user", assignmentId: "1"}, {id: "random", assignmentId: "1""} ]
        System.out.println(membersJson);
        Type genreTypeList = new TypeToken<ArrayList<MemberId>>() {
        }.getType();
        List<MemberId> memberIds = gson.fromJson(membersJson,genreTypeList);
        for (MemberId memberId: memberIds) {
            System.out.println(memberId.getId());
        }
        List<UserDTO> userDTOS = allFacade.addMembersToEvent(memberIds,eventId);

        return gson.toJson(userDTOS);
    }

    //US5 --> Display all membersAssignedToEvent
    @GET
    @Path("getAllMembersAssignedToEvent/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMembersAssignedToEvent(@PathParam("eventId") String eventId){
       List<UserDTO> userDTOS = allFacade.getAllMembersAssignedToEvent(eventId);
        return gson.toJson(userDTOS);
    }



}