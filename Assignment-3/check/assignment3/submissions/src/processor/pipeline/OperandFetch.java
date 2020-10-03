package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
		
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public static char flip(char c)
	{
        return (c == '0') ? '1' : '0';
    }
	
	public static String twosComplement(String bin)
	{
        String twos = "", ones = "";
        for (int i = 0; i < bin.length(); i++)
        {
            ones += flip(bin.charAt(i));
        }

        StringBuilder builder = new StringBuilder(ones);
        boolean b = false;
        for (int i = ones.length() - 1; i > 0; i--)
        {
            if (ones.charAt(i) == '1')
            {
                builder.setCharAt(i, '0');
            }
            else
            {
                builder.setCharAt(i, '1');
                b = true;
                break;
            }
        }
        if (!b)
        {
            builder.append("1", 0, 7);
        }
        twos = builder.toString();
        return twos;
    }
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			OperationType[] operationType = OperationType.values();
			String instruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while(instruction.length()!=32)
			{
				instruction = "0" + instruction;
			}
			String opcode = instruction.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationType[type_operation];

			Instruction inst = new Instruction();
			switch(operation)
			{
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
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo);

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
					break;
				case jmp:
					Operand op = new Operand();
					String imm = instruction.substring(10, 32);
					int imm_val = Integer.parseInt(imm, 2);
					if (imm.charAt(0) == '1')
					{
						imm = twosComplement(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					if (imm_val != 0)
					{
						op.setOperandType(OperandType.Immediate);
						op.setValue(imm_val);
					}
					else
					{
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
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo);

					// Immediate value
					rd = new Operand();
					rd.setOperandType(OperandType.Immediate);
					imm = instruction.substring(15, 32);
					imm_val = Integer.parseInt(imm, 2);
					if (imm.charAt(0) == '1')
					{
						imm = twosComplement(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					rd.setValue(imm_val);

					inst.setOperationType(operationType[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);
					break;

				default:
					// Source register 1
					rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);

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
					if (imm.charAt(0) == '1')
					{
						imm = twosComplement(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					rs2.setValue(imm_val);

					inst.setOperationType(operationType[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);
					break;
			}

			OF_EX_Latch.setInstruction(inst);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
