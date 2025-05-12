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

## Assumptions:
- Block size of L1 and L2 caches are same.
- Bandwidth is sufficient to get the block completely into cache(L1 and L2 too).
### Phase - 1
- Check the java output in teminal and GUI is also implemented for displaying the registers of cores and memory of the simulator.
- Use the drop down to see the registers of each cores and the memory accessed by each core . You can also use the drop down to switch between the display type among hex, binary and signed.
- We implemented the instructions "add","sub","mul","mv","addi","muli","and","or","xor","andi","ori","xori","bne","beq","jal","jalr","lw","sw","la","li","bge","blt","j","jr","ecall".
- The instructions which we implemented of our choice is "rem" which calculates the modulo.
- We implemented the text and data segment .

### Phase - 2

- In this phase, all the instruction execution takes place in a pipelined fashion. Also RAW hazard checking, pipeline forwarding were implemented. 
- Please note that when using branch instruction with CID please use upper case CID instead of lower case CID.

### Phase - 3
- In this phase, all the memory access take place with variable latencies. This is because of the introduction of caches at two levels L1 and L2 each having different latencies which are different from the main memory.
- The configuration of the cache can be found in the cacheConfig.java file where we cn change the configurations manually and compile the SimulatorGUI.java file.
- THe replacement policies implemented are SRRIP and LRU policy. Here we are using the LRU replacement policy as default which can also be changed in the same cacheConfig file.
- Some of the assembly codes are present in the repo that can be used as sample test cases for testing the working of the simulator.

---
### Minutes of Meeting:



#### Date : 11-05-2025

Members : Srikrishna , Sai krishna
Tasks: Tried to understand Scratch Pad Memory (SPM). Asked TA and decided to implenent the spm private for each cache. It will be an array of values similar to memory with limited capacity and less latency
Also initialised testing process of the simulator.

#### Date : 10-05-2025

Members : Srikrishna , Sai krishna
Tasks: Thought about how to implement the SYNC functionality that is hardware realisable. 

#### Date : 9-05-2025

Members : Srikrishna , Sai krishna
Tasks: Resumed the project again after a long gap. This time started with implementaion of the writing to the cache using the sw. Modified the flow of the sw so that before storing into the memory the value is updated in the cache. Completed the implementation of the LRU policy that will take care of the evicting a block from the cache when there is a conflict or capacity miss. Also as a part of implementing a replacement policy of our choice we decided to implement SRRIP replacement policy.

#### Date : 19-04-2025

Members : Srikrishna , Sai krishna
Tasks: Completed the implementation of the lw functionality that will try to load from the cache before going to access the main memory. Also fixed some of the bugs that were present in the previous codes.

#### Date : 14-04-2025

Members : Srikrishna , Sai krishna
Tasks: Extended the implementation of the cache to handle the misses when trying to read or write to the cache. Also we decided to have a seperate class for handling all the memory access that which will act as a high level class that will take care of the cache hits and misses.


#### Date : 13-04-2025

Members : Srikrishna , Sai krishna
Tasks: Completed the initial reading of the phase 3 document and then started the implementation of the L1 and L2 caches. Initially, decided to implement using hash maps that store the tag as key and value as the block. But to ensure the efficiecy of computation, we decided to implement the 2D cache as a single dimensional array though it is mathematically intensive. With the above design we also implemented the process of reading data from the cache.

#### Date : 10-03-2025

Members : Srikrishna , Sai krishna
Tasks: Unified the memory for all the cores(previously in phase1 we divided memory) and implemented GUI for latencies, option for pipeline forwarding and complete memory display.
#### Date : 9-03-2025

Members : Srikrishna , Sai krishna
Tasks: Implemented single IF unit for all the cores and implenmented branch instruction with CID to decide which cores to execute.
#### Date : 8-03-2025

Members : Srikrishna , Sai krishna
Decisions: We decided to check the previous three instructions for hazards detection and used pipeline registers in the instruction class to store the fowarded values.
Tasks: Implemented the pipeline forwarding stalls and values fowarding using the logic similar to w/o forwarding that is checking prev 3 instructions for dependencies and forwarding them.
#### Date : 6-03-2025

Members : Srikrishna , Sai krishna
Tasks: Fixed some bugs in raw hazards detection without pipeline fowarding. 
#### Date : 5-03-2025

Members : Srikrishna , Sai krishna
Decisions: We realised that the previous implementation do not work out and decided to check the previous three instructions and decided to call the raw hazards detector before the execution stage and for branches before the ID/RF.
Tasks: Implemented the control hazards after branch instructions and then implemented stalls due to raw hazards by implementing according to the decision made(now it is success 😊).

#### Date : 3-03-2025

Members : Srikrishna , Sai krishna
Decision: Decided to take the asm code given by the programmer and decided to possible hazards using the program array and stored them in a map(but it failed due to loops).
Task: Tried out to implement the above thing but things did not work out due to loops.
#### Date : 2-03-2025

Members : Srikrishna , Sai krishna
Decision: Decided to put stalls before execute stage for simulating the latencies as execution is the bottle neck for arithmetic operations generally.
Tasks: Implemented pipelining and as part of that we tried out various datastructures available in java and finally sticked to linked list and figured out how to push stalls into the pipeline. Worked on latencies.
#### Date : 1-03-2025

Members : Srikrishna , Sai krishna
Decisions: We decided to use to class for instructions to store all the important meta data about an instruction. We decided mark the instructions in the pipeline that are not meant for execution like stalls as dummy instructions.
Tasks: We broke down our phase 1 execution of our instruction into stages IF, ID/RF, EX, MEM, WB. We implemented instruction class.

#### Date : 28-02-2025

Members : Srikrishna , Sai krishna
Decisions: Tried to understand the implementation of pipelining and brainstormed ideas and thought of ideas like implementing using threads etc., and finally decided to implement pipelining similar to functionality of a queue.

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
