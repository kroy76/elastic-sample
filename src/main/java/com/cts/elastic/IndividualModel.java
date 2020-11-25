package com.cts.elastic;


//import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @author Biswanath Mukherjee
 */

//@Table(name = "ofg_docstore_data")
//@Entity
@Document(indexName = "individual")
public class IndividualModel implements Serializable {
	
	private static SequenceGenerator sequence = new SequenceGenerator();
	
	public IndividualModel() {
		id = sequence.nextId();
	}

	public static SequenceGenerator getSequence() {
		return sequence;
	}

	public static void setSequence(SequenceGenerator sequence) {
		IndividualModel.sequence = sequence;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getLastupd_timestamp() {
		return lastupd_timestamp;
	}

	public void setLastupd_timestamp(Date lastupd_timestamp) {
		this.lastupd_timestamp = lastupd_timestamp;
	}


	@Id
	@Field(type = FieldType.Long)
    private Long id;

    private String firstName;

    private String lastName;

    //@JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSZ")
    private Date dob;

    //@JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSZ")
    private Date lastupd_timestamp;

}
