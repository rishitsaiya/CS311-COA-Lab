package processor.pipeline;

import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;
// import generic.Operand;
import generic.Operand.OperandType;
import generic.Statistics;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch,
			EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performEX() {
		if (OF_EX_Latch.getIsNOP()) {
			EX_MA_Latch.setIsNOP(true);
			OF_EX_Latch.setIsNOP(false);
			EX_MA_Latch.setInstruction(null);
		} else if (OF_EX_Latch.isEX_enable()) {
			Instruction instruction = OF_EX_Latch.getInstruction();
//			System.out.println(instruction);
			System.out.println("EX is enabled: " + instruction);
//			System.out.println(containingProcessor.getRegisterFile().getValue(31) + "-------------**************");
			EX_MA_Latch.setInstruction(instruction);
			OperationType op_type = instruction.getOperationType();
			int opcode = Arrays.asList(OperationType.values()).indexOf(op_type);
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;

			if (opcode == 24 || opcode == 25 || opcode == 26 || opcode == 27 || opcode == 28 || opcode == 29) {
//				System.out.println("I am being executed");
				Statistics.setNumberOfBranchTaken(Statistics.getNumberOfBranchTaken() + 2);
				IF_EnableLatch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(false);
			}
			
			int alu_result = 0;

			if (opcode % 2 == 0 && opcode < 21) {
				int op1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());

				switch (op_type) {
				case add:
					alu_result = op1 + op2;
					break;
				case sub:
					alu_result = op1 - op2;
					break;
				case mul:
					alu_result = op1 * op2;
					break;
				case div:
					alu_result = op1 / op2;
					int remainder = op1 % op2;
					containingProcessor.getRegisterFile().setValue(31, remainder);
					break;
				case and:
					alu_result = op1 & op2;
					break;
				case or:
					alu_result = op1 | op2;
					break;
				case xor:
					alu_result = op1 ^ op2;
					break;
				case slt:
					if (op1 < op2)
						alu_result = 1;
					else
						alu_result = 0;
					break;
				case sll:
					alu_result = op1 << op2;
					break;
				case srl:
					alu_result = op1 >>> op2;
					break;
				case sra:
					alu_result = op1 >> op2;
					break;
				default:
					break;
				}
			} else if (opcode < 23) {
				int i = instruction.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(i);
				int op2 = instruction.getSourceOperand2().getValue();

				switch (op_type) {
				case addi:
					alu_result = op1 + op2;
					break;
				case subi:
					alu_result = op1 - op2;
					break;
				case muli:
					alu_result = op1 * op2;
					break;
				case divi:
					alu_result = op1 / op2;
					int remainder = op1 % op2;
					System.out.println(alu_result + ", " + remainder + "........");
					containingProcessor.getRegisterFile().setValue(31, remainder);
					break;
				case andi:
					alu_result = op1 & op2;
					break;
				case ori:
					alu_result = op1 | op2;
					break;
				case xori:
					alu_result = op1 ^ op2;
					break;
				case slti:
					if (op1 < op2)
						alu_result = 1;
					else
						alu_result = 0;
					break;
				case slli:
					alu_result = op1 << op2;
					break;
				case srli:
					alu_result = op1 >>> op2;
					break;
				case srai:
					alu_result = op1 >> op2;
					break;
				case load:
					alu_result = op1 + op2;
					break;
				default:
					break;
				}
			} else if (opcode == 23) {
				int op1 = containingProcessor.getRegisterFile()
						.getValue(instruction.getDestinationOperand().getValue());
				int op2 = instruction.getSourceOperand2().getValue();
				alu_result = op1 + op2;
			} else if (opcode == 24) {
				OperandType optype = instruction.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register) {
					imm = containingProcessor.getRegisterFile()
							.getValue(instruction.getDestinationOperand().getValue());
				} else {
					imm = instruction.getDestinationOperand().getValue();
				}
				alu_result = imm + currentPC;
				EX_IF_Latch.setIS_enable(true, alu_result);
			} else if (opcode < 29) {
				int op1 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
				int imm = instruction.getDestinationOperand().getValue();
//				System.out.println(instruction.getSourceOperand1().getValue() + "------Check-------" + instruction.getSourceOperand2().getValue());
				switch (op_type) {
				case beq:
					if (op1 == op2) {
						alu_result = imm + currentPC;
						EX_IF_Latch.setIS_enable(true, alu_result);
					}
					break;
				case bne:
					if (op1 != op2) {
						alu_result = imm + currentPC;
						EX_IF_Latch.setIS_enable(true, alu_result);
					}

					break;
				case blt:
					if (op1 < op2) {
						alu_result = imm + currentPC;
						EX_IF_Latch.setIS_enable(true, alu_result);
					}
					break;
				case bgt:
					if (op1 > op2) {
						alu_result = imm + currentPC;
						EX_IF_Latch.setIS_enable(true, alu_result);
					}
					break;
				default:
					break;
				}
			}
//			System.out.println("alu_result" + Integer.toString(alu_result));
			EX_MA_Latch.setALU_result(alu_result);

			// OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
		}
	}

}
