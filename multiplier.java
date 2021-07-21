public class multiplier {

    public static longword multiply(longword a, longword b) {
        int leftShiftAmount = 0; //to track the left shift
        longword arr1 = new longword();
        longword arr2 = new longword();
        longword arr3 = new longword(); 
        longword addition = new longword(); //to store the addition after multiplication
        for(int i = 0; i < 32; i++) { 
            for(int j = 0; j < 32; j++) { 
                if(i == 0) { 
                    int multiplication = b.getBit(i).getValue() * a.getBit(j).getValue(); 
                    arr1.setBit(j, new bit(multiplication)); // when i = 0, store multiplication into arr1 so it can added with arr2 
                } else if(i == 1) { 
                    int multiplication = b.getBit(i).getValue() * a.getBit(j).getValue(); 
                    arr2.setBit(j, new bit(multiplication)); //when i = 1, store multiplication into arr2 so it can be added with arr1
                } else { 
                    int multiplication = b.getBit(i).getValue() * a.getBit(j).getValue(); 
                    arr3.setBit(j, new bit(multiplication)); //store it to arr3 so it can be added to the previously added longword
                }
            }
            if(i == 1) { 
                leftShiftAmount++; 
                arr2 = arr2.leftShift(leftShiftAmount); //leftshift the longword
                addition = rippleAdder.add(arr1, arr2); //when i = 1, add arr1 and arr2 and store it into addition
            } else if(i > 1){ 
                leftShiftAmount++; 
                arr3 = arr3.leftShift(leftShiftAmount); //leftshift the longword
                addition = rippleAdder.add(addition, arr3); //add arr3 to the previously added longword
            }
        }
        return addition; 
    }
}
