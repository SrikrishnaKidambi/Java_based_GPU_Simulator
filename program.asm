.data 


.text

addi x11 x0 3
addi x12 x0 3
beq x12 x11 addFun
addi x14 x11 x11
addFun:
	add x13 x11 x12
	mv x10 x11
	li x17 1
	addi x15 x13 8