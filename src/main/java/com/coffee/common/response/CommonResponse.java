package com.coffee.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * This is the common response for all the API calls.
 *
 * @author Sheranga Perera
 * created 2025-05-28
 */


@Data
@NoArgsConstructor
@JsonPropertyOrder({"statusCode", "statusMessage", "transactionId", "timestamp", "result"})
public class CommonResponse {

    private String statusCode;
    private String statusMessage;
    private String transactionId;
    private Timestamp timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;
}
