package xyz.goome.eternal.dbserver.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import xyz.goome.eternal.common.entity.Account;

import java.util.Date;

/**
 * Created by matrix on 16/4/26.
 */
public class MongoUtil {

    private static final String DBNAME = "eternal";

    private static MongoUtil instance = new MongoUtil();

    private static Gson gson = new GsonBuilder().create();

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    static {
        mongoClient = new MongoClient("127.0.0.1");
        mongoDatabase = mongoClient.getDatabase(DBNAME);
    }

    private MongoUtil() {

    }

    public static MongoUtil getInstance() {
        return instance;
    }

    public static MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public static void add() {
        MongoClient mongoClient = new MongoClient("127.0.0.1");
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DBNAME);
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");
        Document document = new Document();
        document.append("account", "matrix");
        document.append("password", "123456");
        document.append("authid", "1234567890");
        document.append("createTime", new Date());
        collection.insertOne(document);
        mongoClient.close();
    }

    public static void find() {
        MongoClient mongoClient = new MongoClient("127.0.0.1");
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DBNAME);
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");
        Document result = collection.find(Filters.eq("username", "matrix")).first();
        System.out.println(result);
        mongoClient.close();
    }

    public static void find2() {
        MongoClient mongoClient = new MongoClient("127.0.0.1");
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DBNAME);
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");

        BsonDocument bsonDocument = new BsonDocument();
        bsonDocument.append("account", new BsonString("matrix"));
        bsonDocument.append("password", new BsonString("123456"));
        MongoCursor cursor = collection.find(bsonDocument).iterator();
        while (cursor.hasNext()) {
            Document doc = (Document) cursor.next();
            System.out.println("find document: " + doc.toJson());
            Account account = gson.fromJson(doc.toJson(), Account.class);
            System.out.println(account.getAuthid());

        }
    }

    public static void main(String[] args) {
//        add();
        find2();
    }

}
