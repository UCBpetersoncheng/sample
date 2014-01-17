noop:
	lui $r0, 0xff
	ori $r0, $r0, 0xff
	and $r0, $r0, $r0
	disp $r0, 0
self: beq $r0, $r0, self