package group_maap.maap;

public class MaapConstants {
	
	public class Che {
		public static final String MAAP_ORG_NAME = "maap-users";
		public static final String MAAP_ORG_ID = "organizationmtplvzlf0gwt8d4e";
		public static final String CLIENT_ID = "che-public";
		public static final String ADMIN_USERNAME = "admin";
		public static final String ADMIN_PASSWORD = "";
		
		public class Endpoints {
			private static final String API_BASE_URL = "https://che-k8s.maap.xyz";
			public static final String OIDC_TOKEN = API_BASE_URL + "/auth/realms/che/protocol/openid-connect/token";
			public static final String FIND_USER = API_BASE_URL + "/api/user/find";
			public static final String ORGANIZATION = API_BASE_URL + "/api/organization";
			public static final String PERMISSIONS = API_BASE_URL + "/api/permissions";
		}		
	}
	
	public class Syncope {
		public static final String ROOT_REALM = "/";
		public static final String ATTR_EMAIL = "email";
		public static final String ATTR_NAME = "name";
		public static final String API_URL = "https://auth.nasa.maap.xyz/syncope/rest/";
		public static final String API_USERNAME = "admin";
		public static final String API_PASSWORD = "";
	}
}
