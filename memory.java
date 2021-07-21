public class memory {
    private bit[] memory = new bit[8192]; //1024 bytes hardcoded

    public memory() { //constructor that initializes the array of bits to 0 by default
        for(int i = 0; i < memory.length; i++) { 
            memory[i] = new bit(0); 
        }
    }

    public longword read(longword address) { 
        longword result = new longword(); 
        int addressValue = address.getSigned(); 
        int index = 8 * addressValue; //1 byte = 8 bits 
        for(int i = 0; i < 32; i++) { 
            if(memory[i] == null) { //error checking
                System.out.println("Null from read"); 
            }
            result.setBit(i, memory[index]); //reads the value from memory and sets it to the new longword and returns it 
            index++; 
        }
        return result; 
    }

    public void write(longword address, longword value) { 
        int addressValue = address.getSigned(); 
        int index = 8 * addressValue; //1 byte = 8 bits
        for(int i = 0; i < 32; i++) { 
            if(memory[i] == null) { //error checking
                System.out.println("Null from write"); 
            }
            else { //fills up the memory with the value
                if(value.getBit(i).getValue() == 0){ 
                    memory[index].clear(); //set it to 0
                }
                else { 
                    memory[index].set(); //set it to 1
                }
            }
            index++; 
        }
    }
}
