package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Statistics;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public static char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	public static String twosComplement(String bin) {
		String twos = "", ones = "";
		for (int i = 0; i < bin.length(); i++) {
			ones += flip(bin.charAt(i));
		}

		StringBuilder builder = new StringBuilder(ones);
		boolean b = false;
		for (int i = ones.length() - 1; i > 0; i--) {
			if (ones.charAt(i) == '1') {
				builder.setCharAt(i, '0');
			} else {
				builder.setCharAt(i, '1');
				b = true;
				break;
			}
		}
		if (!b) {
			builder.append("1", 0, 7);
		}
		twos = builder.toString();
		return twos;
	}
	
	public static boolean checkConflict(Instruction instruction, int reg_1, int reg_2) {
//		System.out.println("1111");
		int inst_ordinal = instruction != null && instruction.getOperationType() != null ? instruction.getOperationType().ordinal() : 1000;
//		System.out.println("1-Conflict Observed with: " + instruction + ".");
		if ((inst_ordinal <= 21 && inst_ordinal % 2 == 0) || (inst_ordinal <= 21 && inst_ordinal % 2 != 0) || inst_ordinal == 22 || inst_ordinal == 23) {
			int dest_reg = instruction != null ? instruction.getDestinationOperand().getValue() : -1;
			if (reg_1 == dest_reg || reg_2 == dest_reg) {
//				System.out.println("Conflict Observed with: " + reg_1 + ", " + reg_2 + ", " + dest_reg + ".");
				return true;
			} else {
				return false;
			}
		} else return false;
	}
	
	public boolean checkConflictWithDivision(int reg_1, int reg_2) {
		Instruction instruction_ex_stage = OF_EX_Latch.getInstruction();
		Instruction instruction_ma_stage = EX_MA_Latch.getInstruction();
		Instruction instruction_rw_stage = MA_RW_Latch.getInstruction();
		if (reg_1 == 31 || reg_2 == 31) {
			int inst_ex_ordinal = instruction_ex_stage != null && instruction_ex_stage.getOperationType() != null ? instruction_ex_stage.getOperationType().ordinal() : 1000;
			int inst_ma_ordinal = instruction_ma_stage != null && instruction_ma_stage.getOperationType() != null ? instruction_ma_stage.getOperationType().ordinal() : 1000;
			int inst_rw_ordinal = instruction_rw_stage != null && instruction_rw_stage.getOperationType() != null ? instruction_rw_stage.getOperationType().ordinal() : 1000;
			if (inst_ex_ordinal == 6 || inst_ex_ordinal == 7 || inst_ma_ordinal == 6 || inst_ma_ordinal == 7 || inst_rw_ordinal == 6 || inst_rw_ordinal == 7) {
				System.out.println("Conflict in division");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void conflictBubblePCModify () {
		System.out.println("Conflict Observed");
		IF_EnableLatch.setIF_enable(false);
		OF_EX_Latch.setIsNOP(true);
//		int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
//		containingProcessor.getRegisterFile().setProgramCounter(currentPC - 1);
	}
 	
	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			Statistics.setNumberOfOFInstructions(Statistics.getNumberOfOFInstructions() + 1);
			OperationType[] operationType = OperationType.values();
			String instruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			System.out.println("OF is enabled with instruction: " + instruction + "..");
			while (instruction.length() != 32) {
				instruction = "0" + instruction;
			}
			String opcode = instruction.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationType[type_operation];
			
			if (operation.ordinal() == 24 || operation.ordinal() == 25 || operation.ordinal() == 26 || operation.ordinal() == 27 || operation.ordinal() == 28 ) {
//				System.out.println("Instruction Ordinal" + operation.ordinal());
				IF_EnableLatch.setIF_enable(false);
			}
			

			
			boolean conflict_inst = false;
			Instruction instruction_ex_stage = OF_EX_Latch.getInstruction();
			Instruction instruction_ma_stage = EX_MA_Latch.getInstruction();
			Instruction instruction_rw_stage = MA_RW_Latch.getInstruction();
			Instruction inst = new Instruction();
			switch (operation) {
			case add:
			case sub:
			case mul:
			case div:
			case and:
			case or:
			case xor:
			case slt:
			case sll:
			case srl:
			case sra:
				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				int registerNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(registerNo2);
				if (checkConflict(instruction_ex_stage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflict(instruction_ma_stage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflict(instruction_rw_stage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflictWithDivision(registerNo, registerNo2)) {
					conflict_inst = true;
				}
				if (conflict_inst) {
					this.conflictBubblePCModify();
					break;
				}

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(15, 20), 2);
				rd.setValue(registerNo);

				inst.setOperationType(operationType[type_operation]);
				inst.setSourceOperand1(rs1);
				inst.setSourceOperand2(rs2);
				inst.setDestinationOperand(rd);
				break;

			case end:
				inst.setOperationType(operationType[type_operation]);
				IF_EnableLatch.setIF_enable(false);
				break;
			case jmp:
				Operand op = new Operand();
				String imm = instruction.substring(10, 32);
				int imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				if (imm_val != 0) {
					op.setOperandType(OperandType.Immediate);
					op.setValue(imm_val);
				} else {
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					op.setOperandType(OperandType.Register);
					op.setValue(registerNo);
				}

				inst.setOperationType(operationType[type_operation]);
				inst.setDestinationOperand(op);
				break;

			case beq:
			case bne:
			case blt:
			case bgt:
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);
				
				// destination register
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				registerNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
				rs2.setValue(registerNo2);
				
				if (checkConflict(instruction_ex_stage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflict(instruction_ma_stage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflict(instruction_rw_stage, registerNo, registerNo2))
					conflict_inst = true;
				if (checkConflictWithDivision(registerNo, registerNo2)) {
					conflict_inst = true;
				}
				if (conflict_inst) {
					this.conflictBubblePCModify();
					break;
				}
				
	

				// Immediate value
				rd = new Operand();
				rd.setOperandType(OperandType.Immediate);
				imm = instruction.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rd.setValue(imm_val);
				
				inst.setOperationType(operationType[type_operation]);
				inst.setSourceOperand1(rs1);
				inst.setSourceOperand2(rs2);
				inst.setDestinationOperand(rd);
//				System.out.println("------CANCER-----" + inst);
				break;

			default:
				// Source register 1
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
				rs1.setValue(registerNo);
//				System.out.println("Conflicts are ------------ Registers are: " + instruction_ex_stage);
				if (checkConflict(instruction_ex_stage, registerNo, registerNo)) {
//					OF_EX_Latch.setEX_enable(false);
//					System.out.println("-----------Culprit_EX-------------");
					conflict_inst = true;
				}	
				if (checkConflict(instruction_ma_stage, registerNo, registerNo)) {
//					System.out.println("-----------Culprit_MA-------------");
//					EX_MA_Latch.setMA_enable(false);
					conflict_inst = true;
				}
				if (checkConflict(instruction_rw_stage, registerNo, registerNo)) {
//					MA_RW_Latch.setRW_enable(false);
//					System.out.println("-----------Culprit_RW-------------");
					conflict_inst = true;
				}
				if (checkConflictWithDivision(registerNo, registerNo)) {
					conflict_inst = true;
				}
					
				if (conflict_inst) {
					this.conflictBubblePCModify();
					break;
				}

				// Destination register
				rd = new Operand();
				rd.setOperandType(OperandType.Register);
				registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
				rd.setValue(registerNo);

				// Immediate values
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Immediate);
				imm = instruction.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rs2.setValue(imm_val);
				inst.setOperationType(operationType[type_operation]);
				inst.setSourceOperand1(rs1);
				inst.setSourceOperand2(rs2);
				inst.setDestinationOperand(rd);
//				System.out.println("-----------" + rs1 + ", " +  rs2 + ", " +  rd + ", " + "-------------");
//				System.out.println("-----------" + inst + "-------------");
				break;
			}
//			if (!conflict_inst) {
			OF_EX_Latch.setInstruction(inst);
//			} 
//			else {
//				Instruction instruction_temp;
//				instruction_temp
//				OF_EX_Latch.setInstruction(instruction_temp);
//			}
//			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
