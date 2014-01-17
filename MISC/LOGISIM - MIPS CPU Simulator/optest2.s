main:

	jal clear
	jal oriTest

	jal clear
	jal jTest

	jal clear
	jal swTest

	jal clear
	lui $r0, 0xaa
	ori $r0, $r0, 0xaa
	disp $r0, 0
	disp $r0, 1
pass: beq $r0, $r0, pass

oriTest:
	lui $r0 0xff
	ori $r1, $r1, 0x08

	srlv $r0, $r0, $r1
	lui $r1, 0x00
	ori $r1, $r1, 0xff
	bne $r0, $r1, fail

	lui $r0, 0x00
	ori $r0, $r0, 0x06
	disp $r0, 0
	jr $r3

jTestFinish:
	lui $r0, 0x00
	ori $r0, $r0, 0x08
	disp $r0, 0
	jr $r3

jTest:
	j jTestFinish
	beq $r0, $r0, fail

swTest:
	addi $r0, $r0, 0
	addi $r1, $r1, 4
	sw $r0 -4($r1)
	addi $r0, $r0, 1
	sw $r0 -3($r1)
	addi $r0, $r0, 1
	sw $r0 -2($r1)
	addi $r0, $r0, 1
	sw $r0 -1($r1)
	addi $r0, $r0, 1
	sw $r0 0($r1)

	lw $r2, 0($r1)
	bne $r2, $r1, fail
	addi $r1, $r1, -1
	lw $r2, 0($r1)
	bne $r2, $r1, fail
	addi $r1, $r1, -1
	lw $r2, 0($r1)
	bne $r2, $r1, fail
	addi $r1, $r1, -1
	lw $r2, 0($r1)
	bne $r2, $r1, fail
	addi $r1, $r1, -1
	bne $r2, $r2, fail

	lui $r0, 0x00
	ori $r0, $r0, 0x0c
	disp $r0, 0
	jr $r3

clear:
	andi $r0, $r0, 0
	andi $r1, $r1, 0
	andi $r2, $r2, 0
	jr $r3

fail: beq $r0, $r0, fail
	