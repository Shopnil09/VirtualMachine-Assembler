
public class computer {
    private memory Cmemory; 
    private bit halted; 
    
    longword PC = new longword(); 
  
    longword currentInstruction = new longword(); 
    
    longword[] registers = new longword[16]; //array of 16 longwords; the register

    longword SP = new longword(); //stack pointer
    bit[] stackInstruction = new bit[2]; //for the program to identify push, pop, call, or return
    bit[] callAddr = new bit[10]; //to store call address
    boolean isCalled = false; //for return 

    longword op1 = new longword(); 
    longword op2 = new longword(); 
    longword result = new longword(); 
    bit[] opcode = new bit[4]; 
    bit[] reg1 = new bit[4]; 
    bit[] reg2 = new bit[4];  
    bit[] regInstruction = new bit[4]; 
    
    bit[] moveValue = new bit[8]; 
    bit[] newAddress = new bit[12]; 

    bit[] branchIndicatorCompare = new bit[2]; //to store two from compare
    bit[] branchIndicator = new bit[2]; //to store two bits from branch 
    longword bAddress = new longword(); //address for branch
    boolean branchMatchesCompare = false; //to check if branch matches compare in order to branch
    boolean ifGreaterThanOrEqualTo = false; //to check if branch if greater than or equal to
    bit branchSign; //branch sign to indicate if it is a forward or a backword branch
    

    public computer() {  
        halted = new bit(1); //zero indicating that it is halted
        PC.set(0); //PC defaults to zero 
        SP.set(1020); //initializing stack pointer to 1020 
        Cmemory = new memory(); //initializing the memory
        for(int i = 0; i < registers.length; i++) { //initializing all the registers to 0
            registers[i] = new longword(); 
        }
    }

    public void run() { 
        while(halted.getValue() != 0) { 
            fetch(); 
            decode();
            execute(); 
            store(); 
        }
    }

    public void fetch() { 
        longword increment = new longword(); //longword to increment PC by 2 in rippleAdder
        increment.set(2); //setting it to the current incrementValue 
        currentInstruction = Cmemory.read(PC); //reads from memory with address PC and stores it in currentInstruction 
        PC.copy(rippleAdder.add(PC, increment)); //incrementing PC using rippleAdder so the next instruction from memory can be fetched */
    }

    public void decode() {
        for(int i = 0; i < 4; i++) { //storing the first 4 bits into opcode 
            if(currentInstruction.getBit(i).getValue() == 0) { 
                opcode[i] = new bit(0); 
            } else { 
                opcode[i] = new bit(1); 
            }
        }
        currentInstruction = currentInstruction.rightShift(4); //shifting to get the next set of 4 bits

        if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 0 && opcode[3].getValue() == 1) { //if opcode is 0001 move
            for(int j = 0; j < 4; j++) { //storing the next 4 bits into register 1
                if(currentInstruction.getBit(j).getValue() == 0) { 
                    reg1[j] = new bit(0); 
                } else { 
                    reg1[j] = new bit(1); 
                }
            }
            currentInstruction = currentInstruction.rightShift(4);

            for(int x = 0; x < 8; x++) { //store the 8 remaining bits into array called moveValue[]
                if(currentInstruction.getBit(x).getValue() == 0) { 
                    moveValue[x] = new bit(0); 
                } else { 
                    moveValue[x] = new bit(1); 
                }
            }
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 1 && opcode[3].getValue() == 1) { //opcode 0011 for jump
            //if opcode is jump  
            for(int i = 0; i < 12; i++) { //get the 12 remaining bits 
                if(currentInstruction.getBit(i).getValue() == 0) { 
                    newAddress[i] = new bit(0); 
                } else { 
                    newAddress[i] = new bit(1); 
                }
            }
            currentInstruction = currentInstruction.rightShift(12); //right shift to get the following the bits

        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 0 && opcode[3].getValue() == 0) { 
            //if opcode is compare
            currentInstruction = currentInstruction.rightShift(4); //to get rid of the 4 zero bits and get to the registers

            for(int i = 0; i < 4; i++) { //store the value of the first regisyer
                if(currentInstruction.getBit(i).getValue() == 0) { 
                    reg1[i] = new bit(0); 
                } else { 
                    reg1[i] = new bit(1); 
                }
            }
            currentInstruction = currentInstruction.rightShift(4); //shift to get to the other register

            for(int j = 0; j < 4; j++) { //store the value of the second register 
                if(currentInstruction.getBit(j).getValue() == 0) { 
                    reg2[j] = new bit(0); 
                } else { 
                    reg2[j] = new bit(1); 
                }
            }
            currentInstruction = currentInstruction.rightShift(4); //shift to get the next instruction

        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 0 && opcode[3].getValue() == 1) { 
            //if opcode is branch 
            bit[] bAddr = new bit[9]; 
            //storing the type of branch into branchIndicator bit array 
            branchIndicator[0] = currentInstruction.getBit(0); 
            branchIndicator[1] = currentInstruction.getBit(1); 

            //getting the sign of the branch and storing it in branchSign
            if(currentInstruction.getBit(2).getValue() == 0) { 
                branchSign = new bit(0); 
            } else { 
                branchSign = new bit(1); 
            }
            currentInstruction = currentInstruction.rightShift(3); //shifting to get the address
        
            for(int i = 0; i < bAddr.length; i++) { //storing the branch address into bAddr 
                if(currentInstruction.getBit(i).getValue() == 0) { 
                    bAddr[i] = new bit(0); 
                } else { 
                    bAddr[i] = new bit(1); 
                }
            }
            bAddress = getLongword(bAddr); //getting the longword of the branch address
            currentInstruction = currentInstruction.rightShift(9); //shifting to get the next instruction
    
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 1 && opcode[3].getValue() == 0) { 
            //either push, pop, call, or return - 0110
            if(currentInstruction.getBit(0).getValue() == 0 && currentInstruction.getBit(1).getValue() == 0) { // 0110 00 - push 
                //indicator to use in execute method
                stackInstruction[0] = new bit(0); 
                stackInstruction[1] = new bit(0); 

                currentInstruction = currentInstruction.rightShift(8); //get the 4 bits for register
                for(int i = 0; i < 4; i++) { 
                    if(currentInstruction.getBit(i).getValue() == 0) {
                        reg1[i] = new bit(0); 
                    } else { 
                        reg1[i] = new bit(1); 
                    }
                }

            } else if(currentInstruction.getBit(0).getValue() == 0 && currentInstruction.getBit(1).getValue() == 1) { // 0110 01 - pop 
                //indicator to use in execute method 
                stackInstruction[0] = new bit(0); 
                stackInstruction[1] = new bit(1);

                currentInstruction = currentInstruction.rightShift(8); //get the 4 bits for register
                for(int i = 0; i < 4; i++) { 
                    if(currentInstruction.getBit(i).getValue() == 0) {
                        reg1[i] = new bit(0); 
                    } else { 
                        reg1[i] = new bit(1); 
                    }
                }

            } else if(currentInstruction.getBit(0).getValue() == 1 && currentInstruction.getBit(1).getValue() == 0) { //0110 - 10 - call 
                //indicator to use in execute method
                stackInstruction[0] = new bit(1); 
                stackInstruction[1] = new bit(0); 
                currentInstruction = currentInstruction.rightShift(2); //rightshift to get the address 

                for(int i = 0; i < callAddr.length; i++) { //setting the address for call 
                    if(currentInstruction.getBit(i).getValue() == 0) { 
                        callAddr[i] = new bit(0); 
                    } else { 
                        callAddr[i] = new bit(1); 
                    }
                }
            } else { //0110 - 11 - return 
                //indicator to use in execute method and do nothing
                stackInstruction[0] = new bit(1); 
                stackInstruction[1] = new bit(1); 
            }
        } else { 
            //CODE FOR OTHER CASES
            for(int j = 0; j < 4; j++) { //storing the next 4 bits into register 1
                if(currentInstruction.getBit(j).getValue() == 0) { 
                    reg1[j] = new bit(0); 
                } else { 
                    reg1[j] = new bit(1); 
                }
            }
            currentInstruction = currentInstruction.rightShift(4);

            for(int k = 0; k < 4; k++) { //storing the next 4 bits into register 2
                if(currentInstruction.getBit(k).getValue() == 0) { 
                    reg2[k] = new bit(0); 
                } else { 
                    reg2[k] = new bit(1); 
                }
            }
            currentInstruction = currentInstruction.rightShift(4);
    
            for(int x = 0; x < 4; x++) { //storing the last 4 bits into instruction register
                if(currentInstruction.getBit(x).getValue() == 0) { 
                    regInstruction[x] = new bit(0); 
                } else { 
                    regInstruction[x] = new bit(1); 
                }
            
            }
            currentInstruction = currentInstruction.rightShift(4); 

            op1 = registers[getLongword(reg1).getSigned()]; //gets the item indicated by bit instruction of reg1 and stores it in op1
            op2 = registers[getLongword(reg2).getSigned()]; //gets the item indicated by bit instruction of reg2 and stores it in op2
        }
    }

    public void execute() { 
        //PART 1 - HALT!
        if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 0 && opcode[3].getValue() == 0) { //if opcode is 0000
            halted.set(0); //if halt instruction is 0000, computer should not be looping
            return; 
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 0 && opcode[3].getValue() == 1) { //if opcode is 0001 (move)
            move(); //calls the helper method to move the value into the specified register
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 1 && opcode[3].getValue() == 0) { //if opcode is 0010 - interrupt
            if(regInstruction[0].getValue() == 0 && regInstruction[1].getValue() == 0 && regInstruction[2].getValue() == 0 && regInstruction[3].getValue() == 1){ 
                int spaceCounter = 0; //for formatting purposes 
                longword address = new longword(); 
                longword increment = new longword(); 
                longword memHolder = new longword(); 
                increment.set(1);
                address.set(0);
                System.out.println(); 
                System.out.println("Printing memory: "); 
                System.out.println();
                //loop to print memory
                for(int i = 0; i < 1021; i++) {  
                    memHolder = Cmemory.read(address); 
                    if(i == 1020) { //to avoid index out of bound exception
                        int byteCounter = 0; 
                        for(int x = 31; x >= 0; x--) { 
                            if(byteCounter == 8) { 
                                System.out.print(" "); 
                                byteCounter = 0; 
                            }
                            System.out.print(memHolder.getBit(x).getValue()); 
                            byteCounter++; 
                        }
                    } else { 
                        for(int j = 7; j >= 0; j--) {  
                            System.out.print(memHolder.getBit(j).getValue()); 
                        }
                        if(spaceCounter == 15) { //for formatting purposes
                            System.out.println(); 
                            spaceCounter = 0; 
                        } else { //for formatting purposes 
                            System.out.print(" "); 
                            spaceCounter++; 
                        }
                        address = rippleAdder.add(address, increment); 
                    }
                } 
            } else { 
                //System.out.println(); 
                System.out.println("Printing register: "); 
                System.out.println(); 
                //loop to print registers
                for(int i = 0; i < 16; i++) { 
                    if(i < 10) { 
                        System.out.println("Register " + i + ":  " + registers[i].toString() + " = " + registers[i].getSigned()); 
                    } else { 
                        System.out.println("Register " + i + ": " + registers[i].toString() + " = " + registers[i].getSigned()); 
                    }
                }
                System.out.println();
            }
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 1 && opcode[3].getValue() == 1) {
            //if opcode is jump
            return; //no operation to be done for jump in execute()

        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 0 && opcode[3].getValue() == 0){ 
            //0100 opcode for compare
            //get the values from the registers specified in compare 
            longword reg1Value = registers[getLongword(reg1).getSigned()]; 
            longword reg2Value = registers[getLongword(reg2).getSigned()]; 
            
            longword difference = rippleAdder.subtract(reg1Value, reg2Value); //get the difference of the values e.g. x-y

            if(difference.getSigned() == 0) { //if x-y is 0, branchIndicatorCompare is [1,1] (x == y)
                branchIndicatorCompare[0] = new bit(1); 
                branchIndicatorCompare[1] = new bit(1); 
                ifGreaterThanOrEqualTo = true; //for branchifGreaterThanOrEqual
            } else if(difference.getSigned() > 0) { //x-y > 0, branchIndicatorCompare is [1,0] (x > y)
                branchIndicatorCompare[0] = new bit(1); 
                branchIndicatorCompare[1] = new bit(0);
                ifGreaterThanOrEqualTo = true; //for branchifGreaterThanOrEqual
            } else { //x-y < 0, branchIndicatorCompare is [0,0] (x != y)
                branchIndicatorCompare[0] = new bit(0); 
                branchIndicatorCompare[1] = new bit(0); 
                ifGreaterThanOrEqualTo = false; //for branchifGreaterThanOrEqual
            } 

        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 0 && opcode[3].getValue() == 1){
            if(branchIndicator[0].getValue() == 0 && branchIndicator[1].getValue() == 1 && ifGreaterThanOrEqualTo == true) { 
                //the statement checks if the branchIndictor is 01 and if branchIfGreaterOrEqualTo is true 
                //if both are equal, it means BranchIfGreaterThanOrEqual matches the two bits in compare
                branchMatchesCompare = true; 
            } else { 
                //if it does not, the two bits in compare is compared to the two bits in branch,
                if(branchIndicatorCompare[0].getValue() == branchIndicator[0].getValue() && branchIndicatorCompare[1].getValue() == branchIndicator[1].getValue()){ 
                    //if true, branchMatchesCompare is true
                    branchMatchesCompare = true; 
                } else { 
                    //if not, branchMatchesCompare is false
                    branchMatchesCompare = false; 
                }
            }
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 1 && opcode[3].getValue() == 0) {
            //push, pop, call, or return in execute 
            if(stackInstruction[0].getValue() == 0 && stackInstruction[1].getValue() == 0) { //push in execute 
                longword value = registers[getLongword(reg1).getSigned()]; //get the longword from the register 
                Cmemory.write(SP, value); //push it into memory
                SP.set(SP.getSigned()-4); //subtract SP by 4
                System.out.println("stack pointer after push: " + SP.getSigned()); 
            } else if(stackInstruction[0].getValue() == 0 && stackInstruction[1].getValue() == 1) { //pop in execute
                longword zero = new longword(); 
                zero.set(0);
                SP.set(SP.getSigned()+4); //adding 4 to access the longword that was pushed 
                registers[getLongword(reg1).getSigned()] = Cmemory.read(SP); //store pop into specified register
                Cmemory.write(SP, zero); //overwriting it as zero to pop 
                System.out.println("This is memory at SP: " + registers[getLongword(reg1).getSigned()].getSigned()); 
                System.out.println("stack pointer for pop: " + SP.getSigned()); 
            } else if(stackInstruction[0].getValue() == 1 && stackInstruction[1].getValue() == 0) { 
                longword nextPC = PC; //PC is currently incremented and stored in nextPC
                System.out.println("PC that will get written into memory in call: " + nextPC.getSigned()); 
                System.out.println("Stack pointer in call: " + SP.getSigned()); 
                Cmemory.write(SP, nextPC); //pushing the next PC into memory
                //set PC to call address
                longword callAddress = getLongword(callAddr); //changing PC to call address 
                PC.set(callAddress.getSigned());
                System.out.println("PC is set to call address: " + PC.getSigned()); 
                SP.set(SP.getSigned()-4); //subtracting SP by 4
                System.out.println("SP after push in call: " + SP.getSigned()); 
                isCalled = true; //for return 
            } else if(stackInstruction[0].getValue() == 1 && stackInstruction[1].getValue() == 1) { //return in execute
                if(isCalled == true) { //if there was call before return
                    longword zero = new longword(); 
                    zero.set(0); 
                    SP.set(SP.getSigned()+4); //adding 4 to access the longword that was pushed
                    System.out.println("Stack pointer for pop in return: " + SP.getSigned()); 
                    longword newPC = Cmemory.read(SP); //getting the previous PC
                    Cmemory.write(SP, zero); //overwriting the stack as zero
                    PC.set(newPC.getSigned()); //setting the previous PC to current PC in return
                    System.out.println("PC is set in return to: " + PC.getSigned()); 
                    isCalled = false; //for return so it does not cause an infinite loop
                } else { //no call before return
                    System.out.println("returned nothing, because there was no call before");
                    System.out.println(); 
                    return; //do nothing because there was no call before return
                     
                }
            }
        } else { 
            result = ALU.doOperation(opcode, op1, op2); //passes the opcode to ALU along with op1 and op2 from decode()
        }
    }

    public void store() { 
        if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 0 && opcode[3].getValue() == 1) { 
            return; //item already moved in execute so nothing to do here 
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 0 && opcode[3].getValue() == 0) { //if 0000
            return; //do nothing
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 0 && opcode[2].getValue() == 1 && opcode[3].getValue() == 1){ 
            //if the opcode is jump
            longword lwNewAddress = getLongword(newAddress); //getting the longword of the address
            PC.copy(lwNewAddress); //jumping to the new address 
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 0 && opcode[3].getValue() == 0) { 
            //if opcode is compare, do nothing
            return; 
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 0 && opcode[3].getValue() == 1) { 
            //if the opcode is branch
            if(branchMatchesCompare == true) { //if compare two bits and branch two bits are same
                if(branchSign.getValue() == 0) { //it means address is positive 
                    longword two = new longword(); 
                    two.set(2);
                    PC.copy(rippleAdder.subtract(PC, two)); //subtract by two because PC is already incremented to avoid double increment 
                    PC.copy(rippleAdder.add(PC, bAddress)); //add to the current PC and branch
                } else { //means it was negative
                    longword two = new longword(); 
                    two.set(2);
                    PC.copy(rippleAdder.subtract(PC, two)); //subtract by two because PC is already incremented to avoid double increment
                    int newAddy = bAddress.getSigned(); 
                    longword newBranchAddr = new longword(); 
                    newBranchAddr.set(-newAddy);
                    PC.copy(rippleAdder.add(PC, newBranchAddr)); //if it is negative, adding to the current PC will subtract
                    if(PC.getSigned() < 0) { //error check if PC goes below zero
                        System.out.println("PC is less than 0 after branch"); 
                        System.out.println("Setting PC to zero"); 
                        PC.set(0);
                    }
                }
            } else { //if it compare two bits and branch are not same, do nothing and return
                return; 
            }
        } else if(opcode[0].getValue() == 0 && opcode[1].getValue() == 1 && opcode[2].getValue() == 1 && opcode[3].getValue() == 0) { //0110 
            return; //nothing to do in store in store for push, pop, call, return
        } else { 
            registers[getLongword(regInstruction).getSigned()] = result; //stores the result from execute() into register indicated by regInstruction
        }
         
    }

    public longword getLongword(bit[] value)  { //helper function to get the longword of the bits
        if(value.length == 8) { 
            int newlongword = 0;
            int var = 1;
            if (value[0].getValue() == 0) {
                for (int i = value.length-1; i >= 0; i--) {
                    if (value[i].getValue() == 1) {
                        newlongword = newlongword + var;
                    }
                    var = var * 2;
                }
            } else {
                for (int i = value.length-1; i >= 0; i--) {
                    if (value[i].not().getValue() == 1) {
                        newlongword = newlongword + var;
                    }
                    var = var * 2;
                }
                newlongword = newlongword * -1;
                newlongword -= 1;
            }
            longword result = new longword(); 
            result.set(newlongword);
            //System.out.println("longword signed is: " + newlongword); 
            return result; 

        } else { 
            int index = value.length-1; 
            longword result = new longword(); 
            for(int i = 0; i < value.length; i++) { 
                if(value[i].getValue() == 0) { 
                    result.setBit(index, new bit(0));
                } else { 
                    result.setBit(index, new bit(1));
                }
                index--; 
            } 
            return result; 
        }
    }

    public void preload(String[] preloadStr) { 
        longword increment = new longword(); 
        increment.set(4); //incrementing address by 4 to get the next 32 bits
        longword address = new longword(); 
        address.set(0);
        longword preload = new longword();
        int lwIndex = 0; 
        for(int i = 0; i < preloadStr.length; i++) { 
            String word = preloadStr[i].replaceAll("\\s", ""); //removes the spaces 
            for(int j = 0; j < word.length(); j++) { 
                int value = Character.getNumericValue(word.charAt(j)); 
                preload.setBit(lwIndex, new bit(value));
                if(lwIndex == 31) { 
                    lwIndex = 0; 
                    Cmemory.write(address, preload); //writes the longword to the address 
                    preload.set(0); //sets the longword to 0
                    address = rippleAdder.add(address, increment); //increments the address by 4
                } else if(lwIndex == 15 && i == preloadStr.length-1) { 
                    lwIndex = 0; 
                    Cmemory.write(address, preload); //writes the longword to the address
                    preload.set(0); //sets the longword to 0
                    address = rippleAdder.add(address, increment); //increments by the address by 4
                } else { 
                    lwIndex++; 
                }
            }
        }
    }

    public void move() { //helper method to move a value to a specific register 
        longword longwordValue = getLongword(moveValue); //calls the helper function
        registers[getLongword(reg1).getSigned()] = longwordValue; //moves longwordValue to the register specified by reg1
    }
}
