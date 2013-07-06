package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Showreel extends Model implements ViewableType {
	
	@Id
	public Long id;
	  
	@Required
	public String title;
	public String text;
	public List<String> hashtags = new ArrayList<String>();
	public String img_url;
	public String showreel_url;
	public String viewable;
	
	@ManyToOne
    public User user;
	
	@OneToMany(cascade = CascadeType.ALL)
    public List<Contributor> contributors;
	
	public Showreel(User user, String title, String text, List<Contributor> contributors, List<String> hashtags, String img_url, String showreel_url, String viewable) {
		this.user = user;
		this.title = title;
		this.text = text;
		this.contributors = contributors;
		this.hashtags = hashtags;
		this.img_url = img_url;
		this.showreel_url = showreel_url;
		this.viewable = viewable;
	}
	
	// Helper for database operations.
	public static Finder<Long,Showreel> find = new Finder(Long.class, Showreel.class);
	  
	public static List<Showreel> all(User authUser) {
		//return find.all();
		List<Showreel> publiclist = find.where().eq("viewable", PUBLIC).findList();
		List<Showreel> privatelist = find.where().eq("viewable", PRIVATE).eq("user.email", authUser.email).findList();
		
		List<Showreel> finallist = new ArrayList<Showreel>();
		finallist.addAll(publiclist);
		finallist.addAll(privatelist);
		
		return finallist;
	}
	
	public static List<Showreel> allPublic() {
		return find.where().eq("viewable", PUBLIC).findList();
	}
	  
	public static void create(Showreel showreel) {
		showreel.save();
	}
	  
	public static void delete(Long id) {
		find.ref(id).delete();
	}
	
	public static void deleteAll(User authUser) {
		List<Showreel> list = all(authUser);
		for (Showreel item: list) {
			Showreel.delete(item.id);
		}
	}

	public static Showreel update(Showreel showreel, String title, String text, List<Contributor> contributors, List<String> hashtags, String img_url, String showreel_url, String viewable) {
		showreel.title = title;
		showreel.text = text;
		showreel.contributors = contributors;
		showreel.hashtags = hashtags;
		showreel.img_url = img_url;
		showreel.showreel_url = showreel_url;
		showreel.viewable = viewable;
		showreel.update();
		return showreel;
	}
	
	public static Showreel get(Long id) {
		return find.ref(id);
	}

}
