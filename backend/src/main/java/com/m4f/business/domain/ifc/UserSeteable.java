package com.m4f.business.domain.ifc;

import java.util.Collection;
import com.m4f.business.domain.InternalUser;

public interface UserSeteable {
	
	void addUser(InternalUser user);
	void removeUser(InternalUser user);
	void addUsers(Collection<InternalUser> users);
	void removeUsers(Collection<InternalUser> users);
	
}