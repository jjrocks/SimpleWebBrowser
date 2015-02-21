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
	
	Stack<String> backwardStack;
	Stack<String> forwardStick;
	String hostname = "";

	public void goToLocation(String name)
	{
		
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
	}
	
	private void connectToServer() throws UnknownHostException, IOException
	{
		Socket clientSocket = new Socket(hostname, PORT_NAME);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
		
	}
	
}
