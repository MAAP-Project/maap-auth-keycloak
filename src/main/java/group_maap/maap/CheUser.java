package group_maap.maap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheUser {

    private String name;
    private String email;
    private String id;

    public String getname() {
         return name;
    }

    public void setname(String value) {
        this.name = value;
    }

    public String getemail() {
         return email;
    }

    public void setemail(String value) {
        this.email = value;
    }

    public String getid() {
         return id;
    }

    public void setid(String value) {
        this.id = value;
    }
}