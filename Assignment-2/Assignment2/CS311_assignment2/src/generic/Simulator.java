package generic;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


public class Simulator {

	static FileInputStream inputcodeStream = null;

	public static void setupSimulation(String assemblyProgramFile) {
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	public static void assemble(String objectProgramFile) {
		FileWriter file;
		try {
			//1. open the objectProgramFile in binary mode
			file = new FileWriter(objectProgramFile);

			//2. write the firstCodeAddress to the file
			file.write(String.valueOf(ParsedProgram.firstCodeAddress));

			//3. write the data to the file
			for (var value: ParsedProgram.data)
				file.write(String.valueOf(value));

			//4. assemble one instruction at a time, and write to the file
			for (var inst: ParsedProgram.code) {
				// file.write(inst.toString());
				if (inst.getSourceOperand1() != null)
					file.write(String.valueOf((inst.getSourceOperand1().getValue())));
				if (inst.getSourceOperand2() != null)
					file.write(String.valueOf((inst.getSourceOperand2().getValue())));
				if (inst.getDestinationOperand() != null)
					file.write(String.valueOf(inst.getDestinationOperand().getValue()));
				file.write(inst.toString());
			}

			//5. close the file
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
