public class cpu_test3 {

    public void runTests() throws Exception { 
        System.out.println(); 
        //test case 1
       String[] instructionStr1 = new String[] {"move r1 5", "move r2 10", "push r1", "push r2", "pop r3", "pop r4", "interrupt 0", "halt"};
       String[] instructionBits = assembler.assemble(instructionStr1); 
       System.out.println("Test Case 1: ");
       System.out.println(); 
       System.out.print("Instructions: "); 
       for(int i = 0; i < instructionStr1.length; i++) { 
           System.out.print(instructionStr1[i] + ", "); 
       }
       System.out.println(); 
       System.out.println("Solution: r1 should have 5; r2 should have 10; r3 should have 10 (because of pop and LIFO); r4 should have 5 (because of pop and LIFO)"); 
       System.out.println(); 
       computer test1 = new computer(); 
       test1.preload(instructionBits);
       test1.run(); 

       //test case 2
       String[] instructionStr2 = new String[] {"move r4 5", "move r5 10", "move r6 15", "push r4", "push r5", "pop r8", "push r6", "pop r9", "pop r10", "interrupt 0", "halt"};
       instructionBits = assembler.assemble(instructionStr2); 
       System.out.println("Test Case 2: ");
       System.out.println(); 
       System.out.print("Instructions: "); 
       for(int i = 0; i < instructionStr2.length; i++) { 
           System.out.print(instructionStr2[i] + ", "); 
       }
       System.out.println(); 
       System.out.println("Solution: r4 should have 5; r5 should have 10; r6 should have 15; r8 should have 10 (because of pop and LIFO); r9 should have 15 because of previous push and pop; r10 should have 5"); 
       System.out.println(); 
       computer test2 = new computer(); 
       test2.preload(instructionBits);
       test2.run(); 

       //test case 3 
       String[] instructionStr3 = new String[] {"move r1 10", "move r2 12", "call 8", "move r3 14", "move r4 8", "return", "interrupt 0", "halt"};
       instructionBits = assembler.assemble(instructionStr3); 
       System.out.println("Test Case 3: ");
       System.out.println(); 
       System.out.print("Instructions: "); 
       for(int i = 0; i < instructionStr3.length; i++) { 
           System.out.print(instructionStr3[i] + ", "); 
       }
       System.out.println(); 
       System.out.println("Solution: r1 should have 10; r2 should have 12; call happens and r4 should have 8; returns and r3 should have 14"); 
       System.out.println(); 
       computer test3 = new computer(); 
       test3.preload(instructionBits);
       test3.run(); 

       //test case 4 
       String[] instructionStr4 = new String[] {"move r5 2", "move r6 4", "call 12", "move r7 6", "move r8 6", "add r7 r8 r9", "move r10 9", "return", "interrupt 0", "halt"};
       instructionBits = assembler.assemble(instructionStr4); 
       System.out.println("Test Case 4: ");
       System.out.println(); 
       System.out.print("Instructions: "); 
       for(int i = 0; i < instructionStr4.length; i++) { 
           System.out.print(instructionStr4[i] + ", "); 
       }
       System.out.println(); 
       System.out.println("Solution: r5 should have 2; r6 should have 4; call happens r10 should have 9; returns and r7 and r8 should have 6 and r9 should have 12"); 
       System.out.println(); 
       computer test4 = new computer(); 
       test4.preload(instructionBits);
       test4.run(); 

       //test case 5
       String[] instructionStr5 = new String[] {"move r2 2", "call 6", "move r1 -1", "return", "interrupt 0", "halt"};
       instructionBits = assembler.assemble(instructionStr5); 
       System.out.println("Test Case 5: ");
       System.out.println(); 
       System.out.print("Instructions: "); 
       for(int i = 0; i < instructionStr5.length; i++) { 
           System.out.print(instructionStr5[i] + ", "); 
       }
       System.out.println(); 
       System.out.println("Solution: r2 should have 2; call happens which takes the PC to return; return happens and r1 shoould have -1");
       System.out.println(); 
       computer test5 = new computer(); 
       test5.preload(instructionBits);
       test5.run(); 

       //test case 6 
       String[] instructionStr6 = new String[] {"move r0 10", "move r1 -1", "return", "interrupt 0", "halt"};
       instructionBits = assembler.assemble(instructionStr6); 
       System.out.println("Test Case 5: ");
       System.out.println(); 
       System.out.print("Instructions: "); 
       for(int i = 0; i < instructionStr6.length; i++) { 
           System.out.print(instructionStr6[i] + ", "); 
       }
       System.out.println(); 
       System.out.println("Solution: return should not pop and return address because there was no call before return"); 
       System.out.println(); 
       computer test6 = new computer(); 
       test6.preload(instructionBits);
       test6.run(); 
    }
}
