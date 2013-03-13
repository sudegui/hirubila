package com.m4f.business.persistence;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author Eduardo Perrino <eduardo.perrino@gmail.com>
 */
public final class PMF {

	private static PersistenceManagerFactory INSTANCE = null;

	public static PersistenceManagerFactory get() {
		if(INSTANCE == null) {
			INSTANCE = JDOHelper
				.getPersistenceManagerFactory("transactions-optional");
		}
		return INSTANCE;
	}

	private PMF() {
		
	}
}
