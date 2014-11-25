import java.net.*;

public class ServerUDP {
	
	public static void main(String args[]) throws Exception { 

		// cria socket
		DatagramSocket serverSocket = new DatagramSocket(9876);
		
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

				String capitalizedSentence = sentence.toUpperCase(); // converte o conteúdo da mensagem para letras maiúsculas 

				sendData = capitalizedSentence.getBytes(); // armazena a mensagem do usuário no byte de envio

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); // montando pacote de envio

				serverSocket.send(sendPacket); // socket envia o pacote da mensagem

			} 

		} catch (SocketException ex) {
			System.out.println("UDP Port 9876 is occupied.");
			System.exit(1);
		}

	}
}
