package models;

import utils.StringGenerate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bin")
public class Bin implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, updatable = false)
    private String name;

    @Column(name = "created")
    private long created;

    @Column(name = "updated")
    private long updated;

    @Column(name = "requestCount")
    private int requestCount;


    @OneToMany(mappedBy="bin")
    private Set<Request> requestSet = new HashSet<>(0);

    public Bin() {
        long now = new Date().getTime();
        created = now;
        updated = now;
        requestCount = 0;
        name = StringGenerate.Generate(6);
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setRequest(Request request) {
        requestSet.add(request);
    }
//    public void setId(long id) {
//        this.id = id;
//    }

}
