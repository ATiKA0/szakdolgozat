package szakdolgozat;

import java.time.LocalDateTime;

public class CalendarItem{
	String Uid;
	LocalDateTime Dtstart;
	LocalDateTime Dtend;
	String Location;
	String Summary;
	
	public CalendarItem(String uid, LocalDateTime dtstart, LocalDateTime dtend, String location, String summary) {
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
	public LocalDateTime getDtstart() {
		return Dtstart;
	}
	public void setDtstart(LocalDateTime dtstart) {
		Dtstart = dtstart;
	}
	public LocalDateTime getDtend() {
		return Dtend;
	}
	public void setDtend(LocalDateTime dtend) {
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
