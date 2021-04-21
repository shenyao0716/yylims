package lims.util;


import ime.core.ext.IExtension;
import lims.sync.SyncTask;

import java.util.HashMap;
import java.util.Map;

public class Extension implements IExtension{

	private static Map<String, String> prop = new HashMap<String, String>();

	private static SyncTask syncTask;

	public void handleLoad() throws Exception{

		syncTask = new SyncTask();
		syncTask.start();
	}

	public static SyncTask getSyncTask(){
		return syncTask;
	}

	public void handleUnload() throws Exception {
		syncTask.stopTask();
		syncTask.wakeup();
	}

	public static String getProperty(String name){
		if( prop != null )
			return prop.get(name);
		else
			return null;
	}
	public void setProperties(Map<String, String> prop) {
		Extension.prop = prop;
	}


	public Map<String, String> getProperties(){
		return prop;
	}

}