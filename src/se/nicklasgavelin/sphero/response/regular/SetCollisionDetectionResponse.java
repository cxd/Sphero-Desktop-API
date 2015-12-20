package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Created by cd on 12/09/2015.
 */
public class SetCollisionDetectionResponse  extends ResponseMessage {
    /**
     * Create a response message from a response header
     *
     * @param _drh The response header
     */
    public SetCollisionDetectionResponse(ResponseHeader _drh) {
        super(_drh);
    }
}
