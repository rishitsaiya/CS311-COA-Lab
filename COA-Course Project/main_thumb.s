	AREA DATA1, DATA
U_BCD EQU 0x10000000
P_BCD EQU 0x10000010
	AREA P01,CODE ;AREA directive for allocating storage space for data and code region
		ENTRY
	EXPORT __START ;label is start
__START
	; write a simple program to move immediate value 10 to the register r0
	; one end less loop (Embedded system)
	MOV R8, #10			; R8 = 10
	LDR R1, =U_BCD		; store address U_BCD in R1
	LDR R4, [R1]		; store value at address U_BCD in R4
loop
	ADD R1, R1, #4		; R1 += 4
	LDR R2, [R1]		; store value at new address R1 in R2
	CBZ R4, exit		; if R4 == 0, goto exit
multiply
	SUB R4, R4, #1		; R4 -= 1
	MOV R6, R4			; R6 = R4
	MOV R5, #1			; R5 = 1
times
	CBZ R6, draw		; if R6 == 0, goto draw
	MUL R5, R5, R8		; R5 = R5 * R8
	SUB R6, R6, #1		; R6 -= 1
	b times				; goto times
draw
	MLA R7, R2, R5, R7	; R7 = R2 * R5 + R7
	b loop				; goto loop
exit
	LDR R9, =P_BCD		; store address of P_BCD in R9
	STR R7, [R9]		; store value of R7 at P_BCD
	END
	
LABEL B LABEL ; B-branching level
	END