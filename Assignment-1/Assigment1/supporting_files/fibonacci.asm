	.data
n:
	12
	.text
main:
	load %x0, $n, %x15		; x15 = n
	sub %x4, %x4, %x4		; x4 = 0
	addi %x4, 1, %x5		; x5 = x4 + 1 = 1
	addi %x0, 65535, %x20		; x20 = (1 << 16) - 1
	addi %x4, 1, %x10		; x10 = 1, x10 denotes i in for loop
	store %x4, 0, %x20		; store x4 at address 0+(1<<16)
	subi %x20, 1, %x20		; x20 = x20 - 1   (2^16 - i)
	store %x5, 0, %x20		; store x5 at address (2^16 - i)
	jmp increment
loop:
	beq %x10, %x15, exit		; if i == n, exit program
	add %x4, %x5, %x6		; x6 = x4 + x5
	subi %x20, 1, %x20		; x20 = x20 - x10   (2^16 - i)
	store %x6, 0, %x20		; save value of x6 in memory at address (2^16 - i)
update:
	addi %x5, 0, %x4		; x4 = x5
	addi %x6, 0, %x5		; x5 = x6 
increment:
	addi %x10, 1, %x10		; x10 += 1
	jmp loop
exit:
	end
