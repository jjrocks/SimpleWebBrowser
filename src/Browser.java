import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;


public class Browser 
{
	public static final int PORT_NAME = 80;
	
	Socket clientSocket;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	
	Stack<String> backwardStack;
	Stack<String> forwardStack;
	String hostname = "";
	boolean runValue = false;

	public void goToLocation(String name)
	{
		
		String modifiedSentence = "";
		try {
			outToServer.writeBytes(name + '\n');
			modifiedSentence = inFromServer.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(modifiedSentence);
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
			connectToServer();
			runBrowser(is);
		} catch (IOException e) {
			System.out.println("Incorrect host! Exiting!!!");
			e.printStackTrace();
		}
	}
	
	private void connectToServer() throws UnknownHostException, IOException
	{
		clientSocket = new Socket(hostname, PORT_NAME);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		backwardStack = new Stack<String>();
		forwardStack = new Stack<String>();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("You done messed up!");
			}
			respondToCommand(input);
			
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
			goToLocation(command);
			break;
		}
		
	}
	
	/**
	 * Pops from the forward stack if there is anything. Otherwise a message will pop up
	 */
	public void goForward()
	{
		if (backwardStack.empty())
		{
			System.out.println("There is nothing on the forward stack");
		}
		String location = forwardStack.pop();
		goToLocation(location);
	}
	
	/**
	 * Pops form the back stack and pushes into the  back stack if there is anything.
	 */
	public void goBackward()
	{
		
	}
	
	public void refresh()
	{
		
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
