package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;
import generic.Statistics;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) {

		Simulator.processor = p;
		try {

			loadProgram(assemblyProgramFile);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws IOException {
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
		InputStream is = null;
		try {

			is = new FileInputStream(assemblyProgramFile);
		}
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		DataInputStream dis = new DataInputStream(is);

		int address = -1;
		int add_offset = 1;
		while(dis.available() > 0) {

			int next = dis.readInt();
			switch(address) {
				case -1:
					processor.getRegisterFile().setProgramCounter(next);
					break;
				default:
					processor.getMainMemory().setWord(address, next);
					break;
			}

			address += add_offset;
		}
		
		int CONST = 65535;
		
        processor.getRegisterFile().setValue(0, 0);
        processor.getRegisterFile().setValue(1, CONST);
        processor.getRegisterFile().setValue(2, CONST);
	}

	public static void simulate() {

		while(simulationComplete == false) {

			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();

			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
		}
	}
	
	public static void setSimulationComplete(boolean value) {

		simulationComplete = value;
	}
}
