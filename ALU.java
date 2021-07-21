public class ALU {
    
    public static longword doOperation(bit[] operation, longword a, longword b) { 
        //storing the values 
        int num1 = operation[0].getValue(), num2 = operation[1].getValue(), num3 = operation[2].getValue(), num4 = operation[3].getValue(); 
        if(num1 == 1 && num2 == 0 && num3 == 0 && num4 == 0) { //1000 and operation
            longword and = a.and(b); 
            return and; 
        } else if(num1 == 1 && num2 == 0 && num3 == 0 && num4 == 1) { //1001 or operation
            longword or = a.or(b); 
            return or; 
        } else if(num1 == 1 && num2 == 0 && num3 == 1 && num4 == 0) { //1010 xor operation
            longword xor = a.xor(b); 
            return xor; 
        } else if(num1 == 1 && num2 == 0 && num3 == 1 && num4 == 1) { //1011 not operation for longword a
            longword not = a.not(); 
            return not; 
        } else if(num1 == 1 && num2 == 1 && num3 == 0 && num4 == 0) { //1100 left shift operation
            int amount = getShiftValue(b);
            longword leftShift = a.leftShift(amount); 
            return leftShift; 
        } else if(num1 == 1 && num2 == 1 && num3 == 0 && num4 == 1) { //1101 right shift operation
            int amount = getShiftValue(b); 
            longword rightShift = a.rightShift(amount); 
            return rightShift; 
        } else if(num1 == 1 && num2 == 1 && num3 == 1 && num4 == 0) { //1110 add operation on longword a and longword b
            longword add = rippleAdder.add(a, b); 
            return add; 
        } else if(num1 == 1 && num2 == 1 && num3 == 1 && num4 == 1) { //1111 subtract operation on longword a and longword b
            longword subtract = rippleAdder.subtract(a, b); 
            return subtract; 
        } else if(num1 == 0 && num2 == 1 && num3 == 1 && num4 == 1){ //0111 multiplication operation longword a and longword b
            longword multiplication = multiplier.multiply(a, b); 
            return multiplication; 
        } else { 
            System.out.println("Invalid operation!"); 
            return null; //returns null and null pointer exception
        }
    }

    public static int getShiftValue(longword b) { //calculating the unsigned value of the lowest 5 bits
        int value = 0; 
        int powerIndex = 4; //keeping track of the power 
        for(int i = 4; i >= 0; i--) { 
            value = (int) (value + (b.getBit(i).getValue() * Math.pow(2, powerIndex))); 
            powerIndex--; 
        }
        return value; 
    }
}
