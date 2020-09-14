	.data
a:
	101
	.text
main:
	load %x0, $a, %x3			;0 + a = x3
	add %x3, %x0, %x4			;x3 + 0 = x4
	add %0, %x0, %x5			;0 + 0 = x5
Division:
	divi %x4, 10, %x4			;x4 / 10 = x4 (To get remaining digits except last)
	beq %x4, %x0, endPalindrome	;if(x4 == 0), then go to EndPalindrome fn
	add %x31, %x5, %x5			; x5 not NULL
	muli %x5, 10, %x5			; x5 * 10 = x5
	jmp Division
endPalindrome:
	add %x31, %x5, %x5			;
	beq %x3, %x5, isPalindrome	;if(x3 == x5), then go to Palindrome
notPalindrome:
	subi %x0, 1, %x10			;x10 = -1 (a is not Palindrome)
	end
isPalindrome:
	addi %x0, 1, %x10			;x10 = 1 (a is Palindrome)
	end
