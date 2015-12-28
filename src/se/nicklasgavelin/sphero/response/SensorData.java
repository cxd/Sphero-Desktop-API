package se.nicklasgavelin.sphero.response;

/**
 * Created by cd on 28/12/2015.
 */
public class SensorData {

    /**
     * the mask used for the first set of sensor readings
     */
    public int getMask1() {
        return mask1;
    }

    public void setMask1(int mask1) {
        this.mask1 = mask1;
    }

    /**
     * the mask used for the second set of sensor readings
     */
    public int getMask2() {
        return mask2;
    }

    public void setMask2(int mask2) {
        this.mask2 = mask2;
    }

    /**
     * unfiltered accelerometer data
     * units/LSB 4mG
     */
    public Vector3 getAccelerometerRaw() {
        return accelerometerRaw;
    }

    public void setAccelerometerRaw(Vector3 accelerometerRaw) {
        this.accelerometerRaw = accelerometerRaw;
    }

    /**
     * unfiltered gyro data
     * units/LSB 0.068 degrees
     */
    public Vector3 getGyroRaw() {
        return gyroRaw;
    }

    public void setGyroRaw(Vector3 gyroRaw) {
        this.gyroRaw = gyroRaw;
    }

    /**
     * unfiltered motor EMF
     * right motor back
     * left motor back
     * units/LSB 22.5 cm
     */
    public Vector2 getMotorEmfRaw() {
        return motorEmfRaw;
    }

    public void setMotorEmfRaw(Vector2 motorEmfRaw) {
        this.motorEmfRaw = motorEmfRaw;
    }

    /**
     * unfiltered motor PWM
     * units = duty cycle
     *
     */
    public Vector2 getMotorPwmRaw() {
        return motorPwmRaw;
    }

    public void setMotorPwmRaw(Vector2 motorPwmRaw) {
        this.motorPwmRaw = motorPwmRaw;
    }

    /**
     * imu filtered data
     * units degrees
     */
    public Vector3 getImuFiltered() {
        return imuFiltered;
    }

    public void setImuFiltered(Vector3 imuFiltered) {
        this.imuFiltered = imuFiltered;
    }

    /**
     * accelerometer filtered
     * units/LSB 1/4096 G
     */
    public Vector3 getAccelerometerFiltered() {
        return accelerometerFiltered;
    }

    public void setAccelerometerFiltered(Vector3 accelerometerFiltered) {
        this.accelerometerFiltered = accelerometerFiltered;
    }

    /**
     * gyro filtered
     * units/LSB 0.1dps
     */
    public Vector3 getGyroFiltered() {
        return gyroFiltered;
    }

    public void setGyroFiltered(Vector3 gyroFiltered) {
        this.gyroFiltered = gyroFiltered;
    }

    /**
     * emf filtered
     *
     */
    public Vector2 getEmfFiltered() {
        return emfFiltered;
    }

    public void setEmfFiltered(Vector2 emfFiltered) {
        this.emfFiltered = emfFiltered;
    }

    /**
     * quaternion measure of sphero locator
     * units 1/10000 Q
     */
    public Quaternion4 getQuaternion() {
        return quaternion;
    }

    public void setQuaternion(Quaternion4 quaternion) {
        this.quaternion = quaternion;
    }

    /**
     * odometer sensor locator
     * units = cm
     */
    public Vector2 getOdometer() {
        return odometer;
    }

    public void setOdometer(Vector2 odometer) {
        this.odometer = odometer;
    }

    /**
     * accel one sensor
     * unit 1 mG
     */
    public int getAccelOne() {
        return accelOne;
    }

    public void setAccelOne(int accelOne) {
        this.accelOne = accelOne;
    }

    /**
     * locator velocity
     * unit mm/s
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * a vector 2 class for use with sensor data
     */
    public class Vector2 {
        protected int a;
        protected int b;

        public Vector2() {

        }

        public Vector2(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public Vector2 add(Vector2 b) {
            return new Vector2(a + b.a, this.b + b.b);
        }

        public int dot(Vector2 b) {
            return a *b.a + this.b*b.b;
        }

        public Vector2 mult(int c) {
            return new Vector2(c* a, c*b);
        }

        public double length() {
            return Math.sqrt(Math.pow(a,2) + Math.pow(b, 2));
        }

    }


    /**
     * a vector 3 class for use with sensor data
     */
    public class Vector3 extends Vector2 {
        private int c;

        public Vector3() {

        }

        public Vector3(int x, int y, int c) {
            super(x,y);
            this.c = c;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

        public Vector3 add(Vector3 b) {
            return new Vector3(a +b.a, this.b+b.b, c +b.c);
        }

        @Override
        public Vector3 mult(int c) {
            return new Vector3(c* a, c*b, c* this.c);
        }

        public int dot(Vector3 b) {
            return a *b.a + this.b*b.b + c *b.c;
        }

        @Override
        public double length() {
            return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2));
        }
    }

    /**
     * a simple quaternion class to contain the quaternion results.
     * Note that this class has no operations on it.
     *
     * https://en.wikipedia.org/wiki/Quaternion
     *
     */
    public class Quaternion4 {
        private int q0;
        private int q1;
        private int q2;
        private int q3;

        public Quaternion4() {

        }

        public Quaternion4(int q0, int q1, int q2, int q3) {
            this.q0 = q0;
            this.q1 = q1;
            this.q2 = q2;
            this.q3 = q3;
        }

        public int getQ0() {
            return q0;
        }

        public void setQ0(int q0) {
            this.q0 = q0;
        }

        public int getQ1() {
            return q1;
        }

        public void setQ1(int q1) {
            this.q1 = q1;
        }

        public int getQ2() {
            return q2;
        }

        public void setQ2(int q2) {
            this.q2 = q2;
        }

        public int getQ3() {
            return q3;
        }

        public void setQ3(int q3) {
            this.q3 = q3;
        }
    }


    private int mask1;

    private int mask2;

    private Vector3 accelerometerRaw = new Vector3();

    private Vector3 gyroRaw = new Vector3();

    private Vector2 motorEmfRaw = new Vector2();

    private Vector2 motorPwmRaw = new Vector2();

    private Vector3 imuFiltered = new Vector3();

    private Vector3 accelerometerFiltered = new Vector3();

    private Vector3 gyroFiltered = new Vector3();

    private Vector2 emfFiltered = new Vector2();

    private Quaternion4 quaternion = new Quaternion4();

    private Vector2 odometer = new Vector2();

    private int accelOne = 0;

    private Vector2 velocity = new Vector2();

}
