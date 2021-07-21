public class bit implements IBit {
    private int value; // the bit

    public bit() {
        this.value = 0;
    }

    public bit(int v) {
        this.value = v;
    }

    // set the value of the bit
    public void set(int value) {
        if (value == 0) {
            this.value = value;
        } else {
            this.value = value;
        }
    }

    // change value from 0 to 1 and 1 to 0
    public void toggle() {
        if (this.value == 1) {
            this.value = 0;
        } else {
            this.value = 1;
        }
    }

    // sets the bit to 1
    public void set() {
        this.value = 1;
    }

    // sets the bit to 0
    public void clear() {
        this.value = 0;
    }

    // returns the current value
    public int getValue() {
        return value;
    }

    // performs "and" on two bits and returns a new bit set to the finalbit
    // @Override
    public bit and(bit other) {
        bit finalbit = new bit();
        if (value == 0) {
            finalbit.clear();
        } else {
            if (other.getValue() == 0) {
                finalbit.clear();
            } else
                finalbit.set();
        }
        return finalbit;
    }

    // performs "or" on tewo bits and returns a new set to the finalbit
    public bit or(bit other) {
        bit finalbit = new bit();

        if (value == 1) {
            finalbit.set();
        } else {
            if (other.getValue() == 1) {
                finalbit.set();
            } else
                finalbit.clear();
        }

        return finalbit;
    }

    // performs "xor" on teo bits and returns a new bit set to the finalbit
    public bit xor(bit other) {
        bit finalbit = new bit();
        if (value == 1) {
            if (other.getValue() == 0)
                finalbit.set();
            else
                finalbit.clear();
        } else {
            if (other.getValue() == 1)
                finalbit.set();
            else
                finalbit.clear();
        }
        return finalbit;
    }

    // performs not"on the existing bit, returing the finalbit as a new bit
    public bit not() {
        bit finalbit = new bit();
        if (value == 1) {
            finalbit.clear();
        } else {
            finalbit.set();
        }

        return finalbit;
    }

    @Override
    public String toString() {
        return value + "";
    }

}
