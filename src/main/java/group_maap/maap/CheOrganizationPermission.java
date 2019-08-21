package group_maap.maap;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheOrganizationPermission {

    private String userId;
    private String instanceId;
    private String domainId;
    private String[] actions;

    public String getuserId() {
         return userId;
    }

    public void setuserId(String value) {
        this.userId = value;
    }

    public String getinstanceIdl() {
         return instanceId;
    }

    public void setinstanceId(String value) {
        this.instanceId = value;
    }

    public String getdomainId() {
         return domainId;
    }

    public void setdomainId(String value) {
    	this.domainId = value;
    }

    public String[] getactions() {
         return actions;
    }

    public void setactions(String[] value) {
    	this.actions = value;
    }
}