package BuzzerBeater;

//credit: Revised SpellChecker project from AP Computer Science A - Tarun Naveen
public class StopWatch {

	private long watchStart = 0;
	private long watchStop = 0;
	
	public void start() {
		watchStart = System.currentTimeMillis();
	}
	
	public double stop() {
		watchStop = System.currentTimeMillis();
        return getTimeSeconds();
	}
	
	// convert nano to micro
	public long getTimeMicroSec() {
		return (watchStop - watchStart) / 1000;
	}
	
    // convert nano to seconds
	public double getTimeSeconds() {
		return (watchStop - watchStart) / 1000.0;
	}

	public double getCurrentTime() {
		return (System.currentTimeMillis() - watchStart) / 1000.0;
	}

	public String toString() {
        System.out.println(watchStart + ", " + watchStop);
		return String.format("Time: %.4f Seconds", getTimeSeconds());
	}
}

