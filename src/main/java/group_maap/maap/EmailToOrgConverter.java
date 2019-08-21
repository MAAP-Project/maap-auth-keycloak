package group_maap.maap;

public class EmailToOrgConverter {

    public static String convertEmail(String email) {
    	return email
    			.replace("@", "-")
    			.replace(".", "");
    }
    
}