package group_maap.maap;

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.google.gson.Gson;

public class CheOrganizationHelper {

    public static String getCheToken() {

    	Client client = ResteasyClientBuilder.newClient();
    	
    	WebTarget target = client.target(CheConstants.CHE_BASE_URL + CheConstants.Endpoints.OIDC_TOKEN);
    	Form form = new Form()
    			.param("grant_type", "password")
    			.param("client_id", CheConstants.CHE_CLIENT_ID)
    			.param("username", CheConstants.CHE_ADMIN_USERNAME)
    			.param("password", CheConstants.CHE_ADMIN_PASSWORD);
    	Response response = target
    			.request()
    			.accept("application/json")
    			.post(Entity.form(form));
    	
    	CheTokenResponse cheToken = response.readEntity(CheTokenResponse.class);

    	response.close();
    	client.close();
    	
    	return cheToken.getaccess_token();
    }
    
    public static CheUser getUser(String token, String username) {

    	Client client = ResteasyClientBuilder.newClient();

    	WebTarget target = client.target(CheConstants.CHE_BASE_URL + CheConstants.Endpoints.FIND_USER)
    	                         .queryParam("name", username);

    	Response response = target
    			.request()
    			.header("Authorization", "Bearer " + token)
    			.get();
    	
    	CheUser cheUser = response.readEntity(CheUser.class);
    	
    	response.close();
    	client.close();
    	
    	return cheUser;
    }
    
    public static Boolean maapUserExists(String token, String maapOrg, String username) {

    	Client client = ResteasyClientBuilder.newClient();

    	WebTarget target = client.target(CheConstants.CHE_BASE_URL + CheConstants.Endpoints.ORGANIZATION);

    	List<CheOrganization> cheOrganizations = target
    			.request()
    			.header("Authorization", "Bearer " + token)
    			.get(new GenericType<List<CheOrganization>>() {});
    	
    	client.close();
    	
    	Boolean exists = cheOrganizations.stream()
    			  .anyMatch(o -> 
    			  	o.getqualifiedName().contains(maapOrg) && 
    				o.getqualifiedName().contains(EmailToOrgConverter.convertEmail(username)));
    	
    	return exists;
    }
    
    public static Boolean addUserToMaapOrganization(String token, String maapOrg, String username) {
    	
    	if(!maapUserExists(token, maapOrg, username)) {
    		
        	String userId = getUser(token, username).getid();
        	
        	
        	//1) Add user to MAAP org    	
        	Client client1 = ResteasyClientBuilder.newClient();
        	CheOrganizationPermission parentPermission = new CheOrganizationPermission();
        	parentPermission.setdomainId("organization");
        	parentPermission.setuserId(userId);
        	parentPermission.setinstanceId(CheConstants.MAAP_ORG_ID);
        	String[] permissions = new String[1];
        	permissions[0] = "createWorkspaces";
        	parentPermission.setactions(permissions);
        	
        	Gson gson = new Gson();
        	String jsonString = gson.toJson(parentPermission);
        
        	
        	client1.target(CheConstants.CHE_BASE_URL + CheConstants.Endpoints.PERMISSIONS)
                .request()
      			.header("Authorization", "Bearer " + token)
      			.header("Content-Type", "application/json")
    			.accept("application/json")
      			.post(Entity.json(jsonString));      	     	

        	client1.close();  
        	
        	//2) Create new org with the name [MAAP Org]/[Name of user]
        	Client client2 = ResteasyClientBuilder.newClient();
        	CheOrganization cheOrg = new CheOrganization();
        	cheOrg.setqualifiedName(CheConstants.MAAP_ORG_NAME + "/" + EmailToOrgConverter.convertEmail(username));
        	cheOrg.setname(EmailToOrgConverter.convertEmail(username));
        	cheOrg.setparent(CheConstants.MAAP_ORG_ID);
        	
        	gson = new Gson();
        	jsonString = gson.toJson(cheOrg);
        	
        	CheOrganization userOrg = client2.target(CheConstants.CHE_BASE_URL + CheConstants.Endpoints.ORGANIZATION)
                .request()
      			.header("Authorization", "Bearer " + token)
                .post(Entity.json(jsonString))
                .readEntity(CheOrganization.class);

        	client2.close();  
        	
        	
        	//3) Add new user to `[MAAP Org]/[Name of user]` and make them the admin of it
        	Client client3 = ResteasyClientBuilder.newClient();
        	CheOrganizationPermission userOrgPermission = new CheOrganizationPermission();
        	userOrgPermission.setdomainId("organization");
        	userOrgPermission.setuserId(userId);
        	userOrgPermission.setinstanceId(userOrg.getid());
        	String[] userOrgPermissions = new String[5];
        	userOrgPermissions[0] = "update";
        	userOrgPermissions[1] = "setPermissions";
        	userOrgPermissions[2] = "manageResources";
        	userOrgPermissions[3] = "createWorkspaces";
        	userOrgPermissions[4] = "manageWorkspaces";
        	userOrgPermission.setactions(userOrgPermissions);
        	
        	gson = new Gson();
        	jsonString = gson.toJson(userOrgPermission);
        	
        	client3.target(CheConstants.CHE_BASE_URL + CheConstants.Endpoints.PERMISSIONS)
                .request()
      			.header("Authorization", "Bearer " + token)
                .post(Entity.json(jsonString));

        	client3.close();  
        	
        	
        	return true;
    	}

    	return false;
    }
}