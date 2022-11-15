import java.net.*;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.io.*;
import java.lang.Math;
public class SenderHandler {
	private static JTextField Packets_Text;
    public static void setInOrderPacketLabel(JTextField textfield) {
        Packets_Text = textfield;
    }
    
    public static String getFileData(String fileName) throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner;
        File theFile = new File(fileName);
        scanner = new Scanner(theFile);
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return stringBuilder.toString();
    }
    
    //Byte array of string created based on index of packet to be sent
    public static byte[] generateDatagramPacketBuffer(String data, int maxSize, int packetIndex) {
        byte[] bufferData = new byte[maxSize + 1]; 
        int endIndex;
        
        if(packetIndex == data.length()/maxSize) {
        	endIndex = data.length();
        }else {
        	endIndex = maxSize * (packetIndex + 1);
        }
        
        for (int index = maxSize * packetIndex; index < endIndex; index++) {
            bufferData[maxSize + index - endIndex] = (byte) data.charAt(index);
        }
        bufferData[maxSize] = (byte) (packetIndex % 2); //Sequence number of 1 or 0 is attached to end, depending on the index of packet
        return bufferData;
    }
    
    public static boolean is_alive(String ip, int sender_port, int receiver_port) throws IOException, UnknownHostException {
    	int timeout = 2000;
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, 1024);
        DatagramSocket ds = new DatagramSocket(null);
        ds.bind(new InetSocketAddress(ip, sender_port));
        ds.setSoTimeout(timeout);
        
        String alive_msg = "alive";
        byte alive_buf[] = alive_msg.getBytes();
        
       try {
    	   ds.send(new DatagramPacket(alive_buf,alive_buf.length,InetAddress.getByName(ip),receiver_port));
    	   ds.receive(dp);
    	   ds.close();
    	   return true;
       }catch (SocketTimeoutException exception) {
    	   ds.close();
    	   return false;
       }
    	
    }
    public static void start_sending(String ip, int sender_port, int receiver_port, int timeout, String fileName, boolean is_reliable) throws UnknownHostException, IOException {
    	int MDS = 125;
    	timeout = timeout/1000 >= 1 ? (int) Math.ceil(timeout/1000) : 1; //Convert timeout from microseconds to milliseconds
    	
        String fileContent = getFileData(fileName);
        byte[] buf = new byte[1024];
        byte[] bufferData;

        //Datagram variables
        DatagramPacket dp = new DatagramPacket(buf, 1024);
        DatagramSocket ds = new DatagramSocket(null);
        ds.bind(new InetSocketAddress(ip, sender_port));
        ds.setSoTimeout(timeout);
        
        String reliable_msg = "unreliable";
        if(is_reliable == true) {
        	reliable_msg = "reliable";
        }
        byte rel_buf[] = reliable_msg.getBytes();
        ds.send(new DatagramPacket(rel_buf,rel_buf.length,InetAddress.getByName(ip),receiver_port));

        ds.receive(dp);
        
        int max_iterations = (int) Math.ceil(fileContent.length()/MDS);
        int packet_count = 0;
        
        for (int i = 0; i < (max_iterations + 1); i++) {
            //Check if we are on our last Datagram to send
            if (i < max_iterations) {
                //Create byte array of data to store in Datagram
                bufferData = generateDatagramPacketBuffer(fileContent, MDS, i);
            } else {
                //Create EOT DATAGRAM
            	System.out.println("EOT datagram is ready");
                bufferData = new byte[]{(byte) '\t', (byte) 4};
            }
            System.out.print("Sending datagram ");
            ds.send(new DatagramPacket(bufferData, bufferData.length, InetAddress.getByName(ip), receiver_port));
            packet_count++;
            Packets_Text.setText(packet_count + "");
            try {
                System.out.println("awaiting ACK... ");
                ds.receive(dp); //Wait for acknowledgement
                int ackReceived = -1;

                //Last number = ack
                for (byte b : dp.getData()) {
                    String letter = String.valueOf((char) b);
                    
                    if (letter.equals("0") || letter.equals("1") || letter.equals("4"))
                        ackReceived = Integer.parseInt(letter);
                }
                
                if (ackReceived != i % 2 && ackReceived != 4) {
                    System.out.println("Invalid ACK, re-sending previous datagram");
                    i--;
                } 
            } catch (SocketTimeoutException exception) { //re-send the datagram if response timeout
                System.out.println("- ACK timed out, re-sending previous datagram");
                i--;
            }
        }
        ds.close();
    }

}
