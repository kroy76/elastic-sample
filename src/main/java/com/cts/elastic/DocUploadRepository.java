package com.cts.elastic;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Biswanath Mukherjee
 */


//@Repository
//public interface DocUploadRepository extends CrudRepository<CommonModel, Long> {
//    boolean saveDocumentsByDocType(String docType);
//}

@Repository
public interface DocUploadRepository extends ElasticsearchRepository<IndividualModel, String> {
	 
}
