package org.psstools.eclipse.pst.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;

import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.eclipse.lsp4j.services.LanguageServer;

public class PSSConnectionProvider extends ProcessStreamConnectionProvider {
	private Process						m_server_proc;
	private ServerSocket				m_srv_sock;
	private Socket						m_sock;
	
	public PSSConnectionProvider() {
		super();
		System.out.println("PSSConnectionProvider");
	}

	@Override
	public void start() {
		int port = -1;
		try {
			m_srv_sock = new ServerSocket(0);
			port = m_srv_sock.getLocalPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String server_path = "/project/fun/psslangserver/psslangserver/build/psslangserver";
//		String server_path = "c:/usr1/fun/psslangserver/psslangserver/build/psslangserver";
		File server_path = Activator.getLanguageServer();
	
		try {
			m_server_proc = Runtime.getRuntime().exec(new String[] {
					server_path.getAbsolutePath(),
					"-port",
					"" + port
			});
		
			final InputStream stdout = m_server_proc.getInputStream();
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (true) {
						byte tmp[] = new byte[1024];
						try {
							int sz = stdout.read(tmp, 0, tmp.length);
							if (sz <= 0) {
								break;
							}
							System.out.write(tmp, 0, sz);
						} catch (Exception e) {
							break;
						}
					}
				}
			});
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			m_sock = m_srv_sock.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("PSSConnectionProvider.start()");
	}
	
	@Override
	public void handleMessage(Message message, LanguageServer languageServer, URI rootURI) {
//		System.out.println("handleMessage: " + message);
	}

	@Override
	public InputStream getInputStream() {
		try {
			return m_sock.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			return m_sock.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
