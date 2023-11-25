public class Mongo{
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.BsonDocument;

public static void main(String args[]) {
  // Set system properties via commandline or programmatically
  System.setProperty("javax.net.ssl.keyStore", "leaderboarddata");
  System.setProperty("javax.net.ssl.keyStorePassword", "BogiBogi12378&*#");
//^^^ We need to initialize a key to the server that is going to be sent. ^^^

  String uri = "mongodb+srv://leaderboarddata.s28201v.mongodb.net/?authSource=%24external&authMechanism=MONGODB-X509&retryWrites=true&w=majority"; //URI for the Mongo Server database is established.
  ConnectionString connectionString = new ConnectionString(uri);
  MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build(); //BIG DOT OPERATORS


  MongoClient mongoClient = MongoClients.create(settings);
  MongoDatabase database = mongoClient.getDatabase("testDB");
  MongoCollection<Document> collection = database.getCollection("testCol");
  BsonDocument filter = new BsonDocument();
  collection.countDocuments(filter);

  client.close(); //Closes the client server at the end so that it doesn't waste memory usage. 
}

};
