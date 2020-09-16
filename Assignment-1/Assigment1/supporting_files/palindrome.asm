	.data
a:
	2002
	.text
main:
	load %x0, $a, %x3			; x3 = a
	addi %x3, 0, %x4			; x4 = a
	addi %x0, 0, %x5			; x5 = 0
loop:
	divi %x4, 10, %x4			; x4 = x4 / 10 (To get remaining digits except last)
	beq %x4, %x0, endLoop			; if (x4 == 0), then goto endLoop fn
	add %x31, %x5, %x5			; x5 = x5 + x31 (add remainder of division)
	muli %x5, 10, %x5			; x5 = x5 * 10 (Reversing the digits of a)
	jmp loop
endLoop:
	add %x31, %x5, %x5			; x5 = x5 + x31 (add remainder of division)
	beq %x3, %x5, isPalindrome			;if (x3 == x5), then go to Palindrome
isNotPalindrome:
	subi %x0, 1, %x10			; x10 = -1 ('a' is not a Palindrome)
	end
isPalindrome:
	addi %x0, 1, %x10			; x10 = 1 ('a' is a Palindrome)
	end
