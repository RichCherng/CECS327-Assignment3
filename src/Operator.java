import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Operator extends Thread{

	ArrayList<Float> searchWait, replaceWait;
	String[] POOL, ARRAY;
	private int NUM_OPERATION = 500;
	Random rand;
	Lock lock;

	public Operator(int pThreadID, String[] pPool, String[] pArray){
		POOL 		= pPool;
		ARRAY		= pArray;
		lock 		= new ReentrantLock();
		rand  		= new Random();
		searchWait	= new ArrayList<Float>();
		replaceWait = new ArrayList<Float>();
	}

	private void searchArray(){
		int index 		= rand.nextInt(ARRAY.length);
		Long startTime 	= System.nanoTime();
		lock.lock();
		long endTime 	= System.nanoTime();
		try{
			String strSearch = ARRAY[index];
			int occurs = 0;
			for(String s: ARRAY){
				if(strSearch.equals(s)){
					occurs++;
				}
			}
		} finally {
			lock.unlock();
			searchWait.add((float)(endTime - startTime));
		}

	}

	private void searchNReplace(){
		String search 	= POOL[rand.nextInt(POOL.length)];
		String replace 	= POOL[rand.nextInt(POOL.length)];
		Long startTime 	= System.nanoTime();
		lock.lock();
		Long endTime 	= System.nanoTime();
		try{
			for(int i = 0 ; i < ARRAY.length; i++){
				if(ARRAY[i].equals(search)){
					ARRAY[i] = replace;
					break;
				}
			}
		} finally {
			lock.unlock();
			replaceWait.add((float)(endTime - startTime));
		}
	}

	public void stat(){
		/** Average **/
		float searchAvg = 0, replaceAvg = 0;
		for(Float f: searchWait){
			searchAvg += f;
		}
		searchAvg /= searchWait.size();

		for(Float f: replaceWait){
			replaceAvg += f;
		}
		replaceAvg /= replaceWait.size();


		/*** Standard Deviation **/
		float stdSearchMean = 0;
		for(Float f: searchWait){
			stdSearchMean += Math.pow(f - searchAvg, 2);
		}
		stdSearchMean /= searchWait.size();
		stdSearchMean = (float) Math.sqrt(stdSearchMean);

		float stdReplaceMean = 0;
		for(Float f: replaceWait){
			stdReplaceMean += Math.pow(f-replaceAvg, 2);
		}
		stdReplaceMean /= replaceWait.size();
		stdReplaceMean = (float) Math.sqrt(stdReplaceMean);

		System.out.println("\tAverage lock time for search: " + searchAvg + " ns");
		System.out.println("\tAverage lock time for search & replace: " + replaceAvg + "ns");
		System.out.println("\tSTD lock time for search: " + stdSearchMean + "ns");
		System.out.println("\tSTD lock time for search & replace: " + stdReplaceMean + "ns");
	}

	public void run(){
		int operation;
		for(int o = 0; o < NUM_OPERATION; o++){
			operation = rand.nextInt(2501);
			if(operation > -1 && operation < 2000){
				// Range 0 - 1999
				searchArray();
//				System.out.println(mThreadID + " : Search");
			} else {
				// Range 2000-2500
				searchNReplace();
//				System.out.println(mThreadID + " : Repalce");
			}
		}
	}
}
