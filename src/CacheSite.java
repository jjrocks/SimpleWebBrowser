import java.sql.Time;
import java.util.Calendar;
import java.util.Date;


public class CacheSite 
{
	String location = "";
	String text = "";
	Date dateReceived;
	
	public CacheSite(String location, String text)
	{
		this.location = location;
		this.text = text;
		dateReceived = Calendar.getInstance().getTime();
	}
	
	@Override
	public boolean equals(Object object)
	{
		if(object.getClass() != String.class)
		{
			return false;
		}
		return location.equals(object);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	
	
}
