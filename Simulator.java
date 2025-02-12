public class Simulator{
    public Simulator(){
        memory=new Memory();
        clock=0;
        cores=new Cores[4];
        for(int i=0;i<4;i++){
            cores[i]=new Cores(i);
        }
        //testing purpose
        cores[0].registers[2]=1;
        cores[0].registers[3]=2;
        cores[1].registers[2]=1;
        cores[1].registers[3]=2;
        cores[2].registers[2]=1;
        cores[2].registers[3]=2;
        cores[3].registers[2]=1;
        cores[3].registers[3]=2;
    }
    public void initializeProgram(String[] program){
        this.program_Seq=program;
    }
    public void runProgram(){
        while(this.clock<program_Seq.length){
            for(int i=0;i<4;i++){
                this.cores[i].execute(program_Seq);
            }  
            this.clock++; 
        }
    }
    public void printResult(){
        for(int i=0;i<4;i++){
            System.out.println("Core :"+i);
            for(int j=0;j<32;j++){
                System.out.print(cores[i].registers[j]+" ");
            }
            System.out.println();
        }
    }
    private Memory memory;
    private int clock;
    private Cores[] cores;
    public String[] program_Seq;
}