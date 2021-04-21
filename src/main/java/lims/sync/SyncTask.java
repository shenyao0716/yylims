package lims.sync;



public class SyncTask extends Thread {

	private static int WAIT_TIME = 1000 * 60 * 1000000;
	private boolean stoped = false;
	
	public void run() {
		SyncReport syncReport = new SyncReport();
		SyncSubmitReport suncSubmitReport = new SyncSubmitReport();
		
		while( !stoped ){
			try {
				synchronized(this){
					this.wait(WAIT_TIME);
				}
				if(stoped)
					break;
				suncSubmitReport.handle();
				syncReport.handle();
			} catch (InterruptedException e) {
			}
		}
	}
	public void stopTask(){
		stoped = true;
	}
	public void wakeup(){
		synchronized(this){
			this.notify();
		}
	}
}
