package org.psstools.eclipse.pst.core;

import java.io.File;
import java.net.URL;
import java.rmi.activation.ActivationGroupID;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static Activator		plugin;
	private static BundleContext 	context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.plugin = this;
		Activator.context = bundleContext;
		System.out.println("Activator.start");
		Enumeration<URL> items = bundleContext.getBundle().findEntries("/", "*", true);
		while (items.hasMoreElements()) {
			URL url = items.nextElement();
			System.out.println("url: " + url);
		}
		
		Enumeration<URL> exe = bundleContext.getBundle().findEntries("/", "psslangserver*", false);
		while (exe.hasMoreElements()) {
			URL url = exe.nextElement();
			System.out.println("exe: " + url);
			File file = bundleContext.getBundle().getDataFile(url.getFile());
			System.out.println("file: " + file);
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public static File getLanguageServer() {
		Enumeration<URL> exe = context.getBundle().findEntries("/", "psslangserver*", false);
		URL url = exe.nextElement();
		File file = null;
		try {
			url = FileLocator.toFileURL(url);
			System.out.println("Location: " + context.getBundle().getLocation());
			file = URIUtil.toFile(URIUtil.toURI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("file=" + file);
		return file;
	}
}
