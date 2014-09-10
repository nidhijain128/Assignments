import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import edu.usc.bg.base.ByteIterator;
import edu.usc.bg.base.DBException;
import edu.usc.bg.base.ObjectByteIterator;

public class HBaseClient extends edu.usc.bg.base.DB {
	static HBaseConfiguration hc;

	// onetime -schema -db HBase.HBaseDSClient

	// onetime -loadindex -db HBase.HBaseDSClient -P workloads/populateDB -p
	// threadcount=1 -p
	// insertimage=false -p imagesize=2 -p exportfile=results.txt

	public boolean init() throws DBException {
		hc = new HBaseConfiguration(new Configuration());
		return true;
	}

	@Override
	public int insertEntity(String entitySet, String entityPK,
			HashMap<String, ByteIterator> values, boolean insertImage) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "members");
			HTable ht1 = new HTable(hc, "resources");
			HTable ht2 = new HTable(hc, "friends");
			Put put = new Put(Bytes.toBytes(entityPK));
			int i = 0;
			if (entitySet.equalsIgnoreCase("users")) {
				put.add(Bytes.toBytes("details"), Bytes.toBytes("userId"),
						Bytes.toBytes(entityPK));
				for (String id : values.keySet()) {
					if (i == 0 || i == 1 || i == 9)
						put.add(Bytes.toBytes("authDetails"),
								Bytes.toBytes(id),
								Bytes.toBytes(values.get(id).toString()));
					else if (i == 2 || i == 3 || i == 4 || i == 5 || i == 6
							|| i == 7 || i == 8 || i == 10 || i == 11
							|| i == 12)
						put.add(Bytes.toBytes("details"), Bytes.toBytes(id),
								Bytes.toBytes(values.get(id).toString()));
					i++;
				}
				ht.put(put);
			}
			if (entitySet.equalsIgnoreCase("resources")) {
				for (String id : values.keySet()) {
					if (i == 0) {
						Put put1 = new Put(Bytes.toBytes(values.get(id)
								.toString()));
						put1.add(Bytes.toBytes("resIds"),
								Bytes.toBytes(values.get(id).toString()),
								Bytes.toBytes(values.get(id).toString()));
						ht.put(put1);
					}
					put.add(Bytes.toBytes("resDetails"), Bytes.toBytes(id),
							Bytes.toBytes(values.get(id).toString()));
					i++;
				}
				ht1.put(put);
			}
			if (entitySet.equalsIgnoreCase("manipulation")) {
				for (String id : values.keySet()) {
					put.add(Bytes.toBytes("manipulationDetails"),
							Bytes.toBytes(id),
							Bytes.toBytes(values.get(id).toString()));
				}
				ht1.put(put);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int viewProfile(int requesterID, int profileOwnerID,
			HashMap<String, ByteIterator> result, boolean insertImage,
			boolean testMode) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "members");
			Get get = new Get(Bytes.toBytes(Integer.toString(profileOwnerID)));
			Result r = ht.get(get);
			NavigableMap<byte[], byte[]> conf = r.getFamilyMap(Bytes
					.toBytes("confFrnds"));
			NavigableMap<byte[], byte[]> pend = r.getFamilyMap(Bytes
					.toBytes("pendFrnds"));
			NavigableMap<byte[], byte[]> res = r.getFamilyMap(Bytes
					.toBytes("resIds"));
			int confCount = conf.size();
			int pendCount = pend.size();
			int resCount = res.size();
			result.put(
					"pendingcount",
					new ObjectByteIterator(Bytes.toBytes(Integer
							.toString(pendCount))));
			result.put(
					"friendcount",
					new ObjectByteIterator(Bytes.toBytes(Integer
							.toString(confCount))));
			result.put(
					"resourcecount",
					new ObjectByteIterator(Bytes.toBytes(Integer
							.toString(resCount))));
			NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes
					.toBytes("details"));
			result.put(
					"userId",
					new ObjectByteIterator(familyMap.get(Bytes
							.toBytes("userId"))));
			result.put(
					"address",
					new ObjectByteIterator(familyMap.get(Bytes
							.toBytes("address"))));
			result.put(
					"fname",
					new ObjectByteIterator(
							familyMap.get(Bytes.toBytes("fname"))));
			result.put(
					"lname",
					new ObjectByteIterator(
							familyMap.get(Bytes.toBytes("lname"))));
			result.put("dob",
					new ObjectByteIterator(familyMap.get(Bytes.toBytes("dob"))));
			result.put(
					"ldate",
					new ObjectByteIterator(
							familyMap.get(Bytes.toBytes("ldate"))));
			result.put(
					"jdate",
					new ObjectByteIterator(
							familyMap.get(Bytes.toBytes("jdate"))));
			result.put("tel",
					new ObjectByteIterator(familyMap.get(Bytes.toBytes("tel"))));
			result.put(
					"gender",
					new ObjectByteIterator(familyMap.get(Bytes
							.toBytes("gender"))));
			if (insertImage == true) {
				result.put(
						"tpic",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("tpic"))));
				result.put(
						"pic",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("pic"))));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int listFriends(int requesterID, int profileOwnerID,
			Set<String> fields, Vector<HashMap<String, ByteIterator>> result,
			boolean insertImage, boolean testMode) {
		// TODO Auto-generated method stub
		if (requesterID < 0 || profileOwnerID < 0)
			return -1;
		try {
			HTable ht = new HTable(hc, "members");
			Get get = new Get(Bytes.toBytes(Integer.toString(profileOwnerID)));
			get.addFamily(Bytes.toBytes("confFrnds"));
			Result r = ht.get(get);
			for (KeyValue kv : r.raw()) {
				HTable ht1 = new HTable(hc, "members");
				Get get1 = new Get(r.getValue(Bytes.toBytes("confFrnds"),
						kv.getQualifier()));
				Result r1 = ht1.get(get1);
				HashMap<String, ByteIterator> e = new HashMap<String, ByteIterator>();
				NavigableMap<byte[], byte[]> familyMap = r1.getFamilyMap(Bytes
						.toBytes("details"));
				e.put("address",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("address"))));
				e.put("dob",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("dob"))));
				e.put("fname",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("fname"))));
				e.put("gender",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("gender"))));
				e.put("jdate",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("jdate"))));
				e.put("ldate",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("ldate"))));
				e.put("lname",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("lname"))));
				e.put("tel",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("tel"))));
				if (insertImage) {
					e.put("tpic",
							new ObjectByteIterator(familyMap.get(Bytes
									.toBytes("tpic"))));
					e.put("pic",
							new ObjectByteIterator(familyMap.get(Bytes
									.toBytes("pic"))));
				}
				e.put("userId",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("userId"))));
				result.add(e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int viewFriendReq(int profileOwnerID,
			Vector<HashMap<String, ByteIterator>> results, boolean insertImage,
			boolean testMode) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "members");
			Get get = new Get(Bytes.toBytes(Integer.toString(profileOwnerID)));
			get.addFamily(Bytes.toBytes("pendFrnds"));
			Result r = ht.get(get);
			for (KeyValue kv : r.raw()) {
				HTable ht1 = new HTable(hc, "members");
				Get get1 = new Get(r.getValue(Bytes.toBytes("pendFrnds"),
						kv.getQualifier()));
				Result r1 = ht1.get(get1);
				HashMap<String, ByteIterator> e = new HashMap<String, ByteIterator>();
				NavigableMap<byte[], byte[]> familyMap = r1.getFamilyMap(Bytes
						.toBytes("details"));
				e.put("address",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("address"))));
				e.put("dob",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("dob"))));
				e.put("fname",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("fname"))));
				e.put("gender",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("gender"))));
				e.put("jdate",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("jdate"))));
				e.put("ldate",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("ldate"))));
				e.put("lname",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("lname"))));
				e.put("tel",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("tel"))));
				if (insertImage) {
					e.put("pic",
							new ObjectByteIterator(familyMap.get(Bytes
									.toBytes("pic"))));
					e.put("tpic",
							new ObjectByteIterator(familyMap.get(Bytes
									.toBytes("tpic"))));
				}
				e.put("userId",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("userId"))));
				results.add(e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int acceptFriend(int inviterID, int inviteeID) {
		// TODO Auto-generated method stub
		try {
			String inviter = Integer.toString(inviterID);
			String invitee = Integer.toString(inviteeID);
			HTable ht2 = new HTable(hc, "friends");
			Put put = new Put(Bytes.toBytes(inviter + invitee));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId1"),
					Bytes.toBytes(inviter));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId2"),
					Bytes.toBytes(invitee));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("status"),
					Bytes.toBytes("Confirmed"));
			ht2.put(put);
			HTable ht1 = new HTable(hc, "members");
			Put put2 = new Put(Bytes.toBytes(invitee));
			put2.add(Bytes.toBytes("confFrnds"), Bytes.toBytes(inviter),
					Bytes.toBytes(inviter));
			ht1.put(put2);
			Delete delete = new Delete(Bytes.toBytes(invitee));
			delete.deleteColumns(Bytes.toBytes("pendFrnds"),
					Bytes.toBytes(inviter));
			ht1.delete(delete);
			Put put3 = new Put(Bytes.toBytes("" + inviterID));
			put3.add(Bytes.toBytes("confFrnds"), Bytes.toBytes(invitee),
					Bytes.toBytes(invitee));
			ht1.put(put3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int rejectFriend(int inviterID, int inviteeID) {
		// TODO Auto-generated method stub
		try {
			String inviter = Integer.toString(inviterID);
			String invitee = Integer.toString(inviteeID);
			HTable ht2 = new HTable(hc, "friends");
			Put put = new Put(Bytes.toBytes(inviter + invitee));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId1"),
					Bytes.toBytes(inviter));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId2"),
					Bytes.toBytes(invitee));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("status"),
					Bytes.toBytes("Rejected"));
			ht2.put(put);
			HTable ht1 = new HTable(hc, "members");
			Delete delete = new Delete(Bytes.toBytes(invitee));
			delete.deleteColumns(Bytes.toBytes("pendFrnds"),
					Bytes.toBytes(inviter));
			ht1.delete(delete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int inviteFriend(int inviterID, int inviteeID) {
		// TODO Auto-generated method stub
		try {
			String inviter = Integer.toString(inviterID);
			String invitee = Integer.toString(inviteeID);
			HTable ht2 = new HTable(hc, "friends");
			Put put = new Put(Bytes.toBytes(inviter + invitee));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId1"),
					Bytes.toBytes(inviter));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId2"),
					Bytes.toBytes(invitee));
			put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("status"),
					Bytes.toBytes("Pending"));
			ht2.put(put);
			HTable ht1 = new HTable(hc, "members");
			Put put1 = new Put(Bytes.toBytes(invitee));
			put1.add(Bytes.toBytes("pendFrnds"), Bytes.toBytes(inviter),
					Bytes.toBytes(inviter));
			ht1.put(put1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int viewTopKResources(int requesterID, int profileOwnerID, int k,
			Vector<HashMap<String, ByteIterator>> result) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "members");
			Get get = new Get(Bytes.toBytes(Integer.toString(profileOwnerID)));
			Result r = ht.get(get);
			if (k > r.size() / 17) {
				k = r.size() / 17;
				System.out
						.println("value of k is greater than number of resources, thus setting k to number of resources.");
			}
			NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes
					.toBytes("resIds"));
			Long ts[] = new Long[r.size() / 17];
			Integer rid[] = new Integer[r.size() / 17];
			int i = 0;
			Set s = familyMap.keySet();
			Iterator it = s.iterator();
			while (it.hasNext()) {
				ByteIterator bi = new ObjectByteIterator(familyMap.get(it
						.next()));
				rid[i] = Integer.parseInt(bi.toString());
				i++;
			}
			i = 0;
			int count = 0;
			for (KeyValue kv : r.raw()) {
				count++;
				if (count % 17 == 0) {
					ts[i] = kv.getTimestamp();
					i++;
				}
			}
			for (int m = 0; m < ts.length - 1; m++)
				for (int n = m + 1; n < ts.length; n++)
					if (ts[m] < ts[n]) {
						long t1 = ts[m];
						ts[m] = ts[n];
						ts[n] = t1;
						int t2 = rid[m];
						rid[m] = rid[n];
						rid[n] = t2;
					}
			for (int f = 0; f < k; f++) {
				HTable ht1 = new HTable(hc, "resources");
				Get get1 = new Get(Bytes.toBytes(rid[f].toString()));
				Result r1 = ht1.get(get1);
				HashMap<String, ByteIterator> e = new HashMap<String, ByteIterator>();
				NavigableMap<byte[], byte[]> familyMap1 = r1.getFamilyMap(Bytes
						.toBytes("resDetails"));
				e.put("body",
						new ObjectByteIterator(familyMap1.get(Bytes
								.toBytes("body"))));
				e.put("creatorid",
						new ObjectByteIterator(familyMap1.get(Bytes
								.toBytes("creatorid"))));
				e.put("doc",
						new ObjectByteIterator(familyMap1.get(Bytes
								.toBytes("doc"))));
				e.put("type",
						new ObjectByteIterator(familyMap1.get(Bytes
								.toBytes("type"))));
				e.put("walluserid",
						new ObjectByteIterator(familyMap1.get(Bytes
								.toBytes("walluserid"))));
				result.add(e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getCreatedResources(int creatorID,
			Vector<HashMap<String, ByteIterator>> result) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "members");
			Get get = new Get(Bytes.toBytes(Integer.toString(creatorID)));
			Result r = ht.get(get);
			NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes
					.toBytes("resIds"));
			Set s = familyMap.keySet();
			Iterator it = s.iterator();
			int i = 0;
			while (it.hasNext()) {
				i++;
				HashMap<String, ByteIterator> e = new HashMap<String, ByteIterator>();
				e.put("resId" + 1,
						new ObjectByteIterator(familyMap.get(it.next())));
				result.add(e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int viewCommentOnResource(int requesterID, int profileOwnerID,
			int resourceID, Vector<HashMap<String, ByteIterator>> result) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "resources");
			Get get = new Get(Bytes.toBytes(Integer.toString(resourceID)));
			Result r = ht.get(get);
			NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes
					.toBytes("manipulationDetails"));
			int i = 1;
			for (int j = 0; j < familyMap.size(); j += 7) {
				HashMap<String, ByteIterator> e = new HashMap<String, ByteIterator>();
				e.put("mid",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("mid" + Integer.toString(i)))));
				e.put("creatorId",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("creatorId" + Integer.toString(i)))));
				e.put("modifierId",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("modifierId" + Integer.toString(i)))));
				e.put("rid",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("rid" + Integer.toString(i)))));
				e.put("type",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("type" + Integer.toString(i)))));
				e.put("content",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("content" + Integer.toString(i)))));
				e.put("timestamp",
						new ObjectByteIterator(familyMap.get(Bytes
								.toBytes("timestamp" + Integer.toString(i)))));
				i++;
				result.add(e);
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int postCommentOnResource(int commentCreatorID,
			int resourceCreatorID, int resourceID,
			HashMap<String, ByteIterator> values) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "resources");
			Put put = new Put(Bytes.toBytes(Integer.toString(resourceID)));
			put.add(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("mid" + values.get("mid").toString()),
					Bytes.toBytes(values.get("mid").toString()));
			put.add(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("creatorId" + values.get("mid").toString()),
					Bytes.toBytes(Integer.toString(resourceCreatorID)));
			put.add(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("rid" + values.get("mid").toString()),
					Bytes.toBytes(Integer.toString(resourceID)));
			put.add(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("modifierId" + values.get("mid").toString()),
					Bytes.toBytes(Integer.toString(commentCreatorID)));
			put.add(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("timestamp" + values.get("mid").toString()),
					Bytes.toBytes(values.get("timestamp").toString()));
			put.add(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("type" + values.get("mid").toString()),
					Bytes.toBytes(values.get("type").toString()));
			put.add(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("content" + values.get("mid").toString()),
					Bytes.toBytes(values.get("content").toString()));
			ht.put(put);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delCommentOnResource(int resourceCreatorID, int resourceID,
			int manipulationID) {
		// TODO Auto-generated method stub
		try {
			HTable ht = new HTable(hc, "resources");
			Delete delete = new Delete(Bytes.toBytes(Integer
					.toString(resourceID)));
			delete.deleteColumn(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("mid" + Integer.toString(manipulationID)));
			delete.deleteColumn(
					Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("creatorId"
							+ Integer.toString(manipulationID)));
			delete.deleteColumn(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("rid" + Integer.toString(manipulationID)));
			delete.deleteColumn(
					Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("modifierId"
							+ Integer.toString(manipulationID)));
			delete.deleteColumn(
					Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("timestamp"
							+ Integer.toString(manipulationID)));
			delete.deleteColumn(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("type" + Integer.toString(manipulationID)));
			delete.deleteColumn(Bytes.toBytes("manipulationDetails"),
					Bytes.toBytes("content" + Integer.toString(manipulationID)));
			ht.delete(delete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int thawFriendship(int friendid1, int friendid2) {
		// TODO Auto-generated method stub
		try {
			String f1 = Integer.toString(friendid1);
			String f2 = Integer.toString(friendid2);
			HTable ht2 = new HTable(hc, "friends");
			if (friendid1 < friendid2) {
				Put put = new Put(Bytes.toBytes(f1 + f2));
				put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId1"),
						Bytes.toBytes(f1));
				put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("userId2"),
						Bytes.toBytes(f2));
				put.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("status"),
						Bytes.toBytes("Thawed"));
				ht2.put(put);
			} else if (friendid2 < friendid1) {
				Put put1 = new Put(Bytes.toBytes(f2 + f1));
				put1.add(Bytes.toBytes("frndDetails"),
						Bytes.toBytes("userId1"), Bytes.toBytes(f2));
				put1.add(Bytes.toBytes("frndDetails"),
						Bytes.toBytes("userId2"), Bytes.toBytes(f1));
				put1.add(Bytes.toBytes("frndDetails"), Bytes.toBytes("status"),
						Bytes.toBytes("Thawed"));
				ht2.put(put1);
			}
			HTable ht1 = new HTable(hc, "members");
			Delete delete = new Delete(Bytes.toBytes(f1));
			delete.deleteColumns(Bytes.toBytes("confFrnds"), Bytes.toBytes(f2));
			ht1.delete(delete);
			Delete delete1 = new Delete(Bytes.toBytes(f2));
			delete1.deleteColumns(Bytes.toBytes("confFrnds"), Bytes.toBytes(f1));
			ht1.delete(delete1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public HashMap<String, String> getInitialStats() {
		HTable ht;
		HashMap<String, String> a = new HashMap<String, String>();
		try {
			ht = new HTable(hc, "members");
			Scan scan = new Scan();
			ResultScanner rs = ht.getScanner(scan);
			Long userCount = 0L, confFrnds = 0L, pendFrnds = 0L, resId = 0L;
			Result r = rs.next();
			while (r != null) {
				userCount++;
				String data[] = getColumnsInColumnFamily(r, "confFrnds");
				confFrnds += data.length;
				data = getColumnsInColumnFamily(r, "pendFrnds");
				pendFrnds += data.length;
				data = getColumnsInColumnFamily(r, "resIds");
				resId += data.length;
				r = rs.next();
			}
			if (userCount > 0) {
				a.put("avgfriendsperuser", Long.toString(confFrnds / userCount));
				a.put("usercount", Long.toString(userCount));
				a.put("resourcesperuser", Long.toString(resId / userCount));
				a.put("avgpendingperuser", Long.toString(pendFrnds / userCount));
			} else {
				a.put("avgfriendsperuser", Long.toString(0L));
				a.put("usercount", Long.toString(0L));
				a.put("resourcesperuser", Long.toString(0L));
				a.put("avgpendingperuser", Long.toString(0L));
			}
			System.out.println("userCount = " + a.get("usercount"));
			System.out.println("avgfriendsperuser = "
					+ a.get("avgfriendsperuser"));
			System.out.println("avgpendingperuser = "
					+ a.get("avgpendingperuser"));
			System.out.println("resourcesperuser = "
					+ a.get("resourcesperuser"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	private String[] getColumnsInColumnFamily(Result r, String string) {
		// TODO Auto-generated method stub
		String[] a = null;
		try {
			HTable ht = new HTable(hc, "members");
			Get get = new Get(r.getRow());
			List<String> l = new ArrayList<String>();
			NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes
					.toBytes(string));
			for (byte[] bQunitifer : familyMap.keySet())
				l.add(bQunitifer.toString());
			a = new String[l.size()];
			for (int i = 0; i < l.size(); i++)
				a[i] = l.get(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public int CreateFriendship(int friendid1, int friendid2) {
		int r = acceptFriend(friendid1, friendid2);
		return r;
	}

	@Override
	public void createSchema(Properties props) {
		// TODO Auto-generated method stub
		HTableDescriptor ht = new HTableDescriptor("members");
		HTableDescriptor ht1 = new HTableDescriptor("resources");
		HTableDescriptor ht2 = new HTableDescriptor("friends");
		ht.addFamily(new HColumnDescriptor("authDetails"));
		ht.addFamily(new HColumnDescriptor("details"));
		ht.addFamily(new HColumnDescriptor("confFrnds"));
		ht.addFamily(new HColumnDescriptor("pendFrnds"));
		ht.addFamily(new HColumnDescriptor("resIds"));
		ht1.addFamily(new HColumnDescriptor("resDetails"));
		ht1.addFamily(new HColumnDescriptor("manipulationDetails"));
		ht2.addFamily(new HColumnDescriptor("frndDetails"));
		System.out.println("connecting");
		HBaseAdmin hba;
		try {
			hba = new HBaseAdmin(hc);
			if (hba.tableExists("members")) {
				hba.disableTable("members");
				hba.deleteTable("members");
				hba.createTable(ht);
			} else
				hba.createTable(ht);
			if (hba.tableExists("resources")) {
				hba.disableTable("resources");
				hba.deleteTable("resources");
				hba.createTable(ht1);
			} else
				hba.createTable(ht1);
			if (hba.tableExists("friends")) {
				hba.disableTable("friends");
				hba.deleteTable("friends");
				hba.createTable(ht2);
			} else
				hba.createTable(ht2);
		} catch (MasterNotRunningException | ZooKeeperConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done......");
	}

	@Override
	public int queryPendingFriendshipIds(int memberID,
			Vector<Integer> pendingIds) {
		// TODO Auto-generated method stub
		try {
			HTable ht2 = new HTable(hc, "members");
			Get get = new Get(Bytes.toBytes(Integer.toString(memberID)));
			Result r = ht2.get(get);
			NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes
					.toBytes("pendFrnds"));
			Set s = familyMap.keySet();
			Iterator it = s.iterator();
			while (it.hasNext()) {
				ByteIterator bi = new ObjectByteIterator(familyMap.get(it
						.next()));
				pendingIds.add(Integer.parseInt(bi.toString()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int queryConfirmedFriendshipIds(int memberID,
			Vector<Integer> confirmedIds) {
		// TODO Auto-generated method stub
		try {
			HTable ht2 = new HTable(hc, "members");
			Get get = new Get(Bytes.toBytes(Integer.toString(memberID)));
			Result r = ht2.get(get);
			NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes
					.toBytes("confFrnds"));
			Set s = familyMap.keySet();
			Iterator it = s.iterator();
			while (it.hasNext()) {
				ByteIterator bi = new ObjectByteIterator(familyMap.get(it
						.next()));
				confirmedIds.add(Integer.parseInt(bi.toString()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void cleanup(boolean warmup) throws DBException {
	}
}