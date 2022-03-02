package szakdolgozat;

import java.util.Date;

public class CalendarItem{
	String Uid;
	Date Dtstart;
	Date Dtend;
	String Location;
	String Summary;
	
	public CalendarItem(String uid, Date dtstart, Date dtend, String location, String summary) {
		Uid = uid;
		Dtstart = dtstart;
		Dtend = dtend;
		Location = location;
		Summary = summary;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public Date getDtstart() {
		return Dtstart;
	}
	public void setDtstart(Date dtstart) {
		Dtstart = dtstart;
	}
	public Date getDtend() {
		return Dtend;
	}
	public void setDtend(Date dtend) {
		Dtend = dtend;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getSummary() {
		return Summary;
	}
	public void setSummary(String summary) {
		Summary = summary;
	}
	
}
