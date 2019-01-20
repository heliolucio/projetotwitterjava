package br.com.fiap.twitter;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterSmallAnalytics {
	
	
	public static void main(String[] args) {

		try {
			
			Config config = new Config();

			TwitterFactory twitterFactory = new TwitterFactory();
			AccessToken accessToken = loadAccesToken();
			Twitter twitter = twitterFactory.getSingleton();

			String consumerApiKey = config.getProperties().getProperty("consumer.api.key");
			String consumerApiSecretKey = config.getProperties().getProperty("consumer.api.secret.key");

			twitter.setOAuthConsumer(consumerApiKey, consumerApiSecretKey);
			twitter.setOAuthAccessToken(accessToken);

			
			Query query = new Query("#openjdk");
			query.setResultType(ResultType.mixed);

			String ultimaSemana = getUltimaSemana();
			query.setSince(ultimaSemana);

			Map<String, Integer> tweetsDia = new HashMap<>();
			Map<String, Integer> retweetsDia = new HashMap<>();
			Map<String, Integer> favoritosDia = new HashMap<>();

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			List<NomeData> listTweet = new ArrayList<>();
			int contadorTweets = 0;
			int contadorRetweets = 0;
			int contadorFavoritos = 0;

			QueryResult result = twitter.search(query);

			while (result.hasNext()) {
				query = result.nextQuery();
				NomeData nomeData = new NomeData();
				
				for (Status status : result.getTweets()) {

					nomeData.setDate(status.getCreatedAt());
					nomeData.setNome(status.getUser().getName());
					String data = format.format(status.getCreatedAt());

					contadorTweets = TweetPorDias(tweetsDia, contadorTweets, data);

					contadorRetweets = RetweetPorDias(retweetsDia, contadorRetweets, status, data);

					contadorFavoritos = TweetFavoritos(favoritosDia, contadorFavoritos, status, data);

					listTweet.add(nomeData);
				}
				result = twitter.search(query);
			}
			
			for (Map.Entry<String, Integer> dias : tweetsDia.entrySet()) {
				System.out.println("Realizado " + dias.getValue() + " tweets no dia: " + dias.getKey());
			}
			
			System.out.println("--------------------------------------------");

			for (Map.Entry<String, Integer> dias : retweetsDia.entrySet()) {
				System.out.println("Realizado " + dias.getValue() + " Retweets no dia: " + dias.getKey());
			}

			System.out.println("--------------------------------------------");

			for (Map.Entry<String, Integer> dias : favoritosDia.entrySet()) {
				System.out.println("Favoritos " + dias.getValue() + " no dia: " + dias.getKey());
			}


			System.out.println("--------------------------------------------");
			System.out.println("Ordenados por Nome: ");
			Collections.sort(listTweet, NomeData.name);

			for (NomeData nome : listTweet) {
				System.out.println(nome);
			}

			System.out.println("--------------------------------------------");
			System.out.println("Ordenados por Data: ");
			Collections.sort(listTweet, NomeData.data);

			for (NomeData data : listTweet) {
				System.out.println(data);
			}			

		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	private static String getUltimaSemana() {
		final ZonedDateTime zoneDateTime = ZonedDateTime.now();
		final ZonedDateTime zoneDateTime2 = zoneDateTime.minusWeeks(1).with(DayOfWeek.SUNDAY);
		String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(zoneDateTime2);
		return date;
	}

	private static AccessToken loadAccesToken() {
		String token = "498415564-hODjAOn74OY6Ytc6VG23l7MPcdbJWM7WN34y4NQO";
		String tokenSecret = "9tgYPmevX7ZthSI8ZVYIOODL4J42PDqTQmxk4YJwq4x4R";
		return new AccessToken(token, tokenSecret);
	}
	
	private static int TweetFavoritos(Map<String, Integer> favoritosDia,
			int contFavoritos, Status status, String DateToStr) {
		Integer contFavoritosSemana;
		contFavoritosSemana = favoritosDia.get(DateToStr);
		if (contFavoritosSemana != null) {
			contFavoritosSemana += status.getFavoriteCount();
		} else {
			contFavoritosSemana = status.getFavoriteCount();
		}

		favoritosDia.put(DateToStr, contFavoritosSemana);

		contFavoritos += status.getFavoriteCount();
		return contFavoritos;
	}

	private static int RetweetPorDias(Map<String, Integer> retweetsDia,
			int contRetweets, Status status, String DateToStr) {
		Integer contRetweetPorDia;
		contRetweetPorDia = retweetsDia.get(DateToStr);
		if (contRetweetPorDia != null) {
			contRetweetPorDia += status.getRetweetCount();
		} else {
			contRetweetPorDia = status.getRetweetCount();
		}

		retweetsDia.put(DateToStr, contRetweetPorDia);

		contRetweets += status.getRetweetCount();
		return contRetweets;
	}

	private static int TweetPorDias(Map<String, Integer> tweetsDia, int contTweets,
			String DateToStr) {
		Integer contTweetPorDia;
		contTweetPorDia = tweetsDia.get(DateToStr);
		if (contTweetPorDia != null) {
			contTweetPorDia += 1;
		} else {
			contTweetPorDia = 1;
		}

		tweetsDia.put(DateToStr, contTweetPorDia);

		contTweets++;
		return contTweets;
	}


}
