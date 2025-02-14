# Java_based_GPU_Simulator- Dual Core Krishnas
This is a project to simulate the GPU architecture . 

### Minutes of Meeting:

#### Date : 14-02-2025

Members : Srikrishna, Sai Krishna

Decisions : Parsing the instructions seperately based on op codes. Using Hash map for mapping the labels to corresponding instructions.
The above implementation was done together.
Work Division : Srikrishna : Implementation of BEQ, JAL, LW, Li, J, LA.
                Sai Krishna : Implementation of BNE, JALR, SW, JR.


#### Date : 08-02-2025
Members : Srikrishna, Sai Krishna
Work Division : Srikrishna : Cores class and arithmetic operators implementation
                Sai Krishna : Simulator class and logical operators implementation
#### Date : 06-02-2025
Members : Srikrishna, Sai Krishna.
Decisions : Java programming language is used for building the GPU simulator due to its extensive support for Object Oreinted Programming. Along with the prescribed instructions in the given file, the immediate insructions we want to implement tentatively are addi(add immediate), li(load immediate). As it is prescibed that an additonal instruction of our choice can be implemented, we want to implement the multiply instruction (along with multiply immediate instruction, tentatively). Pseudo Instructions include RISC-V instructions j,jr,mv.