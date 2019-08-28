package group_maap.maap;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.syncope.client.lib.SyncopeClient;
import org.apache.syncope.common.lib.patch.MembershipPatch;
import org.apache.syncope.common.lib.patch.UserPatch;
import org.apache.syncope.common.lib.to.GroupTO;
import org.apache.syncope.common.lib.to.ProvisioningResult;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.PatchOperation;
import org.apache.syncope.common.rest.api.beans.AnyQuery;
import org.apache.syncope.common.rest.api.service.GroupService;
import org.apache.syncope.common.rest.api.service.UserService;

public class SyncopeHelper {

	public static GroupTO getGroup(GroupService groupService, String groupName) {
		
    	List<GroupTO> matchingGroups = groupService.search(
    		    new AnyQuery.Builder().realm(MaapConstants.Syncope.ROOT_REALM).
    		    fiql(SyncopeClient.getGroupSearchConditionBuilder()
    		    		.is(MaapConstants.Syncope.ATTR_NAME).equalTo(groupName).query()).
    		    build()).getResult();	
    	
    	if(matchingGroups == null || matchingGroups.isEmpty())
    		return null;
    	else
    		return matchingGroups.get(0);
	}
	
	public static GroupTO createGroup(GroupService groupService, String groupName) {
    	GroupTO groupTO = new GroupTO();
    	groupTO.setRealm(MaapConstants.Syncope.ROOT_REALM);
    	groupTO.setName(groupName);
    	
	    Response response = groupService.create(groupTO);
	    
	    ProvisioningResult<GroupTO> result = 
	    		response.readEntity(new GenericType<ProvisioningResult<GroupTO>>() {});
	    
	    return result.getEntity();
	}

	public static void assignGroup(UserService userService, GroupService groupService, String userEmail, String groupName) {
		
    	GroupTO grp = getGroup(groupService, groupName);
    	
    	if(grp == null)
    		grp = createGroup(groupService, groupName);
    	
    	UserTO usr = getUser(userService, userEmail);    	
    	
        UserPatch userPatch = new UserPatch();
        userPatch.setKey(usr.getKey());
        userPatch.getMemberships().add(
                new MembershipPatch.Builder().operation(PatchOperation.ADD_REPLACE).group(grp.getKey()).build());
        
    	userService.update(userPatch)
    		.readEntity(new GenericType<ProvisioningResult<UserTO>>() { });
	}
	
	public static UserTO getUser(UserService userService, String userEmail) {
		
    	UserTO matchingUser = userService.search(
    		    new AnyQuery.Builder().realm(MaapConstants.Syncope.ROOT_REALM).
    		    fiql(SyncopeClient.getUserSearchConditionBuilder()
    		    		.is(MaapConstants.Syncope.ATTR_EMAIL).equalTo(userEmail).query()).
    		    build()).getResult().get(0);
    	
    	return matchingUser;
	}
}
