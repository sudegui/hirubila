package com.m4f.business.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Inbox extends BaseEntity {
	public enum TYPE {
		PROBLEM("Problem"), SUGGESTION("Suggestion");
		
		private final String displayName;
		
		private TYPE(String dName) {
			this.displayName = dName;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
		
		public String getValue() {
			return this.toString();
		}
	
	};
	
	public enum USER {
		INTERNAL("Internal user"), EXTERNAL("External user");
		
		private final String displayName;
		
		private USER(String dName) {
			this.displayName = dName;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
		
		public String getValue() {
			return this.toString();
		}
	
	};
	
	public enum ORIGIN {
		REQUEST("Problem"), RESPONSE("Suggestion");
		
		private final String displayName;
		
		private ORIGIN(String dName) {
			this.displayName = dName;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
		
		public String getValue() {
			return this.toString();
		}
	
	};
		
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String remoteHost;
	
	@Persistent
	private String content;
	
	@Persistent
	@NotNull(message="{validation.error.null}")
	private TYPE type;
	
	@Persistent
	@NotNull(message="{validation.error.null}")
	private USER user;
	
	@Persistent
	@NotNull(message="{validation.error.null}")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message="{validation.error.regexp.field.mail}")
	private String from;
	
	@Persistent
	private Date created;
	
	@Persistent
	private Boolean readed = Boolean.FALSE;

	@Persistent
	private Boolean active = Boolean.TRUE;
	
	@Persistent
	@NotNull(message="{validation.error.null}")
	private ORIGIN origin = ORIGIN.REQUEST;
	
	@Persistent
	private Long relatedId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Boolean getReaded() {
		return readed;
	}

	public void setReaded(Boolean readed) {
		this.readed = readed;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public USER getUser() {
		return user;
	}

	public void setUser(USER user) {
		this.user = user;
	}

	public ORIGIN getOrigin() {
		return origin;
	}

	public void setOrigin(ORIGIN origin) {
		this.origin = origin;
	}

	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}	
}
