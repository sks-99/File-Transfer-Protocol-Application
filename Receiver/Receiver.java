import java.util.ArrayList;
import java.net.*;
import java.io.*;

public class Receiver {
	public static String ip;
    public static int receiver_port;
    public static int sender_port;
    public static String txtfile;
    static DatagramSocket ds;
    public static boolean reliable = false;
    public static boolean is_finished = false;
    
    public static String get_data(DatagramPacket dp) {
    	StringBuilder data = new StringBuilder();

        for (int i = 0; i < dp.getLength(); i++) {
            if (dp.getData()[i] >= 9) {
                data.append((char) dp.getData()[i]);
            }
        }
        
        return data.toString();
    }
    
    
	public static void main(String[] args) throws SocketException, IOException {
		ip = args[0];
		
		receiver_port = Integer.parseInt(args[1]);
		sender_port = Integer.parseInt(args[2]);
		txtfile = args[3];

        ds = new DatagramSocket(null);
        ds.bind(new InetSocketAddress(ip, receiver_port));
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, 1024);
        
        // Final data to write to file after reading all Datagrams
        StringBuilder finalString = new StringBuilder();
        int packetCount = 0;
        int sequenceNumber;
        
        while(true) {
        	ds.receive(dp);
            String data = get_data(dp);
            if(data.equals("alive")) {
            	String ack_alive = "isalive";
            	ds.send(new DatagramPacket(ack_alive.getBytes(), ack_alive.getBytes().length, InetAddress.getByName(ip), sender_port));
            }else {
            	if(data.equals("reliable")) {
            		reliable = true;
            	}
            	String ack = "acknowledged";
            	ds.send(new DatagramPacket(ack.getBytes(), ack.getBytes().length, InetAddress.getByName(ip), sender_port));
            	break;
            }
        }
        
        long start_time = 0;
        while (true) {
            try {
                System.out.println("Waiting for data");
                //Receive datagram (wait)
                ds.receive(dp);
                if(packetCount == 0) {
                    start_time = System.currentTimeMillis();
                }
                packetCount++;

                if (reliable || packetCount % 10 != 0) {
                    // Build the data String based on the Datagram data excluding last value (our sequence number)
                    StringBuilder data = new StringBuilder();
                    for (int i = 0; i < dp.getLength(); i++) {
                        if (dp.getData()[i] >= 9) {
                            data.append((char) dp.getData()[i]);
                        }
                    }

                    //Cumulative string to add at the end
                    finalString.append(data.toString());

                    // SEQUENCE NUMBER IS LAST INDEX
                    sequenceNumber = dp.getData()[dp.getLength() - 1];

                    //EOT DATAGRAM
                    if (data.toString().contains("\t") && sequenceNumber == 4) {           
                    	File myObj = new File(txtfile);
                        if (myObj.createNewFile()) {
                          System.out.println("File created: " + myObj.getName());
                        } else {
                          System.out.println("File already exists.");
                        }
                        FileWriter myWriter = new FileWriter(txtfile);
                        myWriter.append(finalString);
                        myWriter.close();
                    	
                        finalString = new StringBuilder();
                        is_finished = true;
                    } 

                    // Send back an acknowledgement 
                    String ack = "ACK " + sequenceNumber;
                    System.out.println("Sending ACK " + sequenceNumber);
                    ds.send(new DatagramPacket(ack.getBytes(), ack.getBytes().length, InetAddress.getByName(ip), sender_port));
                    
                    if(is_finished == true) {
                    	System.out.println("Total Transmission Time = " + (double)(System.currentTimeMillis() - start_time)/1000 + " seconds");
                        ds.close();
                    	break;
                    }
                }
            } catch (IOException exception) {
                break;
            }

        }
    }        
}
