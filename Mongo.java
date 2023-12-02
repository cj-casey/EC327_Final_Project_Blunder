package com.example.onechess;




import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mongo{
    private static final String CONNECTION_STRING = "mongodb+srv://<bogdans@bu.edu>:<BogiBogi12378&*#>@leaderboarddata.s28201v.mongodb.net/?retryWrites=true&w=majority";

    public static MongoClient getMongoClient() {
        return MongoClients.create(CONNECTION_STRING);
    }
        public static boolean connect() {
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .serverApi(serverApi)
                    .build();
            // Create a new client and connect to the server
            try (MongoClient mongoClient = MongoClients.create(settings)) {
                try {
                    // Send a ping to confirm a successful connection
                    MongoDatabase database = mongoClient.getDatabase("admin");
                    database.runCommand(new Document("ping", 1));
                    System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                    return true;
                } catch (MongoException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    public static void insertDocument(String username, int score) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("leaderboarddata");
        MongoCollection<Document> collection = database.getCollection("DataSet");

        Document document = new Document("Name", username).append("Score", score);
        collection.insertOne(document);

        if (collection.countDocuments() > 10) {
            Document lowestScoreDocument = collection.find().sort(Sorts.ascending("Score")).first();
            collection.deleteOne(new Document("_id", lowestScoreDocument.getObjectId("_id")));
        }
        mongoClient.close();
    }

    public List<Document> readLeaderboard()
    {
        List<Document> leaderboard = new ArrayList<>();
        MongoClient mongoClient = getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("leaderboarddata");
        MongoCollection<Document> collection = database.getCollection("DataSet");

        FindIterable<Document> iterable = collection.find().sort(Sorts.ascending("Score"));
        mongoClient.close();

        for(Document document: iterable)
        {
            leaderboard.add(document);
        }

        return leaderboard;
    }

}