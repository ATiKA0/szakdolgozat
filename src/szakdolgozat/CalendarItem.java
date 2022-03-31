package szakdolgozat;

import java.time.LocalDateTime;

//The items in calendar
public class CalendarItem{
	String uid;
	LocalDateTime dtStart;
	LocalDateTime dtEnd;
	String location;
	String summary;
//Constructor method
	public CalendarItem(String uid, LocalDateTime dtStart, LocalDateTime dtEnd, String location, String summary) {
		this.uid = uid;
		this.dtStart = dtStart;
		this.dtEnd = dtEnd;
		this.location = location;
		this.summary = summary;
	}
//Getters and setters
	public String getuid() {
		return this.uid;
	}
	public void setuid(String uid) {
		this.uid = uid;
	}
	public LocalDateTime getdtStart() {
		return this.dtStart;
	}
	public void setdtStart(LocalDateTime dtStart) {
		this.dtStart = dtStart;
	}
	public LocalDateTime getdtEnd() {
		return this.dtEnd;
	}
	public void setdtEnd(LocalDateTime dtEnd) {
		this.dtEnd = dtEnd;
	}
	public String getlocation() {
		return this.location;
	}
	public void setlocation(String location) {
		this.location = location;
	}
	public String getsummary() {
		return this.summary;
	}
	public void setsummary(String summary) {
		this.summary = summary;
	}
	
}
