package com.github.uryyyyyyy.riak.client;


import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.Quorum;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManyLoad {

	public static void main(String[] args) throws UnknownHostException, ExecutionException, InterruptedException {

		RiakNode.Builder builder = new RiakNode.Builder();
		builder.withMinConnections(10);
		builder.withMaxConnections(50);

		List<String> addresses = new LinkedList<>();
		addresses.add("172.17.0.3");
		addresses.add("172.17.0.5");
		addresses.add("172.17.0.6");

		List<RiakNode> nodes = RiakNode.Builder.buildNodes(builder, addresses);
		RiakCluster cluster = new RiakCluster.Builder(nodes).build();
		cluster.start();
		System.out.println("cluster start done");
		RiakClient client = new RiakClient(cluster);
		dataIn(client);
		dataOut(client);
		cluster.shutdown();
	}

	private static void dataOut(RiakClient client) throws ExecutionException, InterruptedException {
		Namespace ns = new Namespace("default","my_bucket");
		Location location = new Location(ns, "my_key" + 333);
		FetchValue fv = new FetchValue.Builder(location).build();
		FetchValue.Response response = client.execute(fv);
		RiakObject obj = response.getValue(RiakObject.class);
		System.out.println(new String(obj.getValue().getValue()));
	}

	private static void dataIn(RiakClient client) throws ExecutionException, InterruptedException {
		Namespace ns = new Namespace("default", "my_bucket");
		long start2 = System.currentTimeMillis();
		for(int i = 0; i < 10000; i++){
			Location location = new Location(ns, "my_key" + i);
			RiakObject riakObject = new RiakObject();
			riakObject.setValue(BinaryValue.create("my_value" + i));
			StoreValue store = new StoreValue.Builder(riakObject)
					.withLocation(location)
					.withOption(StoreValue.Option.W, new Quorum(3)).build();
			client.execute(store);
		}
		System.out.println(System.currentTimeMillis() - start2 + " ms");
	}
}
