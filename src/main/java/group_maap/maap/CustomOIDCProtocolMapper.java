package group_maap.maap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jboss.logging.Logger;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityProviderMapper;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.ProtocolMapper;
import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAccessTokenMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAttributeMapperHelper;
import org.keycloak.protocol.oidc.mappers.OIDCIDTokenMapper;
import org.keycloak.protocol.oidc.mappers.UserInfoTokenMapper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;

public class CustomOIDCProtocolMapper extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper {

    /*
     * A config which keycloak uses to display a generic dialog to configure the token.
     */
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();
	
	protected static final Logger logger = Logger.getLogger(CustomOIDCProtocolMapper.class);

	protected static final Logger LOGGER_DUMP_USER_PROFILE = Logger.getLogger("org.keycloak.social.user_profile_dump");

    /*
     * The ID of the token mapper. Is public, because we need this id in our data-setup project to
     * configure the protocol mapper in keycloak.
     */
    public static final String PROVIDER_ID = "oidc-hello-world-mapper";

    static {
        // The builtin protocol mapper let the user define under which claim name (key)
        // the protocol mapper writes its value. To display this option in the generic dialog
        // in keycloak, execute the following method.
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        // The builtin protocol mapper let the user define for which tokens the protocol mapper
        // is executed (access token, id token, user info). To add the config options for the different types
        // to the dialog execute the following method. Note that the following method uses the interfaces
        // this token mapper implements to decide which options to add to the config. So if this token
        // mapper should never be available for some sort of options, e.g. like the id token, just don't
        // implement the corresponding interface.
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, CustomOIDCProtocolMapper.class);
    }

    @Override
    public String getDisplayCategory() {
        return "MAAP token mapper";
    }

    @Override
    public String getDisplayType() {
        return "MAAP token mapper";
    }

    @Override
    public String getHelpText() {
        return "Adds a MAAP URS token to the claim";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }    

    protected void setClaim(final IDToken token, final ProtocolMapperModel mappingModel, final UserSessionModel userSession, final KeycloakSession keycloakSession) {
        // adds our data to the token. Uses the parameters like the claim name which were set by the user
        // when this protocol mapper was configured in keycloak. Note that the parameters which can
        // be configured in keycloak for this protocol mapper were set in the static intializer of this class.
        
        Map<String, Object> otherClaims = token.getOtherClaims();
        
//        userSession.getNotes().forEach((k, v) -> {
//        	LOGGER_DUMP_USER_PROFILE.debug("userSession.Note " + k + ": " + String.valueOf(v));
//        });
        
//        String attributeValue = mappingModel.getConfig().get(CLAIM_VALUE);
//        if (attributeValue == null) return;
//        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, attributeValue);
//        
//        userSession.getUser().getAttributes().forEach((k, v) -> {
//        	LOGGER_DUMP_USER_PROFILE.debug("userSession.getUser().getAttributes() " + k + ": " + String.valueOf(v));
//        	
//        	if(Objects.equals(k.trim(), "public_ssh_keys")) {
//        		LOGGER_DUMP_USER_PROFILE.debug("userSession.getUser().getAttributes() == public_ssh_keys; decoding claim ");
//        			
//        		String decodedKey = new String(org.apache.commons.codec.binary.Base64.decodeBase64(String.valueOf(v)));
//        		
//        		OIDCAttributeMapperHelper.mapClaim(token, mappingModel, attributeValue);(mappingModel, attributeValue)(token, mappingModel, decodedKey);
//        	}
//        });
        
        
        
//        otherClaims.forEach((k, v) -> {
//        	LOGGER_DUMP_USER_PROFILE.debug("otherClaim " + k + ": " + String.valueOf(v));
//        });
//        
//        mappingModel.getConfig().forEach((k, v) -> {
//        	LOGGER_DUMP_USER_PROFILE.debug("mappingModel.Config " + k + ": " + String.valueOf(v));
//        });
        
        LOGGER_DUMP_USER_PROFILE.debug("mappingModel.getName(): " + mappingModel.getName());
//    	
//    	if(Objects.equals(mappingModel.getName().trim(), "affiliation")) {
//    		LOGGER_DUMP_USER_PROFILE.debug("mappingModel.getName() == affiliation; setting affiliation claim ");
//    		OIDCAttributeMapperHelper.mapClaim(token, mappingModel, "Government");
//    	}
    }   

}