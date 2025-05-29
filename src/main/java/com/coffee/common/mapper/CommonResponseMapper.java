package com.coffee.common.mapper;

import com.coffee.common.response.CommonResponse;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * This class responsible for map response related objects.
 *
 * @author Sheranga Perera
 * created 2025-05-28
 */
public class CommonResponseMapper {
    private CommonResponseMapper() {
    }

    /**
     * This method map the success details for common response.
     *  @param commonResponse
     * @param object
     */
    public static void successResponseMapper(CommonResponse commonResponse, Object object) {
        if (!ObjectUtils.isEmpty(object))
            commonResponse.setResult(object);

        commonResponse.setStatusCode("0");
        commonResponse.setStatusMessage(ResponseStatus.SUCCESS.valueOf());
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        commonResponse.setTimestamp(timestamp);
    }

    /**
     * This method map the failure details for common response.
     *  @param commonResponse
     * @param e
     */
    public static void failureResponseMapper(CommonResponse commonResponse, Throwable e) {

        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        commonResponse.setStatusCode("1");
        commonResponse.setTimestamp(timestamp);
        commonResponse.setStatusMessage(ObjectUtils.isEmpty(e.getMessage()) ? ResponseStatus.FAILURE.valueOf() : e.getMessage());
    }
}
