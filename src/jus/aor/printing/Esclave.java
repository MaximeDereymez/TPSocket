package jus.aor.printing;

import static jus.aor.printing.Notification.QUERY_PRINT;
import static jus.aor.printing.Notification.REPLY_PRINT_OK;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Esclave extends Thread{

	private BlockingQueue<Socket> clients;
	private BlockingQueue<Esclave> freePool;
	private Spooler spooler;
	
	public Esclave(Spooler spooler, BlockingQueue<Esclave> freePool){
		this(spooler);
		this.freePool=freePool;
		freePool.add(this);
	}
	
	public Esclave(Spooler spooler){
		super();
		clients = new ArrayBlockingQueue<>(1);
		this.spooler = spooler;
	}
	
	public void addPrint(Socket soc) throws InterruptedException{
		clients.put(soc);
	}
	
	public void run(){
		Socket current;
		while(true){
			//réception de la réponse du serveur d'impression
			try{
				current = clients.take();
				InputStream is = current.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				if(dis.readInt() == QUERY_PRINT.I){
					//réception de la requête
					String read = dis.readUTF();
					JobKey jobkey = new JobKey(read.getBytes());
					long length = dis.readLong();
					byte[] content = new byte[Server.MAX_REPONSE_LEN];
					String contentString = "";
			        while (length > Server.MAX_REPONSE_LEN) {
			        	length -= dis.read(content,0,Server.MAX_REPONSE_LEN);
			            //System.out.println(new String(content));
			        	contentString += new String(content);
			        }
			        dis.read(content,0,(int)length);
			        //System.out.println(new String(content));
		        	contentString += new String(content);
			        
			        spooler.add(new JobPrint(jobkey, contentString));
			        
					//réponse
					OutputStream os = current.getOutputStream();
					DataOutputStream dos = new DataOutputStream(os);
					dos.writeInt(REPLY_PRINT_OK.I);
					dos.writeUTF(new String(jobkey.marshal()));
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			if(freePool!=null)
				freePool.add(this);
			else
				break;
		}
	}
	
	public String toString(){
		return ""+getId();
	}
}
