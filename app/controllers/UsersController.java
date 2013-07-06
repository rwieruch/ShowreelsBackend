package controllers;

//import models.Session;
import models.Session;
import models.User;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import flexjson.JSONSerializer;

/**
 * Controller for authentication and CRUD operations.
 * 
 * @desc
 * 	{"email":"t@t.t", "name":"test", "password": "test"}
 * 
 * @author Robin Wieruch
 *
 */
public class UsersController extends Controller {
	
	/**
	 * Create a new user by registration. Expect email, name and password.
	 */
    public static Result register() {
    	JsonNode json = request().body().asJson();
    	if(json == null) {
    		return badRequest("Expecting Json data");
    	} else {
    		
    		// Retrieve required values.
    		String email = json.findPath("email").getTextValue();
    		String name = json.findPath("name").getTextValue();
    		String password = json.findPath("password").getTextValue();
    		
    		// Check for required values.
    		if(email == null || name == null || password == null) {
    			return badRequest("Missing parameter!");
    		} else {
    			User user = new User(email, name, password);
    			
    			// Check if user already exists, then save to DB.
    			User createdUser = User.create(user);
    			if(createdUser != null)
    				return ok(Json.toJson(createdUser));
    			else
    				return badRequest("User already exists.");
    		}
    	}
    }
    
	/**
	 * Get myself.
	 */
    public static Result getAuthUser() {
        // Check for header and match with valid user in db.
    	String token = request().getHeader("token");
        System.out.println("###UserController Token: " + token);
        User authUser = Session.getAuthUser(token);
        if(authUser == null) {
            return unauthorized("Unauthorized");
        } else {   
			//return ok(new JSONSerializer().include("notes").exclude("password", "class", "notes.class").serialize(authUser));
            return ok(new JSONSerializer().exclude("password", "class").serialize(authUser));
		}
    	
    }
}
