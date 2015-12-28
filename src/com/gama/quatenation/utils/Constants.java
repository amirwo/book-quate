package com.gama.quatenation.utils;

public class Constants {

	public static final String TESSBASE_PATH = "/mnt/sdcard/com.gama.quatenation/";
	public static final String TESSBASE_ENG_TRAIN = "eng.traineddata";
	public static final String DEFAULT_LANGUAGE = "eng";
	public static final String EXPECTED_FILE = TESSBASE_PATH + "tessdata/";
	public static final int REQ_CAMERA_IMAGE = 1;
	
	// QuotePreview 
	public static final String KEY_QUOTE_CONTENT = "key_quote_content"; 
	
	// Intents
	public static final String ON_RECEIVE_RESPONSE_LISTENER_BR_INTENT = "com.gama.quatenation.OnReceiveResponseBroadcastListener";
	public static final String ON_COMPLETE_TESS_API_BR_INTENT = "com.gama.quatenation.OnCompleteTessApiBroadcastListener";
	// Network
	public static final int SOCKET_TIMEOUT = 10000;
	
	// Server API
	public static final String GOOGLE_BOOK_API_GET = "https://www.googleapis.com/books/v1/volumes?";
	public static final String SERVICE_QUOTE_SEND_URL = "http://server-kwoht.rhcloud.com/insertQuote";
	public static final String SERVICE_GET_QUOTES_URL = "http://server-kwoht.rhcloud.com/getQuotes?userID=";
	public static final String SERVICE_QUOTES_POST_URL = "http://server-kwoht.rhcloud.com/getQuotesJSON";
	
	// Error code
	public static final int ERROR_CODE_UNKNOWN = 0;
	public static final String ERROR_GETTING_ADVERTISING_ID = "unknown_advertising_id";
	
	// Intent constants
	
	public static final String KEY_QUOTE = "quote_key";
}
