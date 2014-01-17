octal:
		andi $r0, $r0, 0
		andi $r1, $r1, 0
		andi $r2, $r2, 0
		j convert

convert:
		lw $r0, 0($r0)
		lui $r1, 0x01
		ori $r1, $r1, 0xc0
		and $r0, $r0, $r1
		andi $r1, $r1, 0
		ori $r1, $r1, 2
		sllv $r0, $r0, $r1
		or $r2, $r0, $r2

		andi $r0, $r0, 0
		andi $r1, $r1, 0
		lw $r0, 0($r0)
		ori $r1, $r1, 0x38
		and $r0, $r0, $r1
		andi $r1, $r1, 0
		ori $r1, $r1, 1
		sllv $r0, $r0, $r1
		or $r2, $r0, $r2

		andi $r0, $r0, 0
		andi $r1, $r1, 0
		lw $r0, 0($r0)
		ori $r1, $r1, 0x07
		and $r0, $r0, $r1
		or $r2, $r0, $r2

		disp $r2, 0

		jr $r3

