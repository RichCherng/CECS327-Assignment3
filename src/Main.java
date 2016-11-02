import java.util.Random;

public class Main {

	static int MAX_LENGTH = 25;
	static int MIN_LENGTH = 1;
	static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static void main(String[] args){

		Thread[] operators = new Thread[20];
		String[] POOL = generatePool();
		String[] ARRAY = select(POOL, 100);

		for(int i = 0; i < operators.length; i++){
			operators[i] = new Operator(i, ARRAY, POOL);
			operators[i].start();
		}

		System.out.printf("        \t%10s %20s %20s %20s\n", "Search Average", "Replace Average", "STD Search", "STD Replace");
		System.out.println("----------------------------------------------------------------------------------------");
		for(int i = 0; i < operators.length; i++){
			try {
				operators[i].join();
				System.out.print("Thread "+ i + "\t");
				((Operator) operators[i]).stat();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	public static String[] select(String[] pPool, int pNum){
		Random rand = new Random();
		String[] arr = new String[pNum];
		for(int i = 0; i < pNum; i++){
			arr[i] = pPool[rand.nextInt(pPool.length)];
		}
		return arr;
	}

	/**
	 * Generate 125 random strings with random length
	 * @return
	 */
	public static String[] generatePool(){
		Random rand = new Random();
		String[] arr = new String[125];
		for(int i = 0; i < 125; i++){
			int length = rand.nextInt((MAX_LENGTH - MIN_LENGTH) + 1) + MIN_LENGTH;
			String str = "";
			for(int l = 0; l < length; l++){
				str += alphabet.charAt(rand.nextInt(alphabet.length()));
			}
			arr[i] = str;
		}
		return arr;
	}
}
