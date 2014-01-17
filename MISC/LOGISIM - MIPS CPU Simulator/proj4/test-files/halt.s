         lui $r0, 0x33          D033

         ori $r0, $r0, 0x44     6044

         lui $r1, 0x33          D133

         ori $r1, $r1, 0x44     6544

   self: beq $r0, $r1, self     A1FF