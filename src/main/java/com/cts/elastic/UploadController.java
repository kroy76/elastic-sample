package com.cts.elastic;



import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;  

/**                    
 * @author Biswanath Mukherjee
 */

@RestController
public class UploadController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    
    @Autowired
    public ElasticsearchOperations elasticsearchTemplate;

  
    @PostMapping(value="/search", consumes = "application/json", produces = "application/json")
    public List<IndividualModel> searchFiles(@RequestBody IndividualModel docSearch) {
    	List<IndividualModel> retValue = new ArrayList<IndividualModel>();
    	
    	NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    	if(docSearch.getFirstName()!=null && !docSearch.getFirstName().isEmpty()) {
    		queryBuilder.withFilter(QueryBuilders.matchQuery("firstName",docSearch.getFirstName()));
    	}
    	
    	if(docSearch.getDob()!=null ) {
    		Date toDate = new Date(docSearch.getDob().getTime()+24*60*60*1000);
    		queryBuilder.withFilter(QueryBuilders.rangeQuery("dob").gte(docSearch.getDob()).lt(toDate));
    	}
    	
    	NativeSearchQuery searchQuery = queryBuilder.build();
    	SearchHits<IndividualModel> docs = 
    			elasticsearchTemplate.search(searchQuery, IndividualModel.class, IndexCoordinates.of("individual"));

    	for(int i=0; i< docs.getTotalHits(); i++) {
        	retValue.add(docs.getSearchHit(i).getContent());
    	}

		return retValue;
    }

    @GetMapping(value = "/initialize")
    public ResponseEntity<String> intialize() throws ParseException {
    	
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    	format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
    	
    	IndividualModel individual = new IndividualModel();
    	individual.setFirstName("Kaushik");
    	individual.setLastName("Roychowdhury");
    	individual.setDob(adjustDate(format.parse("12/04/1976")));
    	individual.setLastupd_timestamp(new Date());
    	elasticsearchTemplate.save(individual);

    	IndividualModel individual1 = new IndividualModel();
    	individual1.setFirstName("Roshni");
    	individual1.setLastName("Roychowdhury");
    	individual1.setDob(adjustDate(format.parse("01/10/1981")));
    	individual1.setLastupd_timestamp(new Date());    	
    	elasticsearchTemplate.save(individual1);
    	
    	IndividualModel individual2 = new IndividualModel();
    	individual2.setFirstName("Shouptik");
    	individual2.setLastName("Roychowdhury");
    	individual2.setDob(adjustDate(format.parse("05/09/2008")));
    	individual2.setLastupd_timestamp(new Date());
    	elasticsearchTemplate.save(individual2);
    	
    	IndividualModel individual3 = new IndividualModel();
    	individual3.setFirstName("Shourik");
    	individual3.setLastName("Roychowdhury");
    	individual3.setDob(adjustDate(format.parse("05/09/2017")));
    	individual3.setLastupd_timestamp(new Date());
    	elasticsearchTemplate.save(individual3);
    	
    	final String response = "Initialize successfully.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }    

    @PostMapping(value="/create", consumes = "application/json", produces = "application/json")
    public IndividualModel create(@RequestBody IndividualModel docSearch) {
    	IndividualModel retValue = new IndividualModel();
    	docSearch.setId(retValue.getId());
    	docSearch.setLastupd_timestamp(new Date());
    	elasticsearchTemplate.save(docSearch);
		return docSearch;
    }   
    
    @GetMapping(value = "/test")
    public ResponseEntity<String> testDate() throws ParseException {
    	
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    	//format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
    	
    	Date dt=format.parse("12/04/1976");

    	SimpleDateFormat formatL = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ" );
    	SimpleDateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ" );
    	formatU.setTimeZone(TimeZone.getTimeZone("UTC")); 
    	
    	LOGGER.info(" Date format Local :"+formatL.format(dt));
    	LOGGER.info(" Date format UTC :"+formatU.format(dt));
    	
    	Date dt1 = adjustDate(dt);
    	
    	LOGGER.info(" Adjusted Date format Local :"+formatL.format(dt1));
    	LOGGER.info(" Adjusted Date format UTC :"+formatU.format(dt1));
    	
    	final String response = "Initialize successfully.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }    
   
    public Date adjustDate(Date input) {
    	Calendar cal = Calendar.getInstance();
    	long mills = cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET) ;
    	LOGGER.info(" Adjusted Mills :"+mills);
    	long dateInMills = input.getTime() + mills;
    	return new Date(dateInMills);
    }
    
}
