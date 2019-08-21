package group_maap.maap;

import org.keycloak.Config.Scope;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.IdentityProviderMapper;
import org.keycloak.broker.saml.SAMLEndpoint;
//import org.keycloak.broker.saml.SAMLEndpoint;
//import org.keycloak.broker.oidc.O;
//import org.keycloak.broker.oidc;
import org.keycloak.broker.oidc.OIDCIdentityProviderFactory;
import org.keycloak.dom.saml.v2.assertion.AssertionType;
import org.keycloak.dom.saml.v2.assertion.AttributeStatementType;
import org.keycloak.dom.saml.v2.assertion.AttributeType;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.protocol.oidc.mappers.OIDCAttributeMapperHelper;
import org.keycloak.provider.ProviderConfigProperty;
import org.jboss.logging.Logger;
import java.util.ArrayList;
import java.util.List;

public class MaapAttributeMapper extends AbstractProviderMapper implements IdentityProviderMapper {
    public static final String[] COMPATIBLE_PROVIDERS = {OIDCIdentityProviderFactory.PROVIDER_ID};

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>(); 

    protected static final Logger logger = Logger.getLogger(MaapAttributeMapper.class);
	protected static final Logger LOGGER_DUMP_USER_PROFILE = Logger.getLogger("org.keycloak.social.user_profile_dump");

    public static final String ATTRIBUTE_NAME = "attribute.name";
    public static final String ATTRIBUTE_FRIENDLY_NAME = "attribute.friendly.name";
    public static final String ATTRIBUTE_VALUE = "attribute.value";

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(ATTRIBUTE_NAME);
        property.setLabel("Attribute Name");
        property.setHelpText("Name of attribute to search for in assertion.  You can leave this blank and specify a friendly name instead.");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);
        property = new ProviderConfigProperty();
        property.setName(ATTRIBUTE_FRIENDLY_NAME);
        property.setLabel("Friendly Name");
        property.setHelpText("Friendly name of attribute to search for in assertion.  You can leave this blank and specify a name instead.");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);
        property = new ProviderConfigProperty();
        property.setName(ATTRIBUTE_VALUE);
        property.setLabel("Attribute Value");
        property.setHelpText("Value the attribute must have.  If the attribute is a list, then the value must be contained in the list.");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);
        property = new ProviderConfigProperty();
        property.setName("group");
        property.setLabel("Group");
        property.setHelpText("Group to grant to user. i.e. /Group1/SubGroup2");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);
    }

    public static final String PROVIDER_ID = "maap-idp-attribute-mapper";

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String[] getCompatibleProviders() {
        return COMPATIBLE_PROVIDERS;
    }

    @Override
    public String getDisplayCategory() {
        return "base64 attribute to token";
    }

    @Override
    public String getDisplayType() {
        return "base64 attribute to token";
    }

    @Override
    public void importNewUser(KeycloakSession session, RealmModel realm, UserModel user, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {

    	//super.importNewUser(session, realm, user, mapperModel, context);
    	
    	LOGGER_DUMP_USER_PROFILE.debug("importNewUser. BrokeredIdentityContext.token: " + context.getToken());
    	
//        String groupName = mapperModel.getConfig().get("group");
//        if (isAttributePresent(mapperModel, context)) {
//            GroupModel group = KeycloakModelUtils.findGroupByPath(realm, groupName);
//            if (group == null) throw new IdentityBrokerException("Unable to find group: " + groupName);
//            user.joinGroup(group);
//        }
    }

    protected boolean isAttributePresent(IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        String name = mapperModel.getConfig().get(ATTRIBUTE_NAME);
        if (name != null && name.trim().equals("")) name = null;
        String friendly = mapperModel.getConfig().get(ATTRIBUTE_FRIENDLY_NAME);
        if (friendly != null && friendly.trim().equals("")) friendly = null;
        String desiredValue = mapperModel.getConfig().get(ATTRIBUTE_VALUE);
        
        AssertionType assertion = (AssertionType)context.getContextData().get(SAMLEndpoint.SAML_ASSERTION);
        
        for (AttributeStatementType statement : assertion.getAttributeStatements()) {
            for (AttributeStatementType.ASTChoiceType choice : statement.getAttributes()) {
                AttributeType attr = choice.getAttribute();
                if (name != null && !name.equals(attr.getName())) continue;
                if (friendly != null && !friendly.equals(attr.getFriendlyName())) continue;
                for (Object val : attr.getAttributeValue()) {
                    if (val.equals(desiredValue)) return true;
                }
            }
        }
        return false;
    }
    

    @Override
    public void updateBrokeredUser(KeycloakSession session, RealmModel realm, UserModel user, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        String attributeValue = mapperModel.getConfig().get(ATTRIBUTE_VALUE);
        if (attributeValue == null) return;
        
    	LOGGER_DUMP_USER_PROFILE.debug("updateBrokeredUser. attributeValue: " + attributeValue);
    	
    	String valueValue = user.getFirstAttribute(attributeValue);
        
    	LOGGER_DUMP_USER_PROFILE.debug("updateBrokeredUser. attributeValueValue: " + valueValue);
        
        if(valueValue != null && !valueValue.startsWith("ssh") && org.apache.commons.codec.binary.Base64.isBase64(valueValue)) {
            
    		String decodedKey = new String(org.apache.commons.codec.binary.Base64.decodeBase64(valueValue));
        	LOGGER_DUMP_USER_PROFILE.debug("updateBrokeredUser.decoded value : " + decodedKey);
            user.setSingleAttribute(attributeValue, decodedKey);
        }
    }

    @Override
    public String getHelpText() {
        return "If a claim exists, grant the user the specified realm or application group.";
    }

    @Override
    public MaapAttributeMapper create(KeycloakSession session) {
        return new MaapAttributeMapper();
    }

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Scope arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postInit(KeycloakSessionFactory arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preprocessFederatedIdentity(KeycloakSession arg0, RealmModel arg1, IdentityProviderMapperModel mapperModel,
			BrokeredIdentityContext context) {
				
		String attribute = mapperModel.getConfig().get(ATTRIBUTE_NAME);
		
		context.getContextData().forEach((k, v) -> {
			LOGGER_DUMP_USER_PROFILE.debug("preprocessFederatedIdentity.context: " + k + ": " + v);
		});
		
		LOGGER_DUMP_USER_PROFILE.debug("preprocessFederatedIdentity: " + context.getContextData().get(attribute));
		// TODO Auto-generated method stub
		
	}
}