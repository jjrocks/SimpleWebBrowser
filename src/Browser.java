import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Stream;


public class Browser 
{
	public static final int PORT_NAME = 80;
	
	Socket clientSocket;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	String currentSite = "";
	
	HashMap<String, CacheSite> linkedCache;
	
	Stack<String> backwardStack;
	Stack<String> forwardStack;
	String hostname = "";
	boolean runValue = false;
	
	public Browser()
	{
		backwardStack = new Stack<String>();
		forwardStack = new Stack<String>();
		linkedCache = new HashMap<String, CacheSite>();
	}

	public void goToLocation(String name)
	{
		
		String modifiedSentence = "";
		try {
			HTTPGet getRequest = new HTTPGet(hostname, name);
			connectToWebsite();
			outToServer.writeBytes(getRequest.createPacket());
			
			Stream<String> lines = inFromServer.lines();
			//modifiedSentence = inFromServer.readLine();
			StringBuilder sb = new StringBuilder();
			for(Iterator<String> iterator = lines.iterator(); iterator.hasNext() ;)
			{
				String line = iterator.next();
				sb.append(line);
				System.out.println(line);
			}
			
			
			if(!linkedCache.containsKey(name))
			{
				CacheSite cacheSite = new CacheSite(name, sb.toString());
				linkedCache.put(name, cacheSite);
			}
			
			clientSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
	
	public void startBrowser(InputStream is)
	{
		System.out.print("Please input host name: ");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(is));
		String userInput = "";
		
		try {
			userInput = inFromUser.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Yo, you somehow messed it up");
			return;
		}
		
		hostname = userInput;
		try {
			connectToServer(); //TODO: Check to see if you need this part.
			runBrowser(is);
		} catch (IOException e) {
			System.out.println("Incorrect host! Exiting!!!");
			e.printStackTrace();
			startBrowser(is);
		}
	}
	
	private void connectToServer() throws UnknownHostException, IOException
	{
		
	}
	
	private void connectToWebsite() throws IOException
	{
		clientSocket = new Socket(hostname, PORT_NAME);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	
	public void runBrowser(InputStream is)
	{
		runValue = true;
		String input = "";
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(is));
		while (runValue)
		{
			System.out.print("What command or location do you want to go/visit: ");
			try {
				input = inFromUser.readLine();
				respondToCommand(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("You done messed up!");
			}
			
		}
	}
	
	/**
	 * This will respond to the following commands
	 * b - Goes to previous page looked at
	 * f - Go forward to the next page looked at
	 * r - Refresh the current page
	 * q - Quit the program
	 * All other values will be treated as a website location
	 * @param command The string you want to put in.
	 */
	public void respondToCommand(String command)
	{
		switch (command) {
		case "b":
			goBackward();
			break;
		case "f":
			goForward();
			break;
		case "r":
			refresh();
			break;
		case "q":
			quit();
			break;
		default:
			if (!currentSite.isEmpty())
			{
				backwardStack.push(currentSite);
				forwardStack.clear();
			}
			
			currentSite = command;
			goToLocation(command);
			break;
		}
		
	}
	
	/**
	 * Pops from the forward stack if there is anything. Otherwise a message will pop up
	 */
	public void goForward()
	{
		if (forwardStack.empty())
		{
			System.out.println("There is nothing on the forward stack");
			return;
		}
		String location = forwardStack.pop();
		backwardStack.push(currentSite);
		currentSite = location;
		goToLocation(location);
	}
	
	/**
	 * Pops form the back stack and pushes into the  back stack if there is anything.
	 */
	public void goBackward()
	{
		if(backwardStack.empty())
		{
			System.out.println("There is nothing on the back stack");
			return;
		}
		String location = backwardStack.pop();
		forwardStack.push(currentSite);
		goToLocation(location);
	}
	
	public void refresh()
	{
		CacheSite site = linkedCache.get(currentSite);		
		HTTPLastModified lastModified = new HTTPLastModified(hostname, site);
		String packet = lastModified.createPacket();
		
		
		try {
			connectToWebsite();
			outToServer.writeBytes(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(!inFromServer.readLine().contains("304"))
			{
				Stream<String> lines = inFromServer.lines();
				for(Iterator<String> iterator = lines.iterator(); iterator.hasNext() ;)
				{
					String line = iterator.next();
					System.out.println(line);
				}
			}
			else
			{
				System.out.println("Nothing new! Printing Cache");
				System.out.println(site.getText());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void quit()
	{
		runValue = false;
		System.out.println("Exiting!");
	}
	
	public static void main(String[] args)
	{
		Browser newBrowser = new Browser();
		newBrowser.startBrowser(System.in);
	}
	
}
