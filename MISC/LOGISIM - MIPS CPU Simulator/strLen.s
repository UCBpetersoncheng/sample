strlen:						# Calculates the string length non-destructively
		andi $r0, $r0, 0
		andi $r1, $r1, 0
		andi $r2, $r2, 0 	# Zero out every register by $r3
		addi $r2, $r2, 1	# Make $r2 for comparison (zb == 1)

count:	
		lw $r1, 0($r0)		# Load first word from mem
		zb $r1, $r1, 0 		
		beq $r1, $r2, null	# If find a zero-byte, proceed
		addi $r0, $r0, 1	# Increment Pointer
		j count

null:
		lw $r1, 0($r0)
		lui $r2, 0xff
		and $r1, $r1, $r2
		andi $r2, $r2, 0
		beq $r1, $r2, zero 	# Branch if first byte is zero
		j one

one:	addi $r2, $r2, 1
zero:	andi $r1, $r1, 0
		addi $r1, $r1, 1
		sllv $r0, $r0, $r1
		add $r0, $r0, $r2
		disp $r0, 0	
		jr $r3
		