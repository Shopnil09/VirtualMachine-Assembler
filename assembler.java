
public class assembler { 

    public static String[] assemble(String[] instruction) { 
        String registerValue = "", regValue1 = "", regValue2 = "", regValue3 = "", numberBits = "", instructionBits = "";  
        String[] instructionArr = new String[instruction.length]; //array to store the string instruction in bits
        for(int x = 0; x < instruction.length; x++) { 
            String[] splitInstruct = instruction[x].split(" "); //splitting the string using white spaces and storing it in an array
            if(splitInstruct.length == 3) { //string has three commands like "move r1 -1" 
                int registerCounter = 1; 
                for(int i = 0; i < splitInstruct.length; i++) { 
                    if(splitInstruct[i].toLowerCase().charAt(0) == 'r' && Character.isDigit(splitInstruct[i].charAt(1)) == true) { //if the string is a register 
                        if(splitInstruct[0].equalsIgnoreCase("compare")) { 
                            //System.out.println("compare instruction"); 
                            if(registerCounter == 1) {
                                regValue1 = getRegister(splitInstruct[i].substring(1)); 
                                registerCounter++; 
                            } else { 
                                regValue2 = getRegister(splitInstruct[i].substring(1)); 
                            }
                        } else { 
                            registerValue = getRegister(splitInstruct[i].substring(1)); //getting the number in bits of the register value by using the substring method ex. r10 will be 10 using substring
                        }
                    } else if(isNumeric(splitInstruct[i]) == true) { //if the string is a number - using helper method to determine if the string is a number
                        numberBits = getNumberEightBits(splitInstruct[i].toLowerCase()); //getting the number in bits format using helper method
                    } else { //if the string is an instruction like "move", "add", "subtract", "multiply", etc. 
                        instructionBits = getInstruction(splitInstruct[i].toLowerCase()); //getting the opcode using helper method
                    }
                }

                if(registerValue.equalsIgnoreCase("invalid") || numberBits.equalsIgnoreCase("invalid") || instructionBits.equalsIgnoreCase("invalid")) { //error checking 
                    instructionArr[x] = "invalid instruction"; //if one of the code is invalid, the string is invalid
                    numberBits = ""; //reset the numberBits string to empty space
                    regValue1 = ""; 
                    regValue2 = ""; 
                } else { //if it is not invalid, store the bit in the array 
                    if(splitInstruct[0].equalsIgnoreCase("compare")) { 
                        //System.out.println("compare putting all together"); 
                        //System.out.println("compare: " + instructionBits + " " + regValue1 + regValue2);  
                        instructionArr[x] = instructionBits + " " + regValue1 + " " + regValue2; 
                    } else { 
                        instructionArr[x] = instructionBits + " " + registerValue + " " + numberBits; 
                    }
                }
            } else if(splitInstruct.length == 4) { //if the string has 4 commands like "add r1 r2 r3"
                int registerCounter = 1; //to keep track of the register number conversion 
                for(int i = 0; i < splitInstruct.length; i++) { 
                    //System.out.println(splitInstruct[i]); 
                    if(splitInstruct[i].toLowerCase().charAt(0) == 'r' && Character.isDigit(splitInstruct[i].charAt(1)) == true) { //if string is a register
                        //keeping track of the register number conversion using counter
                        if(registerCounter == 1) { 
                            regValue1 = getRegister(splitInstruct[i].substring(1)); 
                            registerCounter++;
                        } else if(registerCounter == 2) { 
                            regValue2 = getRegister(splitInstruct[i].substring(1));
                            registerCounter++; 
                        } else {
                            regValue3 = getRegister(splitInstruct[i].substring(1)); 
                        }
                    } else if(isNumeric(splitInstruct[i]) == true) { //using helper method to determine if string is a number
                        numberBits = getNumberFourBits(splitInstruct[i].toLowerCase()); //converting number into bits using helper method
                    } else { //if the instruction is an opcode
                        instructionBits = getInstruction(splitInstruct[i].toLowerCase()); //getting the bits for the specific opcode
                    }
                }
                if(regValue1.equalsIgnoreCase("invalid") || regValue2.equalsIgnoreCase("invalid") || regValue3.equalsIgnoreCase("invalid") || numberBits.equalsIgnoreCase("invalid") || instructionBits.equalsIgnoreCase("invalid")) { 
                    instructionArr[x] = "invalid instruction"; //error checking - if one of the variables is invalid, the whole instruction is invalid
                } else  { 
                    instructionArr[x] = instructionBits + " " + regValue1 + " " + regValue2 + " " + regValue3; //store the bits string in the array if the instruction is not invalid
                }
            } else if(splitInstruct.length == 2){ //if string length is one, it is either halt or interrupt 
                if(splitInstruct[0].equalsIgnoreCase("interrupt") == true) { //if it is an interrupt
                    if(splitInstruct[1].equalsIgnoreCase("0")) { //interrupt 0
                        instructionArr[x] = "0010 0000 0000 0000"; 
                    } else { //interrupt 1
                        instructionArr[x] = "0010 0000 0000 0001"; 
                    }
                } else if(splitInstruct[0].toLowerCase().charAt(0) == 'b'){ //for branch
                    instructionArr[x] = getInstruction(splitInstruct[0]).concat(getBranchAddress(splitInstruct[1]));
                } else { 
                    if(splitInstruct[0].equalsIgnoreCase("push")) { 
                        String op = getInstruction(splitInstruct[0]); 
                        //System.out.println(splitInstruct[1].substring(1) + " is the push register"); 
                        String pushRegister = getNumberFourBits(splitInstruct[1].substring(1)); 
                        instructionArr[x] = op + " " + pushRegister; 
                    } else if(splitInstruct[0].equalsIgnoreCase("pop")) { 
                        String op = getInstruction(splitInstruct[0]); 
                        //System.out.println(splitInstruct[1].substring(1) + " is the push register"); 
                        String popRegister = getNumberFourBits(splitInstruct[1].substring(1)); 
                        instructionArr[x] = op + " " + popRegister; 
                    } else if(splitInstruct[0].equalsIgnoreCase("call")) { 
                        String op = getInstruction(splitInstruct[0]); 
                        String callAddr = getNumberTenBitsAddr(splitInstruct[1]); 
                        instructionArr[x] = op.concat(callAddr); 
                    } else { 
                        //jump
                        String op = getInstruction(splitInstruct[0]); 
                        String addr = getNumberTwelveBits(splitInstruct[1]); 
                        instructionArr[x] = op + " " + addr; 
                    }
                }
            } else if(splitInstruct.length == 1) { //return 
                instructionArr[x] = getInstruction(splitInstruct[0].toLowerCase());
            } else { //if none of the if statements match, it means that string is invalid
                instructionArr[x] = "invalid command"; 
            }
        }
        return instructionArr; //return array with bits 
    }

    public static String getInstruction(String value) { //helping method to get the bits for the opcode
        String instructionBits = ""; 
        switch(value.toLowerCase()) { 
            case "and": 
                instructionBits = "1000"; 
                break; 
            case "or": 
                instructionBits = "1001"; 
                break; 
            case "xor": 
                instructionBits = "1010"; 
                break; 
            case "not": 
                instructionBits = "1011";
                break; 
            case "leftshift":
                instructionBits = "1100";
                break;
            case "rightshift":
                instructionBits = "1101";
                break;
            case "add":
                instructionBits = "1110";
                break;
            case "subtract":
                instructionBits = "1111";
                break;
            case "multiply":
                instructionBits = "0111";
                break;
            case "move":
                instructionBits = "0001"; 
                break;
            case "branchifequal": 
                instructionBits = "0101 11"; 
                break; 
            case "branchifgreaterthanorequal": 
                instructionBits = "0101 01"; 
                break; 
            case "branchifgreaterthan": 
                instructionBits = "0101 10"; 
                break; 
            case "branchifnotequal": 
                instructionBits = "0101 00"; 
                break; 
            case "jump": 
                instructionBits = "0011"; 
                break; 
            case "compare": 
                instructionBits = "0100 0000"; 
                break; 
            case "push": 
                instructionBits = "0110 0000 0000"; 
                break; 
            case "pop": 
                instructionBits = "0110 0100 0000"; 
                break; 
            case "call": 
                instructionBits = "0110 10"; 
                break; 
            case "return": 
                instructionBits = "0110 1100 0000 0000"; 
                break; 
            case "halt":
                instructionBits = "0000 0000 0000 0000"; 
                break; 
            default: //if the string does not match, it is an invalid opcode
                instructionBits = "invalid";  
        }
        return instructionBits; //return the string 
    }

    public static String getRegister(String value){ //helping method to get bits for register
        String registerBits = ""; 
        longword bits = new longword(); 
        bits.set(Integer.parseInt(value)); 
        if(Integer.parseInt(value) > 15 || Integer.parseInt(value) < 0) { //error checking - register value can be between 0-15
            registerBits = "invalid"; 
        } else { 
            for(int i = 3; i >= 0; i--) { 
                registerBits = registerBits + String.valueOf(bits.getBit(i)); //setting the bits to the string
            }
        }
        return registerBits; 
    }

    public static String getNumberFourBits(String value) { //helper method to get the 4 bits of the number - for string with 4 commands 
        String numBits = ""; 
        longword bits = new longword(); 
        bits.set(Integer.parseInt(value)); 
        if(Integer.parseInt(value) > 15 || Integer.parseInt(value) < 0) { //error checking  
            numBits = "invalid"; 
        } else { 
            for(int i = 3; i >= 0; i--) { 
                numBits = numBits + String.valueOf(bits.getBit(i)); //setting the bits to the string
            }
        }
        return numBits; 
    }

    public static String getNumberEightBits(String value) { //helper method to get 8 bits of the number - for string with 8 commands 
        String numberBits = ""; 
        longword bits = new longword(); 
        bits.set(Integer.parseInt(value)); 
        if(Integer.parseInt(value) > 255 || Integer.parseInt(value) < -128) { //error checking 8 bits cannot go over 255 or go below -128
            numberBits = "invalid"; 
        } else { 
            for(int i = 7; i >= 0; i--) { //loop to set the bits to the string 
                if(i == 3) { //to include space in between the string
                    numberBits = numberBits + " " + String.valueOf(bits.getBit(i));
                } else { 
                    numberBits = numberBits + String.valueOf(bits.getBit(i)); 
                }
            }
        }
        return numberBits; 
    }

    public static String getNumberTenBitsAddr(String value) { 
        String numberBits = ""; 
        longword bits = new longword(); 
        bits.set(Integer.parseInt(value));
        for(int i = 9; i >= 0; i--) { 
            if(i == 7 || i == 3) { 
                numberBits = numberBits + " " + String.valueOf(bits.getBit(i)); 
            } else { 
                numberBits = numberBits + String.valueOf(bits.getBit(i)); 
            }
        }
        return numberBits; 
    }

    public static String getNumberTwelveBits(String value) { 
        String numberBits = ""; 
        longword bits = new longword(); 
        bits.set(Integer.parseInt(value)); 
        for(int i = 11; i >= 0; i--) { 
            if(i == 3 || i == 7) { //to include space in between the string - formatting
                numberBits = numberBits + " " + String.valueOf(bits.getBit(i));
            } else { 
                numberBits = numberBits + String.valueOf(bits.getBit(i)); 
            }
        }
        return numberBits;     
    }

    public static boolean isNumeric(String value) { //helper method to determine if the string is a number
        try {
            int num = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String getBranchAddress(String value){ 
        String addressBits = ""; 
        longword bits = new longword(); 
        //change code
        if(Integer.parseInt(value) < 0) { 
            String positiveNum = value.substring(1); 
            bits.set(Integer.parseInt(positiveNum));
            addressBits = "1"; 
            for(int i = 8; i >= 0; i--) { //loop to set the bits to the string 
                if(i == 3 || i == 7) { //to include space in between the string
                    addressBits = addressBits + " " + String.valueOf(bits.getBit(i));
                } else { 
                    addressBits = addressBits + String.valueOf(bits.getBit(i)); 
                }
            }

        } else { 
            bits.set(Integer.parseInt(value));
            addressBits = "0"; 
            for(int i = 8; i >= 0; i--) { //loop to set the bits to the string 
                if(i == 3 || i == 7) { //to include space in between the string
                    addressBits = addressBits + " " + String.valueOf(bits.getBit(i));
                } else { 
                    addressBits = addressBits + String.valueOf(bits.getBit(i)); 
                }
            }
        }
        return addressBits;
    }
}