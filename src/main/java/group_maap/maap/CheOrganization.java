package group_maap.maap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheOrganization {

    private String qualifiedName;
    private String name;
    private String parent;
    private String id;

    public String getname() {
         return name;
    }

    public void setname(String value) {
        this.name = value;
    }

    public String getqualifiedName() {
         return qualifiedName;
    }

    public void setqualifiedName(String value) {
        this.qualifiedName = value;
    }

    public String getid() {
         return id;
    }

    public void setid(String value) {
        this.id = value;
    }

    public String getparent() {
         return parent;
    }

    public void setparent(String value) {
        this.parent = value;
    }
}