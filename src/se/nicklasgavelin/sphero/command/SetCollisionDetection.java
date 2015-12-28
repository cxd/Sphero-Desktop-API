package se.nicklasgavelin.sphero.command;

/**
 * Created by cd on 12/09/2015.
 */
public class SetCollisionDetection extends CommandMessage {

    public static enum COLLISION_DETECT_METHOD {
        // 0x00h disabled
        DISABLED(0),
        // 0x01 power impact xmagnitude and ymagnitude
        XY_MAGNITUDE(1),
        // 0x02 acelometer at highest peack of impact a,y,z
        XYZ_ACCELEROMETER(2);

        private int flag = 0;

        private COLLISION_DETECT_METHOD(int f) {
            this.flag = f;
        }

        public int getValue() {
            return this.flag;
        }

    }

    private COLLISION_DETECT_METHOD detectMethod;

    // a axis impact threshold between 100 and 140
    private int xThreshold = 120;
    // a axis impact threshold between 100 and 140
    private int yThreshold = 120;
    // speed threshold is between 0 and 255
    private int xSpeed = 100;
    // speed threshold is between 0 and 255
    private int ySpeed = 100;
    // milliseconds 8bit
    // 10ms steps ie 1000ms = 100 for 1 second
    private int retriggerBuffer = 100;

    public SetCollisionDetection(COLLISION_DETECT_METHOD method) {
        super(COMMAND_MESSAGE_TYPE.SET_COLLISION_DET);
        this.detectMethod = method;
    }

    public SetCollisionDetection(COLLISION_DETECT_METHOD method,
                                 int xt,
                                 int yt,
                                 int xs,
                                 int ys,
                                 int bufTime) {
        super(COMMAND_MESSAGE_TYPE.SET_COLLISION_DET);
        this.detectMethod = method;
        this.xThreshold = xt;
        this.yThreshold = yt;
        this.xSpeed = xs;
        this.ySpeed = ys;
        this.retriggerBuffer = bufTime;
    }

    @Override
    protected byte[] getPacketData() {
        byte[] data = new byte[] {(byte) detectMethod.getValue(),
                (byte)this.xThreshold,
                (byte)this.yThreshold,
                (byte)this.xSpeed,
                (byte)this.ySpeed,
                (byte)this.retriggerBuffer};
        return data;
    }
}
