package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Session extends Model {
	
	@Id
    public Long id;
	
	public String token;
	public Date timestamp;
	
	public String email;
	
	//@ManyToOne
	//public User user;
	
	public Session(String email, String password) {
		//this.user = user;
		this.email = email;
		
	    Date date = new Date();
	    this.timestamp = date;
		
		String orig = email+":"+password+":"+date;
        byte[] encoded = Base64.encodeBase64(orig.getBytes());     
        this.token = new String(encoded);
        
        System.out.println("Base64 Encoded : " + token);   
        System.out.println("Timestamp : " + timestamp);   
	}
	
	public static Finder<Long,Session> find = new Finder<Long,Session>(
        Long.class, Session.class
	); 
	
	public static void create(Session session) {
		//Session existingSession = find.where().eq("email", session.user.email).findUnique();
		// Kill existing but expired (by timestamp) sessions
	    
		session.save();
	}
	
	public static void delete(Long id) {
		find.ref(id).delete();
	}
	
	public static Session authenticate(String token) {
		Session session = find.where().eq("token", token).findUnique();
		
		// If there is no session return 401.
		if(session == null) {
			return null;
		}
		
		
		// Compare current datetime with session dateime + 30 minutes.
		DateTime timestampTime = new DateTime(session.timestamp);
		DateTime m30FromTT = timestampTime.plusMinutes(30);	
		DateTime currentDate = new DateTime();
		
		int value = m30FromTT.compareTo(currentDate);		
		if(value<0) {
			// Timestamp out dated.
			session.delete();		
			return null;
		} else {	
			// Set timestamp of session to current time.
			session.timestamp = currentDate.toDate();
			session.update();
			
			return session;
		}
	}
	
	public static User getAuthUser(String token) {
		//Session session = find.where().eq("token", token).findUnique();
		//return session.user;
		
		Session session = find.where().eq("token", token).findUnique();
		return User.get(session.email);
	}

}
