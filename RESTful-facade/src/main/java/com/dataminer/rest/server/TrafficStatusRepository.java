package com.dataminer.rest.server;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "trafficStatus", path = "traffstatus")
public interface TrafficStatusRepository extends PagingAndSortingRepository<TrafficStatus, Long> {

	List<TrafficStatus> findByTime(@Param("time") String name);

}