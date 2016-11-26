package ComputerNetworks.clientServer;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;

public class Server {

    int sPort = 3000;
    ServerSocket sSocket;
    Socket connection ;
    String message;
    String MESSAGE;
    //Map<Integer,Socket> socketMap = new HashMap<>();
    Worker[] workers = new Worker[400];

    public class Worker implements Runnable{

        Socket cSocket;
        int clientNumber;
        Worker[] workers;
        ObjectOutputStream out;
        ObjectInputStream in;
        private Worker(Socket cSocket , Worker[] workers,int clientNo){
            this.clientNumber = clientNo;
            this.cSocket = cSocket;
            this.workers = workers;
        }

        public void run() {
            try {
                 out = new ObjectOutputStream(cSocket.getOutputStream());
                 in  = new ObjectInputStream(cSocket.getInputStream());
                try {
                    clientNumber = (Integer) in.readObject();
                }
                catch(ClassNotFoundException e){

                }
                    try {
                        while(true) {

                            Message messageObj = (Message) in.readObject();
                            System.out.println("Message from client no: " + messageObj.senderName);
                            System.out.println("Way to send: " + messageObj.wayToSend);

                            if(messageObj.wayToSend.toLowerCase().contains("unicast")){
                                int toSendId = Utils.getInt(messageObj.receiver);
                                System.out.println("receiver is: "+messageObj.receiver);
                                for(int i=0; i < workers.length ; i++){
                                    if(workers[i].clientNumber != toSendId) continue;
                                    Worker workerToSend = workers[i];
                                    if(workerToSend != null)
                                        workerToSend.out.writeObject(messageObj);
                                }
                            }

                            else if(messageObj.wayToSend.toLowerCase().contains("broadcast")){
                                System.out.println("In broadcast");
                                for(int i = 0; i < workers.length ; i++){
                                    if(workers[i].clientNumber == messageObj.senderName) continue;
                                    Worker workerToSend = workers[i];
                                    if(workerToSend != null)
                                        workerToSend.out.writeObject(messageObj);
                                }

                            }
                            else if(messageObj.wayToSend.toLowerCase().contains("blockcast")){
                                System.out.println("In blockcast");
                                int blockedId = Utils.getInt(messageObj.blocked);
                                for(int i=0; i < workers.length ; i++){
                                    if(workers[i].clientNumber == messageObj.senderName
                                            || workers[i].clientNumber == blockedId) continue;
                                    Worker workerToSend = workers[i];
                                    if(workerToSend != null)
                                        workerToSend.out.writeObject(messageObj);
                                }
                            }
                        }
                    }
                    catch(ClassNotFoundException error){
                        System.out.println("Input format not found..");
                    }

            }
            catch(IOException ioe){

            }
            finally{
                try {
                    System.out.println("Closing input and output streams");
                    in.close();
                    out.close();
                    cSocket.close();
                }
                catch(IOException e){

                }
            }
        }
    }


    void runServer(){
        try {
            // create a new socket
            sSocket = new ServerSocket(sPort,10);
            // Waiting for connection
            System.out.println("Waiting for connection");
            // Accept a connection from client

            int clientNo = 0;
            while (true) {
                connection = sSocket.accept();
                workers[clientNo] = new Worker(connection,workers,clientNo+1);
                new Thread(workers[clientNo++]).start();
                System.out.println("Connection received from,client");
            }
        }
        catch(IOException e){

        }
        finally{
            try {
                sSocket.close();
            }
            catch(IOException e){
                System.out.println("Unable to close socket error "+e);
            }
        }
    }
    /// Send a message to the output stream
    void sendMessage(ObjectOutputStream out,String MESSAGE){
        try{
            out.writeObject(MESSAGE);
            out.flush();
            System.out.println("Sending message "+MESSAGE);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args){

        Server server = new Server();
        server.runServer();
    }

}
