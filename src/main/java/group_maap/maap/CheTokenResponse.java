package group_maap.maap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheTokenResponse {

    private String access_token;

    public String getaccess_token() {
         return access_token;
    }

    public void setaccess_token(String value) {
        this.access_token = value;
    }
}