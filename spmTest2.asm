.text


li x8 400 # size of cache
li x9 4  # size of the word 
li x8 100 # x=(size of the cache)/(size of the word)

spmLoad:
    li x19 0
    bne CID 0 loadFor1
    addi x2 x0 0 # i=0 
    addi x3 x0 25 # upper bound of number of elements
    li x28 0 # loading the base address of the spm  
    Loop0_spm:
        beq x2 x3 loadFor1
        lw x27 0(x19)
        sw_spm x27 0(x28)  # store the loaded value in the spm
        addi x19 x19 400   # incrementing the pointer of the array
        addi x28 x28 4     # incrementing the pointer of the spm address
        addi x2 x2 1  # incrementing the iterator.
        j Loop0_spm
    
    loadFor1:
        bne CID 1 loadFor2
        addi x19 x0 10000 # starting with an offset for each core
        addi x2 x0 0 # i=0 
        addi x3 x0 25 # upper bound of number of elements 
        li x28 0 # loading the base address of the spm
        Loop1_spm:
            beq x2 x3  loadFor2
            lw x27 0(x19) 
            sw_spm x27 0(x28)  # storing the loaded value in the spm
            addi x19 x19 400    # incrementing the pointer of the array 
            addi x28 x28 4  # incrementing the pointer of the spm address
            addi x2 x2 1  # incrementing the iterator
            j Loop1_spm
    loadFor2:
        bne CID 2 loadFor3
        addi x19 x0 20000 # starting with an offset
        addi x2 x0 0 # i=0 
        addi x3 x0 25 # upper bound of the total number of elements
        li x28 0 # loading the base address of the spm
        Loop2_spm:
            beq x2 x3 loadFor3 
            lw x27 0(x19)  
            sw_spm x27 0(x28)  # storing the loaded value in the spm
            addi x19 x19 400 
            addi x28 x28 4 
            addi x2 x2 1  # incrementing the iterator
            j Loop2_spm
    loadFor3:
        bne CID 3 Done_loadingSpm
        addi x19 x0 30000
        addi x2 x0 0 # i=0 
        addi x3 x0 25 # upper bound
        li x28 0 
        Loop3_spm:
            beq x2 x3 Done_loadingSpm
            lw x27 0(x19)
            sw_spm x27 0(x28)  # storing the loaded value in the spm
            addi x19 x19 400 
            addi x28 x28 4
            addi x2 x2 1  # incrementing the iterator
            j Loop3_spm

Done_loadingSpm:

addi x21 x0 0
addi x22 x0 10

Loop:
    beq x21 x22 finish
    jal x1 Add
    addi x21 x21 1
    j Loop

Add:
    bne CID 0 afterSum0
    li x18 0
    addi x26 x0 0 #i=0
    addi x27 x0 25 #counter max val
    Loop0:
        beq x26 x27 done0
        lw_spm x25 0(x18)
        add x31 x31 x25
        addi x18 x18 4
        addi x26 x26 1
        j Loop0
    done0:
        j afterSum0

    afterSum0:
    SYNC
    bne CID 1 afterSum1
    li x18 0
    addi x26 x0 0 #i=0
    addi x27 x0 25 #counter max val
    Loop1:
        beq x26 x27 done1
        lw_spm x25 0(x18)
        add x31 x31 x25
        addi x18 x18 4
        addi x26 x26 1
        j Loop1
    done1:
        j afterSum1
    
    afterSum1:
    SYNC
    bne CID 2 afterSum2
    li x18 0
    addi x26 x0 0 #i=0
    addi x27 x0 25 #counter max val
    Loop2:
        beq x26 x27 done2
        lw_spm x25 0(x18)
        add x31 x31 x25
        addi x18 x18 4
        addi x26 x26 1
        j Loop2
    done2:
        j afterSum2
    
    afterSum2:
    SYNC
    bne CID 3 afterSum3
    li x18 0
    addi x26 x0 0 #i=0
    addi x27 x0 25 #counter max val
    Loop3:
        beq x26 x27 done3
        lw_spm x25 0(x18)
        add x31 x31 x25
        addi x18 x18 4
        addi x26 x26 1
        j Loop3
    done3:
        j afterSum3

    afterSum3:
        SYNC
        jr x1


finish:
    SYNC
    bne CID 0 mark1
    sw x31 90000(x0)

    mark1:
    SYNC
    bne CID 1 mark2
    sw x31 90004(x0)

    mark2:
    SYNC
    bne CID 2 mark3
    sw x31 90008(x0)

    mark3:
    SYNC
    bne CID 3 preproces
    sw x31 90012(x0)
preproces:
SYNC
j storingDone

storingDone:
SYNC
bne CID 0 Finish
lw x22 90000(x0)
lw x23 90004(x0)
lw x24 90008(x0)
lw x25 90012(x0)
add x23 x22 x23
add x24 x24 x23
add x25 x25 x24
sw x25 90000(x0)
li x17 1
lw x10 90000(x0)
ecall

Finish: