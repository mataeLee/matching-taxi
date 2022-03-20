package com.flab.matchingtaxi.std;

public class MessagePayloadStandard {
    // key
    public static final String TOPIC = "topic";
    public static final String TYPE = "type";
    public static final String STATE = "state";

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public static final String START_LAT = "start_lat";
    public static final String START_LNG = "start_lng";
    public static final String END_LAT = "end_lat";
    public static final String END_LNG = "end_lng";

    public static final String HOSTNAME = "hostname";
    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String RADIUS = "radius";
    public static final String CALL_REQUEST = "call_request";
    public static final String CALL_RESULT = "call_result";
    public static final String CALL_RESPONSE = "call_response";
    public static final String UPDATE_CELLID = "update_cell_id";
    public static final String CELLID = "cell_id";

    // value
    public static final String UNSUBSCRIBE = "unsubscribe";
    public static final String SEND = "send";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String NOT_FOUND_TAXI = "not_found_taxi";
    public static final String ERROR_COUNT_VAL = "error_count_value";
    public static final String TAXI_COUNT = "taxi_count";
    public static final String MATCH_FAIL = "match_fail";
}