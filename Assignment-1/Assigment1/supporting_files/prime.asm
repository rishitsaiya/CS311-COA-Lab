	.data
a:
	65537
	.text
main:
	load %x0, $a, %x4		; x4 = a
	divi %x4, 2, %x16		; x16 = x4/2
	addi %x16, 1, %x16		; x16 += 1 (check divisibility till x4/2)
	subi %x0, 0, %x31		; x31 = 0
	addi %x5, 2, %x5		; x5 = 2 (i = 2)
	bgt %x4, 1, isPrime		; if x4 > 1, then goto isPrime
	jmp false
isPrime:
	beq %x16, %x5, true		; if x16 == x5 (i == a/2 + 1), then goto true
	div %x4, %x5, %x17		; x17 = x4/x5, x31 = x4 mod x5
	beq %x0, %x31, false		; if x31 == 0, then goto false
	addi %x5, 1, %x5		; x5 += 1 (i += 1)
	jmp isPrime
false:
	subi %x0, 0, %x10		; x10 = 0
	subi %x10, 1, %x10		; x10 = -1
	end
true:
	subi %x0, 0, %x10		; x10 = 0
	addi %x10, 1, %x10		; x10 = 1
	end