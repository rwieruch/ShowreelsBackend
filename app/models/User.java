package models;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.db.ebean.Model;

@Entity
@Table(name="showreelsuser")
public class User extends Model {
	
	@Id
    public String email;
	
    @JsonIgnore
    public String password;

    public String name;
    
    @OneToMany(cascade = CascadeType.ALL)
    public List<Showreel> showreels;
    
    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
    
    public static Finder<String,User> find = new Finder<String,User>(
        String.class, User.class
    ); 
    
	public static User create(User user) {
		User existingUser = find.where().eq("email", user.email).findUnique();
		if(existingUser == null) {       		
			user.save();
			return user;
		} else {
			return null;
		}
	}
	
	public static User update(String email, User user) {
		User oldUser = find.ref(email);
		user.showreels = oldUser.showreels;
		user.update();
		return user;
	}
	
	public static User get(String email) {
		return find.ref(email);
	}
	
    public static User authenticate(String email, String password) {
        return find.where().eq("email", email).eq("password", password).findUnique();
    }
}
