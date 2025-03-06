.data 


.text
addi x11 x0 3
addi x12 x11 3
beq x12 x0 addFun
addi x14 x0 10
addFun:
	add x13 x11 x12
	mv x10 x13
	li x17 1
	ecall