package generic;
import java.io.*;
import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static int cycle=0;
	static EventQueue eventQueue;
	
	/*public Simulator() {
		eventQueue = new EventQueue();
	}*/
	 
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) throws IOException
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		eventQueue = new EventQueue();
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws IOException
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
//		System.out.println(assemblyProgramFile);

		try{
			int i=0;
			DataInputStream din = new DataInputStream(new FileInputStream(assemblyProgramFile)) ;
			processor.getRegisterFile().setProgramCounter(din.readInt());
//			System.out.println("dsfds");
			while (din.available() > 0) {				
				processor.getMainMemory().setWord(i,din.readInt());
				i++ ;	
			}
			processor.getMainMemory().getContentsAsString(0, 10);
			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);
//			System.out.println(processor.getMainMemory().getContentsAsString(0, 50));
			din.close();
			
		}
    	catch(FileNotFoundException e){
    		System.out.println("Cannot Open the Input File");
    		return;
    	}
		
		
	}
	
	public static EventQueue getEventQueue() {
		return eventQueue;
	}
	public static void simulate()
	{
		//System.out.println("sdsdsdsdsd");
		while(simulationComplete == false)
		{
			Clock.incrementClock();
			//System.out.println("HI");
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			
			cycle++;
		}
		//System.out.println(cycle);
		// TODO
		// set statistics
		//Statistics.setNumberOfInstructions(cycle);
		Statistics.setNumberOfCycles(cycle);
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
