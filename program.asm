.data 


.text
addi x8 x0 8
addi x11 x0 8
beq x11 x8 addFun
addi x14 x8 0
addFun:
	add x13 x11 x12
	mv x10 x11
	li x17 1
	addi x15 x13 8