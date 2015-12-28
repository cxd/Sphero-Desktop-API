package se.nicklasgavelin.sphero.command;

/**
 * Set data streaming to received data from the Sphero device.
 * For masks perform bitwise or to get multiple sensor values.
 * <p/>
 * Masks are found under the class SetDataStreamingCommand.DATA_STREAMING_MASKS
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class SetDataStreamingCommand extends CommandMessage {
    // Internal storage
    private int mDivisor, mPacketFrames, mSensorMask, mPacketCount, mSensorMask2 = 0;

    /**
     * Create a data streaming command without first setting the mask.
     * Call the .addMask to add more masks to the packet. If no call to
     * .addMask is performed before this packet is sent the mask that is set
     * is the OFF mask (that will turn a current streaming off)
     *
     * @param mDivisor      Divisor to divide the default sampling rate of 400
     *                      Hz
     * @param mPacketFrames Number of frames per packet
     * @param mPacketCount  Number of packets to receive from the moment data is
     *                      started being captured
     */
    public SetDataStreamingCommand(int mDivisor, int mPacketFrames, int mPacketCount) {
        this(mDivisor, mPacketFrames, 0, mPacketCount, 0);
    }

    /**
     * Create a data streaming command
     *
     * @param mDivisor      Divisor to divide the default sampling rate of 400
     *                      Hz
     * @param mPacketFrames Number of frames per packet
     * @param mSensorMask   Sensor mask for, bitwise or for multiple sensor
     *                      values
     * @param mPacketCount  Number of packets to receive from the moment data is
     *                      started being captured
     */
    public SetDataStreamingCommand(int mDivisor, int mPacketFrames, int mSensorMask, int mPacketCount, int mSensorMask2) {
        super(COMMAND_MESSAGE_TYPE.SET_DATA_STREAMING);

        // Set internal variables
        this.mDivisor = mDivisor;
        this.mPacketFrames = mPacketFrames;
        this.mSensorMask = mSensorMask;
        this.mPacketCount = mPacketCount;
        this.mSensorMask2 = mSensorMask2;
    }

    /**
     * Returns the internal packet count value
     *
     * @return Packet count value
     */
    public int getPacketCount() {
        return this.mPacketCount;
    }

    /**
     * Returns the internal divisor value
     *
     * @return Divisor value
     */
    public int getDivisor() {
        return this.mDivisor;
    }

    /**
     * Returns the internal packet frames value
     *
     * @return Packet frames value
     */
    public int getPacketFrames() {
        return this.mPacketFrames;
    }

    /**
     * Add mask to the already existing one
     *
     * @param mask The mask to add
     */
    public void addMask(int mask) {
        this.mSensorMask |= mask;
    }

    /**
     * Returns the internal sensor mask value
     *
     * @return The sensor mask value
     */
    public int getMask() {
        return this.mSensorMask;
    }


    /**
     * get the second mask used for extended sensor access
     *
     * @return
     */
    public int getMask2() {
        return this.mSensorMask2;
    }

    @Override
    protected byte[] getPacketData() {
        byte[] data = new byte[9];

        if (this.mSensorMask2 > 0) {
            data = new byte[13];
        }

        data[0] = (byte) (this.mDivisor >> 8);
        data[1] = (byte) this.mDivisor;

        data[2] = (byte) (this.mPacketFrames >> 8);
        data[3] = (byte) this.mPacketFrames;

        data[4] = (byte) (this.mSensorMask >> 24);
        data[5] = (byte) (this.mSensorMask >> 16);
        data[6] = (byte) (this.mSensorMask >> 8);
        data[7] = (byte) this.mSensorMask;

        data[8] = (byte) this.mPacketCount;

        if (this.mSensorMask2 > 0) {
            data[9] = (byte) (this.mSensorMask2 >> 24);
            data[10] = (byte) (this.mSensorMask2 >> 16);
            data[11] = (byte) (this.mSensorMask2 >> 8);
            data[12] = (byte) this.mSensorMask2;
        }

        return data;
    }

	/* ******************
     * INNER CLASSES
	 */

    /**
     * Mask values for the data streaming
     */
    public static final class DATA_STREAMING_MASKS {
        public static final int OFF = 0;

        /**
         * Motor masks
         */
        public static final class MOTOR_BACK_EMF {
            /**
             * Both left and right filtered/raw
             */
            public static final class ALL {
                public static final int FILTERED = LEFT.FILTERED | RIGHT.FILTERED,
                        RAW = LEFT.RAW | RIGHT.RAW;
            }

            /**
             * Left motor masks
             */
            public static final class LEFT {
                public static final int FILTERED = 0x00000020, RAW = 0x00200000;
            }

            /**
             * Right motor mask
             */
            public static final class RIGHT {
                public static final int FILTERED = 0x00000040, RAW = 0x00400000;
            }
        }

        public static final class PULSE_WIDTH_MODULATION {

            public static final class LEFT {
                public static final int RAW = 0x00100000;
            }

            public static final class RIGHT {
                public static final int RAW = 0x00080000;
            }

            public static final class ALL {
                public static final int RAW = LEFT.RAW | RIGHT.RAW;
            }
        }

        /**
         * Accelerometer masks
         */
        public static final class ACCELEROMETER {
            /**
             * All axis filtered/raw
             */
            public static final class ALL {
                public static final int FILTERED = (X.FILTERED | Y.FILTERED | Z.FILTERED),
                        RAW = (X.RAW | Y.RAW | Z.RAW);
            }

            /**
             * X-axis filtered/raw
             */
            public static final class X {
                public static final int FILTERED = 0x00008000, RAW = 0x80000000;
            }

            /**
             * Y-axis filtered/raw
             */
            public static final class Y {
                public static final int FILTERED = 0x00004000, RAW = 0x40000000;
            }

            /**
             * Z-axis filtered/raw
             */
            public static final class Z {
                public static final int FILTERED = 0x00002000, RAW = 0x20000000;
            }
        }

        /**
         * Gyro masks
         */
        public static final class GYRO {
            /**
             * All axis filtered/raw
             */
            public static final class ALL {
                public static final int FILTERED = X.FILTERED | Y.FILTERED | Z.FILTERED,
                        RAW = X.RAW | Y.RAW | Z.RAW;
            }

            /**
             * Gyro X-axis value masks
             */
            public static final class X {
                public static final int FILTERED = 0x00001000, RAW = 0x10000000;
            }

            /**
             * Gyro Y-axis value masks
             */
            public static final class Y {
                public static final int FILTERED = 0x00000800, RAW = 0x08000000;
            }

            /**
             * Gyro Z-axis value masks
             */
            public static final class Z {
                public static final int FILTERED = 0x00000400, RAW = 0x04000000;
            }
        }

        /**
         * IMU masks
         */
        public static final class IMU {
            /**
             * All settings filtered/raw
             */
            public static final class ALL {
                public static final int FILTERED = YAW.FILTERED | ROLL.FILTERED | PITCH.FILTERED;
            }

            /**
             * IMU yaw value masks
             */
            public static final class YAW {
                public static final int FILTERED = 0x00040000;
            }

            public static final class ROLL {
                public static final int FILTERED = 0x00020000;
            }

            public static final class PITCH {
                public static final int FILTERED = 0x00010000;
            }
        }

        public static final int ALL = MOTOR_BACK_EMF.ALL.FILTERED |
                MOTOR_BACK_EMF.ALL.RAW |
                PULSE_WIDTH_MODULATION.ALL.RAW |
                ACCELEROMETER.ALL.RAW |
                ACCELEROMETER.ALL.FILTERED |
                GYRO.ALL.RAW |
                GYRO.ALL.FILTERED |
                IMU.ALL.FILTERED;

    }

    /**
     * masks for the second data streaming field
     */
    public static final class DATA_STREAMING_MASK2 {
        public static final class ODOMETER {
            public static final int X = 0x08000000;

            public static final int Y = 0x04000000;

            public static final int ALL = ODOMETER.X | ODOMETER.Y;
        }


        public static final class VELOCITY {
            public static final int X = 0x01000000;
            public static final int Y = 0x00800000;

            public static final int ALL = VELOCITY.X | VELOCITY.Y;
        }


        public static final class QUATERNION {
            public static final int Q0 = 0x80000000;
            public static final int Q1 = 0x40000000;
            public static final int Q2 = 0x20000000;
            public static final int Q3 = 0x10000000;
            public static final int ALL = Q0 | Q1 | Q2 | Q3;
        }

        public static final int ACCEL_ONE = 0x02000000;

        public static final int ALL =
                ACCEL_ONE | ODOMETER.ALL | VELOCITY.ALL | QUATERNION.ALL;

    }

}

