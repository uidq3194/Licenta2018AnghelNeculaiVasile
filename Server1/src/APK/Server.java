package APK;

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.util.*;


public class Server {
	
	protected int Port;
	protected ServerSocket serverSocket =  null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	public static Map<Client, Client> urgentePreluate = new HashMap<Client, Client>();
	public static List<Client> clientsList = new ArrayList<>();
	
	public Server(int port) {
		this.Port = port;
	}
    public Server() {

    }
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
	private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.Port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.Port + ": ", e);
        }
    }
	public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
	public void run(){
	        openServerSocket();
	        while(!isStopped()) {
				Socket clientSocket = null;
				try {
					clientSocket = this.serverSocket.accept();
					System.out.println("cleintSocket : " + clientSocket);
					Client client = new Client(clientSocket, "Client connected ");
					new Thread(client).start();
					System.out.println("Server Started.");
                    System.out.println(clientsList.size());
				} catch (IOException e) {
					if (isStopped()) {
						System.out.println("Server Stopped.");
						return;
					}
					throw new RuntimeException("Error accepting client connection", e);
				}
	        }
	        System.out.println("Server Stopped.") ;
	}

	protected void broadcastSOS(Client cli) throws IOException {
	    for(Client auxCli:clientsList){
	        if(auxCli.clientType.equals("Ambulanta"))
            {
                System.out.println("Am gasit o ambulanta");
                auxCli.oop.writeObject("SOS");
            }
        }

    }
}
