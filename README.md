The Motorola 68000 family of CPUs is a Complex Instruction Set Computer (CISC); ie, there are many formats, lengths, etc. We will consider only four instructions with a single addressing mode. An instruction is assembly language is written like. 

ADD.W       D3,D5

That instruction says to add a "word" (16-bit) value from data register 3 to the value in data register 5. In machine language, the instruction looks like.


Binary                   Hex
1101011001000101         D645


The format of the instructions that we will consider is 

Assembly Language               Machine Language
ADD.s     Dx, Dy                1101xxxsss000yyy
SUB.s     Dx, Dy                1001xxxsss000yyy
MULS.W    Dx, Dy                1100xxx111000yyy
DIVS.W    Dx, Dy                1000xxx111000yyy

where "s" is the size of the data
  000 B      Byte      8 bits
  001 W      Word      16 bits
  010 L      Long      32 bits (note the difference in terminology) and "x" and "y" are register numbers (0 - 7).
