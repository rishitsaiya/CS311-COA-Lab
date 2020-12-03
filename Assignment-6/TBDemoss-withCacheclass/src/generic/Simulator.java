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
	
	static void loadProgram(String assemblyProgramFile) throws FileNotFoundException
	{
		System.out.println("inside load program "+assemblyProgramFile);
		DataInputStream instr = new DataInputStream(new BufferedInputStream(new FileInputStream(assemblyProgramFile)));
		try{
			int n=instr.readInt();
			int i;
			for(i=0;i<n;i++){
				int temp = instr.readInt();
				processor.getMainMemory().setWord(i,temp);
			}
			int pc =i;
			processor.getRegisterFile().setProgramCounter(pc);

			while(instr.available()>0){
				int temp = instr.readInt();
				processor.getMainMemory().setWord(i,temp);
				i++;
			}
			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);

			instr.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
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
	}
	
	public static void simulate()
	{
		int cycles = 0;
		while(Simulator.simulationComplete == false)
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			cycles++;
			System.out.println("-------------------------------------------");
		}
		System.out.println("Cycles Taken:         " + cycles);
		System.out.println("Instruction Executed: " + ins_count);
		// TODO
		Statistics stat = new Statistics();
		stat.setNumberOfCycles(cycles);
		stat.setNumberOfInstructions(ins_count);
		stat.setCPI();
		stat.setIPC();
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
	public static EventQueue getEventQueue() { 
		return eventQueue ; 
	}

}