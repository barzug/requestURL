package models;

import utils.StringGenerate;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;


@Entity
@Table(name = "request")
public class Request implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, updatable = false)
    private String name;

    @Column(name = "created")
    private long created;

    @Column(name = "method")
    private String method;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "header")
    private  HashMap<String, String> header;

    @Column(name = "contentLength")
    private long contentLength;

    @Column(name = "remoteAddr")
    private String remoteAddr;

    @Column(name = "host")
    private String host;

    @Column(name = "requestURL")
    private String requestURL;

    @Column(name = "body")
    private String body;


    @ManyToOne
    @JoinColumn(name="bin_id", nullable=false)
    private Bin bin;
//    private HashMap<String, String> formValue;
//    private Vector<String> formFile;

    public Request(HttpServletRequest request, Bin bin) throws IOException {
        name = StringGenerate.Generate(12);
        created = new Date().getTime();
        method = request.getMethod();
        protocol = request.getProtocol();
        contentLength = request.getContentLength();
        remoteAddr = request.getRemoteAddr();
        host = request.getRemoteHost();
        requestURL = request.getRequestURI();


        //??
        body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        //??

        header = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            header.put(key, value);
        }

        this.bin = bin;

    }
}
