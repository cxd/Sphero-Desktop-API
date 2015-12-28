package se.nicklasgavelin.sphero.response.parser;

import se.nicklasgavelin.sphero.command.SetDataStreamingCommand;
import se.nicklasgavelin.sphero.response.SensorData;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper class that is used to process the response received
 * from the data stream.
 * <p/>
 * Created by cd on 28/12/2015.
 */
public class DataStreamParser {

    /**
     * apply the value to the sensor data
     */
    protected interface Processor {
        boolean apply(SensorData result, int value);
    }

    private int mask1;

    private int mask2;

    // parsing is ordered assuming specific incremental order to data index
    // this example is taken from
    // https://github.com/SoatExperts/sphero-sdk/tree/master/SDK/Sphero
    // in the class "SensorData"
    // additionally the order of evaluation is also defined in:
    // http://www.mathworks.com/matlabcentral/fileexchange/52746-sphero-api-matlab-sdk/content/Sphero_API_Matlab_SDK/Sphero/SpheroInterface/SpeheroCore/SpheroCore.m
    // where each successive iteration decreases the size of the byte array by 2.
    // instead of incrementing a data index.
    // note this is also based on the order of the mask as it appears in the table
    // in the sphero API documentation
    private int[] flags = new int[]{
            SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.X.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Y.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Z.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.X.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Y.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Z.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.RIGHT.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.LEFT.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.PULSE_WIDTH_MODULATION.LEFT.RAW,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.PULSE_WIDTH_MODULATION.RIGHT.RAW,


            SetDataStreamingCommand.DATA_STREAMING_MASKS.IMU.PITCH.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.IMU.ROLL.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.IMU.YAW.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.X.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Y.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Z.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.X.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Y.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Z.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.RIGHT.FILTERED,
            SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.LEFT.FILTERED

    };

    private int[] mask2flags = new int[]{
            SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q0,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q1,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q2,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q3,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.ODOMETER.X,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.ODOMETER.Y,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.ACCEL_ONE,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.VELOCITY.X,
            SetDataStreamingCommand.DATA_STREAMING_MASK2.VELOCITY.Y

    };


    public DataStreamParser(int mask1, int mask2) {
        this.mask1 = mask1;
        this.mask2 = mask2;
    }

    /**
     * determine if a flag exists in the mask
     *
     * @param mask
     * @param flag
     * @return
     */
    private boolean exists(int mask, int flag) {
        return (mask & flag) != 0;
    }

    /**
     * extract a short from the position supplied in the data byte array
     *
     * @param index
     * @param data
     * @return
     */
    private int extract(int index, byte[] data) {
        short value = data[index];
        value = (short) (value << 8);
        value += data[index + 1];
        return value;
    }

    /**
     * check that the flag exists,
     * extract the value if it does from the data at the supplied index
     * assign it to the result using the supplied process function
     *
     * @param i
     * @param mask
     * @param flag
     * @param index
     * @param data
     * @param result
     * @param procFn
     * @return
     */
    private boolean processData(int mask, int flag, int index, byte[] data, SensorData result, Processor procFn) {
        if (exists(mask, flag)) {
            if (index < data.length - 1) {
                int value = extract(index, data);
                return procFn.apply(result, value);
            }
        }
        return false;
    }

    /**
     * parse the sensor packet as received from the
     * data streaming response.
     *
     * @param packet
     * @return
     */
    public SensorData parse(byte[] packet) {
        SensorData result = new SensorData();
        result.setMask1(mask1);
        result.setMask2(mask2);
        int dataIndex = 0;
        // parsing is ordered assuming specific incremental order to data index
        // this example is taken from
        // https://github.com/SoatExperts/sphero-sdk/tree/master/SDK/Sphero
        // in the class "SensorData"
        // additionally the order of evaluation is also defined in:
        // http://www.mathworks.com/matlabcentral/fileexchange/52746-sphero-api-matlab-sdk/content/Sphero_API_Matlab_SDK/Sphero/SpheroInterface/SpeheroCore/SpheroCore.m
        // where each successive iteration decreases the size of the byte array by 2
        // we expect the dataIndex to always increment and eventually consume
        // the available data.
        while (dataIndex < packet.length - 1) {
            boolean parseFlag = false;
            for (final int flag : flags) {
                parseFlag = processData(mask1, flag, dataIndex, packet, result,
                        new Processor() {
                            @Override
                            public boolean apply(SensorData result, int value) {
                                switch (flag) {
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.X.RAW:
                                        result.getAccelerometerRaw().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Y.RAW:
                                        result.getAccelerometerRaw().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Z.RAW:
                                        result.getAccelerometerRaw().setC(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.X.RAW:
                                        result.getGyroRaw().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Y.RAW:
                                        result.getGyroRaw().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Z.RAW:
                                        result.getGyroRaw().setC(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.RIGHT.RAW:
                                        result.getMotorEmfRaw().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.LEFT.RAW:
                                        result.getMotorEmfRaw().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.PULSE_WIDTH_MODULATION.LEFT.RAW:
                                        result.getMotorPwmRaw().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.PULSE_WIDTH_MODULATION.RIGHT.RAW:
                                        result.getMotorPwmRaw().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.IMU.PITCH.FILTERED:
                                        result.getImuFiltered().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.IMU.ROLL.FILTERED:
                                        result.getImuFiltered().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.IMU.YAW.FILTERED:
                                        result.getImuFiltered().setC(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.X.FILTERED:
                                        result.getAccelerometerFiltered().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Y.FILTERED:
                                        result.getAccelerometerFiltered().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.Z.FILTERED:
                                        result.getAccelerometerFiltered().setC(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.X.FILTERED:
                                        result.getGyroFiltered().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Y.FILTERED:
                                        result.getGyroFiltered().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.Z.FILTERED:
                                        result.getGyroFiltered().setC(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.RIGHT.FILTERED:
                                        result.getEmfFiltered().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASKS.MOTOR_BACK_EMF.LEFT.FILTERED:
                                        result.getEmfFiltered().setB(value);
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                if (parseFlag) {
                    dataIndex += 2;
                }
            }
            for (final int flag : mask2flags) {
                parseFlag = processData(mask2, flag, dataIndex, packet, result,
                        new Processor() {

                            @Override
                            public boolean apply(SensorData result, int value) {
                                switch (flag) {
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q0:
                                        result.getQuaternion().setQ0(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q1:
                                        result.getQuaternion().setQ1(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q2:
                                        result.getQuaternion().setQ2(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.QUATERNION.Q3:
                                        result.getQuaternion().setQ3(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.ODOMETER.X:
                                        result.getOdometer().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.ODOMETER.Y:
                                        result.getOdometer().setB(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.ACCEL_ONE:
                                        result.setAccelOne(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.VELOCITY.X:
                                        result.getVelocity().setA(value);
                                        return true;
                                    case SetDataStreamingCommand.DATA_STREAMING_MASK2.VELOCITY.Y:
                                        result.getVelocity().setB(value);
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });

                if (parseFlag) {
                    dataIndex += 2;
                }
            }
        }
        return result;
    }


    private class Tuple<A, B> {
        private A one;
        private B two;

        public Tuple(A a, B b) {
            one = a;
            two = b;
        }

        public A getOne() {
            return one;
        }

        public void setOne(A one) {
            this.one = one;
        }

        public B getTwo() {
            return two;
        }

        public void setTwo(B two) {
            this.two = two;
        }
    }
}
