package com.paytech.paytechsystems.condira;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Relation {

    public String uid;
    public String author;
    public String name;
    public String relation, age, sex, parent, naration;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Relation() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Relation(String uid, String author, String name, String relation, String age, String sex,  String parent, String naration) {
        this.uid = uid;
        this.author = author;
        this.relation = relation;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.parent = parent;
        this.naration = naration;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("name", name);
        result.put("age", age);
        result.put("sex", sex);
        result.put("relation", relation);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("parent", parent);
        result.put("naration", naration);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
