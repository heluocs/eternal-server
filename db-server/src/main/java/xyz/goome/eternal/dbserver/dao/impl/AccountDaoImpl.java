package xyz.goome.eternal.dbserver.dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import xyz.goome.eternal.common.entity.Account;
import xyz.goome.eternal.dbserver.dao.AccountDao;
import xyz.goome.eternal.dbserver.utils.Constants;

/**
 * Created by matrix on 16/4/9.
 */
@Repository
public class AccountDaoImpl implements AccountDao {

    private static Gson gson = new GsonBuilder().create();

    @Override
    public Account getAccount(String account, String password) {
        MongoClient mongoClient = new MongoClient(Constants.MONGO_ADDR);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(Constants.MONGO_DBNAME);
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");
        BsonDocument bsonDocument = new BsonDocument();
        bsonDocument.append("account", new BsonString(account));
        bsonDocument.append("password", new BsonString(password));
        MongoCursor cursor = collection.find(bsonDocument).iterator();
        while (cursor.hasNext()) {
            Document doc = (Document) cursor.next();
            //TODO
            Account obj = gson.fromJson(doc.toJson(), Account.class);
            System.out.println("find document: " + cursor.next());
            return obj;
        }
        return null;
    }

    @Override
    public boolean addAccount(Account account) {
        MongoClient mongoClient = new MongoClient(Constants.MONGO_ADDR);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(Constants.MONGO_DBNAME);
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");
        Document document = new Document();
        document.append("account", account.getAccount());
        document.append("password", account.getPassword());
        document.append("authid", account.getAuthid());
        document.append("createTime", account.getCreateTime());
        collection.insertOne(document);
        mongoClient.close();

        return true;
    }

}
