package ComputerNetworks.clientServer.Client4;
import ComputerNetworks.clientServer.*;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;

public class Client {
    final int name = 4;
    Socket cSocket;
    ObjectOutputStream out;
    ObjectInputStream in;

    /////////////////////////////////////////////////
    public class receivingThread implements Runnable {
        Socket cSocket;

        public receivingThread(Socket cSocket) {
            this.cSocket = cSocket;
        }

        public void run() {
            try {
                in = new ObjectInputStream(cSocket.getInputStream());
                while (true) {
                    try {
                        Message receivedMessage = (Message) in.readObject();
                        if(receivedMessage.messageType.equalsIgnoreCase("message")) {
                            System.out.println("@"+receivedMessage.senderName+":"+receivedMessage.message);
                        }
                        else{
                            String fileName = receivedMessage.fileName;
                            String fileType = receivedMessage.fileType;
                            String filePath = Utils.BASE_PATH +name+"/"+fileName+"."+fileType;
                            String write_path = new File("").getAbsolutePath()+"/"+fileName+name+"."+fileType;
                            System.out.println(filePath);
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                            bos.write(receivedMessage.buffer);
                            bos.flush();
                        }

                    } catch (ClassNotFoundException error) {
                        System.out.println("Input format not found..");
                    }
                }
            } catch (IOException e) {

            }
        }
    }


    /////////////////////////////////////////////
    void runClient(){
        try {
            // create a new socket to request the server
            cSocket = new Socket("Localhost", 3000);

            new Thread(new receivingThread(cSocket)).start();
            try {
                out = new ObjectOutputStream(cSocket.getOutputStream());
                out.writeObject(name);
                while (true) {
                    System.out.println("This is client4 ,Enter a command enter STOP to exit");
                    Scanner sc = new Scanner(System.in);
                    String command = sc.nextLine();
                    if (command.equalsIgnoreCase("stop")) break;
                    String wayToSend = Utils.splitIt(command)[0];
                    String receiver = null;
                    String blocked = null;
                    if(wayToSend.toLowerCase().equals("unicast")){
                        receiver = Utils.splitIt(command)[3];
                        System.out.println("In unicast, receiver is: "+receiver);
                    }
                    else if(wayToSend.toLowerCase().equals("blockcast")){
                        blocked = Utils.splitIt(command)[3];
                        System.out.println("In blockcast, blocked is: "+blocked);
                    }


                    if(command.split(" ")[1].equalsIgnoreCase("file")) {
                        String fileNameRaw = command.split(" ")[2];
                        System.out.println(fileNameRaw);
                        String fileFullName = fileNameRaw.substring(fileNameRaw.indexOf('/')+1,fileNameRaw.length() - 1);
                        String fileName = fileFullName.substring(
                                0,fileFullName.indexOf('.'));
                        String fileType = fileFullName.substring(
                                fileFullName.indexOf('.')+1);

                        System.out.println(" Name is: "+fileName+"fileType is: "+fileType);
                        File file = new File(Utils.BASE_PATH + name +"/"+fileName+"."+fileType);
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                        byte[] buffer = new byte[1024];
                        int count = 0;

                        while ((count = bis.read(buffer)) > 0) {
                            out.writeObject(new Message(name,"file",fileName,fileType,buffer,wayToSend,receiver,blocked,null));
                            out.flush();
                        }
                    }
                    else{
                        String toSendMessage = command;
                        String[] message = Utils.splitIt(command);
                        out.writeObject(new Message(name,"message", null,null,null,wayToSend,receiver,blocked,message[2]));
                    }
                    out.flush();
                }
            }
            catch(IOException ioe){
                ioe.printStackTrace();
            }
            finally{
                try {
                    out.close();
                    cSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch(IOException e){

        }
    }
    /// Send a message to the output stream
    void sendMessage(String MESSAGE){
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
        Client client = new Client();
        client.runClient();

    }

}
