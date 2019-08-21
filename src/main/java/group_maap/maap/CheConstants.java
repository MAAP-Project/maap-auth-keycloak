package group_maap.maap;

public class CheConstants {
	public static final String MAAP_ORG_NAME = "maap-users";
	public static final String MAAP_ORG_ID = "organizationmtplvzlf0gwt8d4e";
	public static final String CHE_BASE_URL = "https://che-k8s.maap.xyz";
	public static final String CHE_CLIENT_ID = "che-public";
	public static final String CHE_ADMIN_USERNAME = "admin";
	public static final String CHE_ADMIN_PASSWORD = "";
	
	public class Endpoints {
		public static final String OIDC_TOKEN = "/auth/realms/che/protocol/openid-connect/token";
		public static final String FIND_USER = "/api/user/find";
		public static final String ORGANIZATION = "/api/organization";
		public static final String PERMISSIONS = "/api/permissions";
	}
}
