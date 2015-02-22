
public class HTTPGet 
{
	String host = "";
	String location = "";

	public HTTPGet(String host, String location)
	{
		this.location = location;
		this.host = host;
	}
	
	public String createPacket()
	{
		String getRequest = "GET /" + location + " HTTP/1.1\r\n";
		String hostConnection = "Host: " + host + "\r\n";
		String accept = "Accept: text/html;q=0.8\r\n";
		String ending = "\r\n";
		
		StringBuilder sb = new StringBuilder();
		sb.append(getRequest).append(hostConnection).append(accept).append(ending);
		System.out.println(sb.toString());
		return sb.toString();
	}
	
}
