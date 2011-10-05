package com.m4f.business.security;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import com.m4f.business.service.ifc.IServiceLocator;
import java.util.logging.Logger;

public class InternalUserDetailService implements UserDetailsService {
	
	private static final Logger LOGGER = Logger.getLogger(InternalUserDetailService.class.getName());
	
	@Autowired
	protected IServiceLocator serviceLocator;
	
	private static InternalUser root;
	
	public InternalUserDetailService(String rootUser, 
			String rootPassword) {
		root = new InternalUser();
		root.setEmail(rootUser);
		root.setPassword(rootPassword);
	}
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		if(root.getEmail().equals(username)) {
			return makeRootUser(root);
		}
		
		try {
			InternalUser user = this.serviceLocator.getUserService().getUser(username);
			return this.makeUser(user);
		} catch(Exception e) {
			throw new UsernameNotFoundException("User not found: " + username);
		}
	}
	
	private UserDetails makeRootUser(InternalUser user) {
		Object salt = null;
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		return new User(user.getEmail(), encoder.encodePassword(user.getPassword(), salt), 
				true, true, true, true, makeRootGrantedAuthorities());
	}

	private java.util.Collection<GrantedAuthority> makeRootGrantedAuthorities() {
		java.util.Collection<GrantedAuthority> authorities = 
				new java.util.ArrayList<GrantedAuthority>();
		Set<String> roles = new HashSet<String>(); 
	    roles.add("ROLE_ROOT");        
	    //roles.add("ROLE_MEDIATOR");
	    roles.add("ROLE_ADMIN");
		for (String role : roles) {
			authorities.add(new GrantedAuthorityImpl(role));
		}
		return authorities;
	}
	
	private UserDetails makeUser(InternalUser user) throws Exception {
		Object salt = null;
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		
		if(user.getAdmin() != null && user.getAdmin()) {
			return new User(user.getEmail(), encoder.encodePassword(user.getPassword(), salt), 
					true, true, true, true, makeAdminGrantedAuthorities());
		} 
		
		// TODO change it! By default it's a provider user
		return new User(user.getEmail(), encoder.encodePassword(user.getPassword(), salt), 
				true, true, true, true, makeProviderGrantedAuthorities(user));
	}

	private java.util.Collection<GrantedAuthority> makeAdminGrantedAuthorities() {
		java.util.Collection<GrantedAuthority> authorities = 
				new java.util.ArrayList<GrantedAuthority>();
		Set<String> roles = new HashSet<String>(); 
	    roles.add("ROLE_ADMIN");        
		for (String role : roles) {
			authorities.add(new GrantedAuthorityImpl(role));
		}
		return authorities;
	}
	
	private java.util.Collection<GrantedAuthority> makeProviderGrantedAuthorities(InternalUser user) throws Exception {
		java.util.Collection<GrantedAuthority> authorities = 
				new java.util.ArrayList<GrantedAuthority>();
		Set<String> roles = new HashSet<String>();
		if(this.isAutomaticMediator(user)) {
			roles.add("ROLE_AUTOMATIC_MEDIATOR");
		} else {
			roles.add("ROLE_MANUAL_MEDIATOR");
		}
		for (String role : roles) {
			authorities.add(new GrantedAuthorityImpl(role));
		}
		return authorities;
	}
	
	private boolean isAutomaticMediator(InternalUser user) throws Exception {
		MediationService mediationService = 
			this.serviceLocator.getMediatorService().getMediationServiceByUser(user.getId(), Locale.getDefault());
		return mediationService.getHasFeed();
	}
	
	
}