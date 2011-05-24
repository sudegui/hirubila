package com.m4f.utils.feeds.events.service.ifc;

import java.util.Date;
import java.util.List;
import com.m4f.utils.feeds.events.model.Dump;

public interface DumpService {
	
	Dump createDump();
	void save(Dump d) throws Exception;
	void delete(Dump d) throws Exception;
	void deleteAllDumps() throws Exception;
	Dump getDump(Long id) throws Exception;
	List<Dump> getAllDumps() throws Exception;
	Long countDumps();
	Long countDumpsByOwner(Long ownerId);
	
	List<Dump> getDumpsByOwner(Long ownerId, int init, int end, String ordering);
	List<Dump> getDumpsByOwner(Long ownerId, int init, int end, Date start, Date finish, String ordering) throws Exception;
	Dump getLastDumpByOwner(Long ownerId) throws Exception;
}