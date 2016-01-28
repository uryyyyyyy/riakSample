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

public class Main {

	public static void main(String[] args) throws UnknownHostException, ExecutionException, InterruptedException {

		RiakNode.Builder builder = new RiakNode.Builder();
		builder.withMinConnections(10);
		builder.withMaxConnections(50);

		List<String> addresses = new LinkedList<>();
		addresses.add("192.168.1.1");
		addresses.add("192.168.1.2");
		addresses.add("192.168.1.3");

		List<RiakNode> nodes = RiakNode.Builder.buildNodes(builder, addresses);
		RiakCluster cluster = new RiakCluster.Builder(nodes).build();
		cluster.start();
		RiakClient client = new RiakClient(cluster);
		dataIn(client);
		dataOut(client);
	}

	private static void dataOut(RiakClient client) throws ExecutionException, InterruptedException {
		Namespace ns = new Namespace("default","my_bucket");
		Location location = new Location(ns, "my_key");
		FetchValue fv = new FetchValue.Builder(location).build();
		FetchValue.Response response = client.execute(fv);
		RiakObject obj = response.getValue(RiakObject.class);
	}

	private static void dataIn(RiakClient client) throws ExecutionException, InterruptedException {
		Namespace ns = new Namespace("default", "my_bucket");
		Location location = new Location(ns, "my_key");
		RiakObject riakObject = new RiakObject();
		riakObject.setValue(BinaryValue.create("my_value"));
		StoreValue store = new StoreValue.Builder(riakObject)
				.withLocation(location)
				.withOption(StoreValue.Option.W, new Quorum(3)).build();
		client.execute(store);
	}
}
