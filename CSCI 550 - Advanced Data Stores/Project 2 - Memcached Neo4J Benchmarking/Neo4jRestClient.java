package neo4j;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.neo4j.graphdb.Transaction;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;


import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import com.meetup.memcached.SockIOPool;
import common.CacheUtilities;

import edu.usc.bg.base.ByteIterator;
import edu.usc.bg.base.DB;
import edu.usc.bg.base.DBException;
import edu.usc.bg.base.ObjectByteIterator;
import edu.usc.bg.base.StringByteIterator;

public class Neo4jRestClient extends DB 
{
	private static RestAPI restGraphDB;
	private static QueryEngine<Map<String,Object>> restEngine;

	private static boolean enableMemcache=true;
	private static MemcachedClient mcc;
	private static boolean indexFlag= false;
	/*private static Semaphore crtcl = new Semaphore(1, true);
	private static Index<Node> uidIndex;
	private static Index<Node> ridIndex;
	private static Index<Relationship> midIndex;*/
	/*private static RestIndexManager index;
	private static Index<Node> uidIndex;
	private static Index<Node> ridIndex;
	private static RelationshipIndex midIndex;*/
	//private static Index<Relationship> midIndex;
	//private static RestIndex<Relationship> midIndex;

	/*private static enum RelTypes implements RelationshipType
	{
		OWNS,
		POSTS,
		FRIENDS,
		INVITES,
		MANIPULATIONS,
		UNKNOWN
	};

	private static enum NodeLabels implements Label
	{
		USERS,
		RESOURCES,
		UNKNOWN
	};*/

	@Override
	public boolean init() throws DBException 
	{
		restGraphDB = new RestAPIFacade("http://localhost:7474/db/data/");
		if (restEngine == null)
			restEngine = new RestCypherQueryEngine(restGraphDB);
		createIndexes();
		if(enableMemcache)
		{
			//BasicConfigurator.configure();
			String[] servers = {"localhost:11211"};
			SockIOPool pool = SockIOPool.getInstance();
			pool.setServers( servers );
			pool.setFailover( true );
			pool.setInitConn( 10 );
			pool.setMinConn( 5 );
			pool.setMaxConn( 250 );
			pool.setMaintSleep( 30 );
			pool.setNagle( false );
			pool.setSocketTO( 3000 );
			pool.setAliveCheck( true );
			pool.initialize();

			try {
				mcc = new MemcachedClient(new BinaryConnectionFactory(),
						AddrUtil.getAddresses("localhost:11211"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*String value=new String("random");
			String key=new String("test");
			try {
				if(!common.CacheUtilities.CacheSet(mcc, key, value.getBytes(), true, 10000, false))
				{
					throw new Exception("Error calling WhalinMemcached set");
				}
				//while(setResult.isDone() == false);
				//cacheclient.shutdown();
			} catch (Exception e1) {
				System.out.println("Error in ApplicationCacheClient, failed to insert the key-value pair in the cache.");
				e1.printStackTrace(System.out);

			}
			String check=common.CacheUtilities.CacheGet(mcc, "test", false ).toString();
			System.out.println(check);
			//com.meetup.memcached.Logger.getLogger( MemcachedClient.class.getName() ).setLevel( com.meetup.memcached.Logger.LEVEL_WARN );
			 */
		}
		/*Transaction tx = restGraphDB.beginTx();
		try
		{
			uidIndex=restGraphDB.index().forNodes("USERS");
			ridIndex=restGraphDB.index().forNodes("RESOURCES");
			midIndex=restGraphDB.index().forRelationships("MANIPULATIONS");
			tx.success();
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		} 
		finally{
			tx.close();
		}*/

		/*Transaction tx=graphDB.beginTx();
		try
		{
			index= restGraphDB.index();
			uidIndex=index.forNodes("users");
			ridIndex=index.forNodes("resources");
			midIndex=index.forRelationships("manipulations");
			tx.success();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.failure();
		}
		finally
		{
			tx.close();
		}*/
		return true;
	}

	public synchronized void  createIndexes() 
	{
		if (indexFlag)
		{
			Transaction tx = restGraphDB.beginTx();
			try 
			{
				String query=new String("CREATE INDEX ON :USERS(userid)");
				restEngine.query(query, Collections.<String, Object> emptyMap());
				query=new String("CREATE INDEX ON :RESOURCES(rid)");
				restEngine.query(query, Collections.<String, Object> emptyMap());
				tx.success();
			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			}
			finally {
				tx.close();
			}
			indexFlag=false;
		}	
		return;
	}

	@Override
	public void cleanup(boolean warmup) throws DBException 
	{
		return;
	}

	public int insertEntity(String entitySet, String entityPK,
			HashMap<String, ByteIterator> values, boolean insertImage) 
	{
		int retval=-1;
		Transaction tx=restGraphDB.beginTx();
		try
		{
			Map<String, Object> props=new HashMap<String,Object>();

			if(entitySet.equalsIgnoreCase("users"))
			{
				//System.out.println("Loading users");
				int uid=Integer.parseInt(entityPK);
				int zero=0;
				props.put("userid", uid);
				props.put("confirmedcount", zero);
				props.put("pendingcount", zero);
				props.put("resourcecount", zero);
				for (String key: values.keySet())
				{
					if ((insertImage)&&(key.equalsIgnoreCase("pic") || key.equalsIgnoreCase("tpic")))
					{
						byte[] profilePic = ((ObjectByteIterator)values.get(key)).toArray();
						props.put(key, profilePic);					
					}
					else
						props.put(key, values.get(key).toString());
				}

				Map<String,Object> params=new HashMap<String,Object>();
				params.put("props", props);
				String query=new String("CREATE (u:USERS{props})");
				restEngine.query(query, params);

				/*QueryResult<Map<String,Object>> queryResult= null;
				queryResult=restEngine.query(query, params);
				Iterator<Map<String, Object>> iterator=queryResult.iterator();
				if(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next();  
					for(String key: row.keySet())
					{
						Node newNode= (Node) row.get(key);
						//System.out.println(key+":"+row.get(key).toString());
						//uidIndex.add(newNode,"userid",uid);
					}
				}


				Map<String, String> config = new HashMap<String, String>();
		        config.put("provider", "lucene");
		        config.put("type", "fulltext");
				final Index<Node> index = restGraphDB.index().forNodes("text-index", config);
			     index.add(node(), "text", "any");
				uidIndex.add(graphNode,"uid",member);*/
				retval=0;
			}
			else if(entitySet.equalsIgnoreCase("resources"))
			{
				//System.out.println("Loading resources");
				int rid=Integer.parseInt(entityPK);
				int owner=-1,wallUser=-1;
				props.put("rid", rid);
				for (String key: values.keySet())
				{
					if (key.equalsIgnoreCase("creatorid"))
						owner=Integer.parseInt(values.get(key).toString());
					else if  (key.equalsIgnoreCase("walluserid"))
						wallUser=Integer.parseInt(values.get(key).toString());
					else if (key.equalsIgnoreCase("body")&& insertImage)
					{
						byte[] body = ((ObjectByteIterator)values.get(key)).toArray();
						props.put(key, body);
					}
					else
						props.put(key, values.get(key).toString());
				}
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("props", props);
				String query= new String("MATCH (u1:USERS{userid:"+owner+"}),(u2:USERS{userid:"+wallUser+"})" +
						" CREATE (n:RESOURCES{props}),(u1)-[r1:OWNS]->(n),(n)-[r2:POSTS]->(u2) " +
						"SET u1.resourcecount=u1.resourcecount+1");
				restEngine.query(query, params);

				/*QueryResult<Map<String,Object>> queryResult= null;
				queryResult=restEngine.query(query, params);
				Iterator<Map<String, Object>> iterator=queryResult.iterator();
				if(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next();  
					for(String key: row.keySet())
					{
						Node newNode= (Node) row.get(key);
						//ridIndex.add(newNode,"rid",rid);
					}
				}

				if(owner!=-1 && wallUser!=-1)
				{
					query=new String("MATCH (u1:USERS{userid:"+owner+"}),(n:RESOURCES{rid:"+rid+"})," +
							"(u2:USERS{userid:"+wallUser+"})" +
							" CREATE (u1)-[r1:OWNS]->(n),(n)-[r2:POSTS]->(u2)");
					restEngine.query(query, Collections.<String, Object> emptyMap());
				}*/
				retval=0;
			}
			else
			{
				System.out.println("Invalid node type");
			}
			tx.success();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.failure();
		}
		finally
		{
			tx.close();
		}
		return retval;
	}

	/*private static String getKeyValue(String key,String value,int profileOwnerID, int requesterID)
	{
		String addKey=null;
		if(key.equalsIgnoreCase("reltype"))
		{
			if(value.equalsIgnoreCase("OWNS"))
				addKey=new String("resourcecount");
			else if(value.equalsIgnoreCase("FRIENDS"))
				addKey=new String("confirmedcount");
			else if(value.equalsIgnoreCase("INVITES"))
				if(profileOwnerID != requesterID)
					addKey=new String("pendingcount");
		}
		return addKey;
	}

	private static void getCount(int profileOwnerID, int requesterID,HashMap<String, ByteIterator> result) 
	{
		QueryResult<Map<String,Object>> queryResult= null;
		String query=null;
		query=new String("MATCH (n:USERS{userid:"+profileOwnerID+"})<-[r]->(n1)RETURN type(r) as reltype,count(r) as count " +
				" ORDER BY count DESC");

		queryResult=restEngine.query(query,Collections.<String, Object> emptyMap());
		Iterator<Map<String, Object>> iterator=queryResult.iterator();
		while(iterator.hasNext()) 
		{ 
			Map<String,Object> row= iterator.next(); 
			String addKey=null;
			addKey=getKeyValue("reltype",row.get("reltype").toString(),profileOwnerID,requesterID);
			if(addKey !=null)
				result.put(addKey, new StringByteIterator(row.get("count").toString()));
		}

		return;
	}*/

	private static int getUserProperties(int userid,boolean insertImage,HashMap<String, ByteIterator> result)
	{
		QueryResult<Map<String,Object>> queryResult= null;
		if(insertImage)
		{
			queryResult=restEngine.query("MATCH (n:USERS) where n.userid="+userid+
					" RETURN n.userid,n.username,n.pw,n.fname,n.lname,n.gender,n.dob," +
					"n.jdate,n.ldate,n.address,n.email,n.tel,n.pic,n.tpic,n.confirmedcount,n.pendingcount,n.resourcecount",
					Collections.<String, Object> emptyMap());
		}
		else
		{
			queryResult=restEngine.query("MATCH (n:USERS) where n.userid="+userid+
					" RETURN n.userid,n.username,n.pw,n.fname,n.lname," +
					"n.gender,n.dob,n.jdate,n.ldate,n.address,n.email,n.tel,n.confirmedcount,n.pendingcount,n.resourcecount",  
					Collections.<String, Object> emptyMap());	
		}
		Iterator<Map<String, Object>> iterator=queryResult.iterator(); 

		if(iterator.hasNext()) 
		{ 
			Map<String,Object> row= iterator.next();  
			for(String key: row.keySet())
			{
				if(key.equalsIgnoreCase("pic")|| key.equalsIgnoreCase("tpic"))
					if(insertImage)
						result.put(key, new ObjectByteIterator((byte[])row.get(key)));
					else
						continue;
				else
					result.put(key, new StringByteIterator(row.get(key).toString()));
			}
		}
		return 0;
	}

	@Override
	public int viewProfile(int requesterID, int profileOwnerID,
			HashMap<String, ByteIterator> result, boolean insertImage,
			boolean testMode) 
	{
		int retval=-1;
		//System.out.println("viewprofile");
		if(enableMemcache)
		{
			String memKey=null;
			if(profileOwnerID==requesterID)
				memKey=new String("vp_"+profileOwnerID+"_"+requesterID);
			else
				memKey=new String("vp_"+profileOwnerID+"_others");
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallHashMap(result,byteArray);
			/*else
				System.out.println("bad get");*/
			retval=0;
			/*if(temp!=null)
			{
				for(String key:temp.keySet())
					result.put(key, temp.get(key));
				retval=0;
			}*/
		}
		if(!enableMemcache || result.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try
			{
				getUserProperties(profileOwnerID,insertImage,result);
				//getCount(profileOwnerID,requesterID,result);
				if(result != null)
					retval=0;
				tx.success();

				if(enableMemcache)
				{
					String memKey=null;
					if(profileOwnerID==requesterID)
						memKey=new String("vp_"+profileOwnerID+"_"+requesterID);
					else
						memKey=new String("vp_"+profileOwnerID+"_others");

					byte[] byteArray=CacheUtilities.SerializeHashMap(result);
					if (byteArray != null)
						mcc.set(memKey,180, byteArray);
					else 
						System.out.println("bad set");
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}

		}
		return retval;
	}

	@Override
	public int listFriends(int requesterID, int profileOwnerID,
			Set<String> fields, Vector<HashMap<String, ByteIterator>> result,
			boolean insertImage, boolean testMode) 
	{
		int retval = -1;
		// System.out.println("listfriends");
		if(enableMemcache)
		{
			String memKey=new String("lf_"+profileOwnerID);
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallVectorOfHashMaps(byteArray, result);
			retval=0;
			/*@SuppressWarnings("unchecked")

			Vector<HashMap<String, ByteIterator>> temp=(Vector<HashMap<String, ByteIterator>>) mcc.get(memKey);
			if(temp!=null)
			{
				int i=0;
				for(i=0;i<temp.size();i++)
					result.add(temp.get(i));
				retval=0;
			}*/
		}
		if(!enableMemcache || result.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try 
			{
				QueryResult<Map<String, Object>> queryResult = null;
				if (insertImage)
				{
					String query=new String("MATCH (n:USERS{userid:"+profileOwnerID+"})-[r:FRIENDS]->(n1:USERS) "
							+ "RETURN n1.userid,n1.username,n1.fname, n1.lname, n1.email, n1.address, n1.tel, "
							+ "n1.gender, n1.jdate, n1.ldate, n1.tpic");
					queryResult = restEngine.query(query,Collections.<String, Object> emptyMap());
				}
				else
				{
					String query=new String("MATCH (n:USERS{userid:"+profileOwnerID+"})-[r:FRIENDS]->(n1:USERS) "
							+ "RETURN n1.userid,n1.username,n1.fname, n1.lname, n1.email, n1.address, n1.tel," 
							+ " n1.gender, n1.jdate, n1.ldate");
					queryResult = restEngine.query(query,Collections.<String, Object> emptyMap());
				}
				Iterator<Map<String, Object>> iterator = queryResult.iterator();

				while (iterator.hasNext()) 
				{
					Map<String, Object> row = iterator.next();
					HashMap<String, ByteIterator> values = new HashMap<String, ByteIterator>();
					for (String key : row.keySet()) 
					{
						if (key.equalsIgnoreCase("tpic") && insertImage)
							values.put(key,	new ObjectByteIterator((byte[]) row.get(key)));
						else
							values.put(key, new StringByteIterator(row.get(key).toString()));
					}
					result.add(values);
				}
				retval = 0;
				tx.success();


				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("lf_"+profileOwnerID);
					byte[] byteArray=CacheUtilities.SerializeVectorOfHashMaps(result);
					if (byteArray != null)
						mcc.set(memKey,180, byteArray);
					else 
						System.out.println("bad set");
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();

			} 
			finally {
				tx.close();
			}
			if (retval == -1)
				System.out.println("Error in list friend");

		}

		return retval;
	}

	@Override
	public int viewFriendReq(int profileOwnerID,
			Vector<HashMap<String, ByteIterator>> results, boolean insertImage,
			boolean testMode)
	{
		//System.out.println("viewfriendreq");
		int retval=-1;
		if(enableMemcache)
		{
			String memKey=new String("vfr_"+profileOwnerID);
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallVectorOfHashMaps(byteArray, results);
			retval=0;
			/*@SuppressWarnings("unchecked")
			Vector<HashMap<String, ByteIterator>> temp=(Vector<HashMap<String, ByteIterator>>) mcc.get(memKey);
			if(temp!=null)
			{
				int i=0;
				for(i=0;i<temp.size();i++)
					results.add(temp.get(i));
				retval=0;
			}*/
		}
		if(!enableMemcache || results.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try
			{
				QueryResult<Map<String,Object>> queryResult= null;
				if(insertImage)
				{
					queryResult=restEngine.query("MATCH (n:USERS {userid:"+profileOwnerID+"})<-[r:INVITES]-(u:USERS)" +
							" RETURN u.userid,u.username,u.pw,u.fname,u.lname,u.gender,u.dob," +
							"u.jdate,u.ldate,u.address,u.email,u.tel,u.tpic",
							Collections.<String, Object> emptyMap());
				}
				else
				{
					queryResult=restEngine.query("MATCH (n:USERS { userid:"+profileOwnerID+" })<-[r:INVITES]-(u:USERS)" +
							" RETURN u.userid,u.username,u.pw,u.fname,u.lname,u.gender,u.dob," +
							"u.jdate,u.ldate,u.address,u.email,u.tel",
							Collections.<String, Object> emptyMap());	
				}
				Iterator<Map<String, Object>> iterator=queryResult.iterator(); 

				while(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next(); 
					HashMap<String,ByteIterator> values=new HashMap<String,ByteIterator>();
					for(String key: row.keySet())
					{
						if(key.equalsIgnoreCase("tpic") && insertImage)
							values.put(key, new ObjectByteIterator((byte[])row.get(key)));
						else
							values.put(key,new StringByteIterator(row.get(key).toString()));
					}
					results.add(values);
				}
				if(results != null)
					retval=0;
				tx.success();

				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("vfr_"+profileOwnerID);
					mcc.set(memKey, 180, CacheUtilities.SerializeVectorOfHashMaps(results));
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}

		}
		return retval;
	}

	@Override
	public int acceptFriend(int inviterID, int inviteeID) 
	{
		// System.out.println("acceptfriends");
		int result = -1;
		Transaction tx = restGraphDB.beginTx();
		try 
		{
			String query=new String("MATCH (u:USERS{userid:"+inviterID+"})-[r:INVITES]->(u1:USERS{userid:"+inviteeID+"})"
					+ " DELETE r"
					+ " CREATE (u)-[r1:FRIENDS]->(u1)"
					+ " CREATE (u1)-[r2:FRIENDS]->(u)" 
					+ " SET u1.pendingcount=u1.pendingcount-1, "
					+ "u.confirmedcount=u.confirmedcount+1, u1.confirmedcount=u1.confirmedcount+1");
			restEngine.query(query, Collections.<String, Object> emptyMap());
			result=0;
			tx.success();
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		}
		finally {
			tx.close();
		}
		if (result == -1)
			System.out.println("Error in accept friend");

		if(enableMemcache)
		{
			invalidate_view_profile(inviteeID,-1);
			invalidate_view_profile(inviterID,-1);
			invalidate_view_friend_req(inviteeID);
			invalidate_query_pending_friendship(inviteeID);
			invalidate_get_initial_stats();
			invalidate_query_confirmed_friendship(inviteeID);
			invalidate_query_confirmed_friendship(inviterID);
			invalidate_list_friends(inviteeID);
			invalidate_list_friends(inviterID);
		}

		return result;
	}

	private void invalidate_list_friends(int profileOwnerId)
	{
		String memKey=null;
		memKey=new String("lf_"+profileOwnerId);
		if(mcc.get(memKey)!=null)
		{
			try 
			{
				mcc.delete(memKey);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return;
	}

	private void invalidate_query_confirmed_friendship(int profileOwnerId)
	{
		String memKey=null;
		memKey=new String("qcf_"+profileOwnerId);
		if(mcc.get(memKey)!=null)
		{
			try 
			{
				mcc.delete(memKey);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return;
	}

	@Override
	public int rejectFriend(int inviterID, int inviteeID) 
	{
		//System.out.println("rejectfriends");
		int result=-1;
		Transaction tx = restGraphDB.beginTx();
		try
		{
			if (inviterID >= 0 && inviteeID >= 0) 
			{
				String query=new String("MATCH (u:USERS{userid:"+inviterID+"})-[r:INVITES]->(u1:USERS{userid:"+inviteeID+"})"
						+ " DELETE r SET u1.pendingcount=u1.pendingcount-1");
				restEngine.query(query, Collections.<String, Object> emptyMap());
			}
			result=0;
			tx.success();
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		} 
		finally{
			tx.close();
		}
		if(result==-1)
			System.out.println("error in reject friend");
		if(enableMemcache)
		{
			invalidate_view_profile(inviteeID,-1);
			invalidate_view_friend_req(inviteeID);
			invalidate_query_pending_friendship(inviteeID);
			invalidate_get_initial_stats();
		}
		return result;
	}

	@Override
	public int inviteFriend(int inviterID, int inviteeID) 
	{
		// System.out.println("invitefriends");
		int result = -1;
		Transaction tx = restGraphDB.beginTx();
		try 
		{
			if (inviterID >= 0 && inviteeID >= 0) 
			{
				String query=new String("MATCH (u:USERS{userid:"+inviterID+"}), (u1:USERS{userid:"+inviteeID+"})"
						+ " CREATE (u)-[r:INVITES]->(u1)"
						+ " SET u1.pendingcount=u1.pendingcount+1");
				restEngine.query(query, Collections.<String, Object> emptyMap());
			}
			result=0;
			tx.success();
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		}
		finally {
			tx.close();
		}
		if (result == -1)
			System.out.println("Error in invite friend");

		if(enableMemcache)
		{
			invalidate_view_profile(inviteeID,-1);
			invalidate_view_friend_req(inviteeID);
			invalidate_query_pending_friendship(inviteeID);
			invalidate_get_initial_stats();
		}
		return result;
	}

	private void invalidate_view_profile(int profileOwner, int requester)
	{
		String memKey=null;
		if(profileOwner==requester)
			memKey=new String("vp_"+profileOwner+"_"+requester);
		else
			memKey=new String("vp_"+profileOwner+"_others");
		if(mcc.get(memKey)!=null)
		{
			try 
			{
				mcc.delete(memKey);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return;
	}

	private void invalidate_view_friend_req(int profileOwner) 
	{
		String memKey=null;
		memKey=new String("vfr_"+profileOwner);
		if(mcc.get(memKey)!=null)
		{
			try 
			{
				mcc.delete(memKey);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return;

	}

	private void invalidate_query_pending_friendship(int profileOwner) 
	{
		String memKey=null;
		memKey=new String("qpf_"+profileOwner);
		if(mcc.get(memKey)!=null)
		{
			try 
			{
				mcc.delete(memKey);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return;
	}

	private void invalidate_get_initial_stats() 
	{
		String memKey=null;
		memKey=new String("get_init_stats");
		if(mcc.get(memKey)!=null)
		{
			try 
			{
				mcc.delete(memKey);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return;
	}

	@Override
	public int viewTopKResources(int requesterID, int profileOwnerID, int k,
			Vector<HashMap<String, ByteIterator>> result) 
	{
		//System.out.println("viewtopkresources");
		int retval=-1;
		if(enableMemcache)
		{
			String memKey=new String("vtk_"+profileOwnerID);
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallVectorOfHashMaps(byteArray, result);
			retval=0;
			/*@SuppressWarnings("unchecked")
			Vector<HashMap<String, ByteIterator>> temp=(Vector<HashMap<String, ByteIterator>>) mcc.get(memKey);
			if(temp!=null)
			{
				int i=0;
				for(i=0;i<temp.size();i++)
					result.add(temp.get(i));
				retval=0;
			}*/
		}
		if(!enableMemcache || result.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try
			{
				QueryResult<Map<String,Object>> queryResult= null;
				String query=new String("MATCH (u:USERS { userid:"+profileOwnerID+" })<-[r:POSTS]-(n:RESOURCES)<-[r1:OWNS]-(u1:USERS)" +
						" RETURN n.rid,n.type,n.body,n.doc,u1.userid as creatorid ORDER BY n.rid DESC LIMIT "+k);
				queryResult=restEngine.query(query,Collections.<String, Object> emptyMap());	
				Iterator<Map<String, Object>> iterator=queryResult.iterator(); 

				while(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next(); 
					HashMap<String,ByteIterator> values=new HashMap<String,ByteIterator>();
					for(String key: row.keySet())
					{
						values.put(key,new StringByteIterator(row.get(key).toString()));
					}
					values.put("walluserid",new StringByteIterator(Integer.toString(profileOwnerID)));
					result.add(values);
				}
				if(result != null)
					retval=0;
				tx.success();

				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("vtk_"+profileOwnerID);
					mcc.set(memKey, 180, CacheUtilities.SerializeVectorOfHashMaps(result));
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}
		}

		return retval;
	}

	@Override
	public int getCreatedResources(int creatorID,
			Vector<HashMap<String, ByteIterator>> result) 
	{
		//System.out.println("getcreatedresources");
		int retval=-1;
		if(enableMemcache)
		{
			String memKey=new String("gcr_"+creatorID);
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallVectorOfHashMaps(byteArray,result);
			retval=0;
			/*@SuppressWarnings("unchecked")
			Vector<HashMap<String, ByteIterator>> temp=(Vector<HashMap<String, ByteIterator>>) mcc.get(memKey);
			if(temp!=null)
			{
				int i=0;
				for(i=0;i<temp.size();i++)
					result.add(temp.get(i));
				retval=0;
			}*/
		}
		if(!enableMemcache || result.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try
			{
				QueryResult<Map<String,Object>> queryResult= null;
				String query=new String("MATCH (u:USERS{userid:"+creatorID+"})-[r:OWNS]->(n:RESOURCES)" +
						"-[r1:POSTS]->(u1:USERS) " +
						"RETURN n.rid,n.type,n.body,n.doc,u1.userid as walluserid");
				queryResult=restEngine.query(query,Collections.<String, Object> emptyMap());	
				Iterator<Map<String, Object>> iterator=queryResult.iterator(); 

				while(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next(); 
					HashMap<String,ByteIterator> values=new HashMap<String,ByteIterator>();
					for(String key: row.keySet())
						values.put(key,new StringByteIterator(row.get(key).toString()));
					values.put("creatorid",new StringByteIterator(Integer.toString(creatorID)));
					result.add(values);
				}
				tx.success();

				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("gcr_"+creatorID);
					mcc.set(memKey, 180, CacheUtilities.SerializeVectorOfHashMaps(result));
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}
		}
		return retval;
	}

	@Override
	public int viewCommentOnResource(int requesterID, int profileOwnerID,
			int resourceID, Vector<HashMap<String, ByteIterator>> result) 
	{
		int retval=-1;
		//System.out.println("ViewCommentonResource");
		if(enableMemcache)
		{
			String memKey=new String("vcor_"+resourceID);
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallVectorOfHashMaps(byteArray, result);
			retval=0;
			/*@SuppressWarnings("unchecked")
			Vector<HashMap<String, ByteIterator>> temp=(Vector<HashMap<String, ByteIterator>>) mcc.get(memKey);
			if(temp!=null)
			{
				int i=0;
				for(i=0;i<temp.size();i++)
					result.add(temp.get(i));
				retval=0;
			}*/
		}
		if(!enableMemcache || result.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try
			{ 
				QueryResult<Map<String,Object>> queryResult= null;
				String q=new String("MATCH (n:RESOURCES{rid:"+resourceID+"})<-[r:MANIPULATIONS]-(u:USERS)" +
						"RETURN r.mid,r.timestamp,r.type,r.content,u.userid as modifierid");
				queryResult=restEngine.query(q,Collections.<String, Object> emptyMap());	
				Iterator<Map<String, Object>> iterator=queryResult.iterator(); 

				while(iterator.hasNext())
				{ 
					Map<String,Object> row= iterator.next();
					HashMap<String, ByteIterator> values=new HashMap<String, ByteIterator>();

					for(String key: row.keySet())
						values.put(key, new StringByteIterator(row.get(key).toString()));
					values.put("rid", new StringByteIterator(Integer.toString(resourceID)));
					values.put("creatorid", new StringByteIterator(Integer.toString(profileOwnerID)));
					result.add(values);
				}
				retval=0;
				tx.success();

				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("vcor_"+resourceID);
					mcc.set(memKey, 180, CacheUtilities.SerializeVectorOfHashMaps(result));
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}
		}
		return retval;
	}

	@Override
	public int postCommentOnResource(int commentCreatorID,
			int resourceCreatorID, int resourceID,
			HashMap<String, ByteIterator> values) 
	{
		int result =-1,mid=-1;
		//System.out.println("PostCommentonResource");
		Transaction tx = restGraphDB.beginTx();
		try
		{
			//StringBuffer props=new StringBuffer();
			Map<String,Object> props=new HashMap<String, Object>();
			for(String key: values.keySet())
			{
				if(key.equalsIgnoreCase("mid"))
				{
					mid=Integer.parseInt(values.get(key).toString());
					props.put(key, mid);
				}
				else if((key.equalsIgnoreCase("creatorid"))|| (key.equalsIgnoreCase("modifierid"))
						|| (key.equalsIgnoreCase("rid")))
					continue;
				else
					props.put(key, values.get(key).toString());
			}

			Map<String,Object> params=new HashMap<String, Object>();
			params.put("props", props);
			String q=new String("MATCH (u:USERS{userid:"+commentCreatorID+"}),(n:RESOURCES{rid:"+resourceID+"})" +
					" CREATE (u)-[r:MANIPULATIONS{props}]->(n) return r");
			restEngine.query(q, params);

			/*QueryResult<Map<String,Object>> queryResult= null;
			queryResult=restEngine.query(q, params);
			Iterator<Map<String, Object>> iterator=queryResult.iterator();
			if(iterator.hasNext()) 
			{ 
				Map<String,Object> row= iterator.next();  
				for(String key: row.keySet())
				{
					Relationship newRel= (Relationship) row.get(key);
					//midIndex.add(newRel,"mid",mid);
				}
			}*/

			result=0;
			tx.success();

			if(enableMemcache)
			{
				invalidate_view_comment_on_resource(resourceID);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		} 
		finally{
			tx.close();
		}
		if (result == -1)
			System.out.println("Error in postcomment");
		return 0;
	}

	private void invalidate_view_comment_on_resource(int resourceID) 
	{
		String memKey=null;
		memKey=new String("vcor_"+resourceID);
		if(mcc.get(memKey)!=null)
		{
			try 
			{
				mcc.delete(memKey);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return;
	}

	@Override
	public int delCommentOnResource(int resourceCreatorID, int resourceID,
			int manipulationID) 
	{
		int result=-1;
		//System.out.println("DelCommentonResource");
		Transaction tx = restGraphDB.beginTx();
		try
		{
			String query =new String("MATCH (n:RESOURCES{rid:"+resourceID+"})<-" +
					"[r:MANIPULATIONS{mid:"+manipulationID+"}]-(u:USERS)" +
					" DELETE r");
			restEngine.query(query, Collections.<String, Object> emptyMap());	
			result=0;
			tx.success();
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		} 
		finally{
			tx.close();
		}
		if (result == -1)
			System.out.println("Error in Delete comment on resource");

		if(enableMemcache)
		{
			invalidate_view_comment_on_resource(resourceID);
		}
		return result;
	}

	@Override
	public int thawFriendship(int friendid1, int friendid2) 
	{
		int result=-1;
		//System.out.println("thawfriendship");
		Transaction tx = restGraphDB.beginTx();
		try
		{
			if (friendid1 >= 0 && friendid2 >= 0) 
			{
				String query=new String("MATCH (u:USERS{userid:"+friendid1+"})<-[r:FRIENDS]->" +
						"(u1:USERS{userid:"+friendid2+"})"
						+ " DELETE r" 
						+ " SET u.confirmedcount=u.confirmedcount-1, "
						+ " u1.confirmedcount=u1.confirmedcount-1");
				restEngine.query(query, Collections.<String, Object> emptyMap());
			}
			result=0;
			tx.success();
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		} 
		finally{
			tx.close();
		}
		if(result==-1)
			System.out.println("error in reject friend");

		if(enableMemcache)
		{
			invalidate_view_profile(friendid1,-1);
			invalidate_view_profile(friendid2,-1);
			invalidate_query_confirmed_friendship(friendid1);
			invalidate_query_confirmed_friendship(friendid2);
			invalidate_list_friends(friendid1);
			invalidate_list_friends(friendid2);
			invalidate_get_initial_stats();
		}
		return result;
	}

	/*private static int getTotalNumOfFriends(int userid)
	{
		int retval=0;
		QueryEngine<Map<String, Object>> engine=new RestCypherQueryEngine(restGraphDB); 
		QueryResult<Map<String,Object>> result= 
				engine.query("match (n:USERS{userid:"+userid+"})-[r:FRIENDS]->(u:USERS) return count(r)", Collections.<String, Object> emptyMap()); 
		Iterator<Map<String, Object>> iterator=result.iterator(); 

		if(iterator.hasNext())
		{
			Map<String,Object> row= iterator.next(); 
			for(String key: row.keySet())
			{
				retval=Integer.parseInt(row.get(key).toString());
				break;
			}
		}
		return retval;
	}

	private static int getTotalResourcesCreated(int userid)
	{
		int retval=0;
		QueryEngine<Map<String, Object>> engine=new RestCypherQueryEngine(restGraphDB); 
		QueryResult<Map<String,Object>> result= 
				engine.query("match (n:USERS{userid:"+userid+"})-[r:OWNS]->(u:USERS) return count(r)", Collections.<String, Object> emptyMap()); 
		Iterator<Map<String, Object>> iterator=result.iterator(); 

		if(iterator.hasNext())
		{
			Map<String,Object> row= iterator.next(); 
			for(String key: row.keySet())
			{
				retval=Integer.parseInt(row.get(key).toString());
				break;
			}
		}
		return retval;
	}


	private static int getPendingFriends(int userid)
	{
		int retval=0;
		QueryEngine<Map<String, Object>> engine=new RestCypherQueryEngine(restGraphDB); 
		QueryResult<Map<String,Object>> result= 
				engine.query("match (n:USERS{userid:"+userid+"})<-[r:INVITES]-(u:USERS) return count(r)", Collections.<String, Object> emptyMap()); 
		Iterator<Map<String, Object>> iterator=result.iterator(); 

		if(iterator.hasNext())
		{
			Map<String,Object> row= iterator.next(); 
			for(String key: row.keySet())
			{
				retval=Integer.parseInt(row.get(key).toString());
				break;
			}
		}
		return retval;
	}*/

	@Override
	public HashMap<String, String> getInitialStats() 
	{
		HashMap<String, String> stats=new HashMap<String,String>();
		int totalNumOfUsers=0,totalNumOfFriends=0,totalNumOfPending=0,totalNumOfResource=0;
		int avgFriends=0,avgPending=0,avgResource=0;

		/*if(enableMemcache)
		{
			String memKey=new String("get_init_stats");
			/*@SuppressWarnings("unchecked")
			HashMap<String,String> temp=(HashMap<String,String>) mcc.get(memKey);
			if(temp!=null)
			{
				for(String key: temp.keySet())
					stats.put(key,temp.get(key).toString());
			}
		}*/
		if(!enableMemcache || stats.isEmpty())
		{

			Transaction tx = restGraphDB.beginTx();
			//System.out.println("getInitialStats");
			try
			{ 
				String query=new String("MATCH (n:USERS) with count(n) as usercount " +
						"match (n2:USERS)-[r2:INVITES|:FRIENDS|:OWNS]->() " +
						"return type(r2) as reltype,count(r2) as total ,usercount");
				QueryResult<Map<String,Object>> retval= restEngine.query(query, Collections.<String, Object> emptyMap()); 
				Iterator<Map<String, Object>> iterator=retval.iterator(); 
				while(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next(); 
					String key=new String("reltype");
					String value=row.get(key).toString();
					if(value.equalsIgnoreCase("invites"))
						totalNumOfPending=Integer.parseInt(row.get("total").toString());
					else if(value.equalsIgnoreCase("friends"))
						totalNumOfFriends=Integer.parseInt(row.get("total").toString());
					else if(value.equalsIgnoreCase("owns"))
						totalNumOfResource=Integer.parseInt(row.get("total").toString());
					totalNumOfUsers=Integer.parseInt(row.get("usercount").toString());
				}

				/*query=new String("match (u1:USERS)-[r:INVITES]->(u2:USERS) return count(r)");
				retval= restEngine.query(query, Collections.<String, Object> emptyMap()); 
				iterator=retval.iterator(); 
				if(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next(); 
					for(String key: row.keySet())
					{
						totalNumOfPending=Integer.parseInt(row.get(key).toString());
						break;
					}
				}

				query=new String("match (u1:USERS)-[r:FRIENDS]->(u2:USERS) return count(r)");
				retval= restEngine.query(query, Collections.<String, Object> emptyMap()); 
				iterator=retval.iterator(); 
				if(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next(); 
					for(String key: row.keySet())
					{
						totalNumOfFriends=Integer.parseInt(row.get(key).toString());
						break;
					}
				}

				query=new String("match (u:USERS)-[r:OWNS]->(n:RESOURCES) return count(r)");
				retval= restEngine.query(query, Collections.<String, Object> emptyMap()); 
				iterator=retval.iterator(); 
				if(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next(); 
					for(String key: row.keySet())
					{
						totalNumOfUsers=Integer.parseInt(row.get(key).toString());
						break;
					}
				}*/

				tx.success();

			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}

			if (totalNumOfUsers > 0)
			{
				avgFriends=totalNumOfFriends/totalNumOfUsers;
				avgPending=totalNumOfPending/totalNumOfUsers;
				avgResource=totalNumOfResource/totalNumOfUsers;
			}
			String strTotalUsers,strAvgFriends,strAvgPending,strAvgResources;
			strTotalUsers=new String(Long.toString(totalNumOfUsers));
			strAvgFriends=new String(Long.toString(avgFriends));
			strAvgPending=new String(Long.toString(avgPending));
			strAvgResources=new String(Long.toString(avgResource));

			stats.put("usercount", strTotalUsers);
			stats.put("avgfriendsperuser", strAvgFriends);
			stats.put("avgpendingperuser", strAvgPending);
			stats.put("resourcesperuser", strAvgResources);
			/*try
			{
				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("get_init_stats");
					ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				    ObjectOutputStream out = new ObjectOutputStream(byteOut);
				    out.writeObject(stats);
					mcc.set(memKey, 180, byteOut);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
			} */
		}
		return stats;
	}

	@Override
	public int CreateFriendship(int friendid1, int friendid2)
	{
		//System.out.println("CreateFriendship");
		int result = -1;
		Transaction tx = restGraphDB.beginTx();
		try 
		{
			String query=new String("MATCH (u1:USERS{userid:"+friendid1+"}),(u2:USERS{userid:"+friendid2+"})"
					+ " CREATE (u1)-[r1:FRIENDS]->(u2),(u2)-[r2:FRIENDS]->(u1) " +
					" SET u1.confirmedcount=u1.confirmedcount+1, u2.confirmedcount=u2.confirmedcount+1");
			restEngine.query(query, Collections.<String, Object> emptyMap());
			result=0;
			tx.success();
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			tx.failure();
		}
		finally {
			tx.close();
		}
		if (result == -1)
			System.out.println("Error in create friendship");
		return result;
	}

	@Override
	public void createSchema(Properties props) 
	{
		return;
	}

	@Override
	public int queryPendingFriendshipIds(int memberID,
			Vector<Integer> pendingIds) 
	{
		int pendingFriends=0;
		//System.out.println("query pending friends");
		if(enableMemcache)
		{
			String memKey=new String("qpf_"+memberID);
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallVectorOfInts(byteArray, pendingIds);
			pendingFriends=0;
			/*@SuppressWarnings("unchecked")
			Vector<Integer> temp=(Vector<Integer>) mcc.get(memKey);
			if(temp!=null)
			{
				int i=0;
				for(i=0;i<temp.size();i++)
					pendingIds.add(temp.get(i));
				pendingFriends=0;
			}*/
		}
		if(!enableMemcache || pendingIds.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try
			{ 
				QueryResult<Map<String,Object>> queryResult= null;
				queryResult=restEngine.query("MATCH (n:USERS { userid:"+memberID+" })<-[r:INVITES]-(n1:USERS) RETURN n1.userid",
						Collections.<String, Object> emptyMap());
				Iterator<Map<String, Object>> iterator=queryResult.iterator(); 

				while(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next();  
					for(String key: row.keySet())
					{
						pendingIds.add(Integer.parseInt(row.get(key).toString()));
						break;
					}
				}
				tx.success();

				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("qpf_"+memberID);

					mcc.set(memKey, 180, CacheUtilities.SerializeVectorOfInts(pendingIds));
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}

		}

		return pendingFriends;
	}

	@Override
	public int queryConfirmedFriendshipIds(int memberID,
			Vector<Integer> confirmedIds) 
	{
		int confirmedFriends=0;
		//System.out.println("query confirmed friends");
		if(enableMemcache)
		{
			String memKey=new String("qcf_"+memberID);
			byte[] byteArray=(byte[])mcc.get(memKey);
			if(byteArray != null)
				CacheUtilities.unMarshallVectorOfInts(byteArray, confirmedIds);
			confirmedFriends=0;
			/*@SuppressWarnings("unchecked")
			Vector<Integer> temp=(Vector<Integer>) mcc.get(memKey);
			if(temp!=null)
			{
				int i=0;
				for(i=0;i<temp.size();i++)
					confirmedIds.add(temp.get(i));
				confirmedFriends=0;
			}*/
		}
		if(!enableMemcache || confirmedIds.isEmpty())
		{
			Transaction tx = restGraphDB.beginTx();
			try
			{
				QueryResult<Map<String,Object>> queryResult= null;
				queryResult=restEngine.query("MATCH (n:USERS { userid:"+memberID+" })-[r:FRIENDS]->(n1:USERS) RETURN n1.userid",
						Collections.<String, Object> emptyMap());
				Iterator<Map<String, Object>> iterator=queryResult.iterator(); 

				while(iterator.hasNext()) 
				{ 
					Map<String,Object> row= iterator.next();  
					for(String key: row.keySet())
					{
						confirmedIds.add(Integer.parseInt(row.get(key).toString()));
						break;
					}
				}
				tx.success();

				if(enableMemcache)
				{
					String memKey=null;
					memKey=new String("qcf_"+memberID);
					mcc.set(memKey, 10, CacheUtilities.SerializeVectorOfInts(confirmedIds));
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace(System.out);
				tx.failure();
			} 
			finally{
				tx.close();
			}
		}
		return confirmedFriends;
	}

	/*public static void main(String args[]) 
	{
		try
		{
			Neo4jClient2 neo=new Neo4jClient2();
			neo.init();
			HashMap<String, ByteIterator>result=new HashMap<String,ByteIterator>();
			neo.viewProfile(1, 2, result, false, false);
			Vector<HashMap<String, ByteIterator>> values=new Vector<HashMap<String,ByteIterator>>();
			neo.listFriends(2, 3, null, values, false, false);
			neo.inviteFriend(1, 4);
			neo.acceptFriend(1, 4);
			neo.inviteFriend(2, 5);
			neo.rejectFriend(2, 5);
			result=new HashMap<String,ByteIterator>();
			neo.postCommentOnResource(2, 4, 4, result);
			values=new Vector<HashMap<String,ByteIterator>>();
			neo.viewCommentOnResource(2, 4, 4, values);
			neo.delCommentOnResource(resourceCreatorID, resourceID, manipulationID)

		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		return;
	}*/

}
