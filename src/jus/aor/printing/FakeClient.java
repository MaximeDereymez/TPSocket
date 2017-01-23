package jus.aor.printing;

import java.io.File;

public class FakeClient implements Runnable {

	private Client c;
	private File f;
	private int n;
	
	public FakeClient(File f, Client c, int n){
		this.f = f;
		this.c = c;
		this.n = n;
	}
	
	@Override
	public void run() {
		c.queryPrint(f, n);
	}

}
