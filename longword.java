import java.util.*;

public class longword implements ILongword {
    private bit[] longword = new bit[32];

    public longword() {
        for (int j = 0; j < 32; j++) {
            longword[j] = new bit();
        }
    }

    // get the bit that is in position i
    public bit getBit(int i) {
        return longword[(31 - i)];
    }

    // sets the value of bit in location i
    public void setBit(int i, bit value) {
        int index = 31 - i;
        longword[index].set(value.getValue());
    }

    // performs "and" on the two longwords and returns new one
    public longword and(longword other) {
        longword newlongword = new longword();
        int index = 31;
        for (int i = 0; i < 32; i++) {
            newlongword.setBit(index, longword[i].and(other.longword[i]));
            index--;
        }

        return newlongword;
    }

    // performs "or" on the two longwords and returns a new one
    public longword or(longword other) {
        longword newlongword = new longword();
        int index = 31;
        for (int i = 0; i < 32; i++) {
            newlongword.setBit(index, longword[i].or(other.longword[i]));
            index--;
        }

        return newlongword;
    }

    // performs "xor" on the two longwords and returns a new one
    public longword xor(longword other) {
        longword newlongword = new longword();
        int index = 31;
        for (int i = 0; i < 32; i++) {
            newlongword.setBit(index, longword[i].xor(other.longword[i]));
            index--;
        }
        return newlongword;
    }

    public longword not() {
        bit nbit = new bit();
        longword newlongword = new longword();
        int index = 31;
        for (int i = 0; i < 32; i++) {
            newlongword.setBit(index, longword[i].not());
            index--;
        }
        for (int j = 0; j < 32; j++) {
            if (newlongword.getBit(j).getValue() == 0) {
                nbit.set();
                newlongword.setBit(j, nbit);
                break;
            } else {
                if (newlongword.getBit(j).getValue() == 1) {
                    nbit.clear();
                    newlongword.setBit(j, nbit);
                }
            }
        }
        return newlongword;
    }

    public longword rightShift(int amount) {
        longword newlongword = new longword();
        int num = 31;
        int var;
        for (int i = 0; i < amount; i++) {
            newlongword.longword[i].clear();
            num--;
        }
        for (int index = amount; index < 32; index++) {
            var = index - amount;
            newlongword.setBit(num, longword[var]);
            num--;
        }
        return newlongword;
    }

    public longword leftShift(int amount) {
        longword newlongword = new longword();
        int var;
        int num = 0;
        for (int i = 31; i > 31 - amount; i--) {
            newlongword.longword[i].clear();
            num++;
        }
        for (int index = 31 - amount; index >= 0; index--) {
            var = index + amount;
            newlongword.setBit(num, longword[var]);
            num++;
        }
        return newlongword;
    }

    @Override
    public String toString() {
        String strLongword = null;
        for (int i = 0; i < longword.length; i++) {
            if (strLongword == null) {
                strLongword = String.valueOf(longword[i].getValue()) + ",";
            } else if (i == longword.length - 1) {
                strLongword = strLongword + String.valueOf(longword[i].getValue());
            } else {
                strLongword = strLongword + String.valueOf(longword[i].getValue()) + ",";
            }
        }
        return strLongword;
    }

    public long getUnsigned() {
        long newlongword = 0;
        long var = 1;
        for (int i = 31; i >= 0; i--) {
            if (longword[i].getValue() == 1) {
                newlongword = newlongword + var;
            }
            var = var * 2;
        }
        return newlongword;
    }

    public int getSigned() {
        int newlongword = 0;
        int var = 1;
        if (longword[0].getValue() == 0) {
            for (int i = 31; i >= 0; i--) {
                if (longword[i].getValue() == 1) {
                    newlongword = newlongword + var;
                }
                var = var * 2;
            }
        } else {
            for (int i = 31; i >= 0; i--) {
                if (longword[i].not().getValue() == 1) {
                    newlongword = newlongword + var;
                }
                var = var * 2;
            }
            newlongword = newlongword * -1;
            newlongword -= 1;
        }
        return newlongword;
    }

    // copies the values of the bits from another longword into this one
    public void copy(longword other) {
        for (int i = 0; i < 32; i++) {
            if (other.longword[i].getValue() == 1) {
                longword[i].set();
            } else {
                longword[i].clear();
            }
        }
    }

    public void set(int value) {
        if (value >= 0) {
            int var = value;
            int tempVar;
            for (int i = 31; i >= 0; i--) {
                tempVar = var % 2;
                if (tempVar == 1) {
                    longword[i].set();
                } else {
                    longword[i].clear();
                }
                if (var == 1)
                    var = 0;
                else
                    var = var / 2;
            }
        } else {
            int var = value * -1;
            int tempVar;
            for (int i = 31; i >= 0; i--) {
                tempVar = var % 2;
                if (tempVar == 1)
                    longword[i].clear();
                else
                    longword[i].set();
                if (var == 1)
                    var = 0;
                else
                    var = var / 2;
            }
            if (longword[31].getValue() == 1) {
                for (int j = 31; j >= 0; j--) {
                    longword[j].clear();
                    if (longword[j - 1] != null && longword[j - 1].getValue() == 1) {
                        continue;
                    } else {
                        longword[j - 1].set();
                        break;
                    }
                }
            } else
                longword[31].set();
        }
    }
}
