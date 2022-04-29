package lab9;

/**
 * 
 * COMP 3021
 * 
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29] 
another thread the max among the cells [30,59] 
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 * 
 * @author valerio
 *
 */
public class FindMax {
	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) throws InterruptedException {
		new FindMax().printMax();
	}

	public void printMax() throws InterruptedException {
		// this is a single threaded version

        LocalMax[] localMax = {new LocalMax(0, 29, array), new LocalMax(30, 59, array), new LocalMax(60, 89, array)};

        Thread[] threads = {new Thread(localMax[0]), new Thread(localMax[1]), new Thread(localMax[2])};

		threads[0].start();
        threads[1].start();
		threads[2].start();
		threads[0].join();
		threads[1].join();
		threads[2].join();
		int max = Math.max(localMax[0].max, Math.max(localMax[1].max, localMax[2].max));
		System.out.println("the max value is " + max);
	}


    class LocalMax implements Runnable{
		int[] array;
		int begin;
		int end;
		int max;

		LocalMax(int begin, int end, int[] array){
			this.begin = begin;
			this.end = end;
			this.array = array;
		}

        @Override
		public void run() {
			// you should NOT change this function
			max = array[begin];
			for (int i = begin + 1; i <= end; i++) {
				if (array[i] > max) {
					max = array[i];
				}
            }
        }
    }
}