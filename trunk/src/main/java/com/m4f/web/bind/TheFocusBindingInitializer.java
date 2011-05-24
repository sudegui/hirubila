package com.m4f.web.bind;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.m4f.business.domain.Inbox;
import com.m4f.utils.StackTraceUtil;

public class TheFocusBindingInitializer extends ConfigurableWebBindingInitializer  {
	private static final Logger LOGGER = Logger.getLogger(TheFocusBindingInitializer.class.getName());
	
	public void initBinder(WebDataBinder binder, WebRequest request) {        
		/*Necessary to run into GAE.*/
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
        binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));
        binder.registerCustomEditor(Float.class, null, new CustomNumberEditor(Float.class, null, true));
        binder.registerCustomEditor(Set.class, "tags", new TagCollectionEditor(Set.class, true));
        binder.registerCustomEditor(List.class, new CustomCollectionEditor(List.class, true));
        binder.registerCustomEditor(Text.class, new CustomTextEditor());
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(GeoPt.class, new CustomGeoPointsEditor());
        
        //binder.registerCustomEditor(Link.class, new CustomLinkEditor());
        /*To validate and more actions. */
        super.initBinder(binder, request);
	}
	
	/**
	 * Example of custom binder to com.google.appengine.api.datastore.Link.class.
	 * @author eduardo.perrino@gmail.com
	 *
	 */
	private class CustomLinkEditor extends PropertyEditorSupport {
		
		public String getAsText() {
			String value = getValue() != null ? ((Link)getValue()).getValue() : "";
			return value;
		}
		
		public void	setAsText(String text) {
			setValue(new Link(text));
		}
	}
	
	/**
	 * Example of custom binder to com.google.appengine.api.datastore.GeoPt.class
	 *
	 */
	private class CustomGeoPointsEditor extends PropertyEditorSupport {
		
		@Override
		public String getAsText() {
			StringBuffer sb = new StringBuffer();
			if(getValue() != null) {
				GeoPt geoPt = (GeoPt) getValue();
				if(geoPt != null) {
					sb.append(geoPt.getLatitude()).append(",").append(geoPt.getLongitude());
				}
			}
			return sb.toString();
		}
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if(text != null && !("").equals(text)) {
				String[] coords = text.split(",");
				if(coords != null && coords.length == 2) {
					try {
						float latitude = Float.parseFloat(coords[0]);
						float longitude = Float.parseFloat(coords[1]);
						GeoPt geoPt = new GeoPt(latitude, longitude);
						setValue(geoPt);
					} catch(Exception e) {
						LOGGER.severe(StackTraceUtil.getStackTrace(e));
					}
				}
			}
		}
	}
	
	private class TagCollectionEditor extends CustomCollectionEditor {

		public TagCollectionEditor(Class collectionType) {
			super(collectionType);
			// TODO Auto-generated constructor stub
		}

		public TagCollectionEditor(Class collectionType, boolean b) {
			super(collectionType, b);
			// TODO Auto-generated constructor stub
		}
		
		@SuppressWarnings("unchecked")
		public String getAsText() {
			Set<Category> tags = getValue() != null ? (Set<Category>)getValue() : new HashSet<Category>();
			String value = "";
			for(Category tag : tags) {
				if("".equals(value)) {
					value += tag.getCategory().trim().toLowerCase();
				} else {
					value += "," + tag.getCategory().trim().toLowerCase();
				}
			}
			return value;
		}
		
		public void	setAsText(String text) {
			Set<Category> tags = new HashSet<Category>();
			String[] inputTags = text.split("[,]");
			for(String tag : inputTags) {
				tags.add(new Category(tag.trim().toLowerCase()));
			}
			setValue(tags);
			/*if(getValue() == null) {
				setValue(tags);
			} else {
				((List<Category>)getValue()).clear();
				((List<Category>)getValue()).addAll(tags);
			}*/	
		}
		
	}
	
	private class CustomTextEditor extends PropertyEditorSupport {
		
		public String getAsText() {
			String value = getValue() != null ? ((Text)getValue()).getValue() : "";
			return value;
		}
		
		public void	setAsText(String text) {
			System.out.println();
			setValue(new Text(text));
		}
	}
	
	private class InboxTypeEnumEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			if(getValue() == null) {
				return "null";
			}
			Inbox.TYPE type = (Inbox.TYPE) getValue(); 
			return type.toString();
		}
		
		@Override
		public void setValue(Object value) {
			if(getValue() == null || !(value instanceof GeoPt)) {
				setValue(null);
			}
			Inbox.TYPE type = (Inbox.TYPE) getValue();
			super.setValue(type);
		}
	}
	
	private class LinkValidator implements Validator {

		@Override
		public boolean supports(Class<?> arg0) {
			return Link.class.equals(arg0);
		}

		@Override
		public void validate(Object arg0, Errors e) {
			// TODO Auto-generated method stub
			ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
		}
		
	}
		
}