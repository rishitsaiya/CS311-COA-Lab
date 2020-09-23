package generic;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.graalvm.compiler.asm.aarch64.AArch64Assembler.Instruction;

// import org.graalvm.compiler.asm.aarch64.AArch64Assembler.Instruction;


public class Simulator {

	static FileInputStream inputcodeStream = null;
	public static Map<Instruction.OperationType, String> mapping = new HashMap<>();

	public Simulator() {
		mapping.put(Instruction.OperationType.add, "00000");
		mapping.put(Instruction.OperationType.addi, "00001");

	}

	public static void setupSimulation(String assemblyProgramFile) {
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	private static String toBinaryString(int n) {
		// Remove this conditional statement
		if (n >= 0) return String.valueOf(n);

		Stack<Integer> bits = new Stack<>();
		do {
			bits.push(n % 2);
			n /= 2;
		} while (n != 0);

		StringBuilder builder = new StringBuilder();
		while (!bits.isEmpty()) {
			builder.append(bits.pop());
		}
		return builder.toString();
	}

	private static String convert(Operand inst) {
		if (inst.getOperandType() == Operand.OperandType.Label)
			return toBinaryString(ParsedProgram.symtab.get(inst.getLabelValue()));

		// write logic for converting to binary/ hex
		return toBinaryString(inst.getValue());
		// check if inst is a label, in that case, use its value 
		// return String.valueOf(inst.getValue());
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
				/**
				 * inst.getSourceOperand().getValue() will be passed to a function as f()
				 * that will change decimal to binary and then will return the string
				 * form of the binary. It will also check if the value is a label,
				 * in case it is a label, it would call ParsedProgram.symtab.get()
				 * to get the address corresponding to the label
				 */
				// print operation type, use toBinaryString() instead of convert()

				if (inst.getSourceOperand1() != null)
					file.write(convert(inst.getSourceOperand1()));
				if (inst.getSourceOperand2() != null)
					file.write(convert(inst.getSourceOperand2()));
				if (inst.getDestinationOperand() != null)
					file.write(convert(inst.getDestinationOperand()));
				file.write(inst.toString());
			}

			//5. close the file
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}
