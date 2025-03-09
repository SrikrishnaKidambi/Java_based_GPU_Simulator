.text
addi x14 x0 10 
addi x16 x0 19
beq CID 2 add
addi x10 x0 9
add:
	add x13 x14 x16 
	mv x10 x13
	li x17 1
	ecall 