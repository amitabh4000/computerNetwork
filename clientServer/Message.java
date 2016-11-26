package ComputerNetworks.clientServer;

import java.io.*;


public class Message implements Serializable{
    public int senderName;
    public String messageType;
    public String fileName;
    public String fileType;
    public byte[] buffer;
    public String wayToSend;
    public String receiver;
    public String blocked;
    public String message;
    public Message(int senderName ,String messageType,String fileName,
                   String fileType, byte[] buffer,String wayToSend,
                   String receiver,String blocked, String message){
        this.senderName = senderName;
        this.messageType = messageType;
        this.buffer = buffer;
        this.fileName = fileName;
        this.fileType = fileType;
        this.wayToSend = wayToSend;
        this.receiver = receiver;
        this.blocked = blocked;
        this.message = message;
    }

}
