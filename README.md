# Java_based_GPU_Simulator- Dual Core Krishnas
This is a project to simulate the GPU architecture . 
## Documentation:
- This is Simulator built using Java.
- Clone the repo.
- To run the program in the teminal type:
```terminal
javac SimulatorGUI.java
java SimulatorGUI
```
### Phase - 1
- Check the java output in teminal and GUI is also implemented for displaying the registers of cores and memory of the simulator.
- Use the drop down to see the registers of each cores and the memory accessed by each core . You can also use the drop down to switch between the display type among hex, binary and signed.
- We implemented the instructions "add","sub","mul","mv","addi","muli","and","or","xor","andi","ori","xori","bne","beq","jal","jalr","lw","sw","la","li","bge","blt","j","jr","ecall".
- The instructions which we implemented of our choice is "rem" which calculates the modulo.
- We implemented the text and data segment .

### Phase - 2

- In this phase, all the instruction execution takes place in a pipelined fashion. 

---
### Minutes of Meeting:

#### Date : 2

#### Date : 24-02-2025

Members : Srikrishna, Sai krishna
Tasks : Understanding the brief overview of phase 2 and thinking about the brief method of implementation.

#### Date : 19-02-2025

Members : Srikrishna, Sai krishna
Decisions : Decided the layout the UI and planned to implement the editor section, console, registers display, navigation to memory display and saving the assembly file and some error handling
Tasks:
    Srikrishna: Editor, registers display for cores, navigation to memory, file saving
    Sai krishna: Console, Error display on console, Memory display, Run logic

#### Date : 18-02-2025

Members : Srikrishna, Sai Krishna
Tasks : Parsing the entire assembly code including text segment and data segment. Allowing the user to have the data segment only at the top of the program. Above parsing was implemented together from two different devices. Minor bug fixes and error handling.

Decisions : Decided to use Java swing for the GUI support. 

#### Date : 14-02-2025

Members : Srikrishna, Sai Krishna

Decisions : Parsing the instructions seperately based on op codes. Using Hash map for mapping the labels to corresponding instructions.
The above implementation was done together.
Work Division : Srikrishna : Implementation of BEQ, JAL, LW, Li, J.
                Sai Krishna : Implementation of BNE, JALR, SW, JR.


#### Date : 08-02-2025
Members : Srikrishna, Sai Krishna
Work Division : Srikrishna : Cores class and arithmetic operators implementation
                Sai Krishna : Simulator class and logical operators implementation

#### Date : 06-02-2025
Members : Srikrishna, Sai Krishna.
Decisions : Java programming language is used for building the GPU simulator due to its extensive support for Object Oreinted Programming. Along with the prescribed instructions in the given file, the immediate insructions we want to implement tentatively are addi(add immediate), li(load immediate). As it is prescibed that an additonal instruction of our choice can be implemented, we want to implement the multiply instruction (along with multiply immediate instruction, tentatively). Pseudo Instructions include RISC-V instructions j,jr,mv.
