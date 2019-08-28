package group_maap.maap;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.credential.CredentialModel;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.cache.UserCache;
import org.keycloak.models.utils.RoleUtils;

import static org.keycloak.models.utils.KeycloakModelUtils.getRoleFromString;

import org.apache.syncope.client.lib.SyncopeClient;
import org.apache.syncope.client.lib.SyncopeClientFactoryBean;
import org.apache.syncope.common.rest.api.service.GroupService;
import org.apache.syncope.common.rest.api.service.UserService;

@JBossLog
public class LoginEventListenerProvider implements EventListenerProvider {

    private final KeycloakSession session;

    private SyncopeClient client;
    private UserService userService;
    private GroupService groupService;

    public LoginEventListenerProvider(KeycloakSession session) {
        this.session = session;
        
    	client = new SyncopeClientFactoryBean().
                setAddress(MaapConstants.Syncope.API_URL).
                create(MaapConstants.Syncope.API_USERNAME, MaapConstants.Syncope.API_PASSWORD);
    	
    	userService = client.getService(UserService.class);
    	groupService = client.getService(GroupService.class);
    }

    @Override
    public void onEvent(Event event) {

        if (!EventType.LOGIN.equals(event.getType())) {
            return;
        }

        log.infof("onEvent: event=%s", event.getType());

        UserProvider users = this.session.getProvider(UserProvider.class);
        RealmModel realm = session.getContext().getRealm();
        UserModel user = users.getUserById(event.getUserId(), realm);
        
        String username = user.getUsername();
        
        log.infof("onEvent: username=%s", username);
        
        if(!"admin".equals(username)) {
            log.infof("onEvent: getting che token for %s", username);
            
            String cheToken = CheOrganizationHelper.getCheToken();
            
            //Add user to Che MAAP and user groups
            Boolean userAdded = CheOrganizationHelper.addUserToMaapOrganization(cheToken, MaapConstants.Che.MAAP_ORG_NAME, username);
            
            log.infof("onEvent: addUserToMaapOrganization result=%s", userAdded);

            //Add user to Syncope MAAP and user groups
            SyncopeHelper.assignGroup(userService, groupService, user.getEmail(), MaapConstants.Che.MAAP_ORG_NAME);
            log.infof("onEvent: SyncopeHelper.assignGroup=%s", MaapConstants.Che.MAAP_ORG_NAME);
            
            SyncopeHelper.assignGroup(userService, groupService, user.getEmail(), EmailToOrgConverter.convertEmail(username));
            log.infof("onEvent: SyncopeHelper.assignGroup=%s", EmailToOrgConverter.convertEmail(username));
        }
    }


    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        log.infof("onEvent adminEvent={}, includeRepresentation=", event, includeRepresentation);
    }

    @Override
    public void close() {
        log.infof("close");
    }
}