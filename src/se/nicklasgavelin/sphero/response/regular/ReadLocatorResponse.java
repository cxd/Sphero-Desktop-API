package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

import java.nio.ByteBuffer;

/**
 * Response for the locator request
 * <p/>
 * The format has
 * <p/>
 * DLen 0Bh
 * XPos - 16 bit cm
 * YPos - 16 bit cm
 * Xvelocity - 16 bit signed cm/sec
 * YVelocity - 16 bit signed cm/sec
 * Speed over ground - 16-bit unsigned cm/sec
 * <p/>
 * Created by cd on 20/12/2015.
 */
public class ReadLocatorResponse extends ResponseMessage {


    private int xPosition;
    private int yPosition;
    private int xVelocity;
    private int yVelocity;
    private int speedOverGround;

    /**
     * Create a response message from a response header
     *
     * @param _drh The response header
     */
    public ReadLocatorResponse(ResponseHeader _drh) {
        super(_drh);
        parseData(_drh.getPacketPayload());
    }

    /**
     * parse data supplied in response header
     *
     * @param data
     */
    private void parseData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data, 0, data.length);
       if (data.length >= 5) {
           xPosition = buffer.getShort();
           yPosition = buffer.getShort();
           xVelocity = buffer.getShort();
           yVelocity = buffer.getShort();
           speedOverGround = buffer.getShort();
       }
    }

    /**
     * x-coord relative to starting position (0,0)
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * y-coord relative to starting position (0,0)
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * xVelocity cm/sec
     */
    public int getxVelocity() {
        return xVelocity;
    }

    /**
     * yVelocity cm/sec
     */
    public int getyVelocity() {
        return yVelocity;
    }

    /**
     * speed over ground cm/sec
     */
    public int getSpeedOverGround() {
        return speedOverGround;
    }
}
