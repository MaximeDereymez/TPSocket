/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.printing;

import static jus.aor.printing.Notification.QUERY_PRINT;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Classe de service fournissant toutes les interactions (read, write) en mode TCP.
 * @author Morat 
 */
class TCP{
	private static final int MAX_LEN_BUFFER = 1024;
	/**
	 * 
	 * @param soc the socket
	 * @param not the notification
	 * @throws IOException
	 */
	static void writeProtocole(Socket soc,  Notification not) throws IOException {
		new DataOutputStream(soc.getOutputStream()).writeInt(not.I);
	}
	/**
	 * 
	 * @param soc the socket 
	 * @return the notification
	 * @throws IOException
	 */
	static Notification readProtocole(Socket soc) throws IOException {
		return Notification.getById(new DataInputStream(soc.getInputStream()).readInt());
	}
	/**
	 * 
	 * @param soc the socket
	 * @param key the JobKey to write
	 * @throws IOException
	 */
	static void writeJobKey(Socket soc, JobKey key) throws IOException {
		new DataOutputStream(soc.getOutputStream()).writeUTF(new String(key.marshal()));
	}
	/**
	 * 
	 * @param soc the socket
	 * @return the JobKey
	 * @throws IOException
	 */
	static JobKey readJobKey(Socket soc) throws IOException {
		return new JobKey(new DataInputStream(soc.getInputStream()).readUTF().getBytes());
	}
	/**
	 * 
	 * @param soc the socket
	 * @param fis the input stream ti transfert
	 * @param len th len of the input stream
	 * @throws IOException
	 */
	static void writeData(Socket soc, InputStream fis, int len) throws IOException {
		byte[] content = new byte[MAX_LEN_BUFFER];
		DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
		dos.writeInt(len);
		int length;
        while ((length = fis.read(content)) > 0) 
        	dos.write(content, 0, length);
	}
	/**
	 * 
	 * @param soc th socket
	 * @return string data 
	 * @throws IOException
	 */
	static String readData(Socket soc) throws IOException {
		DataInputStream dis = new DataInputStream(soc.getInputStream());
		int len = dis.readInt();
		byte[] content = new byte[MAX_LEN_BUFFER];
		String contentString = "";
        while (len > MAX_LEN_BUFFER) {
        	len -= dis.read(content,0,MAX_LEN_BUFFER);
        	contentString += new String(content);
        }
        dis.read(content,0,(int)len);
    	contentString += new String(content);
    	return contentString;
	}
	/**
	 * 
	 * @param soc the socket
	 * @param jobs the JobState
	 * @throws IOException
	 */
	static void writeJobState(Socket soc,  JobState jobs) throws IOException {
	//----------------------------------------------------------------------------- A COMPLETER
	}
	/**
	 * 
	 * @param soc the socket 
	 * @return the JobState
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static JobState readJobState(Socket soc) throws IOException, ClassNotFoundException {
	//----------------------------------------------------------------------------- A COMPLETER
	}
}
