package com.credit_suisse.app.util;

import org.springframework.stereotype.Service;

@Service("commonConstants")
public class CommonConstants {
    public static final String INSTRUMENT1 = "INSTRUMENT1";
    public static final String INSTRUMENT2 = "INSTRUMENT2";
    public static final String INSTRUMENT3 = "INSTRUMENT3";
    public static final String NEW_INSTRUMENT = "NEW_INSTRUMENT";
//    public static final Integer INSTRUMENTS_COUNT = 10000;
    public static final Integer INSTRUMENTS_COUNT = 10;
//    public static final Integer INSTRUMENTS_COUNT = 7;
    public static final Integer NEWST = 10;
//    public static final Integer NEWST = 3;
	public static final int THREAD_POOL_SIZE = 10;
	public static final long WORKER_SLEEP_TIME = 1000;
	public static final boolean INSTRUMENT_MANAGER_ON = true;
	public static final String INSTRUMENT_INPUT_FILE = "src/main/resources/input.txt";
	public static final long INSTRUMENT_REFRESH_MILLIS = 5000;
	public static final boolean INSTRUMENT_WORKER_ON = true;
}
