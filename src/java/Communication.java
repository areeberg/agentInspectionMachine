import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Communication extends Thread {
	
	// MSG;Board;#\0
	// MSG;Check;Line;Type;Angle;Distance;#\0
	private boolean k;
	private Double check;
	private double board;
	private double line;
	private double type;
	private double angle;
	private double distance;
	private double lineCheck;
	
	public void run() {
		
		//UDP SERVER
		// cria socket
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(9876);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			byte[] receiveData = new byte[1024]; // byte de recebimento
			byte[] sendData  = new byte[1024]; // byte de envio

			while(true) { 

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); // montando pacote de recebimento 

				System.out.println ("Waiting for datagram packet...");

				serverSocket.receive(receivePacket); // socket espera o recebimento do pacote

				String sentence = new String(receivePacket.getData()); // armazenamento do conetúdo do pacote da mensagem recebida

				InetAddress IPAddress = receivePacket.getAddress(); // obtendo o endereço IP do pacote da mensagem recebida

				int port = receivePacket.getPort(); // obtendo endereço da porta do pacote da mensagem

				System.out.println ("From: " + IPAddress + ":" + port);
				System.out.println ("Message: " + sentence);
				
				//serverSocket.receive(receivePacket); // socket espera o recebimento do pacote
				String dataIn = new String(receivePacket.getData(), 0, receivePacket.getLength(), "ISO-8859-1"); // armazenamento do conet˙do do pacote da mensagem recebida
				String[] tokens = dataIn.split(";");
				// Handling message receive
				// Message parameters
				
				if(tokens.length == 7) {
					this.setLine(Double.parseDouble(tokens[1]));
					this.setCheck(Double.parseDouble(tokens[2]));
					this.setType(Double.parseDouble(tokens[3]));
					this.setAngle(Double.parseDouble(tokens[4]));
					this.setDistance(Double.parseDouble(tokens[5]));
					//System.out.println("<- Receive MSG: Line = " + Integer.parseInt(tokens[1]) + " | Type = " + double.parsedouble(tokens[2]) + " | Angle = " + double.parsedouble(tokens[3]) + " | Distance = " + double.parsedouble(tokens[4]));
				}else{
					//System.out.println("#ERROR1: Dont receive MSG: " + dataIn);
				}
				// Message Board
				if(tokens.length == 3) {
					this.setBoard(Double.parseDouble(tokens[1]));
				}else{
					//System.out.println("#ERROR2: Dont receive MSG: " + dataIn);
				}

				String capitalizedSentence = sentence.toUpperCase(); // converte o conteúdo da mensagem para letras maiúsculas 

				sendData = capitalizedSentence.getBytes(); // armazena a mensagem do usuário no byte de envio

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); // montando pacote de envio

				serverSocket.send(sendPacket); // socket envia o pacote da mensagem

				
				
			} 

		} catch (SocketException ex) {
			System.out.println("UDP Port 9876 is occupied.");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//

		//System.out.println(" @ Communication: Start thread @ ");

		String lastMessage = "";

		try {
			// nome do servidor
			String serverHostname = new String ("127.0.0.1");

			// cria socket
			DatagramSocket clientSocket = new DatagramSocket(); 

			// obtendo endereÁo IP
			InetAddress IPAddress = InetAddress.getByName(serverHostname); 

			byte[] sendData = new byte[1024]; // byte de envio
			byte[] receiveData = new byte[1024]; // byte de recebimento
			

			/* --- Rotina de recebimento e envio da mensagem --- */
			while(clientSocket.isBound()){
				// Receive
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); // montando pacote de recebimento
				//System.out.println (" @ Communication: Wait for message GPS @ ");
				clientSocket.setSoTimeout(10000); // timeout com o tempo de espera por uma resposta

//				try {
//					clientSocket.receive(receivePacket); // socket espera o recebimento do pacote
//					String dataIn = new String(receivePacket.getData(), 0, receivePacket.getLength(), "ISO-8859-1"); // armazenamento do conet˙do do pacote da mensagem recebida
//					String[] tokens = dataIn.split(";");
//					// Handling message receive
//					// Message parameters
//					
//					System.out.println("Lenght"+ tokens.length);
//					
//					if(tokens.length == 7) {
//						this.setLine(Integer.parseInt(tokens[1]));
//						this.setCheck(Double.parseDouble(tokens[2]));
//						this.setType(Double.parseDouble(tokens[3]));
//						this.setAngle(Double.parseDouble(tokens[4]));
//						this.setDistance(Double.parseDouble(tokens[5]));
//						//System.out.println("<- Receive MSG: Line = " + Integer.parseInt(tokens[1]) + " | Type = " + double.parsedouble(tokens[2]) + " | Angle = " + double.parsedouble(tokens[3]) + " | Distance = " + double.parsedouble(tokens[4]));
//					}else{
//						System.out.println("#ERROR1: Dont receive MSG: " + dataIn);
//					}
//					// Message Board
//					if(tokens.length == 3) {
//						this.setBoard(Double.parseDouble(tokens[1]));
//					}else{
//						System.out.println("#ERROR2: Dont receive MSG: " + dataIn);
//					}
//				} catch (SocketTimeoutException ste) {
//					System.out.println ("  - Timeout Occurred: Packet assumed lost");
//				}

				if(this.isK()) {
					// Handling message send
					String dataOut = "MSG;" + String.valueOf(this.getLineCheck()) + ";#\0";
					if(dataOut != lastMessage){
						sendData = dataOut.getBytes("ISO-8859-1");
						lastMessage = dataOut;
						// Send
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876); // montando pacote de envio 
						clientSocket.send(sendPacket); // socket envia o pacote da mensagem
						System.out.println ("-> Send MSG: " + dataOut);
						this.setK(false);
					}
				}
			}

			clientSocket.close();

		} catch (UnknownHostException ex) { 
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	public boolean isK() {
		return k;
	}

	public void setK(boolean k) {
		this.k = k;
	}
	
	public double getLine() {
		return line;
	}

	public void setLine(double line) {
		this.line = line;
	}
	
	public double isType() {
		return type;
	}
	public void setType(double type) {
		this.type = type;
	}
	public double isAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double isDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getLineCheck() {
		return lineCheck;
	}

	public void setLineCheck(double lineCheck) {
		this.lineCheck = lineCheck;
	}

	public Double getCheck() {
		return check;
	}

	public void setCheck(Double check) {
		this.check = check;
	}

	public double getBoard() {
		return board;
	}

	public void setBoard(double board) {
		this.board = board;
	}
}
