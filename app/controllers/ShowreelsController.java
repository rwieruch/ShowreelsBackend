package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Contributor;
import models.Session;
import models.Showreel;
import models.User;

import org.codehaus.jackson.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;
import flexjson.JSONSerializer;

/**
 * Controller for showreels and CRUD operations.
 * 
 * @desc
 *  {"title":"Test","text":"test","viewable":0,"editable":true,"x":1.0,"y":1.0,"z":1.0}
 * 
 * @author Robin Wieruch
 *
 */
public class ShowreelsController extends Controller {
	
	// Index
	// Returns all nodes. Later by owner, public or owner list.
	
	public static Result indexJson() {
		
		// Check for header and match with valid user in db.
		String token = request().getHeader("token");
		Session session = Session.authenticate(token); 
        if(session == null) {
        	return ok(new JSONSerializer().include("hashtags", "contributors").exclude("class", "user.class", "user.password", "contributors.class", "contributors.id").serialize(Showreel.allPublic()));
        	//return unauthorized("Unauthorized");
        } else { 	
        	User authUser = Session.getAuthUser(token);
        	return ok(new JSONSerializer().include("hashtags", "contributors").exclude("class", "user.class", "user.password", "contributors.class", "contributors.id").serialize(Showreel.all(authUser)));
        }
	}
    
	// Create
	//{"title":"Title 3","text":"showreel 3","contributors": [{"name":"hans","email":"hans@gmx.de"},{"name":"peter","email":"peter@gmx.de"}],"hashtags": ["asdasd","blaaa"], "img_url":"http://img","showreel_url":"http://showreel","viewable":"private"}
    public static Result createJson() {
    	JsonNode json = request().body().asJson();
    	if(json == null) {
    		return badRequest("Expecting Json data");
    	} else {
    		
    		// Check for header and match with valid user in db.
    		String token = request().getHeader("token");
    		Session session = Session.authenticate(token);    		
	        if(session == null) {
	        	return unauthorized("Unauthorized");
	        } else { 	
	        	User authUser = Session.getAuthUser(token);
	        	
	        	String title = json.findPath("title").getTextValue();
	    		String text = json.findPath("text").getTextValue();
                String viewable = json.findPath("viewable").getTextValue();
                
                List<Contributor> contributors = new ArrayList<Contributor>();
                JsonNode conNode = json.path("contributors");
    			Iterator<JsonNode> ite1 = conNode.getElements();
    			while (ite1.hasNext()) {
    				JsonNode temp = ite1.next();
    				System.out.println(temp.findPath("email").getTextValue());
    				System.out.println(temp.findPath("name").getTextValue());
    				
    				String name = temp.findPath("name").getTextValue();
    				String email = temp.findPath("email").getTextValue();
    				Contributor contributor = new Contributor(name, email);
    				contributors.add(contributor);
    			}
                
                List<String> hashtags = new ArrayList<String>();
                JsonNode msgNode = json.path("hashtags");
    			Iterator<JsonNode> ite2 = msgNode.getElements();
    			while (ite2.hasNext()) {
    				JsonNode temp = ite2.next();
    				hashtags.add(temp.getTextValue());
    			}
                
                String img_url = json.findPath("img_url").getTextValue();
                String showreel_url = json.findPath("showreel_url").getTextValue();
	    		if(title == null || text == null | viewable == null | img_url == null | showreel_url == null) {
	    			return badRequest("Missing parameter");
	    		} else {
	    			Showreel showreel = new Showreel(authUser, title, text, contributors, hashtags, img_url, showreel_url, viewable);
	    			Showreel.create(showreel);
	    			
	    			JSONSerializer showreelSerializer = new JSONSerializer().include("hashtags", "contributors").exclude("class", "user.class", "user.password", "contributors.class", "contributors.id");
	    			
	    			return ok(showreelSerializer.serialize(showreel));
	    		}
	        }
    	}
    }

    // Get

    public static Result getJson(Long id) {
        // Check for header and match with valid user in db.
    	String token = request().getHeader("token");
		Session session = Session.authenticate(token); 
        if(session == null) {
            return unauthorized("Unauthorized");
        } else { 
        	
            Showreel showreel = Showreel.get(id);
            
            System.out.println("GET2");
            System.out.println("NOTE: ID: " + showreel.id);
            System.out.println(" text: " + showreel.text);
            System.out.println("showreel.user.email" + showreel.user.email);
            
            JSONSerializer showreelSerializer = new JSONSerializer().include(
                            "id",
                            "title",
                            "text",
                            "contributors",
                            "hashtags",
                            "viewable",
                            "img_url",
                            "showreel_url",
                            "user.email").exclude("*");
           
            return ok(showreelSerializer.serialize(showreel));
        }
    }

    // Update

    public static Result updateJson(Long id) {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            // Check for header and match with valid user in db.
        	String token = request().getHeader("token");
    		Session session = Session.authenticate(token); 
            if(session == null) {
                return unauthorized("Unauthorized");
            } else {
            	String title = json.findPath("title").getTextValue();
                String text = json.findPath("text").getTextValue();
                String viewable = json.findPath("viewable").getTextValue();
                
                List<Contributor> contributors = new ArrayList<Contributor>();
                JsonNode conNode = json.path("contributors");
    			Iterator<JsonNode> ite1 = conNode.getElements();
    			while (ite1.hasNext()) {
    				JsonNode temp = ite1.next();
    				System.out.println(temp.findPath("email").getTextValue());
    				System.out.println(temp.findPath("name").getTextValue());
    				
    				String name = temp.findPath("name").getTextValue();
    				String email = temp.findPath("email").getTextValue();
    				Contributor contributor = new Contributor(name, email);
    				contributors.add(contributor);
    			}
                
                List<String> hashtags = new ArrayList<String>();      
                JsonNode msgNode = json.path("hashtags");
    			Iterator<JsonNode> ite2 = msgNode.getElements();
    			while (ite2.hasNext()) {
    				JsonNode temp = ite2.next();
    				hashtags.add(temp.getTextValue());
    			}
    			
                String img_url = json.findPath("img_url").getTextValue();
                String showreel_url = json.findPath("showreel_url").getTextValue();
                if(title == null || text == null | viewable == null | img_url == null | showreel_url == null) {
                    return badRequest("Missing parameter!");
                } else {
                    Showreel showreel = Showreel.get(id);
                    Showreel updatedShowreel = Showreel.update(showreel, title, text, contributors, hashtags, img_url, showreel_url, viewable);
                    
                    JSONSerializer showreelSerializer = new JSONSerializer().include("hashtags", "contributors").exclude("class", "user.class", "user.password", "contributors.class", "contributors.id");
                    return ok(showreelSerializer.serialize(updatedShowreel));
                }
            }
        }
    }
    
    // Destroy
  
    public static Result destroyJson(Long id) {
    	
    	// Check for header and match with valid user in db.
    	String token = request().getHeader("token");
		Session session = Session.authenticate(token); 
        if(session == null) {
        	return unauthorized("Unauthorized");
        } else { 
        	//Showreel showreel = Showreel.get(id);
        	Showreel.delete(id);
        	return ok();
    	}
    }
    
	public static Result destroyAllJson() {
    	// Check for header and match with valid user in db.
    	String token = request().getHeader("token");
		Session session = Session.authenticate(token); 
        if(session == null)
        	return unauthorized("Unauthorized");
        
        User authUser = Session.getAuthUser(token);
		Showreel.deleteAll(authUser);
		return ok();
	}
}
