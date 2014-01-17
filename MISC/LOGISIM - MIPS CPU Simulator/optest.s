main:

	jal clear
	jal ffoTest

	jal clear
	jal zbTest

	jal clear
	jal bcTest

	jal clear
	jal addiTest

	jal clear
	jal andiTest

	jal clear
	lui $r0, 0xaa
	ori $r0, $r0, 0xaa
	disp $r0, 0
	disp $r0, 1
pass: beq $r0, $r0, pass


ffoTest:
	lui $r0, 0xff
	ori $r0, $r0, 0x00
	ffo $r1, $r0
	bne $r1, $r2, fail
	lui $r0, 0x7f
	ori $r0, $r0, 0x00
	ffo $r1, $r0
	addi $r2, $r2, 1
	bne $r1, $r2, fail
	lui $r0, 0x0f
	ori $r0, $r0, 0x00
	ffo $r1, $r0
	addi $r2, $r2, 3
	bne $r1, $r2, fail
	lui $r0, 0x00
	ori $r0, $r0, 0xff
	ffo $r1, $r0
	addi $r2, $r2, 4
	bne $r1, $r2, fail
	lui $r0, 0x00
	ori $r0, $r0, 0x01
	ffo $r1, $r0
	addi $r2, $r2, 7
	bne $r1, $r2, fail
	lui $r0, 0x00
	ori $r0, $r0, 0x00
	ffo $r1, $r0
	addi $r2, $r2, 1
	bne $r1, $r1, fail

	lui $r0, 0x00
	ori $r0, $r0, 0x01
	disp $r0, 0
	jr $r3


zbTest:
	
	lui $r0, 0xff
	ori $r0, $r0, 0xff
	zb $r1, $r0
	bne $r1, $r2, fail
	lui $r0, 0xff
	ori $r0, $r0, 0xf0
	zb $r1, $r0
	beq $r1, $r2, fail
	lui $r0, 0xf0
	ori $r0, $r0, 0xff
	zb $r1, $r0
	beq $r1, $r2, fail
	lui $r0, 0x81
	ori $r0, $r0, 0x81
	zb $r1, $r0
	bne $r1, $r2, fail

	lui $r0, 0x00
	ori $r0, $r0, 0x02
	disp $r0, 0
	jr $r3

bcTest:
	lui $r2, 0x00
	ori $r2, $r2, 0x10
	lui $r0, 0xff
	ori $r0, $r0, 0xff
	bc $r1, $r0
	bne $r1, $r2, fail
	lui $r0, 0x7f
	ori $r0, $r0, 0xff
	bc $r1, $r0
	addi $r2, $r2, -1
	bne $r1, $r2, fail
	lui $r0, 0x0f
	ori $r0, $r0, 0xff
	bc $r1, $r0
	addi $r2, $r2, -3
	bne $r1, $r2, fail
	lui $r0, 0x00
	ori $r0, $r0, 0xff
	bc $r1, $r0
	addi $r2, $r2, -4
	bne $r1, $r2, fail
	lui $r0, 0x00
	ori $r0, $r0, 0x01
	bc $r1, $r0
	addi $r2, $r2, -7
	bne $r1, $r2, fail
	lui $r0, 0x00
	ori $r0, $r0, 0x00
	bc $r1, $r0
	addi $r2, $r2, -1
	bne $r1, $r1, fail


	lui $r0, 0x00
	ori $r0, $r0, 0x03
	disp $r0, 0
	jr $r3

addiTest:
	lui $r0, 0x00
	ori $r0, $r0, 0x04

	addi $r2, $r2, 4
	bne $r0, $r2, fail

	addi $r2, $r2, -4
	bne $r1, $r2, fail

	lui $r0, 0x00
	ori $r0, $r0, 0x04
	disp $r0, 0
	jr $r3

andiTest:
	lui $r0, 0xff
	ori $r0, $r0,  0xff
	lui $r1, 0x00
	ori $r1, $r1, 0x0f
	andi $r0, $r0, 0x0f
	bne $r0, $r1, fail
	lui $r0, 0xf0
	ori $r0, $r0, 0xf0
	andi $r0, $r0, 0x00
	lui $r1, 0x00
	ori $r1, $r1, 0x00
	bne $r0, $r1, fail

	lui $r0, 0x00
	ori $r0, $r0, 0x05
	disp $r0, 0
	jr $r3 


clear:
	andi $r0, $r0, 0
	andi $r1, $r1, 0
	andi $r2, $r2, 0
	jr $r3

fail: beq $r0, $r0, fail
	