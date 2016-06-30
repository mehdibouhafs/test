package running;

public class RunScheduler {
	
	public void run(){
		try{
			System.out.println("Merci beaucoup le batch va se r�peter toutes le 5 secondes exemple de cron");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
