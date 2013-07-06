package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Contributor extends Model {
	
	@Id
	public Long id;
	
	public String name;
	public String email;
	
	//@ManyToOne
    //public Showreel showreel;
	
	public Contributor(String name, String email) {
		this.name = name;
		this.email = email;
	}

}
