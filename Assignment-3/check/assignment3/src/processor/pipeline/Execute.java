package processor.pipeline;

import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public static char flip(char c) {
		
        return (c == '0') ? '1' : '0';
	}
	
	/**
	 * Computes two's complement of given number in binary form
	 * Source: Stackoverflow
	 * @param bin: String representation of binary form of number
	 * @return: returns binary form of two's complement of given number
	 */
	
	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	
	/**
	 * converts binary representation of number to signed integer
	 * @param binary: Sring representation of binary form of number
	 * @return: returns signed representation of given number
	*/
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}

	private void loopAround(int num) {
		for (int i = 0; i < num; i += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(i, 20));
	}
	
	public static String twosComplement(String bin) {
		
        String twos = "", ones = "";
        for (int i = 0; i < bin.length(); i++) 
            ones += flip(bin.charAt(i));

        StringBuilder builder = new StringBuilder(ones);
        boolean addExtra = false;
		
        for (int i = ones.length() - 1; i > 0; i--) {
		
            if (ones.charAt(i) == '1') 
                builder.setCharAt(i, '0');
            else {
		
                builder.setCharAt(i, '1');
                addExtra = true;
                break;
            }
        }
		
        if (addExtra == false) 
            builder.append("1", 0, 7);
		
        twos = builder.toString();
        return twos;
    }

	public void performEX() {

		if(OF_EX_Latch.isEX_enable()) {

			Instruction instruction = OF_EX_Latch.getInstruction();
			System.out.println(instruction);
			EX_MA_Latch.setInstruction(instruction);
			OperationType op_type = instruction.getOperationType();
			int opcode = Arrays.asList(OperationType.values()).indexOf(op_type);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
			int signedInt = toSignedInteger("001");
			String binaryNum = toBinaryOfSpecificPrecision(signedInt, 5);

			int alu_result = 0;

			loopAround(30);

			if(opcode % 2 == 0 && opcode < 21 && opcode >= 0) {

				int op1 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand2().getValue());

				switch(op_type) {

					case add:
						alu_result = (op1 + op2);
						break;
					case mul:
						alu_result = (op1 * op2);
						break;
					case sub:
						alu_result = (op1 - op2);
						break;
					case load:
						break;
					case and:
						alu_result = (op1 & op2);
						break;
					case div:
						alu_result = (op1 / op2);
						int remainder = (op1 % op2);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case xor:
						alu_result = (op1 ^ op2);
						break;
					case or:
						alu_result = (op1 | op2);
						break;
					case store:
						break;						
					case slt:
						if(op1 < op2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case srli:
						break;
					case srl:
						alu_result = (op1 >>> op2);
						break;
					case sll:
						alu_result = (op1 << op2);
						break;
					case sra:
						alu_result = (op1 >> op2);
						break;
					case end:
						break;
					default:
						break;
				}
			}
			else if(opcode < 23) {

				int i = instruction.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(i);
				int op2 = instruction.getSourceOperand2().getValue();

				switch(op_type) {

					case addi:
						alu_result = (op1 + op2);
						break;
					case muli:
						alu_result = (op1 * op2);
						break;
					case beq:
						break;
					case subi:
						alu_result = (op1 - op2);
						break;
					case andi:
						alu_result = (op1 & op2);
						break;
					case end:
						break;
					case xori:
						alu_result = (op1 ^ op2);
						break;
					case ori:
						alu_result = (op1 | op2);
						break;
					case divi:
						alu_result = (op1 / op2);
						int remainder = (op1 % op2);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case jmp:
						break;
					case srli:
						alu_result = (op1 >>> op2);
						break;
					case slti:
						if(op1 < op2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case slli:
						alu_result = (op1 << op2);
						break;
					case load:
						alu_result = (op1 + op2);
						break;
					case srai:
						alu_result = (op1 >> op2);
						break;
					default:
						break;
				}
			}
			else if(opcode == 23) {

				int op1 = containingProcessor.getRegisterFile().getValue(
					instruction.getDestinationOperand().getValue());
				int op2 = instruction.getSourceOperand2().getValue();
				alu_result = op1 + op2;
			}
			else if(opcode == 24) {

				OperandType optype = instruction.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register) 
					imm = containingProcessor.getRegisterFile().getValue(
						instruction.getDestinationOperand().getValue());
				else 
					imm = instruction.getDestinationOperand().getValue();

				alu_result = imm + currentPC;
				EX_IF_Latch.setIS_enable(true, alu_result);
			}
			else if(opcode < 29) {

				int imm = instruction.getDestinationOperand().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand2().getValue());
				System.out.println(op1);
				System.out.println(op2);
				System.out.println(instruction);
				switch(op_type) {

					case beq:
						if(op1 == op2) {

							alu_result = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}
						break;
					case bne:
						if(op1 != op2) {

							alu_result = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}

						break;
					case blt:
						if(op1 < op2) {

							alu_result = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}
						break;
					case bgt:
						if(op1 > op2) {

							alu_result = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}
						break;
					default:
						break;
				}
			}
			EX_MA_Latch.setALU_result(alu_result);
		}

		OF_EX_Latch.setEX_enable(false);
		EX_MA_Latch.setMA_enable(true);
	}

}
