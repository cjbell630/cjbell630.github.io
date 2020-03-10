public class MarioBot {
    private byte[] seed;

    public MarioBot(byte[] seed) {

    }

    public void react(byte objID, short distance, byte zone) {
        //mario objID 0
        if (distance < getThreshold()) {

        }
    }

    public short getThreshold(byte objID) {
        //Byte modulo 16 is the low nibble, byte div 16 is the high nibble.
        //concat bytes b1 and b2: b1 << 8 | b2
        return (short) (seed[1] << 8 | (seed[2] % 16));
    }
}
