package com.example.thomasburch.localchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.thomasburch.localchat.ChatResponse.Example;
import com.example.thomasburch.localchat.ChatResponse.ResultList;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.ProgressBar;

public class ChatActivity extends AppCompatActivity {
	public static String LOG_TAG = "ChatApplication";

    public interface PostService {
        @GET("default/post_message")
        Call<Example> post_message(@Query("lat") Float lat,
									@Query("lng") Float lng,
				                    @Query("user_id") String my_user_id,
								    @Query("nickname") String my_nickname,
								    @Query("message") String message,
								    @Query("message_id") String message_id);
    }

    public interface GetService {
		@GET("default/get_messages")
        Call<Example> get_message(@Query("lat") Float lat,
								  @Query("lng") Float lng,
		                          @Query("user_id") String user_id);
    }

	public final class SecureRandomString {
		private SecureRandom random = new SecureRandom();
		public String nextString() {
			return new BigInteger(130, random).toString(32);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		spinner = (ProgressBar)findViewById(R.id.progressBar1);

	}

	MyAdapter aa;
	ArrayList<ListElement> aList;
	String user_id;
	String nickname;
	String latitude;
	String longitude;
	float lat;
	float lng;
	String message_id;
	String message;
	String list_nickname;
	String list_message;
	String list_user_id;
	String list_timestamp;

	ProgressBar spinner;

	@Override
	protected void onResume(){
		Intent intent = getIntent();
		//get my nickname
		nickname = intent.getStringExtra(MainActivity.nickname);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		user_id= settings.getString("my_user_id", null);
		latitude = settings.getString("my_lat", null);
		longitude = settings.getString("my_lng", null);

		//convert lat long to float
		lat = Float.valueOf(latitude);
		lng = Float.valueOf(longitude);

		Log.i(LOG_TAG, "Lat is: " + latitude);
		Log.i(LOG_TAG, "Long is: " + longitude);
		Log.i(LOG_TAG, "my_user_id is: " + user_id);
		Log.i(LOG_TAG, "my_nickname is: " + nickname);



		//initialize adapter
		aList = new ArrayList<ListElement>();
		aa = new MyAdapter(this, R.layout.list_element, aList, user_id);
		ListView myListView = (ListView) findViewById(R.id.listView);
		myListView.setAdapter(aa);

		// Sets a listener for the send button
		Button send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				spinner.setVisibility(View.VISIBLE);

				// Reacts to a button press.
				SecureRandomString srs = new SecureRandomString();
				message_id = srs.nextString();

				EditText editText = (EditText) findViewById(R.id.editText2);
				message = editText.getText().toString();

				postChat(lat, lng, user_id, nickname, message, message_id);
				refreshChat(lat, lng, user_id);//search chats near

			}
		});

		// Sets a listener for the refresh button
		Button refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Reacts to a button press.
				refreshChat(lat, lng, user_id);//search chats near
			}
		});

		refreshChat(lat, lng, user_id);//refresh chats
		aa.notifyDataSetChanged();
		super.onResume();
	}

	// refresh THE MESSAGES
	 private void refreshChat(Float latitude, Float longitude, final String user_id){

		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		// set your desired log level
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addInterceptor(logging)
				.build();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://luca-teaching.appspot.com/localmessages/")	//We are using Foursquare API to get data
				.addConverterFactory(GsonConverterFactory.create())	//parse Gson string
				.client(httpClient)	//add logging
                .build();

        GetService get_service = retrofit.create(GetService.class);
        Call<Example> GetMessageCall = get_service.get_message(latitude, longitude, user_id);

        //Call retrofit asynchronously
        GetMessageCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response) {

				//parse the response for list of messages
				List<ResultList> Response_resultList;
				if(response.body().result.equals("ok")) {
					Log.i(LOG_TAG, "Result: ok");
					Response_resultList = response.body().resultList;
				} else {
					Response_resultList= new ArrayList<ResultList>();
				}

                aList.clear();
                for (int i = Response_resultList.size()-1; i >= 0; i--) {
					list_nickname = Response_resultList.get(i).getNickname();
					list_message = Response_resultList.get(i).getMessage();
					list_user_id = Response_resultList.get(i).getUserId();
					list_timestamp = Response_resultList.get(i).getTimestamp();

					Log.i(LOG_TAG, "list_user_id " + list_user_id);
					Log.i(LOG_TAG, "my_user_id: " + user_id);

					if(list_user_id.equals(user_id)) {
						aList.add(new ListElement("You!" , list_message, list_user_id, list_timestamp));
					}else{
						aList.add(new ListElement(list_nickname, list_message, list_user_id, list_timestamp));
					}
				}

				if (Response_resultList.size() == 0) {
					aList.clear();
					aList.add(new ListElement("", "No Results", "", ""));
				}

				spinner.setVisibility(View.GONE);
                // We notify the ArrayList adapter that the underlying list has changed,
                // triggering a re-rendering of the list.
                aa.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }

		});
	}

	//POST MESSAGE
	private void postChat(Float latitude, Float longitude, final String user_id, String nickname, String message, String message_id){

		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		// set your desired log level
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addInterceptor(logging)
				.build();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://luca-teaching.appspot.com/localmessages/")	//We are using Foursquare API to get data
				.addConverterFactory(GsonConverterFactory.create())	//parse Gson string
				.client(httpClient)	//add logging
				.build();

		PostService post_service = retrofit.create(PostService.class);

		aList.add(new ListElement("You!", message, user_id, "now"));
		aa.notifyDataSetChanged();

		Call<Example> PostMessageCall =
				post_service.post_message(latitude, longitude, user_id, nickname, message, message_id);



		//Call retrofit asynchronously
		PostMessageCall.enqueue(new Callback<Example>() {
			@Override
			public void onResponse(Response<Example> response) {

				//parse the response for list of messages
				List<ResultList> Response_resultList;
				if(response.body().result.equals("ok")) {
					Log.i(LOG_TAG, "Result: ok");
					Response_resultList = response.body().resultList;
				} else {
					Response_resultList= new ArrayList<ResultList>();
				}

				//List<ResultList> Response_resultList = response.body().getResultList();
				aList.remove(0);

				for (int i = Response_resultList.size()-1; i >= 0; i--) {

					list_nickname = Response_resultList.get(i).getNickname();
					list_message = Response_resultList.get(i).getMessage();
					list_user_id = Response_resultList.get(i).getUserId();
					list_timestamp = Response_resultList.get(i).getTimestamp();

					aList.add(new ListElement(list_nickname, list_message, list_user_id, list_timestamp));
				}

				// We notify the ArrayList adapter that the underlying list has changed,
				// triggering a re-rendering of the list.
				aa.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable t) {
				// Log error here since request failed
			}
		});
	}

}


