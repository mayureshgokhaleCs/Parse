/* Input : folder containing files with userId and their tweets
 * OutPut: will be in JSON format written in tweet.json
 * author: Mayuresh.Gokhale
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Parser {
	public static void main(String[] args) throws Exception {
		// add all files to folder called as input
		File file = new File("input/");
		File[] files = file.listFiles();
		int countTracker = 0;
		for (File f : files) {
			String sCurrentLine;
			String userId = f.getName();
			BufferedReader br = new BufferedReader(new FileReader("input/" + userId));
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.equals("***")) {
					String nextLine = br.readLine();
					if (nextLine != ("***")) {
						StringBuilder s = new StringBuilder();
						/*
						 * extract Type Origin Text URL ID Time RetCount
						 * Favorites MentionedEntities HashTags and place them
						 * as namaskar seperated values the reason for using
						 * such delimiters is because the original file uses :
						 * as delimiter which occurs in tweets and url as well
						 * which makes it painful to extract the values
						 */
						s.append(nextLine + "namaskar");
						for (int i = 0; i < 10; i++)
							s.append(br.readLine() + "namaskar");
						String result = s.toString();
						String patternString = "namaskar";
						Pattern pattern = Pattern.compile(patternString);
						String[] splittedArray = pattern.split(result);
						Tweet tweet = new Tweet();
						// set userid which is file name
						tweet.setUserId(userId);
						for (String str : splittedArray) {
							String[] splitIndividual = str
									.split(":");/* eg: Origin: abcdef:ghijl */
							StringBuilder cleanData = new StringBuilder();
							for (int i = 1; i < splitIndividual.length; i++) {
								cleanData.append(splitIndividual[i]);
								/*
								 * eg: to keep original data intact abcdef:ghijl
								 */
							}
							tweet = findColumnSetTweet(splitIndividual[0], cleanData.toString(), tweet);
						}
						printinJson(tweet);
					}
				}
			}
			br.close();
			System.out.println("No of files completed:" + (++countTracker));
		}
		System.out.println("JOB Completed!! Zhala re!");
	}

	private static Tweet findColumnSetTweet(String field, String value, Tweet tweet) {
		switch (field) {
		case "Type":
			tweet.setType(value);
			break;
		case "Origin":
			tweet.setOrigin(value);
			break;
		case "Text":
			tweet.setText(value);
			break;
		case "URL":
			tweet.setURL(value);
			break;
		case "ID":
			tweet.setID(value);
			break;
		case "Time":
			tweet.setTime(value);
			break;
		case "RetCount":
			tweet.setRetCount(value);
			break;
		case "Favorite":
			tweet.setFavorites(value);
			break;
		case "MentionedEntities":
			tweet.setMentionedEntities(value);
			break;
		case "Hashtags":
			tweet.setHashTags(value);
			break;
		}
		return tweet;
	}

	private static void printinJson(Tweet tweet) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// Convert object to JSON string
			String jsonInString = mapper.writeValueAsString(tweet);
			try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tweet.json", true)))) {
				out.println(jsonInString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Tweet {
		String Type;
		String Origin;
		String Text;
		String URL;
		String ID;
		String Time;
		String RetCount;
		String Favorites;
		String MentionedEntities;
		String HashTags;
		String userId;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getType() {
			return Type;
		}

		public void setType(String type) {
			Type = type;
		}

		public String getOrigin() {
			return Origin;
		}

		public void setOrigin(String origin) {
			Origin = origin;
		}

		public String getText() {
			return Text;
		}

		public void setText(String text) {
			Text = text;
		}

		public String getURL() {
			return URL;
		}

		public void setURL(String uRL) {
			URL = uRL;
		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		public String getTime() {
			return Time;
		}

		public void setTime(String time) {
			Time = time;
		}

		public String getRetCount() {
			return RetCount;
		}

		public void setRetCount(String retCount) {
			RetCount = retCount;
		}

		public String getFavorites() {
			return Favorites;
		}

		public void setFavorites(String favorites) {
			Favorites = favorites;
		}

		public String getMentionedEntities() {
			return MentionedEntities;
		}

		public void setMentionedEntities(String mentionedEntities) {
			MentionedEntities = mentionedEntities;
		}

		public String getHashTags() {
			return HashTags;
		}

		public void setHashTags(String hashTags) {
			HashTags = hashTags;
		}
	}
}
