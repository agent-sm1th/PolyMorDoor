package za.co.polymorph.polymordoor.web.core;

/**
 * 
 * @author Legion
 * 
 * Listener for the web API requests
 *
 */
public interface WebRequestListener {
	public void webTaskComplete(WebRequest task, Boolean result);
	public void webTaskCancelled(WebRequest task, Boolean result);
	public void webTaskProgress(WebRequest task, Double progress);
}
