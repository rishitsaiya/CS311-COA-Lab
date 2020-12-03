package generic;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	public static long storeresp;
	public static int ins_count;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) throws FileNotFoundException
	{
		eventQueue = new EventQueue();
		storeresp = 0;
		ins_count = 0;
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws FileNotFoundException {
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
		 
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(assemblyProgramFile)));
		
		try{
			int n = dis.readInt();
			int i;
			for(i=0;i<n;i++){
				int temp = dis.readInt();
				processor.getMainMemory().setWord(i,temp);
			}
			
			int pc = i;
			int offset = 1;
			processor.getRegisterFile().setProgramCounter(pc);

			while(dis.available()>0){
				int temp = dis.readInt();
				processor.getMainMemory().setWord(i,temp);
				i += offset;
			}
			
			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);

			dis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static EventQueue getEventQueue() { 
		return eventQueue ; 
	}
	
	public static void simulate() {
		int cycles = 0;
		
		while(Simulator.simulationComplete == false) {
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			cycles += 1;
		}
		
		// TODO
		// set statistics
		Statistics.setNumberOfCycles(cycles);
		Statistics.setNumberOfInstructions(ins_count);
		Statistics.setCPI();
		
	}
	
	public static void setSimulationComplete(boolean value)	{
		simulationComplete = value;
	}

}