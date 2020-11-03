package class118133.tranhoangchuan;

public abstract class RunTime {
	long msRunTime;
	private int numsRun = 1; 
	public RunTime(int numsRun) {
		this.numsRun = numsRun;
	}
	public RunTime() {
	}
	public abstract void run();
	public void process() {
		long start = System.currentTimeMillis();
		for (int i =0; i <numsRun; ++i) {
			run();
		}
		long finish = System.currentTimeMillis();
		msRunTime = (finish - start)/numsRun;
	}
}
