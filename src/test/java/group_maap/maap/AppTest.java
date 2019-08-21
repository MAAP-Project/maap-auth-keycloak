package group_maap.maap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest 
    extends TestCase
{
    /**     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
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
    			CheConstants.MAAP_ORG_NAME, 
    			"gchang");
    	
    	assertTrue(cheUserExists);
    }

    public void testAddUserToMaapOrg()
    {
    	String token = CheOrganizationHelper.getCheToken();
    	
    	Boolean success = CheOrganizationHelper.addUserToMaapOrganization(
    			token,
    			CheConstants.MAAP_ORG_NAME, 
    			"eyam");
    	
    	String s = "";
    }
}
