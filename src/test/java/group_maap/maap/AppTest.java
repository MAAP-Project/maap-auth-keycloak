package group_maap.maap;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.syncope.client.lib.SyncopeClient;
import org.apache.syncope.client.lib.SyncopeClientFactoryBean;
import org.apache.syncope.common.lib.patch.AttrPatch;
import org.apache.syncope.common.lib.patch.GroupPatch;
import org.apache.syncope.common.lib.patch.UserPatch;
import org.apache.syncope.common.lib.to.AttrTO;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.common.lib.to.ProvisioningResult;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.PatchOperation;
import org.apache.syncope.common.rest.api.beans.AnyQuery;
import org.apache.syncope.common.rest.api.service.GroupService;
import org.apache.syncope.common.rest.api.service.UserService;
import org.keycloak.models.UserModel;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest 
    extends TestCase
{
    private SyncopeClient client;
    private UserService userService;
    private GroupService groupService;
    
    /**     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
        
    	client = new SyncopeClientFactoryBean().
                setAddress(MaapConstants.Syncope.API_URL).
                create(MaapConstants.Syncope.API_USERNAME, MaapConstants.Syncope.API_PASSWORD);
    	
    	userService = client.getService(UserService.class);
    	groupService = client.getService(GroupService.class);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testTokenRetrieval()
    {
    	String token = CheOrganizationHelper.getCheToken();
    	
    	assertNotNull(token);
    }

    public void testUserRetrieval()
    {
    	String token = CheOrganizationHelper.getCheToken();
    	
    	CheUser cheUser = CheOrganizationHelper.getUser(token, "satorius99@hotmail.com");
    	
    	assertNotNull(cheUser);
    }

    public void testOrgContains()
    {
    	String token = CheOrganizationHelper.getCheToken();
    	
    	Boolean cheUserExists = CheOrganizationHelper.maapUserExists(
    			token, 	
    			MaapConstants.Che.MAAP_ORG_NAME, 
    			"gchang");
    	
    	assertTrue(cheUserExists);
    }

    public void testAddUserToMaapOrg()
    {
    	String token = CheOrganizationHelper.getCheToken();
    	
    	Boolean success = CheOrganizationHelper.addUserToMaapOrganization(
    			token,
    			MaapConstants.Che.MAAP_ORG_NAME, 
    			"eyam");
    }

    public void testAddUserOrgToSyncope()
    {
    	String userEmail = "elizabeth.yam@jpl.nasa.gov";
    	String username = "eyam";	
        
        SyncopeHelper.assignGroup(userService, groupService, userEmail, MaapConstants.Che.MAAP_ORG_NAME);
        
        SyncopeHelper.assignGroup(userService, groupService, userEmail, EmailToOrgConverter.convertEmail(username));
    }
}
