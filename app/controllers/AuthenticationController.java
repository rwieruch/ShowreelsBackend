package controllers;

import models.Session;
import models.User;

import org.codehaus.jackson.JsonNode;

import flexjson.JSONSerializer;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controller for authentication and CRUD operations.
 * 
 * @desc
 * 	{"email":"t@t.t", "password": "test"}
 * 
 * @author Robin Wieruch
 * 
 */
public class AuthenticationController extends Controller {
    
	/**
	 * Login a user. Expect email and password.
	 */
    public static Result login() {
    	JsonNode json = request().body().asJson();
    	if(json == null) {
    		return badRequest("Expecting Json data");
    	} else {
    		String email = json.findPath("email").getTextValue();
    		String password = json.findPath("password").getTextValue();
    		if(email == null || password == null) {
    			return badRequest("Missing parameter!");
    		} else {
    			User authUser = User.authenticate(email, password);
    			if(authUser != null) {
    				
    				// Create session.
    				Session session = new Session(email, password);
    				Session.create(session);
    				
                    //return ok(new JSONSerializer().include("user.notes").exclude("id", "timestamp", "user.password", "class", "user.notes.class", "user.class").serialize(session));
    				return ok(new JSONSerializer().exclude("id", "timestamp", "class").serialize(session));
    			} else {
    				return badRequest("Wrong email/password.");
    			}
    		}
    	}
    }
    
    /**
	 * Logout a user. Expect email and password.
	 */
    public static Result logout() {  	
    	// Check for header and match with valid user in db.
		String token = request().getHeader("token");
		Session session = Session.authenticate(token); 
        if(session == null) {
        	return unauthorized("Unauthorized");
        } else { 	
        	Session.delete(session.id);
        	return ok();
        }    	
    }
	
}
