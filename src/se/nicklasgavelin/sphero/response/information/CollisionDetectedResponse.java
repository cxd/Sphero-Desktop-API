package se.nicklasgavelin.sphero.response.information;

import oracle.jrockit.jfr.events.Bits;
import se.nicklasgavelin.sphero.response.InformationResponseMessage;

/**
 * Created by cd on 12/09/2015.
 */
public class CollisiondetectedResponse extends InformationResponseMessage {

    public enum AXIS {
        YAXIS,
        XAXIS;
    }

    /**
     * data from response.
     */
    private byte[] data;

    private int x;
    private int y;
    private int z;

    private AXIS axis;

    private int xMag;

    private int yMag;

    private int speed;

    private int timestamp;

    /**
     * Create an information response from the response header
     *
     * @param rh The response header
     */
    public CollisiondetectedResponse(ResponseHeader rh) {
        super(rh);
        parseSensorData();
    }


    /**
     * Returns the data received in the data message
     *
     * @return The data for the sensors
     */
    public byte[] getSensorData()
    {
        return super.getMessageHeader().getPacketPayload();
    }


    /**
     * parse the sensor data
     */
    private void parseSensorData() {
        byte[] data = this.getMessageHeader().getPacketPayload();
        if (data.length >= 3) {
            this.x = (int) data[0];
            this.y = (int) data[1];
            this.z = (int) data[2];
        }
        if (data.length >= 4) {
            if (data[3] == 1) {
                this.axis = AXIS.XAXIS;
            } else {
                this.axis = AXIS.YAXIS;
            }
        }
        if (data.length >= 5) {
            this.speed = (int) data[4];
        }
        if (data.length >= 6) {
            this.timestamp = (int)data[5];
        }
    }

    /**
     * componens of the collision
     */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    /**
     * axis of collision
     */
    public AXIS getAxis() {
        return axis;
    }

    /**
     * magnitude of a collision
     */
    public int getxMag() {
        return xMag;
    }

    /**
     * magnitude of y collision
     */
    public int getyMag() {
        return yMag;
    }

    /**
     * speed of collision
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * timestamp of collision
     */
    public int getTimestamp() {
        return timestamp;
    }




}
