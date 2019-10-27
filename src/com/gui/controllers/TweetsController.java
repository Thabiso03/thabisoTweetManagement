package com.gui.controllers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ManagedBean;

import twitter4j.*;
import twitter4j.auth.*;

import com.entities.TweetEntity;
import com.gui.models.Tweet;
import com.service.TweetService;

import java.util.*;

@ManagedBean(name = "tweetsController")
public class TweetsController {	
	@EJB
	private TweetService tweetService;
	
	//from form
  	@ManagedProperty(value="#{tweet}")
    private Tweet tweet;
  	
  	private final static String CONSUMER_KEY = "z6gxhDXY7RUjWCikMhoqwewgu";
    private final static String CONSUMER_KEY_SECRET = "o1ErIULhtmQIryrR880khh3tYZCClKyC50QU9mdh7yljFmfx0a";
  	
  	@PostConstruct
  	public void init() {
  		tweet = new Tweet();
  	}
	
	public List<Tweet> getTweets() {
		List<TweetEntity> tweetEntities = (List<TweetEntity>) tweetService.getTweets();
		Iterator<TweetEntity> tweetIterator = tweetEntities.iterator();
		List<Tweet> tweets = new ArrayList<>();
		
		while(tweetIterator.hasNext()) {
			TweetEntity row = tweetIterator.next();
			Tweet t = new Tweet();
			
			t.setTweetID(row.getTweetID());
			t.setTweetBody(row.getTweetBody());
			t.setTimestamp(row.getTimestamp());
			
			tweets.add(t);
		}
		
		return tweets;
	}
	
	public String postTweet(Tweet tweet) throws TwitterException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
		
		String accessToken = getSavedAccessToken();
		String accessTokenSecret = getSavedAccessTokenSecret();
		AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
		 
		twitter.setOAuthAccessToken(oathAccessToken);
		
		twitter.updateStatus(tweet.getTweetBody());
		
		TweetEntity te = new TweetEntity();
		te.setTweetBody(tweet.getTweetBody());
		tweetService.postTweet(te);
		
		return "home.xhtml?faces-redirect=true";
	}
	
	public Tweet getTweet() {
		return tweet;
	}
	
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}
	
	private String getSavedAccessTokenSecret() {
		// consider this is method to get your previously saved Access Token
		// Secret
		return "XehtD2an2r9ApYSe0BkauoRrw7Lj7ol34t6Ezuz1S6Fb1";
	}
	 
    private String getSavedAccessToken() {
		// consider this is method to get your previously saved Access Token
		return "1187111045271379968-KyttBkr00sL2vdStv7wAQUIRUawXHy";
    }
}
