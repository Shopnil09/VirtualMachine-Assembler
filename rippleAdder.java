public class rippleAdder {

    public static longword subtract(longword word1, longword word2) {
        rippleAdder rippleAdd = new rippleAdder();
        longword finalValue = new longword();
        // Using add method to subtract
        // .not() methods negates everything and adds 1 (Two's Compliment)
        if (word1.getSigned() >= 0) {
            if (word2.getSigned() >= 0) // if both longwords are +ve
                finalValue = rippleAdd.add(word1, word2.not());
            else
                finalValue = rippleAdd.add(word1, word2.not());

        } else { // if word1 is negative
            if (word2.getSigned() >= 0) {
                finalValue = rippleAdd.add(word1.not(), word2);
                finalValue = finalValue.not();
            } else
                finalValue = rippleAdd.add(word1, word2.not());
        }
        return finalValue;
    }

    public static longword add(longword word1, longword word2) {
        int SIZE = 32;
        bit newBit = new bit();
        longword finalValue = new longword();
        int extraValue = 0; // value to hold carry
        for (int i = 0; i < SIZE; i++) { // loop to run through all the cases covering all of them
            if (word1.getBit(i).getValue() == 1) {
                if (word2.getBit(i).getValue() == 1) { // if both longwords are 1
                    if (extraValue == 1) {
                        newBit.set();
                        finalValue.setBit(i, newBit);
                    } else {
                        newBit.clear();
                        finalValue.setBit(i, newBit);
                        extraValue = 1;
                    }
                } else {
                    if (word2.getBit(i).getValue() == 0) { // if word1 is one and word2 is zero
                        if (extraValue == 1) { // if there is carry or not
                            newBit.clear();
                            finalValue.setBit(i, newBit);
                            extraValue = 1;
                        } else { // no carry
                            newBit.set();
                            finalValue.setBit(i, newBit);
                            extraValue = 0;
                        }
                    }
                }
            } else { // if word1 is not 1
                if (word1.getBit(i).getValue() == 0) {
                    if (word2.getBit(i).getValue() == 1) { // if word1 is 0 and word2 is 1
                        if (extraValue == 1) { // if there is carry or not
                            newBit.clear();
                            finalValue.setBit(i, newBit);
                            extraValue = 1;
                        } else { // no carry
                            newBit.set();
                            finalValue.setBit(i, newBit);
                            extraValue = 0;
                        }
                    } else { // if both longwords are zero
                        if (word2.getBit(i).getValue() == 0) {
                            if (extraValue == 1) { // seeing if there is carry
                                newBit.set();
                                finalValue.setBit(i, newBit);
                                extraValue = 0;
                            } else { // no carry
                                newBit.clear();
                                finalValue.setBit(i, newBit);
                                extraValue = 0;
                            }
                        }
                    }
                }
            }
        }
        return finalValue;
    }

}
