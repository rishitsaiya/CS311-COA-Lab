	.data
a:
	10
	.text
main:
	load %x0, $a, %x3			; x3 = a
	divi %x3, 2, %x3			; x3 = floor(x3/2), x31 = x3 mod 2
	beq %x0, %x31, even			; if x31 == 0, then go to even fn (i.e., if remainder = 0)
	addi %x10, 1, %x10			; x10 = 1 (a is odd)
	end
even:
	subi %x10, 1, %x10			; x10 = -1 (a is even)
	end
