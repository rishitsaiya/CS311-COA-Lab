	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x4		; x4 = n
	subi %x0, 0, %x10		; x10 = 0
	addi %x10, 0, %x5		; x5 = x10	(i.e. j = i) j is inner loop, i is outer loop
loop:
	beq %x10, %x4, moveOut	; if x4 == x10 (ie. n = i), end prog
	load %x10, $a, %x11		; x11 = a[x10] (ie. x11 = a[i])
	load %x5, $a, %x6		; x6 = a[x5]   (ie. x6 = a[j])
	bgt %x6, %x11, swap		; if x6 > x11  (ie. a[j] > a[i]),then swap
increment:
	addi %x5, 1, %x5		; j += 1
	beq %x5, %x4, shift		; if j == n, increase i, set j = i
	jmp loop
shift:
	addi %x10, 1, %x10		; x10 += 1 (i.e. i += 1)
	addi %x10, 0, %x5		; x5 = x10 (i.e. j = i)
	jmp loop
swap:
	addi %x6, 0, %x28		; x28 = x6
	addi %x11, 0, %x6		; x6 = x11
	addi %x28, 0, %x11		; x11 = x28
	store %x11, $a, %x10	; a[x10] = x11 (After swap between x11 and x6)
	store %x6, $a, %x5		; a[x5] = x6 (After swap between x11 and x6)
	jmp increment
moveOut:
	end