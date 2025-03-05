.data 


.text
addi x11 x0 3
addi x12 x0 3
beq x0 x0 addFun
addi x14 x0 10
addFun:
	add x13 x11 x12
	mv x10 x11
	li x17 1