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
				if(TCP.readProtocole(current)==QUERY_PRINT){
					//réception de la requête
					JobKey jobkey = TCP.readJobKey(current);
					String content = TCP.readData(current);
			        
			        spooler.add(new JobPrint(jobkey, content));
			        
					//réponse
			        TCP.writeProtocole(current, REPLY_PRINT_OK);
			        TCP.writeJobKey(current, jobkey);
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
