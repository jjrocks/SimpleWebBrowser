import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class HTTPLastModified 
{
	String host = "";
	CacheSite currentSite;

	public HTTPLastModified(String host, CacheSite currentSite)
	{
		this.host = host;
		this.currentSite = currentSite;
	}
	
	public String createPacket()
	{
		DateFormat dateFormatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
		
		String getRequest = "GET /" + currentSite.getLocation() + " HTTP/1.1\r\n";
		String hostConnection = "Host: " + host + "\r\n";
		String accept = "Accept: text/html;q=0.8\r\n";
		String modifiedSince = "If-Modified-Since: " + dateFormatter.format(currentSite.dateReceived) + "\r\n";
		String ending = "\r\n";
		
		StringBuilder sb = new StringBuilder();
		sb.append(getRequest).append(hostConnection).append(accept).append(modifiedSince).append(ending);
		System.out.println(sb.toString());
		return sb.toString();
	}
}
