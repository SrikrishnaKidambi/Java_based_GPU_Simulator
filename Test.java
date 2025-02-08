public class Test {
    public static void main(String[] args) {
        Cores core1= new Cores(1);
        String[] program={"Add X1 X2 X3","ADDi X2 X3 100"};
        core1.registers[3]=3;
        core1.registers[2]=1;
        for(int ele: core1.registers){
            System.out.print(ele+" ");
        }
        System.out.println();
        core1.execute(program);
        for(int ele: core1.registers){
            System.out.print(ele+" ");
        }
        System.out.println();
        core1.execute(program);
        for(int ele: core1.registers){
            System.out.print(ele+" ");
        }
        System.out.println();
    }
}
