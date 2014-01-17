         lui $r0, 0x33          #033
         disp $r0, 0 			#F000
         ori $r0, $r0, 0x44     #6044
         disp $r0, 1			#F401
         lui $r1, 0x33          #D133
         disp $r1, 1 			#F400
         ori $r1, $r1, 0x44     #6544
         disp $r1, 0 			#F400
   self: beq $r0, $r1, self     #A1FF